package com.zl.utils.random;

import java.util.Random;

/*
 * @Description: 生成随机整数
 * @Param:
 * @Author: zl
 * @Date: 2019/12/1 23:49
 */
public class NumRandomUtils {
    private static final Random random;
    private static long seed;

    private NumRandomUtils() {

    }

    static {
        seed = System.currentTimeMillis();
        random = new Random(seed);
    }

    public static long getSeed() {
        return seed;
    }

    public static void setSeed(long seed) {
        NumRandomUtils.seed = seed;
    }

    // integer
    public static int uniform(int upperBound) {
        return random.nextInt(upperBound);
    }

    public static int uniform(int lowerBound, int upperBound) {
        return lowerBound + uniform(upperBound - lowerBound);
    }

    public static double uniform() {
        return random.nextDouble();
    }

    public static double uniform(double upperBound) {
        return upperBound * uniform();
    }

    public static double uniform(double lowerBound, double upperBound) {
        return lowerBound + uniform(upperBound - lowerBound);
    }

    public static void main(String[] args) {

    }
}
