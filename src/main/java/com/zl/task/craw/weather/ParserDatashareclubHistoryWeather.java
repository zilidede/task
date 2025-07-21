package com.zl.task.craw.weather;
// 爬取天气数据
import com.zl.dao.generate.CityWeatherHistoryDO;
import com.zl.dao.generate.CityWeatherHistoryDao;
import com.zl.task.impl.SaveService;
import com.zl.task.impl.SaveServiceImpl;
import com.zl.task.save.Saver;
import com.zl.utils.io.FileIoUtils;
import com.zl.utils.time.SimpleDateFormatUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class ParserDatashareclubHistoryWeather {

    public static void main(String[] args) throws Exception {
        File input = new File("./data/test/cityweather.txt");
        Document doc = Jsoup.parse(input, "UTF-8");
        SimpleDateFormatUtils.setDateFormat("yyyy-MM-dd");
        String scope="上海";
        // 提取日级数据
        List<CityWeatherHistoryDO> dailyData = parseCityWeatherHistoryDO(FileIoUtils.readTxtFile("./data/test/cityweather.txt","UTF-8"),scope);

        // 打印结果
        for (CityWeatherHistoryDO dw : dailyData) {
            System.out.println(dw.getRecordTime() + " - 最高温度: " + dw.getMaxTemp() + "℃, AQI: " + dw.getAqi());
        }
        CityWeatherHistoryDao dao = new CityWeatherHistoryDao();
        SaveService<CityWeatherHistoryDao> saveService=new SaveServiceImpl();
        saveService.savePgSql(dao, dailyData);
    }


        // 跳过表头
        // 解析每日气象数据
        public static List<CityWeatherHistoryDO> parseCityWeatherHistoryDO(String html,String cityName) {
            List<CityWeatherHistoryDO> data = new ArrayList<>();
            Document doc = Jsoup.parse(html);

            // 定位每日数据表格
            Element table = doc.selectFirst(String.format("div.card:contains(%s近30天历史天气) table.table",cityName));
            if (table == null) return data;

            // 提取表格行（跳过表头）
            Elements rows = table.select("tbody tr");
            for (Element row : rows) {
                Elements cols = row.select("td");
                if (cols.size() < 16) continue;

                CityWeatherHistoryDO dw = new CityWeatherHistoryDO();
                for (int i = 0; i < cols.size(); i++){
                   // System.out.println(cols.get(i).text());
                }
                dw.setRecordTime(SimpleDateFormatUtils.parserDate(cols.get(0).text()));
                dw.setScope(cityName);
                try {
                    // 处理日期
                    // 解析数值型数据
                    dw.setMaxTemp(parseDouble(cols.get(1).text().replace("℃", "")));
                    dw.setMinTemp(parseDouble(cols.get(2).text().replace("℃", "")));
                    dw.setTempDiff(parseDouble(cols.get(3).text().replace("℃", "")));
                    dw.setAvgTemp(parseDouble(cols.get(4).text().replace("℃", "")));
                    dw.setAvgHumidity(parseDouble(cols.get(5).text().replace("%", "")));

                    // 文本型数据
                    dw.setWeather(cols.get(6).text());
                    dw.setWind(cols.get(7).text());

                    // 降水量处理（可能包含mm单位）
                    dw.setPrecipitation24h(parseDouble(cols.get(8).text().replace("mm", "")));

                    // 体感温度
                    dw.setFeelsLike(parseDouble(cols.get(9).text().replace("℃", "")));

                    // 日出日落时间
                    dw.setSunriseSunset(cols.get(10).text());

                    // 月升月落时间
                 //   dw.setMoonRiseSet(cols.get(11).text());

                    // 空气质量指数
                    dw.setAqi(parseInt(cols.get(12).text()));

                    // 白昼时长（秒）和日照时长（秒）
                    // 假设原始数据已经是数字，没有单位
                    dw.setDaylightSeconds(parseInt(cols.get(13).text()));
                 //   dw.setSunshineHours(parseDouble(cols.get(14).text()));

                    // 太阳辐射总量（MJ/m²）
                    String solarText = cols.get(15).text();
                    if (solarText.contains("MJ/m²")) {
                        solarText = solarText.replace("MJ/m²", "").trim();
                    }
                    dw.setSolarTotal(parseDouble(solarText));


                } catch (NumberFormatException e) {
                    System.err.println("数字格式解析错误: " + e.getMessage());
                }
                data.add(dw);
            }
            return data;
        }
    // 安全解析Double的方法
    private static Double parseDouble(String value) {
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    // 安全解析Integer的方法
    private static Integer parseInt(String value) {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }



}
