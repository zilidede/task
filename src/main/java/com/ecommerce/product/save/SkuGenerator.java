package com.ecommerce.product.save;

import java.text.Normalizer;
import java.util.Random;
import java.util.regex.Pattern;

public class SkuGenerator {

    private static final Random RANDOM = new Random();

    /**
     * 生成符合要求的SKU ID (格式: 供应商缩写-货号-规格编码-唯一后缀)
     * 示例: ABC-12345-SMALL-20240904-1234
     */
    public static String generateSku(String supplierName, String itemNumber, String specification) {
        // 1. 规范化供应商名称 (转大写/去除非字母数字/截取前5位)
        String supplierCode = normalizeSupplier(supplierName);

        // 2. 规格信息转换为标准编码 (示例映射)
        String specCode="";
        try {
            specCode = convertSpecToCode(specification);
        }
        catch (Exception e) {
            specCode = specification;
        }
        // 3. 生成唯一后缀 (日期+随机序列)
        String uniqueSuffix = generateUniqueSuffix();

        // 4. 组合完整SKU
        return String.format("%s-%s-%s-%s",
                supplierCode,
                itemNumber,
                specCode,
                uniqueSuffix
        );
    }

    /** 规范化供应商名称 */
    private static String normalizeSupplier(String name) {
        if (name == null || name.isEmpty()) {
            return "UNKN";
        }

        // 处理Unicode字符 (如中文转拼音)
        String normalized = Normalizer.normalize(name, Normalizer.Form.NFD);
        String ascii = normalized.replaceAll("[^\\p{ASCII}]", "");

        // 清理特殊字符并转大写
        String clean = ascii.replaceAll("[^a-zA-Z0-9]", "").toUpperCase();

        // 截取前5个字符 (不足补零)
        return String.format("%-5s", clean).replace(' ', '0').substring(0, 5);
    }

    /** 规格信息标准化编码 */
    private static String convertSpecToCode(String spec) {
        if (spec == null) return "GEN";

        // 示例规格映射表 (实际应使用配置或数据库)
        if (spec.contains("small") || spec.contains("S")) return "S";
        if (spec.contains("medium") || spec.contains("M")) return "M";
        if (spec.contains("large") || spec.contains("L")) return "L";
        if (spec.contains("color")) {
            return Pattern.compile("\\d+").matcher(spec).find() ?
                    "CLR" + spec.replaceAll("\\D+", "") : "CLR";
        }

        // 通用哈希编码 (处理复杂规格)
        return Integer.toHexString(spec.toLowerCase().hashCode()).substring(0, 3).toUpperCase();
    }

    /** 生成唯一后缀 (日期+随机序列) */
    private static String generateUniqueSuffix() {
        return String.format("%tY%<tm%<td-%04d",
                System.currentTimeMillis(),
                RANDOM.nextInt(10000)
        );
    }

    // 使用示例
    public static void main(String[] args) {
        String sku = generateSku(
                "ABC供应商(上海)",  // 供应商名称
                "ITEM-2024-001",  // 货号
                "红色/尺寸:XL"     // 规格信息
        );
        System.out.println("生成的SKU: " + sku);
        // 可能输出: ABC0-ITEM-2024-001-CLR-20240904-7321
    }
}
