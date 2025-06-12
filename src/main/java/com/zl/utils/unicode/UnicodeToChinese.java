package com.zl.utils.unicode;

public class UnicodeToChinese {
    public static void main(String[] args) {
        // Unicode 编码的字符串
        String unicodeString = "\\u4F60\\u597D\\u4E16\\u754C";

        // 将 Unicode 编码的字符串转换为中文
        String chineseString = toChinese(unicodeString);

        // 输出转换后的中文字符串
        System.out.println("Converted Chinese string: " + chineseString);
    }

    public static String toChinese(String unicodeString) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        int pos = 0; // 位置索引

        while (i < unicodeString.length()) {
            if (unicodeString.charAt(i) == '\\') {
                // 如果下一个字符是 'u'，则表示开始了一个 Unicode 编码
                if (i + 5 <= unicodeString.length() && unicodeString.charAt(i + 1) == 'u') {
                    // 读取接下来的 4 个字符
                    String hexStr = unicodeString.substring(i + 2, i + 6);
                    // 将十六进制转换为字符
                    char ch = (char) Integer.parseInt(hexStr, 16);
                    sb.append(ch);
                    // 跳过 Unicode 编码
                    i += 6;
                } else {
                    // 如果不是 'u'，则直接添加字符
                    sb.append(unicodeString.charAt(i));
                    i++;
                }
            } else {
                sb.append(unicodeString.charAt(i));
                i++;
            }
        }
        return sb.toString();
    }
}