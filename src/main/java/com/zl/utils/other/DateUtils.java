package com.zl.utils.other;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @className: com.craw.nd.util-> DateUtils
 * @description: 日期工具类
 * @author: zl
 * @createDate: 2022-12-15 15:39
 * @version: 1.0
 * @todo:
 */
public class DateUtils {
    private static DateUtils instance;
    private static Date nowDate;
    private static final SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");

    public static DateUtils getInstance() throws ParseException {
        if (instance == null) {

            instance = new DateUtils();
        }
        return instance;
    }

    public static String getNowDates() {
        return sf.format(new Date());
    }

    /*
     * 将时间戳转换为时间
     */
    public static Date stampToDate(Long lt) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date(lt * 1000);
        res = simpleDateFormat.format(date);
        return date;
    }

    /*
     * 将时间戳转换为时间
     */
    public static Date stampToDateMin(Long lt) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH");
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return date;
    }

    public static String stampToDates(Long lt) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    /*
     * 将时间转换为时间戳
     */
    public static String dateToStamp(String s) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }

    public static Date getNowDate() throws ParseException {
        return nowDate;
    }

    public static Integer calculateDaysFromYearMonth(Integer year, Integer month) {
        //通过年月计算天数；
        if ((year % 4 == 0) && (year % 100 != 0) || (year % 400 == 0))
            /*判断闰年的方法：年份除以4余数为零且年份除以100余数不为零，或年份除以400余数为零*/ {
            if (month == 2)
                return 29;
            if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12)
                return 31;
            if (month == 4 || month == 6 || month == 9 || month == 11)
                return 30;
        } else {
            if (month == 2)
                return 28;
            if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12)
                return 31;
            if (month == 4 || month == 6 || month == 9 || month == 11)
                return 30;
        }

        return 0;
    }

    public static Boolean compareToNow(Date date) {
        //日期前后判断；
        return date.getTime() - nowDate.getTime() > 0;
    }
}
