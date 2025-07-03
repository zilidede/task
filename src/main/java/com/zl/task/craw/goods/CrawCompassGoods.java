package com.zl.task.craw.goods;

import com.ll.drissonPage.base.By;
import com.ll.drissonPage.element.ChromiumElement;
import com.ll.drissonPage.page.ChromiumTab;
import com.zl.task.craw.base.CompassPageTurn;
import com.zl.task.craw.base.CrawBaseXHR;
import com.zl.task.vo.task.taskResource.TaskVO;
import com.zl.utils.log.LoggerUtils;

import java.util.List;

// 爬取抖店罗盘商品
public class CrawCompassGoods  extends CrawBaseXHR {
    public CrawCompassGoods(ChromiumTab tab) {
        super(tab);
    }
    public void run(TaskVO vo){

    }

    @Override
    public void craw(TaskVO task) throws Exception {
        String[] strings = task.getTaskDesc().split("&");
        //选择爬取的类目
        String xpath = "//*[@style=\"width: 264px;\"]";
        ChromiumElement element = getTab().ele(By.xpath(xpath)); //获取行业类型选择框
        Thread.sleep(1000);
        element.click().click(); //打开行业选择框
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
                        CompassPageTurn.crawCompassListOne(getTab(),name);// 爬取类目列表
                        xpath = "//*[@style=\"width: 264px;\"]";
                        element = getTab().ele(By.xpath(xpath)); //获取行业类型选择框
                        Thread.sleep(1000);
                        element.click().click(); //打开行业选择框
                        Thread.sleep(1000);
                    }

                } else {
                    ////dowork
                    //打开行业选择框
                    xpath = "//*[@style=\"width: 264px;\"]";
                    element = getTab().ele(By.xpath(xpath)); //获取行业类型选择框
                    Thread.sleep(1000);
                    element.click().click(); //打开行业选择框
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
        if (len == 5) {
            if (timeType.equals("实时")) {
                elements.get(0).click().click();
                Thread.sleep(1000);

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
    public void selectListType(String categoryName) throws InterruptedException {
        //选择时间；
        String xpath = "//*[@style=\"transform: translate(0px, 0px);\"]/div";
        List<ChromiumElement> elements = getTab().eles(By.xpath(xpath)); //获取榜单类型选择框
        Thread.sleep(1000);
        int len = elements.size();
        if (len < 1) {
            LoggerUtils.logger.warn("选择抖店榜单类型失败");
            return;
        }
        if (len == 8) {
            if (categoryName.equals("总榜")) {
                elements.get(0).click().click();
                Thread.sleep(1000);

            } else if (categoryName.equals("搜索榜")) {
                elements.get(1).click().click();
                Thread.sleep(1000);
            } else if (categoryName.equals("直播榜")) {
                elements.get(2).click().click();
                Thread.sleep(1000);
            } else if (categoryName.equals("商品卡榜单")) {
                elements.get(3).click().click();
                Thread.sleep(1000);
            }
            else if (categoryName.equals("短视频榜")) {
                elements.get(5).click().click();
                Thread.sleep(1000);
            }else {

            }
        }
    }

    public void selectPrice(String categoryName) throws InterruptedException {

    }
}
