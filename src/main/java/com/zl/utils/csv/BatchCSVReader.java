package com.zl.utils.csv;

import com.csvreader.CsvReader;
import com.zl.task.save.parser.order.SaveOrder;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BatchCSVReader {
    private static final int BATCH_SIZE = 40000; // 每批次处理的记录数
    private static final List<Map<String, String>> maps = new ArrayList<>();
    private static final Map<String, String> orderMap = new HashMap<>();
    private static final List<String> keys = new ArrayList<>();
    private static String shop;
    private static String platform;
    private static String filess;

    public static List<Map<String, String>> getMaps() {
        return maps;
    }

    public static void read(String filePath, SaveOrder saveOrder) throws IOException {
        String fileName = Paths.get(filePath).getFileName().toString(); // 获取文件名
        filess = fileName;
        System.out.println("导入文件" + fileName);
        String[] strings1 = fileName.split("-");
        platform = strings1[0];
        shop = strings1[1];
        maps.clear();
        orderMap.clear();
        keys.clear();
        CsvReader csvReader = new CsvReader(filePath, ',', StandardCharsets.UTF_8);
        String[] strings = null;
        // 如果你的文件没有表头，这行不用执行
        // 读取第一行的内容作为标头
        int i = 0;

        while (csvReader.readRecord()) {
            // 获取内容的两种方式
            // 1. 通过下标获取
            strings = new String[csvReader.getColumnCount()];
            Map<String, String> map = null;
            String s1 = csvReader.get(i);
            if (i == 0) {
                for (int j = 0; j < csvReader.getColumnCount(); j++) {
                    strings[j] = csvReader.get(j).replace("\t", "").replace("\uFEFF", "");
                    keys.add(strings[j]);
                }
                break;
            }
        }
        csvReader.close();
        i = 0;
        try (Reader reader = new FileReader(filePath);
             CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT)) {
            List<CSVRecord> batch = new ArrayList<>();
            for (CSVRecord record : parser) {
                batch.add(record);
                i++;
                if (batch.size() >= BATCH_SIZE) {
                    processBatch(batch, saveOrder);
                    batch.clear(); // 清空批次，准备下一批数据
                    // System.out.println(i++);
                }
            }
            // 处理最后不足一个批次的记录
            if (!batch.isEmpty()) {
                processBatch(batch, saveOrder);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        //System.out.println(i++);
    }

    private static void processBatch(List<CSVRecord> batch, SaveOrder saveOrder) throws ParseException {
        // 在这里处理一个批次的数据，例如批量插入数据库、数据分析等
        //System.out.println("Processing a batch of " + batch.size() + " records.");
        // 示例：打印每条记录
        for (CSVRecord record : batch) {
            Map<String, String> map = new HashMap<>();
            for (int i = 0; i < record.size(); i++) {
                String s = record.get(i).replace("\t", "").replace("\uFEFF", "");
                try {
                    if ((keys.get(i).equals("主订单编号")) && (s.indexOf("6") < 0)) {
                        break;
                    }
                    if (keys.get(i).equals("子订单编号")) {
                        if (!orderMap.containsKey(s))
                            orderMap.put(s, "");
                        else
                            break;
                    }
                    map.put(keys.get(i), s);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
            if (map.size() > 0) {
                try {
                    if (!map.get("子订单编号").equals("")) {
                        maps.add(map);
                    } else {
                        System.out.println(map.get("子订单编号"));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println("导入失败" + filess);
                    return;
                }

            }
        }
        saveOrder.call(platform, shop, getMaps());
    }
}
