package com.zl.task.craw.main;

import com.ll.drissonPage.page.ChromiumTab;
import com.zl.task.craw.market.CrawSeleniumDouYinCategoryList;
import com.zl.task.vo.task.taskResource.DefaultTaskResourceCrawTabList;
import com.zl.task.vo.task.taskResource.TaskVO;
import com.zl.utils.io.FileIoUtils;
import com.zl.utils.log.LoggerUtils;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

// 爬取单类目信息的类
public class CrawCategoryTestCase {
    public static void main(String[] args) throws Exception {
        int i=0;
        CrawEcommerceMarketTestCase.ParameterizedMarketThread marketThread = new CrawEcommerceMarketTestCase.ParameterizedMarketThread(DefaultTaskResourceCrawTabList.getTabList().get(i++));
        marketThread.start();
    }
    static class ParameterizedMarketThread extends Thread {
        private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(0);
        //市场
        private final ChromiumTab tab;

        ParameterizedMarketThread(ChromiumTab tab) {
            this.tab = tab;
        }

        private static long calculateDelayUntilNextExecution(int hourOfDay, int minuteOfHour) {
            long now = System.currentTimeMillis();
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(now);
            int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
            int currentMinute = calendar.get(Calendar.MINUTE);
            if (hourOfDay < currentHour || (hourOfDay == currentHour && minuteOfHour <= currentMinute)) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minuteOfHour);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            return calendar.getTimeInMillis() - now;
        }

        @Override
        public void run() {
            //每日执行爬取固定类目市场
            // 获取当前时间
            LocalTime now = LocalTime.now();

            // 获取当前小时和分钟
            int currentHour = now.getHour();
            int currentMinute = now.getMinute();

            // 如果分钟大于58分钟，则将小时加1
            if (currentMinute > 58) {
                currentHour = currentHour + 1;
            }
            long delay = calculateDelayUntilNextExecution(currentHour, currentMinute + 1); // 设定每日任务在每天的9点执行
            scheduler.scheduleAtFixedRate(() -> {
                try {
                    doWork();
                } catch (Exception e) {
                    LoggerUtils.logger.error("爬取市场大盘记录失败");
                    throw new RuntimeException(e);
                }
            }, delay, TimeUnit.DAYS.toMillis(1), TimeUnit.MILLISECONDS);
        }
        public  void doWork() throws Exception {
            LoggerUtils.logger.debug("初始化CrawSeleniumDouYinCategoryLis对象");
            CrawSeleniumDouYinCategoryList crawler = null;
            try {
                crawler = new CrawSeleniumDouYinCategoryList();
            } catch (Exception e) {
                LoggerUtils.logger.debug("初始化CrawSeleniumDouYinCategoryLis对象失败");
                throw new RuntimeException(e);
            }
            crawler.setTab(tab);
            LoggerUtils.logger.info("开始爬取市场大盘:");
            String [] categorys= FileIoUtils.readFile("./data/task/抖店市场大盘类目").split("\n");
            for(String category:categorys){
                LoggerUtils.logger.info("爬取市场大盘:"+category);
                crawMarketAppointCategory(crawler,category);
                LoggerUtils.logger.info("爬取市场大盘成功:"+category);
            }
        }
        public  void crawMarketAppointCategory( CrawSeleniumDouYinCategoryList crawler,String categoryName) throws Exception {
            try {
                TaskVO taskVO=new TaskVO(1, "抖店罗盘类目榜单");
                taskVO.setTaskDesc(categoryName);
                crawler.run(taskVO);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }

    }
}
