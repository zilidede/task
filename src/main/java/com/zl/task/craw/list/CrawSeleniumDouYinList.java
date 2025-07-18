package com.zl.task.craw.list;


import com.ll.drissonPage.base.By;
import com.ll.drissonPage.element.ChromiumElement;
import com.ll.drissonPage.page.ChromiumTab;
import com.ll.drissonPage.units.listener.DataPacket;
import com.zl.task.craw.base.x.DefaultCrawSeleniumDouYinList;
import com.zl.task.impl.ExecutorTaskService;
import com.zl.task.vo.task.taskResource.DefaultTaskResourceCrawTabList;
import com.zl.task.vo.task.taskResource.TaskResource;
import com.zl.task.vo.task.DouYinShopListTypeVO;
import com.zl.task.vo.task.taskResource.TaskVO;
import com.zl.utils.io.FileIoUtils;
import com.zl.utils.log.LoggerUtils;
import com.zl.utils.other.Ini4jUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @className: com.craw.nd.service.other.person.Impl-> CrawDouyinStoreServiceImpl
 * @description: 爬取抖音小店电商罗盘商品榜单页-信息来源 抖店电商罗盘页-商品榜单
 * url-https://compass.jinritemai.com/shop/chance/product-rank?first_rank_type=product&second_rank_type=1
 * @author: zl
 * @createDate: 2022-12-13 14:50
 * @version: 1.0
 * @todo: 修改时间： 2024-01-17
 * 优化代码，增加单次任务爬取功能
 * * 修改时间： 2024-03-24
 * * 优化代码，增加小时榜爬取
 * * 修改时间： 2024-03-26
 * * 优化代码，修正爬取异常-服饰内衣小时榜
 * * 修改时间： 2024-11-14
 * * 优化代码:
 * 1.选择小时数；
 * 2.选择达播
 */
public class CrawSeleniumDouYinList implements ExecutorTaskService {
    private final String industryFilePath = "./data/config/douYinShopLists";
    private final Logger logger = LoggerFactory.getLogger(CrawSeleniumDouYinList.class);
    private Map<String, Integer> listTypeMap;
    private Map<String, Integer> industryMap;
    private SimpleDateFormat sf;
    private Integer hour = -1;
    private ChromiumTab tab;
    private String xhrDir = "";

    public CrawSeleniumDouYinList() throws Exception {
        Ini4jUtils.loadIni("./data/config/config.ini");
        Ini4jUtils.setSectionValue("list");
        xhrDir = Ini4jUtils.readIni("xhrDir");


    }

    public static void main(String[] args) {
        String s = FileIoUtils.readTxtFile("./data/task/抖店罗盘日榜.txt", "utf-8");
        String[] strings = s.split("\r\n");
        for (String string : strings) {
            TaskVO taskVO = new TaskVO(1, "抖店罗盘日榜");
            taskVO.setTaskDesc(string);
            DefaultCrawSeleniumDouYinList.setTab(DefaultTaskResourceCrawTabList.getTabList().get(0));
            try {
                DefaultCrawSeleniumDouYinList.getInstance().run(taskVO);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public ChromiumTab getTab() {
        return tab;
    }

    public void setTab(ChromiumTab tab) {
        this.tab = tab;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    @Override
    public void ExecutorTaskService(TaskResource taskResource) {

    }

    @Override
    public void ExecutorTaskService(Object  object) {

    }

    @Override
    public void run(TaskVO vo) throws Exception {
        Lock lock = new ReentrantLock();
        lock.lock();
        try {
            openGoodListPage();
            List<String> list = new ArrayList<>();
            list.add("board_list");
            list.add("video_rank/hot_video_rank_v2_luopan");
            tab.listen().start(list); //监听商品榜单xhr
            Thread.sleep(4000);
            // 爬取直播交易小时版
            //在hour 等于10：00 -11：00 进行每日校监。
            if (vo.getTaskDesc().indexOf("小时榜") >= 0) {
                crawLiveHourList(vo);
            } else
                crawEBusVideoDayList(vo);
        } finally {
            lock.unlock();
        }

    }

    public void crawEBusVideoDayList(TaskVO vo) throws Exception {
        //电商属性短视频包含引流短视频等跟带货相关的所有素材视频；
        logger.info("今日爬取" + vo.getTaskDesc() + "行业商品榜单");
        craw(vo);
        logger.info("已爬取今日" + vo.getTaskDesc() + "已选行业商品榜单");
    }

    public void crawUnEBusVideoDayList(TaskVO vo) {

    }

    public void crawLiveHourList(TaskVO vo) throws Exception {
        LocalTime now = LocalTime.now();
        int hour = now.getHour();
        if (hour == -2) {
            setHour(-1);
        } else {
            while (now.getHour() - getHour() < 1) {
                Thread.sleep(1000 * 60 * 10);
                LoggerUtils.logger.info("当前时间：" + now.getHour() + "小时，未到爬取时间，请稍后重试");
            }
            logger.info("今日爬取" + getHour() + vo.getTaskDesc() + "行业商品榜单");
            vo.setStatus(craw(vo));
            //save();
            logger.info("已爬取今日" + vo.getTaskDesc() + "已选行业商品榜单");
            Thread.sleep(2000);
        }
    }

    public int craw(TaskVO vo) throws Exception {
        DouYinShopListTypeVO listTypeVO = new DouYinShopListTypeVO();
        String[] s1 = vo.getTaskDesc().split("&");
        listTypeVO.setCrawTime(s1[0]);
        listTypeVO.setListCarrier(s1[1]);
        listTypeVO.setListType(s1[2]);
        listTypeVO.setIndustryName(s1[3]);
        return selectCrawListType((listTypeVO));
    }


    public int selectCrawListType(DouYinShopListTypeVO vo) throws Exception {
        //监听
        //监听价格带商品分布

        SimpleDateFormat sf = new SimpleDateFormat("HH:00");
        //选择爬取的榜单信息；
        openGoodListPage();
        String xpath = "";
        List<ChromiumElement> elements;
        //选择载体；
        xpath = "//*[@class=\"ecom-tabs-nav-list\"]/div";
        elements = tab.eles(By.xpath(xpath));
        Thread.sleep(3000);
        int len = elements.size();
        if (len < 1) {
            logger.warn("选择抖店榜单载体类型失败");
            return 0;
        }
        if (vo.getListCarrier().equals("商品")) {
            elements.get(0).click().click();
            Thread.sleep(1000);
        } else if (vo.getListCarrier().equals("店铺")) {
            elements.get(1).click().click();

            Thread.sleep(1000);
        } else if (vo.getListCarrier().equals("内容")) {
            elements.get(2).click().click();
            Thread.sleep(2000);
        } else if (vo.getListCarrier().equals("账号")) {
            elements.get(3).click().click();
            Thread.sleep(1000);
        }
        //选择榜单类型
        xpath = "//*[@id=\"root\"]/div/div[2]/div/div[1]/div[1]/div[2]/div";
        if (vo.getListCarrier().equals("内容"))
            xpath = "//*[@id=\"root\"]/div/div[2]/div[1]/div[1]/div[2]/div";
        elements = tab.eles(By.xpath(xpath));
        Thread.sleep(1000);
        for (ChromiumElement element : elements) {
            if (element.text().equals(vo.getListType())) {
                element.click().click();
                Thread.sleep(1000);
            }
        }
        if (len < 1) {
            logger.warn("选择抖店榜单类型失败");
            return 0;
        }
        if (vo.getIndustryName().equals(""))
            return 0;
        selectTime(vo);
        //选择类型
        Thread.sleep(1000);
        if (vo.getListType().indexOf("引流短视频") >= 0 || vo.getListType().indexOf("热门短视频") >= 0) {
            //选择榜单类型
            xpath = "//*[@id=\"root\"]/div/div[2]/div/div[2]/div[1]/div[2]/div";
            List<ChromiumElement> elements1 = tab.eles(By.xpath(xpath));
            for (ChromiumElement element : elements1) {
                if (vo.getListType().indexOf(element.text()) >= 0) {
                    element.click().click();
                    Thread.sleep(1000);
                    break;
                }
            }
            selectTime(vo);
            crawVideo(vo.getIndustryName());
        } else {
            if (vo.getCrawTime().equals("小时榜")) {

//*[@id="root"]/div/div[2]/div/div[2]/div[1]/div[1]
                String[] strings = vo.getListType().split("&");
                //选择榜单小时点
                logger.info("选择榜单小时点");
                // 小时标题
                xpath = "//*[@id=\"root\"]/div/div[2]/div/div[3]/div[1]/div[1]/div";
                elements = tab.eles(By.xpath(xpath));//获取小时标题
                Thread.sleep(1000);
                for (ChromiumElement element : elements) {
                    //System.out.println(element.text());
                }
                String s = elements.get(elements.size() - 1).text();
                if (hour == -1) {
                    if (!sf.format(new Date()).equals(s)) {
                        // return 0;
                    }
                } else {
                    logger.info("选择预设的时间段" + hour);

                    xpath = "//*[@style=\"margin-top: 12px;\"]";
                    elements = tab.ele(By.xpath(xpath)).eles(By.xpath("./div[2]/div"));
                    Thread.sleep(1000);
                    s = s.replace(":00", "");
                    int j = 24 - Integer.parseInt(s) + hour;

                    elements.get(j).click().click();
                    Thread.sleep(1000 * 2);

                }
                xpath = "//*[@id=\"root\"]/div/div[2]/div/div[2]/div[4]/div[2]/div";
                elements = tab.eles(By.xpath(xpath)); //选择自播或者达人
                try {
                    selectIndustry(vo.getIndustryName(), vo.getListType()); //爬取自播
                    Thread.sleep(1000);
                    elements.get(1).click().click(); //选择达播
                    Thread.sleep(1000);
                    selectIndustry(vo.getIndustryName(), vo.getListType()); //爬取达播
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return 0;
                }


            } else
                try {
                    selectIndustry(vo.getIndustryName(), vo.getListType());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return 0;
                }
        }

        return 3;
    }


    public List<String> getListVideoAll() throws Exception {
        //openEnterUrl("https://compass.jinritemai.com/shop/chance/product-rank?first_rank_type=product&second_rank_type=1");
        Thread.sleep(1000 * 5);
        DouYinShopListTypeVO vo = new DouYinShopListTypeVO();
        vo.setCrawTime("近一天");
        vo.setListCarrier("内容");
        vo.setListType("引流短视频");
        vo.setIndustryName("");
        selectCrawListType(vo);
        //打开类目选择框
        List<String> result = new ArrayList<>();
        String xpath = "//*[@class=\"ecom-select-selection-item\"]";
        List<ChromiumElement> elements = tab.eles(By.xpath(xpath));
        Thread.sleep(1000);
        elements.get(1).click().click();
        Thread.sleep(2000);
        //first
        //类目选择
        xpath = "//*[@class=\"rc-virtual-list-holder-inner\"]";
        List<ChromiumElement> elements1 = tab.eles(By.xpath(xpath));
        Thread.sleep(1000);
        String s3 = elements1.get(0).text();
        List<ChromiumElement> elements2 = elements1.get(0).eles("./div");
        Thread.sleep(1000);
        String s = "";
        int i = 0;
        for (ChromiumElement element : elements2) {
            s = element.text() + "-" + i++;
            result.add(s);
        }
        return result;
    }

    public void crawVideo(String s) throws Exception {
        //爬取视频
        String[] strings = s.split("-");
        //选择电商视频还是非电商视频
        String xpath = "//*[@id=\"root\"]/div/div[2]/div/div[2]/div[2]/div[2]/div";
        List<ChromiumElement> elements = tab.eles(By.xpath(xpath));
        if (strings[0].equals("电")) {
            elements.get(0).click().click();
        } else if (strings[0].equals("非")) {
            elements.get(1).click().click();
        } else {
            LoggerUtils.logger.error("爬取店铺视频榜单-视频类型选择错误:" + s);
        }
        //打开类目选择框
        xpath = "//*[@class=\"ecom-select-selection-item\"]";
        elements = tab.eles(By.xpath(xpath)); //类目选择框
        Thread.sleep(2000);
        if (strings[0].equals("非"))
            elements.get(1).click().click(); //
        else
            elements.get(0).click().click();
        Thread.sleep(2000);
        //类目选择
        xpath = "//*[@class=\"rc-virtual-list-holder-inner\"]";
        List<ChromiumElement> elements1 = tab.eles(By.xpath(xpath));
        Thread.sleep(1000);
        String s3 = elements1.get(0).text();
        List<ChromiumElement> elements2 = elements1.get(0).eles(By.xpath("./div"));
        Thread.sleep(2000);
        elements2.get(Integer.parseInt(strings[2])).click().click(); // 选择行业名
        Thread.sleep(3000);
        //first
        //打开子类目选择框
        if (strings[0].equals("非"))
            elements.get(2).click().click();
        else {
            xpath = "//*[@class=\"ecom-cascader-picker ecom-cascader-picker-show-search\"]";
            tab.ele(By.xpath(xpath)).click().click();

        }
        Thread.sleep(2000);
        //获取选择框列表
        if (strings[0].equals("非")) {
            xpath = "//*[@class=\"rc-virtual-list-holder-inner\"]";
            List<ChromiumElement> elements3 = tab.eles(By.xpath(xpath));
            elements1 = elements3.get(1).eles(By.xpath("./div"));
        } else {
            xpath = "//*[@class=\"rc-virtual-list-holder-inner\"]";
            List<ChromiumElement> elements3 = tab.eles(By.xpath(xpath));
            elements1 = elements3.get(1).eles(By.xpath("./li"));
        }
        for (int i = 0; i < elements1.size(); i++) {
            elements1.get(i).click().click();
            Thread.sleep(3000);
            //second
            if (strings[0].equals("非")) {
                saveXhr();
                //重新打开子类目选择框
                //打开子类目选择框
                if (strings[0].equals("非"))
                    elements.get(2).click().click();
                else {
                    xpath = "//*[@class=\"ecom-cascader-picker ecom-cascader-picker-show-search\"]";
                    tab.ele(By.xpath(xpath)).click().click();

                }
                Thread.sleep(2000);
                continue;
            } else {
                xpath = "//*[@class=\"rc-virtual-list-holder-inner\"]";
                List<ChromiumElement> elements3 = tab.eles(By.xpath(xpath));
                elements2 = elements3.get(2).eles(By.xpath("./li"));
                Thread.sleep(2000);
                for (int j = 1; j < elements2.size(); j++) {
                    elements2.get(j).click().click();
                    Thread.sleep(3000);
                    //
                }
                saveXhr();
            }
        }
        Thread.sleep(1000);
    }

    public void selectTime(DouYinShopListTypeVO vo) throws InterruptedException {
        //选择时间；
        String xpath = "//*[@class=\"ecom-radio-group ecom-radio-group-outline\"]/label";
        List<ChromiumElement> elements = tab.eles(By.xpath(xpath));
        Thread.sleep(1000);
        int len = elements.size();
        if (len < 1) {
            logger.warn("选择抖店榜单时间类型失败");
            return;
        }
        if (len == 4) {
            if (vo.getCrawTime().equals("近一天")) {
                elements.get(0).click().click();
                Thread.sleep(1000);
            } else if (vo.getCrawTime().equals("近七天")) {
                elements.get(1).click().click();

                Thread.sleep(1000);
            } else if (vo.getCrawTime().equals("近30天")) {
                elements.get(2).click().click();

                Thread.sleep(1000);
            } else {
                //自定义
                elements.get(3).click().click();

                Thread.sleep(1000);
            }

        } else if (len == 5) {
            if (vo.getCrawTime().equals("实时") || vo.getCrawTime().equals("小时榜")) {
                elements.get(0).click().click();
                Thread.sleep(1000);
            } else if (vo.getCrawTime().equals("近一天")) {
                elements.get(1).click().click();

                Thread.sleep(1000);
            } else if (vo.getCrawTime().equals("近七天")) {
                elements.get(2).click().click();

                Thread.sleep(1000);
            } else if (vo.getCrawTime().equals("近30天")) {
                elements.get(3).click().click();

                Thread.sleep(1000);
            } else {
                elements.get(4).click().click();

                Thread.sleep(1000);
            }

        }
    }

    public void selectIndustry(String s, String type) throws Exception {
        //选择类目名
        String[] strings = s.split("-");
        //打开选择框
        String xpath = "//*[@class=\"ecom-select-selector\"]";
        List<ChromiumElement> elements = tab.eles(By.xpath(xpath));
        Thread.sleep(1000);
        elements.get(1).click().click();
        Thread.sleep(2000);
        //行业类目选择-选择行业；
        xpath = "//*[@style=\"display: flex; flex-direction: column;\"]/div";
        elements = tab.eles(By.xpath(xpath));
        Integer ilen = Integer.parseInt(strings[1]);
        elements.get(ilen).click().click();
        Thread.sleep(1000);
        //打开类目选择框
        xpath = "//*[@class=\"ecom-cascader-picker ecom-cascader-picker-show-search\"]";
        ChromiumElement element = tab.ele(By.xpath(xpath));
        Thread.sleep(1000);
        element.click().click();
        Thread.sleep(1000);
        //first
        xpath = "//*[@class=\"ecom-cascader-menu\"]";
        List<ChromiumElement> elements1 = tab.eles(By.xpath(xpath));
        Thread.sleep(1000);
        List<ChromiumElement> elements2 = elements1.get(0).eles(By.xpath("./div[2]/div/div/div/li"));
        Thread.sleep(1000);
        for (int j = 0; j < elements2.size(); j++) {
            elements2.get(j).click().click();
            Thread.sleep(1000);
            System.out.println(elements2.get(j).text());
            crawCompassList();
            saveXhr();
            //重新打开选择框
            Thread.sleep(1000);
            tab.runJs("var q=document.documentElement.scrollTop=0"); //滚动到顶部
            Thread.sleep(3000);
            xpath = "//*[@class=\"ecom-cascader-picker ecom-cascader-picker-show-search\"]";
            try {
                ChromiumElement element1 = tab.ele(By.xpath(xpath));
                Thread.sleep(1000);
                element1.click().click();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            Thread.sleep(1000);

        }


    }

    public void crawCompassVideoList() throws Exception {


    }

    public void saveXhr() {
        List<DataPacket> res = tab.listen().waits(100, 2.1, false, true);
        if (res.size() >= 1) {
            for (DataPacket data : res)
                if (data != null)
                    try {
                        saveFile(xhrDir, data);
                    } catch (Exception e) {
                        LoggerUtils.logger.info("保存文件失败：" + data.url());
                    }

        } else {
            System.out.println("error");
        }
    }

    public void crawCompassList() throws Exception {
        //爬取已经选择的抖店罗盘榜单-翻页操作
        try {
            pageTurnTwo();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        //
        //保存xhr文件
        List<DataPacket> res = tab.listen().waits(4, 2.1, false, true);
        if (res.size() >= 1) {
            for (DataPacket data : res)
                if (data != null)
                    try {
                        saveFile(xhrDir, data);
                    } catch (Exception e) {
                        LoggerUtils.logger.info("保存文件失败：" + data.url());
                    }

        } else {
            System.out.println("error");
        }
    }

    public void saveFile(String fileDir, DataPacket data) throws Exception {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss-SSS");
        String timestamp = sdf.format(now);
        String target = data.getTarget();
        if (target == "board_list")
            fileDir = fileDir + "boardList\\";
        else if (target == "video_rank/hot_video_rank_v2_luopan") {
            fileDir = fileDir + "hotVideoRankV2Luopan\\";
        }
        // 示例URL和响应体（这里需要替换为实际值）
        String url = data.url();
        String requestBody = data.request().postData();
        String responseBody = data.response().rawBody();
        // 构造文件路径
        String filePath = "";
        filePath = fileDir + timestamp + ".txt";
        // 写入文件
        FileIoUtils.writeToFile(filePath, "url: " + url + " Request body: " + requestBody + " Response body: " + responseBody);
    }


    public boolean pageTurnOne() throws InterruptedException {
        //翻页操作-查看更多
        // 查看更多下一页调整页面
        String xpath = "//*[@id=\"root\"]/div/div[2]/div/div[2]/div/div/div/div/div/div[2]";
        ChromiumElement element = null;
        try {
            element = tab.ele(By.xpath(xpath));
            if (element.text().equals("查看更多") || element.text().equals("没有更多了")) {

                for (int i = 0; i < 20; i++) {
                    element = tab.ele(By.xpath(xpath));
                    if (element.text().equals("没有更多了")) {
                        return false;
                    } else if (element.text().equals("查看更多")) {
                        element.click();

                    }

                }
                return true;
            }
        } catch (Exception ex) {
            //  ex.printStackTrace();
            LoggerUtils.logger.warn("翻页操作失败,不存在查看更多翻页键");
            return false;
        }
        return true;
    }

    public boolean pageTurnTwo() throws InterruptedException {
        //翻页操作-查看更多
        String xpath = "//*[@style=\"margin-top: 8px;\"]/li";
        List<ChromiumElement> elements = tab.eles(By.xpath(xpath)); //获取翻页列表
        Thread.sleep(1000);
        System.out.println("开始爬取当前页" + elements.size());
        int len = -1;
        if (elements.size() == 0)
            return false;
        String s1 = elements.get(elements.size() - 2).attr("title");
        if (s1.equals("下一页"))
            s1 = elements.get(elements.size() - 3).attr("title");
        try {
            len = Integer.parseInt(s1);
        } catch (Exception ex) {
            ex.printStackTrace();
            //没有队列
            return false;
        }

        if (len < 0) {

            return false;
        }
        for (int i = 0; i < len - 1; i++) {
            try {
                xpath = "//*[@class=\"ecom-pagination-next\"]";
                ChromiumElement element = tab.ele(By.xpath(xpath));
                element.click().click();
                Thread.sleep(1500);
            } catch (Exception ex) {
                System.out.println(i);
                ex.printStackTrace();
                continue;
            }

        }
        return true;
    }

    public void openGoodListPage() throws Exception {
        //打开商品榜单页面
        String url = "https://compass.jinritemai.com/shop/chance/product-rank?first_rank_type=product&second_rank_type=1";
        tab.get(url);
        Thread.sleep(1000 * 10);
    }


}
