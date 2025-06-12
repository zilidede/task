package com.zl.utils.random;

/*
 * @Description: 生成随机字符串
 * @Param:
 * @Author: zl
 * @Date: 2019/12/21 11:37
 */

public class StringRandomUtils {
    public static String generateRandomChineseString(int len) {
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < len; i++) {
            buffer.append(CharRandomUtils.uniformChinese(0));
        }
        return buffer.toString();
    }

    public static void main(String[] args) {
        System.out.println(generateRandomChineseString(5));
    }
}
