package com.zl.task.craw.list;

import com.ll.drissonPage.base.By;
import com.ll.drissonPage.element.ChromiumElement;
import com.zl.task.vo.task.TaskVO;
import com.zl.utils.log.LoggerUtils;
import com.zl.utils.other.Ini4jUtils;

import java.util.List;

//爬取抖音店铺榜单
public class CrawDouYinShopList extends CrawBaseDouYinList {
    CrawDouYinShopList() throws Exception {
        Ini4jUtils.loadIni("./data/config/config.ini");
        Ini4jUtils.setSectionValue("list");
        setXhrSaveDir(Ini4jUtils.readIni("xhrDir"));
        Ini4jUtils.loadIni("./data/task/xhr.ini");
        setXhrList(Ini4jUtils.traSpecificSection("list"));
    }

    public void select(TaskVO task) throws InterruptedException {
        //确定爬取范围；
        String s = task.getTaskDesc();
        String[] strings = s.split("&");
        selectTime(strings[0]);
        selectListType(strings[0]);
        selectListRank(strings[2]);

    }

    public void selectListType(String s) throws InterruptedException {
        //选择榜单类型 分总榜，飙升，搜索榜；
        String xpath = "//*[@style=\"transform: translate(0px, 0px);\"]"; // 榜单list表单
        List<ChromiumElement> elements = getTab().eles(By.xpath(xpath)).get(0).eles(By.xpath("./div"));
        Thread.sleep(1000);
        for (ChromiumElement element : elements) {
            String text = element.text();
            if (text.indexOf(s) >= 0) {
                element.click().click();
                Thread.sleep(1000);
                break;
            }
        }
    }

    public void selectTime(String time) throws InterruptedException {
        //选择时间；
        String hour = "";
        if (time.indexOf("小时榜") >= 0) {
            String[] strings = time.split("-");
            hour = strings[1];
        }
        String xpath = "//*[@class=\"ecom-radio-group ecom-radio-group-outline\"]/label";
        List<ChromiumElement> elements = getTab().eles(By.xpath(xpath));
        Thread.sleep(1000);
        int len = elements.size();
        if (len < 1) {
            LoggerUtils.logger.warn("选择抖店榜单时间类型失败");
            return;
        }
        if (len == 4) {
            if (time.equals("近一天")) {
                elements.get(0).click().click();
                Thread.sleep(1000);
            } else if (time.equals("近七天")) {
                elements.get(1).click().click();

                Thread.sleep(1000);
            } else if (time.equals("近30天")) {
                elements.get(2).click().click();

                Thread.sleep(1000);
            } else {
                //自定义
                elements.get(3).click().click();

                Thread.sleep(1000);
            }

        } else if (len == 5) {
            if (time.equals("实时") || time.indexOf("小时榜") >= 0) {
                elements.get(0).click().click();
                Thread.sleep(2000);
                //选择小时榜
                if (hour.length() > 0) {
                    String xpath1 = "//*[@style=\"width: 120px;\"]";
                    ChromiumElement element1 = getTab().eles(By.xpath(xpath1)).get(0); //打开小时榜选择框
                    element1.click().click();
                    Thread.sleep(1000);
                    xpath1 = "//*[@style=\"display: flex; flex-direction: column;\"]/div";
                    List<ChromiumElement> elements1 = getTab().eles(By.xpath(xpath1)); //获取小时榜选择框内容列表
                    Thread.sleep(1000);
                    for (ChromiumElement element : elements1) {
                        String text = element.text();
                        if (text.indexOf(hour) >= 0) {
                            element.click().click();
                        }
                    }
                }
            } else if (time.equals("近一天")) {
                elements.get(1).click().click();

                Thread.sleep(1000);
            } else if (time.equals("近七天")) {
                elements.get(2).click().click();

                Thread.sleep(1000);
            } else if (time.equals("近30天")) {
                elements.get(3).click().click();

                Thread.sleep(1000);
            } else {
                elements.get(4).click().click();

                Thread.sleep(1000);
            }

        }
    }

    public void selectListRank(String s) throws InterruptedException {
        //选择榜单类型 分直播 商品卡 各渠道；
        String xpath = "//*[@style=\"transform: translate(0px, 0px);\"]"; // 榜单list表单
        List<ChromiumElement> elements = getTab().eles(By.xpath(xpath)).get(2).eles(By.xpath("./div"));
        Thread.sleep(1000);
        for (ChromiumElement element : elements) {
            String text = element.text();
            if (text.indexOf(s) >= 0) {
                element.click().click();
                Thread.sleep(1000);
                break;
            }
        }

    }

    public void craw(TaskVO task) throws Exception {
        selectCategory(task);
        crawCompassListOne(task.getTaskDesc()); //遍历爬取子类目
    }

    public void crawCompassListOne(String s) throws Exception {
        //first //爬取已选择类目榜单;
        String xpath = "//*[@style=\"height: 164px; overflow-y: auto; overflow-anchor: none;\"]";
        List<ChromiumElement> elements1 = getTab().eles(By.xpath(xpath));
        Thread.sleep(1000);
        List<ChromiumElement> elements2 = elements1.get(1).eles(By.xpath("./div[1]/div[1]/li")); //子类目选择列表
        Thread.sleep(1000);
        for (int j = 2; j < elements2.size(); j++) {
            elements2.get(j).click().click();
            Thread.sleep(1000);
            System.out.println(elements2.get(j).text());
            xpath = "//*[@style=\"height: 164px; overflow-y: auto; overflow-anchor: none;\"]";
            elements1 = getTab().eles(By.xpath(xpath));
            Thread.sleep(2000);
            List<ChromiumElement> elements4 = elements1.get(2).eles(By.xpath("./div[1]/div[1]/li"));
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

    public void selectCategory(TaskVO task) throws InterruptedException {
        String s = task.getTaskDesc();
        String[] s1 = s.split("&");
        String[] strings = s1[s1.length - 1].split("-");//获取类目名
        String xpath = "//*[@class=\"ecom-cascader-picker\"]";
        List<ChromiumElement> elements = getTab().eles(By.xpath(xpath)); //打开行业选择框
        Thread.sleep(1000);
        elements.get(0).click().click();
        Thread.sleep(2000);
        // 选择行业
        xpath = "//*[@style=\"display: flex; flex-direction: column;\"]";
        elements = getTab().eles(By.xpath(xpath));
        String s11 = elements.get(0).text();
        elements = getTab().eles(By.xpath(xpath)).get(0).eles(By.xpath("./li"));
        Integer ilen = Integer.parseInt(strings[1]);
        elements.get(ilen).click().click(); //行业类目选择-选择行业；
        Thread.sleep(1000);
    }
}
