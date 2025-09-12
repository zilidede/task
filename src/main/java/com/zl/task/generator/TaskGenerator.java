package com.zl.task.generator;


import com.zl.dao.generate.LocalTaskDO;
import com.zl.utils.excel.other.ExcelReaderUtils;
import com.zl.utils.log.LoggerUtils;
import com.zl.utils.uuid.UUIDGeneratorUtils;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

// 修改日志： 自动生产每日任务
// 修改时间：2024-07-23
// 修改内容：本地主机计划任务生成器
// 修改内容：每天不断循环 执行每日任务 小时级任务 和一次性任务 和 间隔十分钟任务 10秒任务

public class TaskGenerator {
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(3);

    public static void main(String[] args) {
        scheduleDailyTask();
        scheduleHourlyTask();
        scheduleOneTimeTask();

    }

    private static void scheduleDailyTask() {
        // 每日任务执行器
        long delay = calculateDelayUntilNextExecution(18, 03); // 设定每日任务在每天的9点执行
        scheduler.scheduleAtFixedRate(() -> {
            LoggerUtils.logger.info("每日任务执行：" + new Date());
            // 新增每日任务
            List<Map<String, String>> list = null;
            List<LocalTaskDO> taskDOS = new ArrayList<>();
            try {
                list = ExcelReaderUtils.readExcel("./data/task/每日任务队列.xlsx");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            for (Map<String, String> map : list) {
                String s1 = map.get("类型");
                if (s1 != null)
                    if (map.get("类型").equals("每日")) {
                        LocalTaskDO localTaskDO = new LocalTaskDO();
                        localTaskDO.setStatus(0);
                        localTaskDO.setName(map.get("名称"));
                        localTaskDO.setContent(map.get("内容"));
                        localTaskDO.setId(UUIDGeneratorUtils.generateCustomUUID());
                        localTaskDO.setStartTime(new Date());
                        localTaskDO.setEndTime(new Date());
                        taskDOS.add(localTaskDO);
                    }
            }
            try {
                LocalTaskCommon.save(taskDOS);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


        }, delay, TimeUnit.DAYS.toMillis(1), TimeUnit.MILLISECONDS);
    }

    private static void scheduleHourlyTask() {
        //小时任务执行器
        scheduler.scheduleAtFixedRate(() -> {
            LoggerUtils.logger.info("小时任务执行：" + new Date());
            List<Map<String, String>> list = null;
            List<LocalTaskDO> taskDOS = new ArrayList<>();

            try {
                list = ExcelReaderUtils.readExcel("./data/task/每日任务队列.xlsx");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            for (Map<String, String> map : list) {
                String s1 = map.get("类型");
                if (s1 != null)
                    if (map.get("类型").equals("小时")) {
                        LocalTime now = LocalTime.now();
                        int hour = now.getHour();
                        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        LocalTaskDO localTaskDO = new LocalTaskDO();
                        localTaskDO.setStatus(0);
                        localTaskDO.setName(map.get("名称"));
                        localTaskDO.setContent(hour + "&" + map.get("内容"));
                        localTaskDO.setId(UUIDGeneratorUtils.generateCustomUUID());
                        localTaskDO.setStartTime(new Date());
                        taskDOS.add(localTaskDO);
                    }
            }
            try {
                LocalTaskCommon.save(taskDOS);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            //        }, 0, TimeUnit.HOURS.toMillis(1), TimeUnit.MILLISECONDS);
        }, 0, 60 * 50, TimeUnit.SECONDS); //  立即执行小时任务 每个50分钟检查


    }

    private static void scheduleOneTimeTask() {
        // 一次性任务执行器
        scheduler.schedule(() -> {
            LoggerUtils.logger.info("一次性任务执行：" + new Date());
            List<Map<String, String>> list = null;
            List<LocalTaskDO> taskDOS = new ArrayList<>();
            try {
                list = ExcelReaderUtils.readExcel("./data/task/每日任务队列.xlsx");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            for (Map<String, String> map : list) {
                String s1 = map.get("类型");
                if (s1 != null)
                    if (map.get("类型").equals("一次")) {
                        LocalTaskDO localTaskDO = new LocalTaskDO();
                        localTaskDO.setStatus(0);
                        localTaskDO.setName(map.get("名称"));
                        localTaskDO.setContent(map.get("内容"));
                        localTaskDO.setId(UUIDGeneratorUtils.generateCustomUUID());
                        localTaskDO.setStartTime(new Date());
                        localTaskDO.setEndTime(new Date());
                        taskDOS.add(localTaskDO);
                    }
            }
            try {
                LocalTaskCommon.save(taskDOS);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 0, TimeUnit.SECONDS); // 立即执行一次性任务
    }

    private static void scheduleTenSecondsTask() {
        scheduler.scheduleAtFixedRate(() -> {
            System.out.println("每10秒任务执行：" + java.time.LocalDateTime.now());
            // 在这里放置每10秒任务的代码
        }, 0, 10, TimeUnit.SECONDS);
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
}

//



