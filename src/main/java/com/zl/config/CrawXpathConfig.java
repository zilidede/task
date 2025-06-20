package com.zl.config;

import java.util.HashMap;
import java.util.Map;

/**
 * @className: com.craw.nd.config-> CrawXpathConfig
 * @description: xpath配置类
 * @author: zl
 * @createDate: 2022-12-13 16:05
 * @version: 1.0
 * @todo:
 */
public class CrawXpathConfig {
    private static Map<String, String> xpathMaps;

    public static void init() {
        xpathMaps = new HashMap<>();
    }

    public static Map<String, String> getXpathMaps() {
        return xpathMaps;
    }

    public static void setXpathMaps(Map<String, String> xpathMaps) {
        CrawXpathConfig.xpathMaps = xpathMaps;
    }
}
