package com.zl.utils.excel;

import java.util.List;
import java.util.Map;

public class ExcelProcessor {
    public static void main(String[] args) {
        try {
            String filePath = "./data/productContent/抖音&真维斯官方旗舰店&商品表 - 副本.xlsx";
            List<Map<String, String>> excelData = ExcelReader.readExcel(filePath);
            // 处理数据
            for (Map<String, String> row : excelData) {
                String name = row.get("商品名称");
                // 进行业务逻辑处理...
                System.out.println("商品名称: " + name );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
