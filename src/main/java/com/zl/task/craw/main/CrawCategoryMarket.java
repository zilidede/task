package com.zl.task.craw.main;

import com.ll.drissonPage.page.ChromiumTab;
import com.zl.task.craw.goods.CrawCompassGoods;
import com.zl.task.craw.list.CrawSeleniumDouYinList;
import com.zl.task.craw.live.CrawCompassLive;
import com.zl.task.craw.video.CrawCompassVideo;
import com.zl.task.vo.task.taskResource.DefaultTaskResourceCrawTabList;
import com.zl.task.vo.task.taskResource.TaskVO;
import com.zl.utils.io.FileIoUtils;
import com.zl.utils.log.LoggerUtils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

// 集成测试-//单主机多线程-电商单类目市场爬取测试
public class CrawCategoryMarket {
    public static void main(String[] args) throws Exception {
            // 小时直播榜 -每日商品榜 -视频榜单;
        runEveryDay();
    }
    public static void runEveryDay() throws InterruptedException {
        int i = 0;
        ParameterizedLiveListThread hourLiveList = new ParameterizedLiveListThread(DefaultTaskResourceCrawTabList.getTabList().get(i++));
        ParameterizedGoodsThread goodsList = new ParameterizedGoodsThread(DefaultTaskResourceCrawTabList.getTabList().get(i++));
        ParameterizedVideoThread videoList = new ParameterizedVideoThread(DefaultTaskResourceCrawTabList.getTabList().get(i++));
        hourLiveList.start(); //每小时执行一次 
        Thread.sleep(1000 * 2);
        goodsList.start(); //一天一次
        Thread.sleep(1000 * 2);
        videoList.start(); //一天一次；
    }

    static class ParameterizedLiveListThread extends Thread {
        private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
        private ChromiumTab tab;

        //爬取天级任务-未被记录小时榜

        ParameterizedLiveListThread(ChromiumTab tab) {
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
            //每天执行爬取固定类目日榜
            LocalTime now = LocalTime.now();
            // 获取当前小时和分钟
            int currentHour = now.getHour();
            int currentMinute = now.getMinute();
            // 如果分钟大于30分钟，则将小时加1
            if (currentMinute > 58) {
                currentHour = currentHour + 1;
            }
            if (currentHour < 11) {
                currentHour = 11;
            }
            // 定义日期格式
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            long delay = calculateDelayUntilNextExecution(currentHour, currentMinute + 1); // 设定每日任务在每天的9点执行
            scheduler.scheduleAtFixedRate(() -> {
                try {
                    LoggerUtils.logger.info("每日24小时榜任务执行：" + new Date());
                    List<Integer> integerLists = new ArrayList<>();
                    // dowork
                    CrawCompassLive crawler= new CrawCompassLive();
                    doWork(crawler);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }, delay, TimeUnit.DAYS.toMillis(1), TimeUnit.MILLISECONDS);
        }
        public void doWork(CrawCompassLive crawler) throws Exception {
            String filePath = "./data/task/抖店罗盘小时榜.txt";
            //爬取24小时所有时间点的抖店罗盘小时榜
            LoggerUtils.logger.info("爬取抖店罗盘小时榜任务开始：" + filePath);
            for (int i = 0; i < 24; i++) {
                String s = FileIoUtils.readTxtFile(filePath, "utf-8");
                String[] strings = s.split("\n");
                for (String string : strings) {
                    TaskVO taskVO = new TaskVO(1, "抖店罗盘小时榜");
                    String taskDes = String.format("小时榜&%s:00&%s", i, string);
                    taskVO.setTaskDesc(taskDes);
                    try {
                        crawler.setTab(tab);
                        crawler.setHour(i);
                        crawler.run(taskVO);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    static class ParameterizedGoodsThread extends Thread {
        private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
        private ChromiumTab tab;

        //爬取天级任务-未被记录小时榜

        ParameterizedGoodsThread(ChromiumTab tab) {
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
            //每天执行爬取固定类目日榜
            LocalTime now = LocalTime.now();
            // 获取当前小时和分钟
            int currentHour = now.getHour();
            int currentMinute = now.getMinute();
            // 如果分钟大于30分钟，则将小时加1
            if (currentMinute > 58) {
                currentHour = currentHour + 1;
            }
            if (currentHour < 11) {
                currentHour = 11;
            }
            // 定义日期格式
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            long delay = calculateDelayUntilNextExecution(currentHour, currentMinute + 2); // 设定每日任务在每天的9点执行
            scheduler.scheduleAtFixedRate(() -> {
                try {
                    LoggerUtils.logger.info("每日抖店罗盘商品日榜任务执行：" + new Date());
                    List<Integer> integerLists = new ArrayList<>();
                    // dowork
                    CrawCompassGoods crawler= new CrawCompassGoods(tab);
                    doWork(crawler);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }, delay, TimeUnit.DAYS.toMillis(1), TimeUnit.MILLISECONDS);

        }
        public void doWork(CrawCompassGoods crawler) throws Exception {
            String filePath = "./data/task/抖店罗盘商品日榜";
            //爬取24小时所有时间点的抖店罗盘小时榜
            LoggerUtils.logger.info("爬取抖店罗盘商品榜任务开始：" + filePath);
            String s = FileIoUtils.readTxtFile(filePath, "utf-8");
            String[] strings = s.split("\n");
            for (String string : strings) {
                    TaskVO taskVO = new TaskVO(1, "抖店罗盘商品日榜");
                    taskVO.setTaskDesc(string);
                    try {
                        crawler.setTab(tab);
                        crawler.openEnterUrl("https://compass.jinritemai.com/shop/chance/product-rank?from_page=%2Fshop%2Fcommodity%2Fproduct-list");
                        Thread.sleep(5000);
                        crawler.craw(taskVO);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }

        }


    }

    static class ParameterizedVideoThread extends Thread {
        private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
        private ChromiumTab tab;
        //爬取天级任务-未被记录小时榜
        ParameterizedVideoThread(ChromiumTab tab) {
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
            //每天执行爬取固定类目日榜
            LocalTime now = LocalTime.now();
            // 获取当前小时和分钟
            int currentHour = now.getHour();
            int currentMinute = now.getMinute();
            // 如果分钟大于30分钟，则将小时加1
            if (currentMinute > 58) {
                currentHour = currentHour + 1;
            }
            if (currentHour < 11) {
                currentHour = 11;
            }
            // 定义日期格式
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            long delay = calculateDelayUntilNextExecution(currentHour, currentMinute + 3); // 设定每日任务在每天的9点执行
            scheduler.scheduleAtFixedRate(() -> {
                try {
                    LoggerUtils.logger.info("每日抖店罗盘视频日榜执行：" + new Date());
                    List<Integer> integerLists = new ArrayList<>();
                    // dowork
                    CrawCompassVideo crawler= new CrawCompassVideo(tab);
                    doWork(crawler);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }, delay, TimeUnit.DAYS.toMillis(1), TimeUnit.MILLISECONDS);
        }

        public void doWork(CrawCompassVideo crawler) throws Exception {
            String filePath = "./data/task/抖店罗盘视频日榜";
            //爬取24小时所有时间点的抖店罗盘小时榜
            LoggerUtils.logger.info("爬取抖店罗盘视频日榜任务开始：" + filePath);
            String s = FileIoUtils.readTxtFile(filePath, "utf-8");
            String[] strings = s.split("\n");
            for (String string : strings) {
                TaskVO taskVO = new TaskVO(1, "抖店罗盘视频日榜");
                taskVO.setTaskDesc(string);
                try {
                    crawler.setTab(tab);
                    crawler.openEnterUrl("https://compass.jinritemai.com/shop/chance/video-rank?from_page=%2Fshop%2Fvideo%2Foverview");
                    Thread.sleep(5000);
                    crawler.craw(taskVO);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }

}
