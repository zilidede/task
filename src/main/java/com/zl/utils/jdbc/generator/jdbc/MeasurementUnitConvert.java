package com.zl.utils.jdbc.generator.jdbc;

/**
 * @className: com.craw.nd.common-> MeasurementUnitConvert
 * @description: 特殊计量单位换算
 * @author: zl
 * @createDate: 2023-02-16 17:51
 * @version: 1.0
 * @todo:
 */
public class MeasurementUnitConvert {

    public static String covertGoodsMeasurementUnit(String s) {
        String temp = "";
        if (s.indexOf("w+") >= 0) {
            temp = s.replaceAll("w\\+", "0000");
        } else if (s.indexOf("+") >= 0)
            temp = s.replaceAll("\\+", "");
        else
            temp = s;
        return temp;
    }
}
