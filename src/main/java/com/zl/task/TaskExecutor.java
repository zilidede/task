package com.zl.task;

import com.ll.drissonPage.page.ChromiumTab;
import com.zl.dao.generate.HourLiveRankDO;
import com.zl.dao.generate.HourLiveRankDao;
import com.zl.task.main.CrawSearchKeyWordTestCase;
import com.zl.task.craw.account.CrawHuiTunAccount;
import com.zl.task.craw.base.x.DefaultCrawSeleniumDouYinList;
import com.zl.task.craw.keyword.CrawSeleniumDouHotSearchKeyWords;
import com.zl.task.craw.keyword.CrawSeleniumOceanEngineKeyWords;
import com.zl.task.craw.list.CrawSeleniumDouYinList;
import com.zl.task.craw.market.CrawSeleniumDouYinCategoryList;
import com.zl.task.craw.weather.CrawCityWeather;
import com.zl.task.vo.task.taskResource.DefaultTaskResourceCrawTabList;
import com.zl.task.save.Saver;
import com.zl.task.save.db.SaveToPgSql;
import com.zl.task.save.parser.account.SaveHuiTunAccount;
import com.zl.task.save.parser.list.SaveHourLiveList;
import com.zl.task.vo.task.taskResource.TaskVO;
import com.zl.utils.io.DiskIoUtils;
import com.zl.utils.io.FileIoUtils;
import com.zl.utils.jdbc.generator.jdbc.DefaultDatabaseConnect;
import com.zl.utils.log.LoggerUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


// 任务执行者单元
public class TaskExecutor {
    public static void main(String[] args) throws Exception {
       // multipleThreadsCraw();
        CrawSearchKeyWordTestCase.crawYunTuCategorySecondSearchKeyWord();
    }

    public static void multipleThreadsCraw() throws InterruptedException, SQLException {
        //多线程tab爬取 hourListThread 与dayListThread 不能同时运行同一page;
        Integer i = 0;

        // ParameterizedHourListThread hourListThread = new ParameterizedHourListThread(DefaultTaskResourceCrawTabList.getTabList().get(i++));
        //   ParameterizedHuiTunLiveThread huiTunLiveThread = new ParameterizedHuiTunLiveThread(DefaultTaskResourceCrawTabList.getTabList().get(i++));
        //     ParameterizedDayListThread dayListThread = new ParameterizedDayListThread(DefaultTaskResourceCrawTabList.getTabList().get(i++));
            ParameterizedUnHourListThread unHourListThread= new ParameterizedUnHourListThread(DefaultTaskResourceCrawTabList.getTabList().get(i++),"./data/task/抖店罗盘小时榜.txt");
        //   ParameterizedUnHourListThread unHourListThread2= new ParameterizedUnHourListThread(DefaultTaskResourceCrawTabList.getTabList().get(i++),"./data/task/抖店罗盘小时榜2.txt");
        ParameterizedSaverThread saverThread = new ParameterizedSaverThread();
        ParameterizedWeatherThread weatherThread = new ParameterizedWeatherThread(DefaultTaskResourceCrawTabList.getTabList().get(i++));
        ParameterizedSearchKeywordsThread keywordsThread = new ParameterizedSearchKeywordsThread(DefaultTaskResourceCrawTabList.getTabList().get(i++));
        ParameterizedMarketThread marketThread = new ParameterizedMarketThread(DefaultTaskResourceCrawTabList.getTabList().get(i++));
        keywordsThread.start(); //每日执行一次 巨量云图搜索词爬取；-csv下载
        Thread.sleep(1000 * 2);
        weatherThread.start(); //2小时一次天气
        Thread.sleep(1000 * 2);
      // saverThread.start(); //每小时进行数据保存；
        Thread.sleep(1000 * 2);
        marketThread.start();
        Thread.sleep(1000 * 2);
        /*
        //  huiTunLiveThread.start(); //每天3小时执行一次-爬取灰豚数据直播数据爬取当前时间段前3个小时
        Thread.sleep(2000); //
        //   hourListThread.start(); //每小时执行一次
        Thread.sleep(2000);
        //   unHourListThread.start(); //每日执行完24小时抖店直播交易小时榜单；-1
        Thread.sleep(2000);
        // unHourListThread2.start();//每日执行完24小时抖店直播交易小时榜单2；-1
        Thread.sleep(2000);
        //  marketThread.start(); //每日抖电市场大盘 -4
        Thread.sleep(1000 * 2);
        keywordsThread.start(); //每日执行一次 巨量云图搜索词爬取；-3
        Thread.sleep(1000 * 2);
        //  dayListThread.start(); //每日执行一次日 抖音短视频榜单爬取 2
        Thread.sleep(1000 * 2);

        /*
         unHourListThread.start(); //每日执行完24小时抖店直播交易小时榜单；-1
          Thread.sleep(2000);
        unHourListThread2.start();//每日执行完24小时抖店直播交易小时榜单2；-1
        Thread.sleep(2000);
        marketThread.start(); //每日抖电市场大盘 -4
        Thread.sleep(1000 * 2);
     //   keywordsThread.start(); //每日执行一次 巨量云图搜索词爬取；-3
        Thread.sleep(1000 * 2);
     //   dayListThread.start(); //每日执行一次日 抖音短视频榜单爬取 2
        Thread.sleep(1000 * 2);
       // hourListThread.start(); //每小时执行一次，抖音直播交易榜；
        Thread.sleep(2000);
        // saverThread.start();///每小时进行数据保存；
        Thread.sleep(1000 * 2);

     //   dayListThread.start(); //每日执行一次日 抖音短视频榜单爬取 2

         */
        //  Thread.sleep(1000 * 2);


    }
    //小时级别爬取天气
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
            CrawCityWeather crawler = new CrawCityWeather();
            crawler.setTab(tab);
            crawler.run(new TaskVO(1, "爬取天气小时榜"));
        }


    }

    static class ParameterizedHuiTunLiveThread extends Thread {
        private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(0);
        //每日任务-爬取灰豚数据直播数据
        private final ChromiumTab tab;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        private final HourLiveRankDao hourLiveRankDao = new HourLiveRankDao(DefaultDatabaseConnect.getConn());

        ParameterizedHuiTunLiveThread(ChromiumTab tab) throws SQLException {
            this.tab = tab;
        }

        @Override
        public void run() {
            LoggerUtils.logger.info("每小时执行灰豚直播数据爬取任务：" + new Date());
            scheduler.scheduleAtFixedRate(() -> {
                try {
                    doWork();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }, 0, 60 * 180, TimeUnit.SECONDS);
        }

        public void doWork() throws Exception {
            Map<String, Integer> map = getAccountRoomId();
            List<HourLiveRankDO> volist = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                String key = entry.getKey();
                String[] string = key.split("&");
                HourLiveRankDO vo = new HourLiveRankDO();
                vo.setRoomId(Long.parseLong(string[0]));
                vo.setAuthorId(Long.parseLong(string[1]));
                volist.add(vo);
            }
            CrawHuiTunAccount crawler = new CrawHuiTunAccount(tab, "huiTunLive", "S:\\data\\task\\爬虫\\huiTunLive\\");
            for (HourLiveRankDO vo : volist) {
                try {
                    crawler.crawlLiveDetail(vo);
                    crawler.crawLiveRecord(vo, 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //保存到数据库
            try {
                Saver.syn();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            String sDir = "S:\\data\\task\\爬虫\\huiTunLive\\";
            //获取环境变量
            LocalDate currentDate = LocalDate.now();
            String computerName = System.getenv("COMPUTERNAME");
            if (computerName == null) {
                // 如果上面的方法不奏效，尝试使用USERNAME环境变量
                computerName = System.getenv("USERNAME");
            }
            String dir = String.format("%s%s\\list-%s", sDir, currentDate, computerName);
            if (DiskIoUtils.isExist(dir + "huiTunLive-ZL\\")) {
                SaveHuiTunAccount saver = new SaveHuiTunAccount();
                saver.save(dir + "huiTunLive-ZL\\"); //
            }

        }

        public Map<String, Integer> getAccountRoomId() throws SQLException, IOException {
            Map<String, Integer> result = new HashMap<>();
            String s = FileIoUtils.readFile("./data/task/待爬取账号.txt");
            s = s.replace(" ", "");
            String[] strings = s.split("\r\n");
            for (String string : strings) {
                if (string.equals(""))
                    continue;
                HourLiveRankDO vo = new HourLiveRankDO();
                String[] s1 = string.split("&");
                vo.setRoomId(Long.parseLong(s1[0]));
                vo.setAuthorId(Long.parseLong(s1[1]));
                result.put(s1[0] + "&" + s1[1], 0);
            }
            List<Integer> categoryIds = getCategoryIds();


            // 获取当天的日期
            LocalDate currentDate = LocalDate.now();
            // 定义时间格式
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            // 存储结果的列表
            List<String> dateTimes = new ArrayList<>();
            int currenthour = LocalDateTime.now().getHour();
            // 遍历前一日的24小时
            for (int hour = currenthour; hour > currenthour - 3; hour--) {
                LocalDateTime dateTime = LocalDateTime.of(currentDate, LocalTime.of(hour, 0));
                String formattedDateTime = dateTime.format(formatter);
                dateTimes.add(formattedDateTime);
            }
            for (int i = 2; i > 0; i--) {
                for (Integer categoryId : categoryIds) {
                    List<HourLiveRankDO> hourLiveRankDOS = hourLiveRankDao.findHourLiveRankByCriteria(categoryId, dateTimes.get(i), LocalDateTime.parse(dateTimes.get(i), formatter).plusHours(1).format(formatter), 10000);
                    for (HourLiveRankDO hourLiveRankDO : hourLiveRankDOS) {
                        result.put(hourLiveRankDO.getRoomId() + "&" + hourLiveRankDO.getAuthorId(), hourLiveRankDO.getWatchCnt());
                    }
                }
            }
            return result;
        }

        public List<Integer> getCategoryIds() {
            List<Integer> list = new ArrayList<>();
            list.add(20009);
            list.add(20005);
            list.add(20026);
            return list;
        }

    }


    static class ParameterizedUnHourListThread extends Thread {
        private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
        private final ChromiumTab tab;
        private final HourLiveRankDao dao = new HourLiveRankDao(DefaultDatabaseConnect.getConn());
        //爬取天级任务-未被记录小时榜
        private final String filePath;//爬取的类目文件路径

        public ParameterizedUnHourListThread(ChromiumTab tab, String filePath) throws SQLException {
            this.tab = tab;
            this.filePath = filePath;
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
                    crawHourLists(filePath);
                    /*
                    List<Integer> categoryIds = new ArrayList<>();
                    //categoryIds.add(20009); //男装
                    for (Integer categoryId : categoryIds) {
                        // 获取当前日期
                        LocalDate currentDate = LocalDate.now();
                        // 获取前一天的日期
                        LocalDate previousDate = currentDate.minusDays(1);
                        List<Date> dates = dao.findDateHourLiveRank(categoryId,  previousDate.format(formatter), currentDate.format(formatter));
                        List<Integer> integerList = new ArrayList<>();
                        for (Date date : dates) {
                            // 将 java.util.Date 转换为 LocalDateTime
                           integerList.add(date.getHours());
                        }

                        for (Integer hour : integerLists) {
                            crawHourList(hour);
                        }

                    }

                     */

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }, delay, TimeUnit.DAYS.toMillis(1), TimeUnit.MILLISECONDS);
        }

        public void crawHourLists(String filePath) throws Exception {
            //爬取24小时所有时间点的抖店罗盘小时榜
            LoggerUtils.logger.info("爬取抖店罗盘小时榜任务开始：" + filePath);
            CrawSeleniumDouYinList crawler = new CrawSeleniumDouYinList();
            for (int i = 0; i < 24; i++) {
                String s = FileIoUtils.readTxtFile(filePath, "utf-8");
                String[] strings = s.split("\r\n");
                for (String string : strings) {
                    TaskVO taskVO = new TaskVO(1, "抖店罗盘小时榜");
                    taskVO.setTaskDesc(string);
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

        public void crawHourList(Integer hour) throws Exception {
            String s = FileIoUtils.readTxtFile("./data/task/抖店罗盘小时榜.txt", "utf-8");
            String[] strings = s.split("\r\n");
            for (String string : strings) {
                TaskVO taskVO = new TaskVO(1, "抖店罗盘小时榜");
                taskVO.setTaskDesc(string);

                try {
                    DefaultCrawSeleniumDouYinList.setTab(tab);
                    DefaultCrawSeleniumDouYinList.setHour(hour);
                    DefaultCrawSeleniumDouYinList.getInstance().run(taskVO);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }

    static class ParameterizedSaverThread extends Thread {
        private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(0);

        public ParameterizedSaverThread() {

        }

        @Override
        public void run() {
            //每小时进行一次保存
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
            }, 0, 60 * 54, TimeUnit.SECONDS); //  立即执行小时任务 每个50分钟检查
        }

        public void doWork() throws Exception {
            Saver.syn();
            SaveToPgSql.cycleSave(6);

        }


    }


    static class ParameterizedDoubotThread extends Thread {
        //热点宝
        private final ChromiumTab tab;
        private final int page;

        public ParameterizedDoubotThread(ChromiumTab tab) {
            this.tab = tab;
            this.page = 1;
        }

        @Override
        public void run() {
            //热点宝
            try {
                CrawSeleniumDouHotSearchKeyWords crawSeleniumDouHot = new CrawSeleniumDouHotSearchKeyWords();
                crawSeleniumDouHot.setTab(tab);
                crawSeleniumDouHot.setPage(400 * page);
                crawSeleniumDouHot.run(new TaskVO(1, "test"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static class ParameterizedHourListThread extends Thread {
        private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(0);
        //直播小时榜
        private final ChromiumTab tab;
        private final HourLiveRankDao hourLiveRankDao = new HourLiveRankDao(DefaultDatabaseConnect.getConn());

        public ParameterizedHourListThread(ChromiumTab tab) throws SQLException {
            this.tab = tab;

        }

        @Override
        public void run() {
            String sDir = "S:\\data\\back\\day\\";
            //每小时执行爬取固定类目小时榜-保存内容到数据库-爬取灰豚直播数据-》列出异动账号-分析异动原因-生产excel表单；
            scheduler.scheduleAtFixedRate(() -> {
                System.out.println("小时任务执行：" + new Date());
                LocalTime now = LocalTime.now();
                int hour = now.getHour();
                if (hour < 10 && hour > 0) {
                    return;
                }
                String s = FileIoUtils.readTxtFile("./data/task/抖店罗盘小时榜.txt", "utf-8");
                String[] strings = s.split("\r\n");

                for (String string : strings) {
                    TaskVO taskVO = new TaskVO(1, "抖店罗盘小时榜");
                    taskVO.setTaskDesc(string);
                    try {
                        DefaultCrawSeleniumDouYinList.setTab(tab);
                        DefaultCrawSeleniumDouYinList.getInstance().run(taskVO);
                        //同步
                        /*
                        LocalDate currentDate = LocalDate.now();
                        String computerName = System.getenv("COMPUTERNAME");
                        if (computerName == null) {
                            // 如果上面的方法不奏效，尝试使用USERNAME环境变量
                            computerName = System.getenv("USERNAME");
                        }
                        String dir=String.format("%s%s\\list-%s",sDir,currentDate.toString(),computerName);
                        if (DiskIoUtils.isExist(dir)) {
                            // TODO: 实现保存抖音直播交易榜小时榜的数据;
                            SaveHourLiveList saveHourLiveList= new SaveHourLiveList();
                            saveHourLiveList.save(dir);
                        }
                        //
                        doHuiTunLiveWork();//爬取灰豚数据
                        System.out.println("已爬取当前时间灰豚直播数据");

                         */

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                try {
                    Saver.syn();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                //获取环境变量
                LocalDate currentDate = LocalDate.now();
                String computerName = System.getenv("COMPUTERNAME");
                if (computerName == null) {
                    // 如果上面的方法不奏效，尝试使用USERNAME环境变量
                    computerName = System.getenv("USERNAME");
                }
                String dir = String.format("%s%s\\list-%s", sDir, currentDate, computerName);
                if (DiskIoUtils.isExist(dir)) {
                    // TODO: 实现保存抖音直播交易榜小时榜的数据;
                    SaveHourLiveList saveHourLiveList = null;
                    try {
                        saveHourLiveList = new SaveHourLiveList();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        saveHourLiveList.save(dir);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }

            }, 0, 60 * 55, TimeUnit.SECONDS); //  立即执行小时任务 每个50分钟检查
        }

        public void doHuiTunLiveWork() throws SQLException, IOException {
            Map<String, Integer> map = getAccountRoomId();
            List<HourLiveRankDO> volist = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                String key = entry.getKey();
                String[] string = key.split("&");
                HourLiveRankDO vo = new HourLiveRankDO();
                vo.setRoomId(Long.parseLong(string[0]));
                vo.setAuthorId(Long.parseLong(string[1]));
                volist.add(vo);
            }
            CrawHuiTunAccount crawler = new CrawHuiTunAccount(tab, "huiTunLive", "S:\\data\\task\\爬虫\\huiTunLive\\");
            for (HourLiveRankDO vo : volist) {
                try {
                    crawler.run(vo, 2);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        }

        public Map<String, Integer> getAccountRoomId() throws SQLException, IOException {
            Map<String, Integer> result = new HashMap<>();
// 定义日期时间格式
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:00:00");
            // 设置时区，例如：系统默认时区
            ZoneId zoneId = ZoneId.systemDefault();

            // 获取当前时间
            ZonedDateTime now = ZonedDateTime.now(zoneId);
            System.out.println("当前时间: " + now.format(formatter));

            // 获取前一个小时的时间
            ZonedDateTime oneHourAgo = now.minusHours(1);
            System.out.println("前一个小时: " + oneHourAgo.format(formatter));

            // 获取前两个小时的时间
            ZonedDateTime twoHoursAgo = now.minusHours(2);
            System.out.println("前两个小时: " + twoHoursAgo.format(formatter));
            List<HourLiveRankDO> hourLiveRankDOS = hourLiveRankDao.findHourLiveRankByCriteria(-1, twoHoursAgo.format(formatter), oneHourAgo.format(formatter), 10000);
            for (HourLiveRankDO hourLiveRankDO : hourLiveRankDOS) {
                result.put(hourLiveRankDO.getRoomId() + "&" + hourLiveRankDO.getAuthorId(), hourLiveRankDO.getWatchCnt());
            }
            return result;
        }

        public List<Integer> getCategoryIds() {
            List<Integer> list = new ArrayList<>();
            list.add(20009);
            list.add(20005);
            list.add(20026);
            return list;
        }

    }

    static class ParameterizedDayListThread extends Thread {
        private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(0);
        //list-日榜
        private final ChromiumTab tab;

        public ParameterizedDayListThread(ChromiumTab tab) {
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
            long delay = calculateDelayUntilNextExecution(currentHour, currentMinute + 2); // 设定每日任务在每天的9点执行
            scheduler.scheduleAtFixedRate(() -> {
                LoggerUtils.logger.info("每日视频榜单任务爬取：" + new Date());
                String s = FileIoUtils.readTxtFile("./data/task/抖店罗盘日榜.txt", "utf-8");
                String[] strings = s.split("\r\n");
                CrawSeleniumDouYinList crawler = null;
                try {
                    crawler = new CrawSeleniumDouYinList();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                crawler.setTab(tab);
                for (String string : strings) {
                    TaskVO taskVO = new TaskVO(1, "抖店罗盘日榜");
                    taskVO.setTaskDesc(string);
                    try {
                        crawler.run(taskVO);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }, delay, TimeUnit.DAYS.toMillis(1), TimeUnit.MILLISECONDS);

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
                while (!rName.equals("quit")) {
                    crawler.setTab(tab);
                    crawler.setFlag( true);
                    crawler.setSecondFlag(true);
                    try {
                        Saver.save();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        rName = CrawSeleniumOceanEngineKeyWords.crawAll(crawler, rName);
                    } catch (Exception e) {
                        e.printStackTrace();
                        try {
                            Thread.sleep(1000 * 60);
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                    }

                }
            }, delay, TimeUnit.DAYS.toMillis(1), TimeUnit.MILLISECONDS);
        }


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
                CrawSeleniumDouYinCategoryList crawler = null;
                try {
                    crawler = new CrawSeleniumDouYinCategoryList();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                crawler.setTab(tab);
                try {
                    crawler.run(new TaskVO(1, "抖店罗盘类目榜单"));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }, delay, TimeUnit.DAYS.toMillis(1), TimeUnit.MILLISECONDS);
        }

    }
}
