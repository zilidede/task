package com.zl.task.craw.live;

import com.ll.drissonPage.base.By;
import com.ll.drissonPage.element.ChromiumElement;
import com.zl.task.craw.base.CompassPageTurn;
import com.zl.task.craw.list.CrawBaseDouYinList;
import com.zl.task.vo.task.taskResource.TaskVO;
import com.zl.utils.log.LoggerUtils;
import com.zl.utils.other.Ini4jUtils;

import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CrawCompassLive extends CrawBaseDouYinList {
    private Integer hour = 0;
    private  int crawCount = 0;
    private final int maxCrawCount = 100;
    public CrawCompassLive() throws Exception {
        Ini4jUtils.loadIni("./data/config/config.ini");
        Ini4jUtils.setSectionValue("list");
        setXhrSaveDir(Ini4jUtils.readIni("xhrDir"));
        Ini4jUtils.loadIni("./data/task/xhr.ini");
        Ini4jUtils.setSectionValue("list");
        setXhrList(Ini4jUtils.traSpecificSection("list"));
    }

    public void run(TaskVO vo) throws Exception {
        Lock lock = new ReentrantLock();
        lock.lock();
        try {
            openEnterUrl("https://compass.jinritemai.com/shop/chance/live-rank?from_page=%2Fshop%2Flive-list");
            getTab().listen().start(getXhrList()); //监听商品榜单xhr
            Thread.sleep(4000);
            // 爬取直播交易小时版
            //在hour 等于10：00 -11：00 进行每日校监。
            if (vo.getTaskDesc().indexOf("小时榜") >= 0) {
                crawLiveHourList(vo);
            } else{

            }
               // crawEBusVideoDayList(vo);
        } finally {
            lock.unlock();
        }
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
            LoggerUtils.logger.info("今日爬取" + getHour() + vo.getTaskDesc() + "行业商品榜单");
            String []strings=vo.getTaskDesc().split("&");
            selectTime(strings[1], strings[0]);
           // selectCategory(strings[2]);
            vo.setStatus(craw(vo));
            //save();
            LoggerUtils.logger.info("已爬取今日" + vo.getTaskDesc() + "已选行业商品榜单");
            Thread.sleep(2000);
        }
    }
    public int craw(TaskVO task) throws Exception {
        String []strings=task.getTaskDesc().split("&");
        //选择爬取的类目
        String xpath = "//*[@style=\"width: 264px; height: 32px;\"]";
        ChromiumElement element = getTab().ele(By.xpath(xpath)); //获取行业类型选择框
        Thread.sleep(1000);
        element.click().click(); //打开行业选择框
        xpath = "//*[@class=\"rc-virtual-list-holder-inner\"]";
        List<ChromiumElement> elements = getTab().eles(By.xpath(xpath)); //获取行业选择框
        Thread.sleep(1000);
        int len = elements.size();
        if (len < 1) {
            LoggerUtils.logger.warn("获取行业选择框失败");
            return -1;
        }
        List<ChromiumElement> elements1 =elements.get(1).eles(By.xpath("./li"));
        for (int i = 0; i < elements1.size(); i++) {
            String name = elements1.get(i).text();
            if(!name.equals(strings[2]))
                continue;
            else{
                elements1.get(i).click().click();
            }
            LoggerUtils.logger.info("选择行业：" + name);

            Thread.sleep(1000);
            //打开第二子类目
            xpath = "//*[@class=\"rc-virtual-list-holder-inner\"]";
            elements = getTab().eles(By.xpath(xpath)); //获取行业选择框
            Thread.sleep(1000);
           len = elements.size();
            if (len < 1) {
                LoggerUtils.logger.warn("获取行业选择框失败");
                return -1;
            }
            List<ChromiumElement> elements2 =elements.get(2).eles(By.xpath("./li"));
            for (int j = 0; j < elements2.size(); j++) {
                name = elements2.get(j).text();
                elements2.get(j).click().click();
                Thread.sleep(3000);
                //检测是否存在三级类目
                xpath = "//*[@class=\"rc-virtual-list-holder-inner\"]";
                List<ChromiumElement> elements3 = getTab().eles(By.xpath(xpath)); //获取行业选择框
                Thread.sleep(1000);
                if(elements3.size() == 4){
                    List<ChromiumElement> elements4 =elements3.get(3).eles(By.xpath("./li"));
                    for (int k = 1; k < elements4.size(); k++) {
                        name = elements4.get(k).text();
                        try {
                            elements4.get(k).click().click();
                        }
                        catch (Exception e){
                            LoggerUtils.logger.info("子类目选择失败：" + name);
                            break;
                        }
                        Thread.sleep(3000);
                        //爬取自营和合作列表
                        xpath="//*[@id=\"root\"]/div[2]/div/form/div[2]/div[1]/div/div/div[2]/div/div/div/div/div[1]";
                        ChromiumElement element11 = getTab().ele(By.xpath(xpath));
                        Thread.sleep(1000);
                        if(element11.text().equals("自营")){
                            element11.click().click();
                            Thread.sleep(1000);
                        }
                        else {
                            continue;
                        }
                        CompassPageTurn.crawCompassListOne(getTab(),name);// 爬取类目列表
                        xpath="//*[@id=\"root\"]/div[2]/div/form/div[2]/div[1]/div/div/div[2]/div/div/div/div/div[2]";
                        element11 = getTab().ele(By.xpath(xpath));
                        Thread.sleep(1000);
                        if(element11.text().equals("合作")){
                            element11.click().click();
                            Thread.sleep(1000);
                        }
                        else {
                            continue;
                        }
                        CompassPageTurn.crawCompassListOne(getTab(),name);// 爬取类目列表
                        if (crawCount++ > maxCrawCount) {
                            LoggerUtils.logger.warn("翻页次数超过最大限制，休眠1小时");
                            Thread.sleep(60 * 60 * 1000);
                            crawCount = 0;
                        }
                        //重新打开行业类型选择框
                        xpath = "//*[@style=\"width: 264px; height: 32px;\"]";
                        element = getTab().ele(By.xpath(xpath)); //获取行业类型选择框
                        Thread.sleep(1000);
                        element.click().click(); //打开行业选择框
                        Thread.sleep(1000);
                    }

                }
                else{
                    ////dowork
                    //打开行业选择框
                    xpath = "//*[@style=\"width: 264px; height: 32px;\"]";
                    element = getTab().ele(By.xpath(xpath)); //获取行业类型选择框
                    Thread.sleep(1000);
                    element.click().click(); //打开行业选择框
                    Thread.sleep(1000);
                }

            }

        }

       return 0;

    }
    public void crawCompassListOne(String s) throws Exception {
        //first //爬取已选择类目榜单;
        crawCompassList();


    }
    public boolean crawCompassList() throws Exception {
        //爬取已经选择的抖店罗盘榜单-翻页操作
        try {
            getTab().runJs("var q=document.documentElement; q.scrollTop = q.scrollHeight"); //滚动到底部部
            Thread.sleep(1000);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        try
            {
                pageTurnTwo(); //翻页操作
            }
        catch (Exception e) {
            e.printStackTrace();

        }

        //
        //保存xhr文件
        save();
        try {
            getTab().runJs("window.scrollTo({ top: 0, behavior: 'smooth' });"); //滚动顶部
            Thread.sleep(1000);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean pageTurnTwo() throws InterruptedException {
        //翻页操作-查看更多
        List<ChromiumElement> elements = null;
        String xpath = "//*[@class=\"ecom-pagination ecom-table-pagination ecom-table-pagination-right\"]/li";
        try {
            elements = getTab().eles(By.xpath(xpath)); //获取翻页列表
            Thread.sleep(1000);
        } catch (Exception ex) {
            ex.printStackTrace();
            LoggerUtils.logger.warn("获取失败，页面数为空");
            return false;
        }
        int len = -1;
        if (elements.size() == 0)
            return false;
        Integer len1 = 0;
        String s1 = elements.get(0).text().replace("共", "").replace("条", "");
        if (s1.equals("")) {
            s1 = elements.get(elements.size() - 2).attr("title");
            len = Integer.parseInt(s1);
        } else {

            try {
                len1 = Integer.parseInt(s1);
            } catch (Exception ex) {
                LoggerUtils.logger.warn("获取失败，页面数为空");
                return false;
            }
            len = len1 / 10 + (len1 % 10 == 0 ? 0 : 1);
            if (len < 0) {
                return false;
            }
        }
        for (int i = 0; i < len-1; i++) {
            try {
                xpath = "//*[@class=\"ecom-pagination-next\"]";
                ChromiumElement element = getTab().ele(By.xpath(xpath));
                element.click().click();
                Thread.sleep(3000);
                if (crawCount++ > maxCrawCount) {
                    LoggerUtils.logger.warn("翻页次数超过最大限制，休眠1小时");
                    Thread.sleep(60 * 60 * 1000);
                    crawCount = 0;

                }
            } catch (Exception ex) {
                LoggerUtils.logger.warn("翻页失败");
                ex.printStackTrace();
                break;
            }

        }
        return true;
    }

    public void selectTime(String time,String timeType) throws InterruptedException {

        if (timeType.indexOf("小时榜") >= 0) {
            String strings = time.replace(":00", "");
            hour= Integer.parseInt(strings);
        }
        //选择时间；
        String xpath = "//*[@class=\"ecom-radio-group ecom-radio-group-outline\"]/label";
        List<ChromiumElement> elements = getTab().eles(By.xpath(xpath)); //获取时间类型选择框
        Thread.sleep(1000);
        int len = elements.size();
        if (len < 1) {
            LoggerUtils.logger.warn("选择抖店榜单时间类型失败");
            return;
        }
        if (len == 5) {
            if (timeType.equals("小时榜")) {
                elements.get(0).click().click();
                //
                xpath = "//*[@class=\"ecom-select ecom-select-single ecom-select-show-arrow\"]";
                ChromiumElement elements1 = getTab().ele(By.xpath(xpath)); //选择小时选择框
                elements1.click().click();
                Thread.sleep(1000);
                xpath = "//*[@class=\"ecom-select-item ecom-select-item-option\"]";
                List<ChromiumElement> elements2 = getTab().eles(By.xpath(xpath));
                for (ChromiumElement element : elements2) {
                    String s=element.attr("title");
                    if (s.equals(time)) {
                        element.click().click();  //选择小时点
                        Thread.sleep(1000);
                        break;
                    }
                }
            } else if (timeType.equals("近一天")) {
                elements.get(1).click().click();
                Thread.sleep(1000);
            } else if (timeType.equals("近七天")) {
                elements.get(2).click().click();
                Thread.sleep(1000);
            } else if (timeType.equals("近30天")) {
                elements.get(3).click().click();
                Thread.sleep(1000);
            } else {
                //自定义
                elements.get(4).click().click();

                Thread.sleep(1000);
            }


        }
    }
    public void selectCategory(String categoryName) throws InterruptedException {
        String xpath = "//*[@style=\"width: 264px; height: 32px;\"]";
        ChromiumElement element = getTab().ele(By.xpath(xpath)); //获取行业类型选择框
        Thread.sleep(1000);
        element.click().click();
        Thread.sleep(1000);
        xpath = "//*[@class=\"rc-virtual-list-holder-inner\"]";
        List<ChromiumElement> elements = getTab().eles(By.xpath(xpath)); //获取行业选择框
        Thread.sleep(1000);
        int len = elements.size();
        if (len < 1) {
            LoggerUtils.logger.warn("获取行业选择框失败");
            return;
        }
        List<ChromiumElement> elements1 =elements.get(0).eles(By.xpath("./li"));
        for (ChromiumElement element1 : elements1) {
            String s=element1.text();
            if(s.equals(categoryName)){
                element1.click().click();
                Thread.sleep(1000);
                LoggerUtils.logger.info("选择行业成功："+s);
                return;
            }
        }
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }
}
