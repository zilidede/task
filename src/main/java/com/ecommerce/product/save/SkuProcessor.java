package com.ecommerce.product.save;

import java.io.*;
import java.util.*;
import java.util.regex.*;

public class SkuProcessor {
    public static String  process(String sku) {

        // 使用正则表达式移除颜色和尺码信息
        String processed = sku.replaceAll("-([\\u4e00-\\u9fa5a-zA-Z]+)-([0-9]?[XLSML]+)$", "");
        return processed;

    }
}
