package com.zl.task.craw.main;

import com.ll.drissonPage.page.ChromiumTab;
import com.zl.task.craw.keyword.CrawSeleniumOceanEngineKeyWords;
import com.zl.task.craw.market.CrawSeleniumDouYinCategoryList;
import com.zl.task.craw.weather.CrawCityWeather;
import com.zl.task.save.Saver;
import com.zl.task.vo.task.taskResource.DefaultTaskResourceCrawTabList;
import com.zl.task.vo.task.taskResource.TaskVO;
import com.zl.utils.io.FileIoUtils;
import com.zl.utils.log.LoggerUtils;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

//单主机多线程-电商市场爬取测试
public class CrawEcommerceMarketTestCase {
    public static void main(String[] args) throws Exception {
        runEveryDay();
    }
    public static void runEveryDay() throws InterruptedException {
        int i = 0;
        ParameterizedSearchKeywordsThread keywordsThread = new ParameterizedSearchKeywordsThread(DefaultTaskResourceCrawTabList.getTabList().get(i++));
        ParameterizedWeatherThread weatherThread = new ParameterizedWeatherThread(DefaultTaskResourceCrawTabList.getTabList().get(i++));
        ParameterizedMarketThread marketThread = new ParameterizedMarketThread(DefaultTaskResourceCrawTabList.getTabList().get(i++));
        keywordsThread.start(); //每日执行一次 巨量云图搜索词爬取；-csv下载
        Thread.sleep(1000 * 2);
     //   weatherThread.start(); //2小时一次天气
        Thread.sleep(1000 * 2);
  //      marketThread.start();
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
            long delay = calculateDelayUntilNextExecution(currentHour, currentMinute + 4); // 设定每日任务在每天的9点执行
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
            String [] categorys= FileIoUtils.readFile("./data/task/抖店市场大盘类目").split("\r\n");
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
    static class ParameterizedWeatherThread extends Thread {
        private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(0);
        ChromiumTab tab;

        public ParameterizedWeatherThread(ChromiumTab tab) {
            this.tab = tab;
        }
        @Override
        public void run() {
            //每小时进行一次保存
            LoggerUtils.logger.info("开始执行小时天气任务");
            scheduler.scheduleAtFixedRate(() -> {
                System.out.println("小时任务执行：" + new Date());
                LocalTime now = LocalTime.now();
                int hour = now.getHour();
                try {
                    doWork();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                    // LoggerUtils.logger.error(now+hour+"小时级保存每日爬取记录失败");
                }
            }, 0, 120 * 54, TimeUnit.SECONDS); //  立即执行小时任务 每个50分钟检查
        }

        public void doWork() throws Exception {
            LoggerUtils.logger.debug("初始化CrawCityWeather对象");
            CrawCityWeather crawler;
            try {
                crawler = new CrawCityWeather();
                crawler.setTab(tab);
            }
            catch (Exception e){
                LoggerUtils.logger.error("初始化CrawCityWeather对象失败");
                throw new RuntimeException(e);
            }
            LoggerUtils.logger.debug("开始爬取天气小时榜");
            crawler.run(new TaskVO(1, "爬取天气小时榜"));
            LoggerUtils.logger.debug("爬取天气小时榜结束");
        }


    }
    static class ParameterizedSearchKeywordsThread extends Thread {
        //巨量云图搜索词
        private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(0);
        //天气
        private final ChromiumTab tab;

        ParameterizedSearchKeywordsThread(ChromiumTab tab) {
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
            //每天执行爬取固定类目日榜 凌晨1点执行
            LocalTime now = LocalTime.now();
            // 获取当前小时和分钟
            int currentHour = now.getHour();

            int currentMinute = now.getMinute();

            // 如果分钟大于30分钟，则将小时加1
            if (currentMinute > 58) {
                currentHour = currentHour + 1;
            }
            //currentHour=15;
            long delay = calculateDelayUntilNextExecution(currentHour, currentMinute + 2); // 设定每日任务在每天的9点执行
            scheduler.scheduleAtFixedRate(() -> {
                //云图搜索词
                CrawSeleniumOceanEngineKeyWords crawler = null;
                try {
                    crawler = new CrawSeleniumOceanEngineKeyWords();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                String rName = "";
                int i = 1;
                try {
                    Saver.save();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                try {
                    doWork();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }, delay, TimeUnit.DAYS.toMillis(1), TimeUnit.MILLISECONDS);
        }
        public void  doWork() throws Exception {
            //chromium浏览器标签页，用于爬虫操作
            CrawSeleniumOceanEngineKeyWords crawler;
            String categoryFilePath= "./data/task/全部云图行业.txt";
            try {
                crawler = new CrawSeleniumOceanEngineKeyWords();
                crawler.setTab(tab);
                crawler.setFlag(true);
                crawler.setSecondFlag(false);
            } catch (Exception e) {
                // 记录日志并重新抛出
                LoggerUtils.logger.error("初始化爬虫失败: " + e.getMessage());
                //  System.err.println("初始化爬虫失败: " + e.getMessage());
                throw new RuntimeException("初始化爬虫失败", e);
            }
            CrawSearchKeyword.crawYunTuSearchKeyword(crawler,categoryFilePath);
        }


    }

}
