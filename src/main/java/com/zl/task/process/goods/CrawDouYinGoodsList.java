package com.zl.task.process.goods;

import com.ll.drissonPage.base.By;
import com.ll.drissonPage.element.ChromiumElement;
import com.zl.task.craw.list.CrawBaseDouYinList;
import com.zl.task.vo.task.taskResource.TaskVO;
import com.zl.utils.log.LoggerUtils;
import com.zl.utils.other.Ini4jUtils;

import java.util.List;

//爬取抖店商品榜单-
public class CrawDouYinGoodsList extends CrawBaseDouYinList {
    public CrawDouYinGoodsList() throws Exception {
        Ini4jUtils.loadIni("./data/config/config.ini");
        Ini4jUtils.setSectionValue("list");
        setXhrSaveDir(Ini4jUtils.readIni("xhrDir"));
        Ini4jUtils.loadIni("./data/task/xhr.ini");
        setXhrList(Ini4jUtils.traSpecificSection("list"));
    }

    public static void main(String[] args) throws Exception {
        CrawDouYinGoodsList craw = new CrawDouYinGoodsList();
        craw.run(new TaskVO(1, "爬取抖音商品榜单"));
    }

    @Override
    public void select(TaskVO task) throws InterruptedException {
        //确定爬取范围；
        String s = task.getTaskDesc();
        String[] strings = s.split("&");
        selectTime(strings[0]);
        selectListRank(strings[1]);
        // selectIndustry("");
    }

    public void selectTime(String time) throws InterruptedException {
        //选择时间；
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
            if (time.equals("实时") || time.equals("小时榜")) {
                elements.get(0).click().click();
                Thread.sleep(1000);
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
        String xpath = "//*[@style=\"transform: translate(0px, 0px);\"]/div"; // 榜单list表单
        List<ChromiumElement> elements = getTab().eles(By.xpath(xpath));
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

    @Override
    public void craw(TaskVO task) throws Exception {
        String s = task.getTaskDesc();
        String[] s1 = s.split("&");
        String[] strings = s1[s1.length - 1].split("-");//获取类目名
        String xpath = "//*[@class=\"ecom-cascader-picker\"]";
        List<ChromiumElement> elements = getTab().eles(By.xpath(xpath)); //打开行业选择框
        Thread.sleep(1000);
        elements.get(0).click().click();
        Thread.sleep(2000);
        xpath = "//*[@style=\"display: flex; flex-direction: column;\"]";
        elements = getTab().eles(By.xpath(xpath)).get(0).eles(By.xpath("./li"));
        Integer ilen = Integer.parseInt(strings[1]);
        elements.get(ilen).click().click(); //行业类目选择-选择行业；
        Thread.sleep(1000);
        //first
        xpath = "//*[@style=\"display: flex; flex-direction: column;\"]";
        List<ChromiumElement> elements1 = getTab().eles(By.xpath(xpath));
        Thread.sleep(1000);
        List<ChromiumElement> elements2 = elements1.get(1).eles(By.xpath("./li"));
        Thread.sleep(1000);
        for (int j = 0; j < elements2.size(); j++) {
            elements2.get(j).click().click();
            Thread.sleep(1000);
            System.out.println(elements2.get(j).text());
            //second
            xpath = "//*[@style=\"display: flex; flex-direction: column;\"]";
            List<ChromiumElement> elements3 = getTab().eles(By.xpath(xpath));
            Thread.sleep(1000);
            List<ChromiumElement> elements4 = elements1.get(2).eles(By.xpath("./li"));
            Thread.sleep(1000);
            for (int k = 1; k < elements4.size(); k++) {
                elements4.get(k).click().click();
                Thread.sleep(2000);
                //判断是否三叶子类目
                xpath = "//*[@style=\"display: flex; flex-direction: column;\"]";
                List<ChromiumElement> elements5 = getTab().eles(By.xpath(xpath));
                Thread.sleep(1000);
                if (elements5.size() > 3) {
                    List<ChromiumElement> elements6 = elements5.get(3).eles(By.xpath("./li"));
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
}







