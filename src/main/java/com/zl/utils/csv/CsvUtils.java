package com.zl.utils.csv;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Stream;


/**
 * @className: com.craw.nd.util-> CsvUtils
 * @description: csv文件读取
 * @author: zl
 * @createDate: 2023-11-09 10:55
 * @version: 1.0
 * @todo:
 */
public class CsvUtils {
    public static List<Map<String, String>> read(String csvFile, Integer start, Integer end) throws IOException {
        List<Map<String, String>> csvContent = new ArrayList<>();
        List<String> keys = new ArrayList<>();
        String[] strings = null;
        // 第一参数：读取文件的路径 第二个参数：分隔符（不懂仔细查看引用百度百科的那段话） 第三个参数：字符集
        CsvReader csvReader = new CsvReader(csvFile, ',', StandardCharsets.UTF_8);

        // 如果你的文件没有表头，这行不用执行
        // 这行是为了从表头的下一行读，也就是过滤表头

        // csvReader.readHeaders();

        // 读取每行的内容
        int i = 0;
        while (csvReader.readRecord()) {
            // 获取内容的两种方式
            // 1. 通过下标获取
            strings = new String[csvReader.getColumnCount()];
            Map<String, String> map = null;
            String s1 = csvReader.get(i);
            if (i == 0) {
                for (int j = 0; j < csvReader.getColumnCount(); j++) {
                    strings[j] = csvReader.get(j).replace("\t", "").replace("\uFEFF", "").replace("\"","");
                    keys.add(strings[j]);
                }
                i++;
            } else {
                if (i++ < start)
                    continue;
                map = new HashMap<>();
                for (int j = 0; j < csvReader.getColumnCount(); j++) {
                    strings[j] = csvReader.get(j).replace("\t", "").replace("\uFEFF", "").replace("\"","");
                    map.put(keys.get(j), strings[j]);
                }
                csvContent.add(map);

            }

            if (i > end)
                return csvContent;

        }
        return csvContent;

    }


    public static List<Map<String, String>> readBuffer(String csvFile) {
        //流处理-处理大文件
        List<Map<String, String>> csvContent = new ArrayList<>();
        List<String> keys = new ArrayList<>();


        //并行流 没有数据安全,需要synchronized
        CollUtil Lists = null;
        Collection<String> objects = Collections.synchronizedCollection(CollUtil.newArrayList());
        FileInputStream fileInputStream = IoUtil.toStream(new File(csvFile));
        BufferedReader utf8Reader = IoUtil.getUtf8Reader(fileInputStream);
        Stream<String> lines = utf8Reader.lines();
        int i = 0;
        lines.parallel().forEach(s -> {
                    objects.add(s);

                }
        );
        int k = 0;
        for (String s : objects) {
            k++;
            String[] strings = s.split(",");
            if (i == 0) {
                for (String s1 : strings) {
                    keys.add(s1.replace("\t", "").replace("\uFEFF", ""));
                }
            } else {
                if (k < 400000)
                    continue;
                Map<String, String> map = new HashMap<>();
                int j = 0;
                for (String s1 : strings) {
                    if (j >= keys.size()) {
                        System.out.println(strings.toString());
                        continue;

                    }
                    map.put(keys.get(j++), s1.replace("\t", "").replace("\uFEFF", ""));
                }
                csvContent.add(map);
            }
            i++;
        }
        System.out.println(i);
        return csvContent;
    }

    public static void writer(String csvFile, String[] headers, String[] content) throws IOException {

        // 第一参数：新生成文件的路径 第二个参数：分隔符（不懂仔细查看引用百度百科的那段话） 第三个参数：字符集
        CsvWriter csvWriter = new CsvWriter(csvFile, ',', StandardCharsets.UTF_8);

        // 表头和内容
        // String[]  headers = {"姓名", "年龄", "性别"};
        //  String[] content = {"张三", "18", "男"};

        // 写表头和内容，因为csv文件中区分没有那么明确，所以都使用同一函数，写成功就行
        csvWriter.writeRecord(headers);
        csvWriter.writeRecord(content);

        // 关闭csvWriter
        csvWriter.close();

    }
}
