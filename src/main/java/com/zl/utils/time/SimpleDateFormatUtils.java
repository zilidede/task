package com.zl.utils.time;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SimpleDateFormatUtils {
    // 默认的日期格式
    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    // 静态的 SimpleDateFormat 实例
    private static SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);

    // 私有构造函数，防止实例化
    private SimpleDateFormatUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    // 获取当前日期时间的字符串表示
    public static String formatCurrentDate() {
        return dateFormat.format(new Date());
    }

    // 设置新的日期格式
    public static void setDateFormat(String pattern) {
        dateFormat = new SimpleDateFormat(pattern);
    }

    // 获取当前的日期格式
    public static String getCurrentDateFormat() {
        return dateFormat.toPattern();
    }

    // 示例方法，格式化指定日期
    public static String formatDate(Date date) {
        return dateFormat.format(date);
    }

    public static Date parserDate(String date) {
        try {
            return dateFormat.parse(date);
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] args) {
        // 获取当前日期时间的字符串表示
        String currentDate = SimpleDateFormatUtils.formatCurrentDate();
        System.out.println("Current Date: " + currentDate);

        // 设置新的日期格式
        SimpleDateFormatUtils.setDateFormat("yyyy/MM/dd HH:mm:ss");

        // 获取当前的日期格式
        String currentDateFormat = SimpleDateFormatUtils.getCurrentDateFormat();
        System.out.println("Current Date Format: " + currentDateFormat);
    }
}

