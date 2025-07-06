package com.zl.task.craw.base;

import com.ll.drissonPage.base.By;
import com.ll.drissonPage.element.ChromiumElement;
import com.ll.drissonPage.page.ChromiumTab;
import com.zl.task.craw.SaveXHR;
import com.zl.task.save.Saver;
import com.zl.utils.log.LoggerUtils;

import java.util.List;

// 抖店罗盘翻页
public class CompassPageTurn {

    public static void main(String[] args) throws Exception {
    }
    public static void crawCompassListOne(ChromiumTab tab,String s) throws Exception {
        //first //爬取已选择类目榜单;
        crawCompassList(tab);
    }
    public static boolean crawCompassList(ChromiumTab tab) throws Exception {
        //爬取已经选择的抖店罗盘榜单-翻页操作
        try {
            tab.runJs("var q=document.documentElement; q.scrollTop = q.scrollHeight"); //滚动到底部部
            Thread.sleep(1000);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        try
        {
            pageTurnTwo(tab); //翻页操作
        }
        catch (Exception e) {
            e.printStackTrace();

        }

        //
        //保存xhr文件
        try {
            tab.runJs("window.scrollTo({ top: 0, behavior: 'smooth' });"); //滚动顶部
            Thread.sleep(1000);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean pageTurnTwo(ChromiumTab tab) throws InterruptedException {
        //翻页操作-查看更多
        List<ChromiumElement> elements = null;
        String xpath = "//*[@class=\"ecom-pagination ecom-table-pagination ecom-table-pagination-right\"]/li";
        try {
            elements = tab.eles(By.xpath(xpath)); //获取翻页列表
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
        if(len-1>9){
           len=9;
        }
        Thread.sleep(2000);
        for (int i = 0; i < len-1; i++) {
            try {
                xpath = "//*[@class=\"ecom-pagination-next\"]";
                ChromiumElement element = tab.ele(By.xpath(xpath));
                Thread.sleep(2000);
                element.click().click();
                Thread.sleep(4000);

            } catch (Exception ex) {
                LoggerUtils.logger.warn("翻页失败");
                ex.printStackTrace();
                break;
            }

        }
        return true;
    }

}
