package com.zl.task.craw.live;

import com.ll.drissonPage.base.By;
import com.ll.drissonPage.element.ChromiumElement;
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
            openEnterUrl("https://compass.jinritemai.com/shop/live-list?from_page=%2Fshop%2Fvideo%2Foverview");
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
            vo.setStatus(craw(vo));
            //save();
            LoggerUtils.logger.info("已爬取今日" + vo.getTaskDesc() + "已选行业商品榜单");
            Thread.sleep(2000);
        }
    }
    public int craw(TaskVO task) throws Exception {
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
            elements1.get(i).click().click();
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
            for (int j = 1; j < elements2.size(); j++) {
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
                        elements4.get(k).click().click();
                        Thread.sleep(2000);

                        //检测是否存在四级类目
                        xpath = "//*[@class=\"rc-virtual-list-holder-inner\"]";
                        List<ChromiumElement> elements5= getTab().eles(By.xpath(xpath)); //获取行业选择框
                        Thread.sleep(1000);
                        if (elements5.size() == 5){
                            List<ChromiumElement> elements455 =elements5.get(4).eles(By.xpath("./li"));
                            for(int m = 1; m < elements455.size(); m++){
                                name = elements455.get(m).text();
                                elements455.get(m).click().click();
                                Thread.sleep(2000);
                                //dowork
                                //打开行业选择框
                                xpath = "//*[@style=\"width: 264px; height: 32px;\"]";
                                element = getTab().ele(By.xpath(xpath)); //获取行业类型选择框
                                Thread.sleep(1000);
                                element.click().click(); //打开行业选择框
                                Thread.sleep(1000);
                            }
                        }
                        else{
                            //dowork
                            //third
                            //打开行业选择框
                            xpath = "//*[@style=\"width: 264px; height: 32px;\"]";
                            element = getTab().ele(By.xpath(xpath)); //获取行业类型选择框
                            Thread.sleep(1000);
                            element.click().click(); //打开行业选择框
                            Thread.sleep(1000);
                        }


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



               // crawCompassListOne(name);// 爬取类目列表
            }

        }

       return 0;

    }
    public void crawCompassListOne(String s) throws Exception {

        //first //爬取已选择类目榜单;
        String xpath = "//*[@style=\"display: flex; flex-direction: column;\"]";
        List<ChromiumElement> elements1 = getTab().eles(By.xpath(xpath));
        Thread.sleep(1000);
        List<ChromiumElement> elements2 = elements1.get(2).eles(By.xpath("./li"));
        Thread.sleep(1000);
        for (int j = 0; j < elements2.size(); j++) {
            elements2.get(j).click().click();
            Thread.sleep(1000);
            System.out.println(elements2.get(j).text());
            //second
            xpath = "//*[@style=\"display: flex; flex-direction: column;\"]";
            elements1 = getTab().eles(By.xpath(xpath));
            Thread.sleep(2000);
            List<ChromiumElement> elements4 = elements1.get(3).eles(By.xpath("./li"));
            Thread.sleep(1000);
            for (int k = 1; k < elements4.size(); k++) {
                elements4.get(k).click().click();
                Thread.sleep(3000);
                //判断是否三叶子类目
                xpath = "//*[@class=\"rc-virtual-list-holder-inner\"]";
                List<ChromiumElement> elements5 = getTab().eles(By.xpath(xpath));
                Thread.sleep(1000);
                if (elements5.size() > 4) {
                    List<ChromiumElement> elements6 = elements5.get(4).eles(By.xpath("./li"));
                    Thread.sleep(1000);
                    for (int m = 1; m < elements6.size(); m++) {
                        try {
                            elements6.get(m).click().click();
                        } catch (Exception e) {
                            LoggerUtils.logger.info("子类目选择失败：" + s);
                        }
                        crawCompassList();
                        //重新打开行业选择框
                        getTab().runJs("var q=document.documentElement.scrollTop=0"); //滚动到顶部
                        Thread.sleep(1000);
                        xpath = "//*[@class=\"ecom-cascader-picker\"]";
                        List<ChromiumElement> elementss = getTab().eles(By.xpath(xpath)); //打开行业选择框
                        Thread.sleep(1000);
                        elementss.get(0).click().click();
                        Thread.sleep(1000);
                    }
                } else {
                    crawCompassList();
                    //重新打开行业选择框
                    getTab().runJs("var q=document.documentElement.scrollTop=0"); //滚动到顶部
                    Thread.sleep(1000);
                    xpath = "//*[@class=\"ecom-cascader-picker\"]";
                    List<ChromiumElement> elementss = getTab().eles(By.xpath(xpath)); //打开行业选择框
                    Thread.sleep(1000);
                    elementss.get(0).click().click();
                    Thread.sleep(1000);
                }


            }
        }
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
        //选择时间；
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
