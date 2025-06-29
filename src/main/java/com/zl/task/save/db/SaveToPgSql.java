package com.zl.task.save.db;

import com.zl.dao.DaoService;
import com.zl.dao.generate.CityWeatherDO;
import com.zl.dao.generate.CityWeatherDao;
import com.zl.dao.generate.FileRecordDO;
import com.zl.dao.generate.FileRecordDao;
import com.zl.task.craw.keyword.SaveOceanEngineKeyWords;
import com.zl.task.impl.SaveService;
import com.zl.task.impl.SaveServiceImpl;
import com.zl.task.save.parser.weather.SaverCityWeather;
import com.zl.utils.csv.CsvUtils;
import com.zl.utils.io.DiskIoUtils;
import com.zl.utils.io.FileHashGeneratorUtils;
import com.zl.utils.io.FileIoUtils;
import com.zl.utils.jdbc.generator.jdbc.DefaultDatabaseConnect;
import com.zl.utils.log.LoggerUtils;
import com.zl.utils.other.Ini4jUtils;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class SaveToPgSql {

    //依照日期周期进行遍历保存数据
    public static void cycleSave(int day) throws Exception {
        Ini4jUtils.loadIni("./data/config/config.ini");
        Ini4jUtils.setSectionValue("back");
        String dayDir = Ini4jUtils.readIni("dayDir");
        String backDir = Ini4jUtils.readIni("BaiduSyncdisk");
        LocalDate currentDate = LocalDate.now();
        FileRecordDao fileRecordDao = new FileRecordDao();
        SaveService saveService = new SaveServiceImpl();
// 将 LocalDate 转换为 OffsetDateTime
        OffsetDateTime startDateTime = currentDate.minusDays(day)
                .atStartOfDay() // 设置时间为当天的开始（00:00:00）
                .atZone(ZoneId.systemDefault()) // 指定时区
                .toOffsetDateTime(); // 转换为 OffsetDateTime

        OffsetDateTime endDateTime = currentDate
                .atTime(23, 59, 59) // 设置时间为当天的结束（23:59:59）
                .atZone(ZoneId.systemDefault())
                .toOffsetDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss XXX");
        // String result = startDateTime.format(formatter);
        List<String> parserFiles = fileRecordDao.findFilePathsByTimeRange(startDateTime, endDateTime);

        Map<String, Integer> parserFileMap = new HashMap<>(); //已解析文件列表map
        for (String file : parserFiles) {
            parserFileMap.put(file, 0);
        }
        // 创建日期格式化对象
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<String> unParsedFiles = new ArrayList<>();
        // 保存最近天数爬取文件信息
        List<FileRecordDO> fileRecordDOS = getUnParserFileRecordFromDir(day, dayDir, parserFileMap);
        //saveService.saveBatchPgSql(fileRecordDao, fileRecordDOS);
        //保存最近爬取文件内容
        SaveOceanEngineKeyWords saveOceanEngineSearch = new SaveOceanEngineKeyWords();
        for (FileRecordDO file : fileRecordDOS) {
            if (file.getFileLocalPath().indexOf("yunTu") >= 0) {
                saveOceanEngineKeyWords(file, saveOceanEngineSearch);// 解析云图关键词文件
            } else {

            }
        }
        fileRecordDao.batchUpdateFileStatus(fileRecordDOS);
        saveOceanEngineSearch.saveToPgSql(); //保存关键词文件;保存
        fileRecordDOS.clear();
    }

    private static List<FileRecordDO> getUnParserFileRecordFromDir(int day, String dayDir, Map<String, Integer> parserFiles) throws SQLException, IOException, NoSuchAlgorithmException {
        // 获取待处理的文件列表
        List<FileRecordDO> fileRecordDOS = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (int i = 0; i < day; i++) {
            // 计算过去的第i天
            LocalDate pastDate = currentDate.minusDays(i);
            // 格式化日期
            String formattedDate = pastDate.format(formatter);
            List<String> files = DiskIoUtils.getFileListFromDir(dayDir + formattedDate + "\\");
            if (files == null)
                continue;
            for (String file : files) {
                if (!parserFiles.containsKey(file)) {
                    FileRecordDO fileRecordDO = new FileRecordDO();
                    File f = new File(file);
                    // 将毫秒数转换为 Instant
                    Instant instant = Instant.ofEpochMilli(f.lastModified());
                    // 将 Instant 转换为 OffsetDateTime，并指定时区偏移量
                    OffsetDateTime offsetDateTime = OffsetDateTime.ofInstant(instant, ZoneId.systemDefault());
                    fileRecordDO.setFileLastUpdate(offsetDateTime);
                    fileRecordDO.setFileLocalPath(file);
                    fileRecordDO.setFileName(f.getName());
                    fileRecordDO.setFileStatus(0);
                    fileRecordDO.setFileMd5(FileHashGeneratorUtils.generateHashWithMetadata(f));
                    fileRecordDOS.add(fileRecordDO);
                }
            }
        }
        return fileRecordDOS;
    }

    //将天气数据保存到pgsql
    public static void saveCityWeatherToPgSql(String srcDir) throws Exception {
        String dir = srcDir ;
        if (!DiskIoUtils.isExist(dir)) {
            LoggerUtils.logger.warn("天气文件数据:" + dir + "不存在");
            return;
        }
        SaveServiceImpl saveService = new SaveServiceImpl();
        DaoService daoService = new CityWeatherDao(DefaultDatabaseConnect.getConn());
        List<CityWeatherDO> list = new ArrayList<>();
        List<Map<String, CityWeatherDO>> cityMaps = SaverCityWeather.parser(dir);
        for (Map<String, CityWeatherDO> cityMap : cityMaps) {
            for (Map.Entry<String, CityWeatherDO> entry : cityMap.entrySet()) {
                list.add(entry.getValue());
            }
        }
        saveService.savePgSql(daoService, list);
        list.clear();
    }

    //将巨量云图关键词保存到pgsql
    public static void saveOceanEngineKeyWords(FileRecordDO file, SaveOceanEngineKeyWords saveOceanEngineSearch) throws SQLException, IOException, ParseException {
        if (file.getFileLocalPath().indexOf(".csv") >= 0) {
            //解析csv文件
            List<Map<String, String>> csvContent = null;
            try {
                csvContent = CsvUtils.read(file.getFileLocalPath(), 0, 400000);
            } catch (Exception e) {
                e.printStackTrace();

            }
            File file1 = new File(file.getFileLocalPath());

            Long lastModified = file1.lastModified();
            Date date = new Date(lastModified);
            LoggerUtils.logger.info(file1.getName());
            int c = saveOceanEngineSearch.parserCsvFile(csvContent, date, file1.getName());
            if (c == -1) {

            } else {
                file.setFileStatus(1);
            }
        } else if (file.getFileLocalPath().indexOf("liteKeywordsPacketGetSearchWordDetail") >= 0) {
            //
            String content = FileIoUtils.readFile(file.getFileLocalPath());
            content = content.replaceAll(" ", "").replaceAll("\0", "");
            saveOceanEngineSearch.parserKeyWordDetail(content);
            file.setFileStatus(1);
        }


    }


}
