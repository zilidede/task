package com.zl.task.craw.market;


import com.ll.drissonPage.base.By;
import com.ll.drissonPage.element.ChromiumElement;
import com.ll.drissonPage.page.ChromiumTab;
import com.ll.drissonPage.units.listener.DataPacket;
import com.zl.task.impl.ExecutorTaskService;
import com.zl.task.vo.task.taskResource.DefaultTaskResourceCrawTabList;
import com.zl.task.vo.task.taskResource.TaskResource;
import com.zl.task.vo.task.taskResource.TaskVO;
import com.zl.utils.io.DiskIoUtils;
import com.zl.utils.io.FileIoUtils;
import com.zl.utils.log.LoggerUtils;
import com.zl.utils.other.Ini4jUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @className: com.craw.nd.service.other.person.Impl-> CrawDouyinStoreServiceImpl
 * @description: 爬取抖音小店电商罗盘类目-信息来源
 * url-https://compass.jinritemai.com/shop/chance/product-rank?first_rank_type=product&second_rank_type=1
 * @author: zl
 * @createDate: 2022-12-13 14:50
 * @version: 1.0
 * @todo: 修改时间： 2024-08-08
 * 优化代码，增加爬取类目信息
 * *修改时间： 2024-08-16
 * 优化代码，增加爬取类目渠道和价格域信息。
 * *修改时间： 2024-09-20
 * *  优化代码，增加三级类目爬取。
 * 修改时间：2024-11-08
 * 新增功能：增加爬取时间选择项-手动
 * 增加固定四级类目爬取
 */
public class CrawSeleniumDouYinCategoryList implements ExecutorTaskService {
    private final SimpleDateFormat sf;
    private final Map<String, Integer> fourCategorys;
    private final String time;
    private String xhrDir = ""; //xhrDir
    private ChromiumTab tab;
    private Boolean isThreeCraw; // 三级类目爬取

    public CrawSeleniumDouYinCategoryList() throws Exception {
        Thread.sleep(4000);
        sf = new SimpleDateFormat("yyyy-MM-dd");
        isThreeCraw = true;
        time = "0";
        fourCategorys = fourCategorysToIntMap("./data/task/market.txt");
        Ini4jUtils.loadIni("./data/config/config.ini");
        Ini4jUtils.setSectionValue("market");
        xhrDir = Ini4jUtils.readIni("xhrDir");
    }

    public static void main(String[] args) throws Exception {
        crawlerList();
    }

    public static void crawlerList() throws Exception {
        CrawSeleniumDouYinCategoryList crawler = new CrawSeleniumDouYinCategoryList();
        crawler.setTab(DefaultTaskResourceCrawTabList.getTabList().get(0));
        crawler.run(new TaskVO(1, "抖店罗盘类目榜单"));
    }

    public static Map<String, Integer> fourCategorysToIntMap(String filePath) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        String[] contents = FileIoUtils.readTxtFile(filePath, "utf-8").split("\r\n");
        for (String s : contents) {
            map.put(s, 0);
        }
        return map;
    }

    public Boolean getThreeCraw() {
        return isThreeCraw;
    }

    public void setThreeCraw(Boolean threeCraw) {
        isThreeCraw = threeCraw;
    }

    public ChromiumTab getTab() {
        return tab;
    }

    public void setTab(ChromiumTab tab) {
        this.tab = tab;
    }



    @Override
    public void ExecutorTaskService(TaskResource taskResource) {

    }

    @Override
    public void ExecutorTaskService(Object object) {

    }

    @Override
    public void run(TaskVO vo) throws Exception {
        //
        openGoodListPage();
        Thread.sleep(2000);
        craw(time, vo.getTaskDesc());
        vo.setStatus(3);
        LoggerUtils.logger.info("已爬取抖店罗盘类目榜单任务：" + vo.getTaskDesc());
        Thread.sleep(2000);
        // LocalTaskCommon.updateTaskStatus(vo);
    }

    private void openGoodListPage() {
        tab.get("https://compass.jinritemai.com/shop/chance/category-overview?from_page=%2");
    }

    public void craw(String time,String selectorCategoryName) throws Exception {


        List<ChromiumElement> elements = null;
        //选择时间
        String xpath = "//*[@class=\"ecom-radio-group ecom-radio-group-outline\"]/label";
        selectTime(time);
        //选择类目
        selectOrIndustry();
        xpath = "//*[@style=\"display: flex; flex-direction: column;\"]/div";
        elements = tab.eles(By.xpath(xpath));
        Thread.sleep(1000);
        //监听价格带商品分布
        List<String> list = new ArrayList<>();
        list.add("compass_api/shop/product/child_category_by_industry_id");
        list.add("compass_api/shop/product_lander/lander/interface");
        list.add("compass_api/shop/product/product_chance_market/category_overview_price_band_distribution");
        list.add("compass_api/shop/product/product_chance_market/category_overview_price_analysis_product");
        tab.listen().start(list); //监听类目xhr
        Boolean isEndSign = false; // 结束标识
        for (int j = 0; j < elements.size(); j++) {
            String name1 = elements.get(j).text(); //第一类目名称
            String s = name1;
            if(!selectorCategoryName.equals("")){
                if(isEndSign){
                    return;
                }
                if(!selectorCategoryName.equals(name1)){
                    continue;
                }
                else{
                    isEndSign=true;
                }
            }
            try {
                elements.get(j).click().click();
            } catch (Exception e) {
                LoggerUtils.logger.info("行业选择失败：" + s);
                selectOrIndustry();
                elements.get(j).click().click();
            }
            Thread.sleep(2000);
            //选择第二子类目
            selectOrCategory();
            Thread.sleep(1000);
            xpath = "//*[@class=\"rc-virtual-list-holder-inner\"]";
            List<ChromiumElement> elements1 = tab.eles(By.xpath(xpath)).get(1).eles(By.xpath("./li")); //获取子类目类别
            Thread.sleep(1000);
            for (int i = 0; i < elements1.size(); i++) {
                String name2 = elements1.get(i).text(); //第一子类目名称
                try {
                    elements1.get(i).click().click();
                } catch (Exception e) {
                    LoggerUtils.logger.info("子类目选择失败：" + s);
                    selectOrCategory();
                    elements1.get(i).click().click();
                }
                Thread.sleep(1000);
                crawCategoryInfo();

                Thread.sleep(1000);
                //选择第三子类目；
                isThreeCraw = s.equals("服饰内衣") || s.equals("生鲜") || s.equals("美妆") || s.equals("母婴宠物") || s.equals("鞋靴箱包") || s.equals("珠宝文玩")
                        || s.equals("3C数码家电") || s.equals("钟表配饰") || s.equals("运动户外");
                if (isThreeCraw) {
                    selectOrCategory();
                    xpath = "//*[@class=\"rc-virtual-list-holder-inner\"]";
                    List<ChromiumElement> elements3 = tab.eles(By.xpath(xpath)).get(2).eles(By.xpath("./li")); //获取子类目类别
                    Thread.sleep(1000);
                    if (elements3.size() == 1)
                        break;
                    else {
                        for (int k = 1; k < elements3.size(); k++) {
                            String name3 = elements3.get(k).text(); //第三子类目名称
                            String [] categoryIds = null;


                            try {
                                elements3.get(k).click().click();
                            } catch (Exception e) {
                                LoggerUtils.logger.info("子类目选择失败：" + s);
                                selectOrCategory();
                                elements3.get(k).click().click();

                            }
                            //   选择第四个子类目；
                            String categortName = elements3.get(k).text();
                            if (fourCategorys.containsKey(categortName)) {
                                selectOrCategory();
                                xpath = "//*[@class=\"ecom-cascader-menu\"]";
                                List<ChromiumElement> elements4 = tab.eles(By.xpath(xpath));
                                List<ChromiumElement> elements5 = elements4.get(2).eles(By.xpath("./li"));
                                if (elements5.size() == 1)
                                    break;
                                else {
                                    for (int m = 1; m < elements5.size(); m++) {
                                        String name4 = elements5.get(m).text(); //第四子类目名称
                                        try {
                                            elements5.get(m).click().click();
                                        } catch (Exception e) {
                                            LoggerUtils.logger.info("子类目选择失败：" + s);
                                            selectOrCategory();
                                            elements5.get(m).click().click();
                                        }
                                        Thread.sleep(1000);
                                        crawCategoryInfo();
                                        selectOrCategory();
                                    }

                                }
                            }
                            Thread.sleep(1000);
                            crawCategoryInfo();

                            //保存xhr
                            selectOrCategory();
                        }
                    }
                }

                selectOrCategory();

            }
            //重新打开行业选择框
            selectOrIndustry();
        }
    }

    public void selectOrIndustry() throws InterruptedException {
        //选择框没打开 重新打开行业选择框。
        //重新打开行业选择框
        String xpath = "//*[@class=\"ecom-select-selection-item\"]";
        tab.ele(By.xpath(xpath)).click().click();
        Thread.sleep(1000);

    }

    public void selectOrCategory() throws InterruptedException {

        //移动到顶部
        tab.runAsyncJs("window.scrollTo(0, 0);");
        Thread.sleep(1000);
        //选择类目选择框
        String xpath = "//*[@class=\"ecom-cascader-picker-label\"]";
        tab.ele(By.xpath(xpath)).click().click();
        Thread.sleep(2000);

    }

    public void selectTime(String name) throws InterruptedException {
        int i = 0;
        try {
            i = Integer.parseInt(name);
        } catch (Exception ex) {
            //
            return;
        }

        //选择时间 0-一天 1-一周 2-一个月
        String xpath = "//*[@class=\"ecom-radio-group ecom-radio-group-outline\"]/label";
        List<ChromiumElement> elements = null;
        try {
            elements = tab.eles(By.xpath(xpath));
            Thread.sleep(1000);
            elements.get(i).click().click();
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void crawSingleCategoryInfo(String name) throws InterruptedException {
        //爬取类目信息 流量渠道以及价格域 width:

    }

    public void crawCategoryInfo() throws Exception {
        //爬取类目信息 流量渠道以及价格域 width:
        saveXHr();
        Thread.sleep(1000);
        String xpath = "//*[@style=\"width: max-content; min-width: 100%; table-layout: auto;\"]";
        List<ChromiumElement> elements = tab.eles(By.xpath(xpath));
        Thread.sleep(1000);
        List<ChromiumElement> elements1 = elements.get(1).eles(By.xpath("./tbody/tr"));
        if (elements1.size() <= 1) {
            LoggerUtils.logger.info("价格域爬取失败");
            return;
        }
        Thread.sleep(1000);
        //
        selectOrCategory();
        Thread.sleep(1000);
        //价格域-监控

        for (int i = 1; i < elements1.size(); i++) {
            ChromiumElement element = elements1.get(i);
            String name = element.innerHtml();
            try {
                element.click().click();
                Thread.sleep(1000);
            } catch (Exception e) {
                continue;
            }

            Thread.sleep(2000);
            elements1 = elements.get(1).eles(By.xpath("./tbody/tr"));
        }
        //流量渠道
        xpath = "//*[@class=\"ecom-dorami-data-card-list-inner-wrapper\"]";
        elements = tab.eles(By.xpath(xpath));
        if (elements.size() <= 1)
            return;
        for (int i = 2; i < 6; i++) {
            ChromiumElement element = elements.get(elements.size() - i);
            String name = element.text();
            element.click().click();
            Thread.sleep(1000);
        }
    }

    public void saveXHr() {
        //保存xhr到磁盘
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

    public void saveFile(String fileDir, DataPacket data) throws Exception {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss-SSS");
        String timestamp = sdf.format(now);
        String target = data.getTarget();
        if (target == "compass_api/shop/product_lander/lander/interface")
            fileDir = fileDir + "compassApiShopProductLanderLanderInterface\\";
        else if (target.contains("compass_api/shop/product/product_chance_market/category_overview_price_band_distribution"))
            fileDir = fileDir + "compassApiShopProductProductChanceMarketCategoryOverviewPriceBandDistribution\\";
        else if (target.contains("compass_api/shop/product/product_chance_market/category_overview_price_analysis_product"))
            fileDir = fileDir + "compassApiShopProductProductChanceMarketCategoryOverviewPriceAnalysisProduct\\";
        else if (target.contains("compass_api/shop/product/child_category_by_industry_id"))
            fileDir = fileDir + "compassApiShopProductChildCategoryByIndustryId\\";
        // 示例URL和响应体（这里需要替换为实际值）
        if (!DiskIoUtils.isExist(fileDir)) {
            DiskIoUtils.createDir(fileDir);
        }
        String url = data.url();
        String requestBody = data.request().postData();
        String responseBody = data.response().rawBody();
        // 构造文件路径
        String filePath = "";
        filePath = fileDir + timestamp + ".txt";
        // 写入文件
        FileIoUtils.writeToFile(filePath, "url: " + url + " Request body: " + requestBody + " Response body: " + responseBody);
    }


}
