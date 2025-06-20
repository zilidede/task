package com.zl.utils.time;

import org.junit.Test;

import java.util.Date;

public class TimeUtilsTest {

    @Test
    public void convertDateToLocalDateTime() {
        System.out.println(TimeUtils.convertDateToLocalDateTime(new Date()));
    }
}