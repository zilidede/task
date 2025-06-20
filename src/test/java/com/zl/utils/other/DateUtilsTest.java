package com.zl.utils.other;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DateUtilsTest {

    @Test
    void calculateMonthsBetween() {
        System.out.println(DateUtils.calculateMonthsBetween("2020-01-04","2020-02-01"));
    }
}