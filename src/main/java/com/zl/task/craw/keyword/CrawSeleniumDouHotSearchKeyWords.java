package com.zl.task.craw.keyword;

import com.ll.drissonPage.base.By;
import com.ll.drissonPage.element.ChromiumElement;
import com.ll.drissonPage.page.ChromiumPage;
import com.ll.drissonPage.page.ChromiumTab;
import com.zl.config.ConfigIni;
import com.zl.task.impl.ExecutorTaskService;
import com.zl.task.impl.taskResource.TaskResource;
import com.zl.task.vo.task.TaskVO;
import com.zl.utils.log.LoggerUtils;
import com.zl.utils.webdriver.WebDriverUtils;

import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

/**
 * @className: com.craw.nd.service.other.person.Impl.craw.selenium-> CrawOceanEngineSearch
 * @description: 爬取热点宝页面
 * @author: zl
 * @createDate: 2024-07-12 17:14
 * @version: 1.0
 * @todo: 修改日期 ： 2024-07-12
 * 修改内容： 大盘搜索词小时详情爬取
 * 修改日期：2024-10-24；
 * 1、话题榜，视频榜 爬取;
 * 2. 话题高赞视频爬取；
 */
public class CrawSeleniumDouHotSearchKeyWords implements ExecutorTaskService {
    private ChromiumTab tab;
    private Integer page = 1;
    private Integer crawCount = 0;//爬取次数
    private Integer maxCrawPage = 0; //最大页数
    private Integer minCrawPage = 0; //最小页数

    public CrawSeleniumDouHotSearchKeyWords() throws Exception {

    }

    public static void main(String[] args) throws Exception {
        WebDriverUtils webDriverUtils = new WebDriverUtils(ConfigIni.DRIVE_PATH, 9223);
        ChromiumPage chromiumPage = webDriverUtils.getDriver();
        chromiumPage.newTab("https://douhot.douyin.com/square/hotspot?active_tab=hotspot_search&date_window=1&sub_type=3001");
        Thread.sleep(1000 * 12);
        int page = 1;
        //热点宝
        try {
            CrawSeleniumDouHotSearchKeyWords crawSeleniumDouHot = new CrawSeleniumDouHotSearchKeyWords();
            crawSeleniumDouHot.setTab(webDriverUtils.getDriver().getTab(0));
            crawSeleniumDouHot.setPage(300 * page);
            crawSeleniumDouHot.run(new TaskVO(1, "test"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
        minCrawPage = page;
        maxCrawPage = page + 400;
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
    public void run(TaskVO task) throws Exception {
        //跳转到初始页
        String xpath = "";
        LocalTime now = LocalTime.now();
        int oldHour = now.getHour(); //获取老小时
        Thread.sleep(1000 * 20);
        while (true) {
            //最大页操作
            Integer maxPage = getMaxPage();
            Integer page = minCrawPage + crawCount;
            if (page > maxPage) {
                LoggerUtils.logger.info("热点宝搜索词页最小需爬取页数：" + page + "不存在，休眠5分钟，刷新重置");
                Thread.sleep(1000 * 60 * 10);
                tab.refresh();
                continue;
            }
            //当前页操作。
            Integer currentPage = getCurrentPage();
            if (currentPage > maxCrawPage) {
                LoggerUtils.logger.info("热点宝搜索词页当前页数已到达：" + "，最大爬取页数：" + maxPage + "等待下小时重置爬取次数");
                Thread.sleep(1000 * 60 * 5);
                crawCount = 0;
            } else if (currentPage < minCrawPage) {
                LoggerUtils.logger.info("热点宝搜索词页当前页数小于：" + "，最小爬取页数：" + minCrawPage + "跳转页面到已完成最小爬取页面");
                jumpPage(minCrawPage + crawCount);
                continue;
            } else if (currentPage >= minCrawPage && currentPage <= maxCrawPage) {
                try {
                    xpath = " //*[@aria-label=\"下一页\"]";
                    // xpath="//*[@id=\"arco-tabs-0-panel-5\"]/div/div/div[2]/div[1]/div/div/div[2]/div/ul/li[9]";
                    List<ChromiumElement> element = tab.eles(By.xpath(xpath));
                    element.get(0).click().click();
                    Thread.sleep(6000);
                } catch (Exception ex) {
                    LoggerUtils.logger.info("热点宝搜索词页热点宝搜索词当前页数：" + currentPage + "爬取失败" + ex.getMessage() + "，重新刷新页面，跳转页面，等待10秒后重试");
                    tab.refresh();
                    continue;
                }
                crawCount++;
            }
            //小时判断
            now = LocalTime.now();
            int newHour = now.getHour(); //获取老小时
            if (oldHour != newHour) {
                crawCount = 0;
                oldHour = newHour;
            }
        }
    }

    public void jumpPage(int page) throws InterruptedException {
        Thread.sleep(1000 * 10);
        String xpath = "//*[@class=\"arco-input arco-input-size-default arco-pagination-jumper-input\"]";
        ChromiumElement element = tab.ele(By.xpath(xpath));
        element.input(page + "\n");

    }

    public int getMaxPage() throws InterruptedException {
        //获取当前页面最大值
        int maxPage = 9999;
        try {
            String xpath = "//*[@class=\"arco-pagination-list\"]/li";
            List<ChromiumElement> elements = tab.eles(By.xpath(xpath));
            maxPage = Integer.parseInt(elements.get(elements.size() - 2).text());
        } catch (Exception ex) {
            maxPage = -1;
        }


        return maxPage;
    }

    public Integer getCurrentPage() throws InterruptedException {
        String xpath = "//*[@class=\"arco-pagination-item arco-pagination-item-active\"]";
        ChromiumElement element = tab.ele(By.xpath(xpath));
        int page = Integer.parseInt(element.text());
        return page;
    }

}
