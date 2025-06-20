package com.zl.utils.time;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TimeUtils {
    /**
     * 比较两个时间戳，判断它们之间的差值是否小于10秒。
     *
     * @param timestamp1 第一个时间戳（毫秒）
     * @param timestamp2 第二个时间戳（毫秒）
     * @return 如果两个时间戳之间的差值小于10秒，则返回true，否则返回false
     */
    public static long isDifferenceLessThanSeconds(long timestamp1, long timestamp2) {
        // 将毫秒时间戳转换为Instant对象
        Instant instant1 = Instant.ofEpochSecond(timestamp1);
        Instant instant2 = Instant.ofEpochSecond(timestamp2);

        // 计算两个时间戳之间的持续时间
        Duration duration = Duration.between(instant1, instant2);

        // 获取绝对值的秒数差异
        long secondsDifference = Math.abs(duration.getSeconds());

        //单位为秒
        return secondsDifference;
    }

    public static int convertToSeconds(String timeString) {
        int hours = 0;
        int minutes = 0;
        int seconds = 0;

        // 使用正则表达式匹配小时、分钟和秒
        String[] parts = timeString.split("小时|分|秒");

        for (String part : parts) {
            part = part.trim();
            if (!part.isEmpty()) {
                if (timeString.contains("小时") && timeString.indexOf(part) < timeString.indexOf("小时")) {
                    hours = Integer.parseInt(part);
                } else if (timeString.contains("分") && timeString.indexOf(part) < timeString.indexOf("分")) {
                    minutes = Integer.parseInt(part);
                } else if (timeString.contains("秒") && timeString.indexOf(part) < timeString.indexOf("秒")) {
                    seconds = Integer.parseInt(part);
                }
            }
        }

        // 计算总秒数
        return hours * 3600 + minutes * 60 + seconds;
    }

    public static int convertToSecond(String timeString) {
        int days = 0;
        int hours = 0;
        int minutes = 0;
        int seconds = 0;

        // 使用正则表达式匹配天、小时、分钟和秒
        String[] parts = timeString.split("(d|h|m|s)");
        String[] units = timeString.split("[0-9]+");

        for (int i = 0; i < parts.length; i++) {
            if (units[i].contains("d")) {
                days = Integer.parseInt(parts[i]);
            } else if (units[i].contains("h")) {
                hours = Integer.parseInt(parts[i]);
            } else if (units[i].contains("m")) {
                minutes = Integer.parseInt(parts[i]);
            } else if (units[i].contains("s")) {
                seconds = Integer.parseInt(parts[i]);
            }
        }

        return days * 86400 + hours * 3600 + minutes * 60 + seconds;
    }


    public static List<Integer> isExistingHours(List<Integer> existingHours) {
        // System.out.println("2025年2月19日不存在的小时点：");
        List<Integer> result = new ArrayList<>();
        for (int hour = 0; hour < 24; hour++) {
            boolean exists = false;
            for (int existingHour : existingHours) {
                if (hour == existingHour) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                result.add(hour);

            }
        }
        return result;
    }

    public static LocalDateTime convertDateToLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static long calculateDelayUntilNextExecution(int hourOfDay, int minuteOfHour) {
        // 获取当前时间的毫秒数
        long now = System.currentTimeMillis();
        // 创建一个Calendar实例并设置时间为当前时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(now);
        // 获取当前的小时和分钟
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);
        // 如果指定的时间点已经过去，则将日期加一天
        if (hourOfDay < currentHour || (hourOfDay == currentHour && minuteOfHour <= currentMinute)) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        // 设置Calendar实例的时间为指定的小时和分钟，并将秒和毫秒设为0
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minuteOfHour);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        // 计算从当前时间到指定时间点的延迟时间（毫秒）
        return calendar.getTimeInMillis() - now;
    }
}
