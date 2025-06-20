package com.zl.task.process.keyword;


import com.zl.dao.generate.OceanenSearchKeywordsDetailDao;
import com.zl.dao.generate.OceanengineSearchKeywordsDO;
import com.zl.dao.generate.OceanengineSearchKeywordsDao;
import com.zl.utils.jdbc.generator.jdbc.DefaultDatabaseConnect;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//对巨量云图搜索词进行词干化
public class OceanEngineKeyWordsStemmer {
    public static final OceanenSearchKeywordsDetailDao dao;

    static {
        try {
            dao = new OceanenSearchKeywordsDetailDao(DefaultDatabaseConnect.getConn());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) throws SQLException {
        // 从数据获取关键词词根列表并保存到文本
        Map<String, Integer> keywordsMaps = getSearchKeywords(); //获取搜索词列表

        List<String> keywords = new ArrayList<>();
        // 使用 keySet() 遍历
        for (String key : keywordsMaps.keySet()) {
            Integer value = keywordsMaps.get(key);
            keywords.add(key);
            //System.out.println("Key: " + key + ", Value: " + value);
        }
        // 使用HANLP 进行词干化 生成关键字列表词根
        Stemmer stemmer = StemmerFactory.getStemmer(StemmerFactory.StemmerType.HANLP);
        List<String> stems = getStems(keywords, stemmer);
        Map<String, Integer> searchKeywordRootMaps = new HashMap<>();
        // 获取当前日期
        LocalDate currentDate = LocalDate.now();
        // 创建日期格式化对象
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (String stem : stems) {
            // 循环最近90天的日期
            for (int i = 17; i < 18; i++) {
                // 计算过去的第i天
                LocalDate pastDate = currentDate.minusDays(i);
                // 格式化日期
                String formattedDate = pastDate.format(formatter);
                int count = dao.findLikeKeyWordCount(stem, formattedDate); //查看词根关联词组搜索次数总和
                if (count > 1000)
                    searchKeywordRootMaps.put(stem, count);
            }
        }
        // 从搜索词订单数量和搜索此次过滤，从七天搜索词求平均值>100,搜索次数>20000；
        OceanengineSearchKeywordsDao dao1 = new OceanengineSearchKeywordsDao(DefaultDatabaseConnect.getConn());
        List<String> contents = new ArrayList<>();
        for (String key : searchKeywordRootMaps.keySet()) {
            Integer value = searchKeywordRootMaps.get(key);
            OceanengineSearchKeywordsDO vo = new OceanengineSearchKeywordsDO();
            vo.setKeyword(key);
            int icount = dao1.findOrderCount(vo) / 7;
            if (icount > 100 || value > 20000)
                contents.add(key + "," + icount);

        }
        //保存
        String filePath = "./data/task/待爬取关键字列表.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, StandardCharsets.UTF_8))) {
            for (String line : contents) {
                writer.write(line);
                writer.newLine(); // 换行
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static List<String> getStems(List<String> words, Stemmer stemmer) {
        return stemmer.stem(words);
    }

    public static Map<String, Integer> getSearchKeywords() throws SQLException {
        List<String> keywords = new ArrayList<>();
        Map<String, Integer> map = new HashMap<>();
        return dao.findOcSearchKeywordsDetail(map);
    }
}
