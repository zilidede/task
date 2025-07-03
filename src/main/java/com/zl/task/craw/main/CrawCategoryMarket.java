package com.zl.task.craw.main;

import com.ll.drissonPage.page.ChromiumTab;
import com.zl.task.vo.task.taskResource.DefaultTaskResourceCrawTabList;
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
        int i = 0;
        ParameterizedLiveListThread hourLiveList = new ParameterizedLiveListThread(DefaultTaskResourceCrawTabList.getTabList().get(i++));
        ParameterizedGoodsThread goodsList = new ParameterizedGoodsThread(DefaultTaskResourceCrawTabList.getTabList().get(i++));
        ParameterizedVideoThread videoList = new ParameterizedVideoThread(DefaultTaskResourceCrawTabList.getTabList().get(i++));
        hourLiveList.start(); //每小时执行一次 巨量云图搜索词爬取；-csv下载
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


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }, delay, TimeUnit.DAYS.toMillis(1), TimeUnit.MILLISECONDS);
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
            long delay = calculateDelayUntilNextExecution(currentHour, currentMinute + 1); // 设定每日任务在每天的9点执行
            scheduler.scheduleAtFixedRate(() -> {
                try {
                    LoggerUtils.logger.info("每日24小时榜任务执行：" + new Date());
                    List<Integer> integerLists = new ArrayList<>();
                    // dowork


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }, delay, TimeUnit.DAYS.toMillis(1), TimeUnit.MILLISECONDS);
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
            long delay = calculateDelayUntilNextExecution(currentHour, currentMinute + 1); // 设定每日任务在每天的9点执行
            scheduler.scheduleAtFixedRate(() -> {
                try {
                    LoggerUtils.logger.info("每日24小时榜任务执行：" + new Date());
                    List<Integer> integerLists = new ArrayList<>();
                    // dowork


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }, delay, TimeUnit.DAYS.toMillis(1), TimeUnit.MILLISECONDS);
        }
    }

}
