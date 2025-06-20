package com.zl.utils.other;

/**
 * @className: com.craw.nd.util-> Ini4jUtils
 * @description: Ini4j读取ini文件工具类
 * @author: zl
 * @createDate: 2022-12-13 16:28
 * @version: 1.0
 * @todo:
 */

import org.ini4j.Ini;
import org.ini4j.Wini;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class Ini4jUtils {
    public static String sectionValue = "compass";
    public static String iniFile = "data/config/config.ini";
    private static Wini wini = null;

    static {
        try {
            wini = new Wini(new File(iniFile));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            readIni("");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static Map<String, String> traSpecificSection(String specificSection) throws IOException {
        // 特定部分的遍历
        if (wini.containsKey(specificSection)) {
            System.out.println("Entries in section [" + specificSection + "]:");
            Map<String, String> sectionMap = wini.get(specificSection);

            for (Map.Entry<String, String> entry : sectionMap.entrySet()) {
                //System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
            }
            return sectionMap;
        } else
            return null;
    }

    public static Wini getWini() {
        return wini;
    }

    public static void loadIni(String iniFile) {
        try {
            wini = new Wini(new File(iniFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getIniFile() {
        return iniFile;
    }

    public static void setIniFile(String iniFile) {
        Ini4jUtils.iniFile = iniFile;
    }

    public static String getSectionValue() {
        return sectionValue;
    }

    public static void setSectionValue(String sectionValue) {
        Ini4jUtils.sectionValue = sectionValue;
    }


    public static String readIni(String key) throws Exception {
        // 读
        Ini.Section section = wini.get(sectionValue);
        return section.get(key);
    }

    public static void writeIni(Map<String, String> iniMap) throws IOException {


        Wini ini = new Wini();
        ini.load(new File(iniFile));
        Ini.Section section = ini.get(sectionValue);
        for (Map.Entry<String, String> entry : iniMap.entrySet()) {
            //Map.entry<Integer,String> 映射项（键-值对）  有几个方法：用上面的名字entry
            //entry.getKey() ;entry.getValue(); entry.setValue();
            //map.entrySet()  返回此映射中包含的映射关系的 Set视图。
            section.put(entry.getKey(), entry.getValue());
            //section.put(entry.getKey(),entry.getValue(),1);
            // System.out.println("key= " + entry.getKey() + " and value= "
            //+ entry.getValue());
        }
        ini.store(new File(iniFile));


    }
}

