package com.zl.utils.random;

/*
 * @Description: 随机字符
 * @Param:
 * @Author: zl
 * @Date: 2019/12/2 22:27
 */
public class CharRandomUtils {
    private String StringEncoders = "ASCII";

    private CharRandomUtils() {
    }

    private static final CharRandomUtils instance = new CharRandomUtils();

    public static CharRandomUtils getInstance() {
        return instance;
    }

    public void setStringEncoders(String stringEncoders) {
        StringEncoders = stringEncoders;
    }

    public String getStringEncoders() {
        return StringEncoders;
    }

    public static char uniformAscii() {
        return (char) NumRandomUtils.uniform(127);
    }

    public static char uniformChinese(int n) {
        // -1->随机产生中文字符，0-产生0X4e00-0x9fa5的中文字符。
        if (n == -1)
            n = NumRandomUtils.uniform(6);
        if (n == 0)
            return (char) NumRandomUtils.uniform(0X4e00, 0x9fa5);
        else if (n == 1)
            return (char) NumRandomUtils.uniform(0X20000, 0x2A6D6);
        else if (n == 2)
            return (char) NumRandomUtils.uniform(0X2A700, 0x2B734);
        else if (n == 3)
            return (char) NumRandomUtils.uniform(0X2B820, 0x2CEA1);
        else if (n == 4)
            return (char) NumRandomUtils.uniform(0X2CEB0, 0x2EBE0);
        else
            return (char) NumRandomUtils.uniform(0X3400, 0x4DB5);
    }

    public static String uniformChinese(int n, int len) {

        char[] a = new char[len];
        for (int i = 0; i < len; i++) {
            a[i] = uniformChinese(n);
        }
        String s = new String(a);
        return s;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10000; i++)
            System.out.println(CharRandomUtils.uniformChinese(-1));
    }
}
