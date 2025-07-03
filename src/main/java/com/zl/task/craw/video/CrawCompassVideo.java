package com.zl.task.craw.video;

import com.ll.drissonPage.base.By;
import com.ll.drissonPage.element.ChromiumElement;
import com.ll.drissonPage.page.ChromiumTab;
import com.zl.task.craw.base.CompassPageTurn;
import com.zl.task.craw.base.CrawBaseXHR;
import com.zl.task.vo.task.taskResource.TaskVO;
import com.zl.utils.log.LoggerUtils;

import java.util.List;

// 爬取抖店罗盘商品
public  class CrawCompassVideo extends CrawBaseXHR {

    public CrawCompassVideo(ChromiumTab tab) {
        super(tab);
    }

    public void run(TaskVO vo){


    }

    @Override
    public void craw(TaskVO task) throws Exception {
        String[] strings = task.getTaskDesc().split("&");
        String listType=strings[1];
        String xpath="";
        List<ChromiumElement> elementss=null;// 自营 -电商-类目选择框列表；
        ChromiumElement eleCategorySelect = null; //类目选择器
        //设置电商/非电商类目
        if(listType.equals("视频销量榜")){

        }
        else {
            // 选择电商类目或非电商类目：
            xpath = "//*[@class=\"ecom-form-item-control-input-content\"]";
            elementss = getTab().eles(By.xpath(xpath));
            List<ChromiumElement> elementss1 = elementss.get(0).eles("./div/div/div");
            if (strings[2].equals("电商类目")) {
                elementss1.get(0).click().click();
                Thread.sleep(1000);
            } else {
                elementss1.get(0).click().click();
                Thread.sleep(1000);
            }
        }
        //获取类目选择器
        xpath = "//*[@class=\"ecom-form-item-control-input-content\"]";
        elementss = getTab().eles(By.xpath(xpath));
        if(listType.equals("视频销量榜")){
            eleCategorySelect = elementss.get(1);
        }
        else if(listType.equals("引流直播榜")){
            eleCategorySelect = elementss.get(2);
        }
        else if(listType.equals("热门视频榜")){
            eleCategorySelect = elementss.get(1);
        }
        //选择爬取的类目
        eleCategorySelect.click().click(); //打开行业选择框
        xpath = "//*[@class=\"rc-virtual-list-holder-inner\"]";
        List<ChromiumElement> elements = getTab().eles(By.xpath(xpath)); //获取行业选择框
        Thread.sleep(1000);
        int len = elements.size();
        if (len < 1) {
            LoggerUtils.logger.warn("获取行业选择框失败");
            return ;
        }
        List<ChromiumElement> elements1 = elements.get(0).eles(By.xpath("./li"));
        for (int i = 0; i < elements1.size(); i++) {
            String name = elements1.get(i).text();
            if (!name.equals(strings[3]))
                continue;
            else {
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
                return ;
            }
            List<ChromiumElement> elements2 = elements.get(1).eles(By.xpath("./li"));
            for (int j = 0; j < elements2.size(); j++) {
                name = elements2.get(j).text();
                elements2.get(j).click().click();
                Thread.sleep(3000);
                //检测是否存在三级类目
                xpath = "//*[@class=\"rc-virtual-list-holder-inner\"]";
                List<ChromiumElement> elements3 = getTab().eles(By.xpath(xpath)); //获取行业选择框
                Thread.sleep(1000);
                if (elements3.size() == 3) {
                    List<ChromiumElement> elements4 = elements3.get(2).eles(By.xpath("./li"));
                    for (int k = 1; k < elements4.size(); k++) {
                        name = elements4.get(k).text();
                        try {
                            elements4.get(k).click().click();
                        } catch (Exception e) {

                            LoggerUtils.logger.info("子类目选择失败：" + name);
                            continue;
                        }
                        Thread.sleep(3000);
                        if(listType.equals("视频销量榜")||listType.equals("引流直播榜")) {
                            // 选择自营和合作类目
                            //*[@id="root"]/div[2]/div/form/div/div[1]/div/div/div[2]/div/div
                            //*[@id="root"]/div[2]/div/form/div/div[1]/div/div/div[2]/div/div
                           ChromiumElement  element11 = getTab().ele("//*[@id=\"root\"]/div[2]/div/form/div/div[1]/div/div/div[2]/div/div/div/div/div[1]");
                            Thread.sleep(1000);
                            if (element11.text().equals("自营")) {
                                element11.click().click();
                                Thread.sleep(1000);
                            } else {
                                continue;
                            }
                            CompassPageTurn.crawCompassListOne(getTab(), name);// 爬取类目列表
                            element11 = getTab().ele("//*[@id=\"root\"]/div[2]/div/form/div/div[1]/div/div/div[2]/div/div/div/div/div[2]");
                            Thread.sleep(1000);
                            if (element11.text().equals("合作")) {
                                element11.click().click();
                                Thread.sleep(1000);
                            } else {
                                continue;
                            }
                            CompassPageTurn.crawCompassListOne(getTab(), name);// 爬取类目列表
                        }
                        else{
                            CompassPageTurn.crawCompassListOne(getTab(), name);// 爬取类目列表
                        }

                        Thread.sleep(1000);
                        eleCategorySelect.click().click(); //打开行业选择框
                        Thread.sleep(1000);
                    }

                } else {
                    ////dowork
                    //打开行业选择框

                    Thread.sleep(1000);
                    eleCategorySelect.click().click(); //打开行业选择框
                    Thread.sleep(1000);
                }

            }

        }
    }

    @Override
    public void craw() throws InterruptedException {

    }

    public void selectTime(String time,String timeType) throws InterruptedException {
        //选择时间；
        String xpath = "//*[@class=\"ecom-radio-group ecom-radio-group-outline\"]/label";
        List<ChromiumElement> elements = getTab().eles(By.xpath(xpath)); //获取时间类型选择框
        Thread.sleep(1000);
        int len = elements.size();
        if (len < 1) {
            LoggerUtils.logger.warn("选择抖店榜单时间类型失败");
            return;
        }
        if (timeType.equals("近一天")) {
                elements.get(0).click().click();
                Thread.sleep(1000);
            } else if (timeType.equals("近七天")) {
                elements.get(1).click().click();
                Thread.sleep(1000);
            } else if (timeType.equals("近30天")) {
                elements.get(2).click().click();
                Thread.sleep(1000);
            }
    }
    public void selectListType(String categoryName) throws InterruptedException {
        //选择时间；

        String xpath = "//*[@style=\"transform: translate(0px, 0px);\"]";
        List<ChromiumElement> elements1 = getTab().eles(By.xpath(xpath)); //获取时间类型选择框
        Thread.sleep(1000);
        List<ChromiumElement> elements= elements1.get(0).eles(By.xpath("./div"));
        Thread.sleep(1000);
        int len = elements.size();
        if (len < 1) {
            LoggerUtils.logger.warn("选择抖店榜单类型失败");
            return;
        }
        if (len == 6) {
            if (categoryName.equals("视频销量榜")) {
                elements.get(0).click().click();
                Thread.sleep(1000);

            } else if (categoryName.equals("引流直播榜")) {
                elements.get(1).click().click();
                Thread.sleep(1000);
            } else if (categoryName.equals("热门视频榜")) {
                elements.get(2).click().click();
                Thread.sleep(1000);
            } else if (categoryName.equals("图文榜")) {
                elements.get(4).click().click();
                Thread.sleep(1000);
            }

        }
    }
}


