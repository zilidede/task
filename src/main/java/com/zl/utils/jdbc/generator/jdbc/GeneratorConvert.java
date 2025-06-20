package com.zl.utils.jdbc.generator.jdbc;

import com.google.gson.JsonElement;

/**
 * @className: com.craw.nd.common-> Convert
 * @description:
 * @author: zl
 * @createDate: 2023-01-03 16:46
 * @version: 1.0
 * @todo:
 */
public class GeneratorConvert {
    public static String getJsonFieldType(JsonElement element) {
        //json 对象类型转化
        if (element.isJsonArray())
            return "";
        if (element.isJsonNull())
            return "";
        if (element.isJsonObject())
            return "";
        if (element.isJsonPrimitive()) {
            String s = element.toString();
            //string long int double;
            if (s.indexOf("\"") >= 0) {
                return "String";
            } else {
                try {
                    if (s.indexOf(".") > 0) {
                        Double.valueOf(element.getAsDouble());
                        return "Double";
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                try {
                    if (s.equals("false") || element.toString().equals("true")) {
                        Boolean.valueOf(element.getAsBoolean());
                        return "Boolean";
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                try {
                    if (s.length() < 9) {
                        Integer.valueOf(element.getAsInt());
                        return "Integer";
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                try {
                    Long.valueOf(element.getAsLong());
                    return "Long";
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        return "String";

    }

    public static String javaBeanTypeToPotSqlType(String javaBeanType) {
        // java bean 数据类型转 pgslq数据类型；
        if (javaBeanType.equals("Integer")) {
            return "int4";
        } else if (javaBeanType.equals("Long")) {
            return "int4";
        } else if (javaBeanType.equals("Double")) {
            return "int4";
        } else if (javaBeanType.equals("Float")) {
            return "int4";
        } else if (javaBeanType.equals("String")) {
            return "int4";
        } else if (javaBeanType.equals("Date")) {
            return "int4";
        } else if (javaBeanType.equals("Long")) {
            return "int4";
        } else
            return "";
    }
}
