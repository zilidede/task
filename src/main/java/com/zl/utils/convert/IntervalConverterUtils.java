package com.zl.utils.convert;

public class IntervalConverterUtils {

    /**
     * 将区间字符串转换为整形数据（这里选择区间的平均值）
     * @param intervalStr 区间字符串，格式为 "¥数字-¥数字"
     * @return 区间的平均值
     */
    public static long convertIntervalToInteger(String intervalStr) {
        // 去掉 "¥" 和 "万"
        String cleanStr = intervalStr.replace("¥", "").replace("万", "");

        // 使用正则表达式提取数字
        String regex = "(\\d+(\\.\\d+)?)";
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
        java.util.regex.Matcher matcher = pattern.matcher(cleanStr);
        double[] numbers = new double[2];
        int index = 0;

        while (matcher.find() && index < 2) {
            String numStr = matcher.group(1);
            numbers[index++] = Double.parseDouble(numStr);
        }

        // 如果字符串包含 "万"，则需要将数字乘以 10000
        if (intervalStr.contains("万")) {
            numbers[0] *= 10000;
            numbers[1] *= 10000;
        }

        // 计算平均值并转换为长整型
        return (long) ((numbers[0] + numbers[1]) / 2);
    }

    public static void main(String[] args) {
        // 测试示例
        String[] intervals = {"¥100万-¥250万", "¥75万-¥100万", "¥50万-¥75万", "¥2,500-¥5,000"};

        for (String interval : intervals) {
            long result = convertIntervalToInteger(interval);
            System.out.println("Interval: " + interval + ", Average Value: " + result);
        }
    }
}


