package com.zl.utils.jdbc.generator.convert;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Param: 数据库字段名称与javabean转化
 * @Auther: zl
 * @Date: 2020/4/27 15:30
 */
public class FieldConvert {
    private static String toLowerFirst(String str) {
        // 首字母小写
        char[] cs = str.toCharArray();
        int cAscii = str.charAt(0);
        if (cAscii < 97)
            cs[0] += 32;
        return String.valueOf(cs);
    }

    public static String toFirstCharUppercase(String str) {
        //字符串第一个字符大写
        char[] buf = new char[str.length()];
        for (int i = 0; i < str.length(); i++) {
            if (i == 0) {
                int cAscii = str.charAt(0);
                if ((cAscii > 96) && (cAscii < 123)) {
                    buf[i] = (char) (cAscii - 32);
                } else
                    buf[i] = str.charAt(i);
            } else
                buf[i] = str.charAt(i);
        }
        return new String(buf);
    }

    public static String toCameCase(String str) {
        //字符串转化为驼峰命名
        if (str.length() <= 0)
            return "";
        String[] strings = str.split("-");
        if (strings.length == 1)
            strings = str.split("_");
        String result = strings[0];
        for (int i = 1; i < strings.length; i++) {
            result = result + toFirstCharUppercase(strings[i]);
        }
        return result;
        // return new String(buf);
    }

    public static String toField(String str) {
        //讲javabean对象名转数据库字段名格式 比如 javaObj -java_obj
        if (str.length() <= 0)
            return "";
        List<String> result = new ArrayList<>();
        int j = 0;
        for (int i = 0; i < str.length(); i++) {
            int cAscii = str.charAt(i);
            if (cAscii >= 65) {
                if (cAscii < 97) {
                    String s = str.substring(j, i);
                    result.add(s);
                    j = i;
                } else {
                }
            }
        }
        result.add(str.substring(j));
        String s1 = "";
        for (String s : result) {
            if (!s.equals(""))
                s1 = s1 + toLowerFirst(s) + "_";
        }
        s1 = s1.substring(0, s1.length() - 1);
        return s1;
        // return new String(buf);
    }

    public static String getShortClassType(String longClassType) {
        //缩减字段类型名称 如 java.lang.Integer = Integer
        if (longClassType.equals("java.lang.Integer")) {
            return "Integer";
        } else if (longClassType.equals("java.lang.String")) {
            return "String";
        } else if (longClassType.equals("java.lang.Long")) {
            return "Long";
        } else if (longClassType.equals("java.lang.Short")) {
            return "Short";
        } else if (longClassType.equals("java.lang.Double")) {
            return "Double";
        } else if (longClassType.equals("java.lang.Float")) {
            return "Float";
        } else if (longClassType.equals("java.lang.Boolean")) {
            return "Boolean";
        } else if (longClassType.equals("java.util.List")) {
            return "String";
        }
        return "";
    }

    public static void main(String[] args) {
        FieldConvert obj = new FieldConvert();
        System.out.println(toField("user"));

    }
}
