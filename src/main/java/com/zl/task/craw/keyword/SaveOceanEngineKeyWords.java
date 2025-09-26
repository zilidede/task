package com.zl.task.craw.keyword;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zl.dao.generate.*;
import com.zl.task.impl.SaveServiceImpl;
import com.zl.utils.csv.CsvUtils;
import com.zl.utils.io.DiskIoUtils;
import com.zl.utils.io.FileIoUtils;
import com.zl.utils.log.LoggerUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @className: com.craw.nd.service.other.person.Impl.craw.app.douYin.self.keyword.search-> AnalysisOceanEnginSearch
 * @description: 保存巨量云图搜索词
 * @author: zl
 * @createDate: 2023-11-08 14:22
 * @version: 1.0
 * @todo:
 */
public class SaveOceanEngineKeyWords {
    private List<OceanengineSearchKeywordsDO> keywordsDOS = null;
    private OceanengineSearchKeywordsDao keywordsDao = null;
    private final SaveServiceImpl saveService;
    private Map<String, Integer> keywordMap;
    private String srcDir;
    private OceanenSearchKeywordsProductDao productDao ;
    private OceanenSearchKeywordsVideoDao videoDao ;
    private OceanenSearchKeywordsDetailDao keywordsDetailDao ;


    private final List<OceanenSearchKeywordsProductDO> productDOList = new ArrayList<>();
    private final List<OceanenSearchKeywordsVideoDO> videoDOList = new ArrayList<>();
    private final List<OceanenSearchKeywordsDetailDO> keywordsDetailDOList = new ArrayList<>();

    public String getSrcDir() {
        return srcDir;
    }

    public void setSrcDir(String srcDir) {
        this.srcDir = srcDir;
    }

    public SaveOceanEngineKeyWords() throws SQLException {
        saveService = new SaveServiceImpl();
        keywordMap = new HashMap<>();
        keywordsDOS = new ArrayList<>();
        keywordsDao = new OceanengineSearchKeywordsDao();
        productDao = new OceanenSearchKeywordsProductDao();
        videoDao = new OceanenSearchKeywordsVideoDao();
        keywordsDetailDao = new OceanenSearchKeywordsDetailDao();
    }

    public void save() throws SQLException, IOException, ParseException {
        saveKeyWordsDetail();//保存关键词详情
        saveCsvKeywords(); //保存搜索关键词csv文件
    }

    public void saveKeyWordsDetail() throws SQLException, IOException {
        //保存关键词详情

        String dir = srcDir + "liteKeywordsPacketGetSearchWordDetail\\";
        if (!DiskIoUtils.isExist(dir)) {
            return;
        }
        List<String> files = DiskIoUtils.getFileListFromDir(dir);
        for (int i = 0; i < files.size(); i++) {
            String content = FileIoUtils.readFile(files.get(i));
            content = content.replaceAll(" ", "").replaceAll("\0", "");
            parserKeyWordDetail(content);

        }

    }

    public void parserKeyWordDetail(String content) throws UnsupportedEncodingException {
        String[] strings = content.split("\\{");
        String url = strings[0];
        String json = content.replace(url, "");
        //
        url = URLDecoder.decode(url, StandardCharsets.UTF_8);
        strings = url.split("&");
        String keyword = "";
        try {
            keyword = strings[1].replace("query=", "");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        JsonParser parser = new JsonParser();
        JsonObject object;
        JsonArray jsonArray;
        try {
            object = parser.parse(json).getAsJsonObject();
            jsonArray = null;
            jsonArray = object.get("data").getAsJsonObject().get("search_word_trend_list").getAsJsonArray();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        for (int i = 0; i < jsonArray.size(); i++) {
            OceanenSearchKeywordsDetailDO vo = new OceanenSearchKeywordsDetailDO();
            String s = jsonArray.get(i).getAsJsonObject().get("date").getAsString();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

            LocalDate date = LocalDate.parse(jsonArray.get(i).getAsJsonObject().get("date").getAsString(), formatter);
            Date utilDate = Date.from(date.atStartOfDay()
                    .atZone(ZoneId.systemDefault())
                    .toInstant());
            vo.setRecordTime(utilDate);
            vo.setKeyword(keyword);

            vo.setKeywordCount(jsonArray.get(i).getAsJsonObject().get("metrics").getAsInt());
            s = s + vo.getKeyword();
            vo.setPlatformSource("抖音-巨量云图");
            if (!keywordMap.containsKey(s)) {
                keywordsDetailDOList.add(vo);
                keywordMap.put(s, 0);
            }
        }
        jsonArray = object.get("data").getAsJsonObject().get("search_product_index_list").getAsJsonArray();
        for (int i = 0; i < jsonArray.size(); i++) {
            OceanenSearchKeywordsProductDO vo = new OceanenSearchKeywordsProductDO();
            vo.setRecordTime(new Date());
            vo.setKeyword(keyword);
            vo.setPrductId(jsonArray.get(i).getAsJsonObject().get("prduct_id").getAsLong());
            vo.setPrductName(jsonArray.get(i).getAsJsonObject().get("prduct_name").getAsString());
            vo.setProductUrl(jsonArray.get(i).getAsJsonObject().get("product_url").getAsString());
            vo.setProductAmountIndex(jsonArray.get(i).getAsJsonObject().get("product_amount_index").getAsInt());
            vo.setProductOrderCnt(jsonArray.get(i).getAsJsonObject().get("product_order_cnt").getAsInt());
            vo.setProductClickCnt(jsonArray.get(i).getAsJsonObject().get("product_click_cnt").getAsInt());
            vo.setProductShowCnt(jsonArray.get(i).getAsJsonObject().get("product_show_cnt").getAsInt());
            vo.setRoductOutLink(jsonArray.get(i).getAsJsonObject().get("roduct_out_link").getAsString());
            productDOList.add(vo);
        }

        jsonArray = object.get("data").getAsJsonObject().get("search_content_index_list").getAsJsonArray();
        for (int i = 0; i < jsonArray.size(); i++) {
            OceanenSearchKeywordsVideoDO vo = new OceanenSearchKeywordsVideoDO();
            vo.setRecordTime(new Date());
            vo.setKeyword(keyword);
            vo.setVideoId(jsonArray.get(i).getAsJsonObject().get("video_id").getAsString());
            vo.setVideoTitle(jsonArray.get(i).getAsJsonObject().get("video_title").getAsString());
            vo.setVideoKeywordList(jsonArray.get(i).getAsJsonObject().get("video_keyword_list").getAsJsonArray().toString());
            vo.setVideoShowCnt(jsonArray.get(i).getAsJsonObject().get("video_show_cnt").getAsInt());
            vo.setVideoPlayOverRate(jsonArray.get(i).getAsJsonObject().get("video_play_over_rate").getAsDouble());
            vo.setVideoInteractRate(jsonArray.get(i).getAsJsonObject().get("video_interact_rate").getAsDouble());
            videoDOList.add(vo);
        }


    }

    public void saveCsvKeywords() throws IOException, SQLException, ParseException {
        //保存搜索关键词csv文件
        String dir = srcDir + "巨量云图行业搜索词\\";


        int max = 1000;
        List<String> files = DiskIoUtils.getFileListFromDir(dir);
        if (files == null)
            return;
        for (int i = 0; i < files.size(); i++) {
            List<Map<String, String>> csvContent = null;
            try {
                csvContent = CsvUtils.read(files.get(i), 0, 400000);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            File file = new File(files.get(i));

            Long lastModified = file.lastModified();
            Date date = new Date(lastModified);
            LoggerUtils.logger.info(file.getName());
            int c = parserCsvFile(csvContent, date, file.getName());
            if (c == -1) {

            }
        }
    }

    public int saveToPgSql() {
        saveService.savePgSql(keywordsDao, keywordsDOS);
        keywordsDOS.clear();
        keywordMap.clear();
        saveService.savePgSql(keywordsDetailDao, keywordsDetailDOList);
        keywordsDetailDOList.clear();
        saveService.savePgSql(productDao, productDOList);
        productDOList.clear();
        saveService.savePgSql(videoDao, videoDOList);
        videoDOList.clear();
        return 0;
    }

    public int parserCsvFile(List<Map<String, String>> csvContent, Date date, String fileName) throws ParseException {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        String[] strings = fileName.split(" ~ ");
        if (strings.length <= 1)
            return -1;
        date = sf.parse(strings[1].substring(0, 10));
        Date date1 = sf.parse(strings[0]);
        // System.out.println("开始日期"+sf.format(date)+"结束日期"+sf.format(date1));

        for (int i = 0; i < csvContent.size(); i++) {
            OceanengineSearchKeywordsDO vo = new OceanengineSearchKeywordsDO();
            Map<String, String> tmap=csvContent.get(i);

            String searchKeyword = "搜索词";
            if (tmap.get("搜索词").equals("＂")) {

            }
            vo.setRecordEndTime(date);
            vo.setRecordStartTime(date1);
            int j = 1;
            try {
                vo.setKeyword(csvContent.get(i).get("搜索词"));
                vo.setSearchCount(Integer.parseInt(csvContent.get(i).get("搜索次数")));
                try {
                    vo.setRunningCount(Integer.parseInt(csvContent.get(i).get("跑量情况")));
                } catch (Exception ex) {
                    Map<String, String> map = csvContent.get(i);
                    continue;
                }

                vo.setClickRate(Double.valueOf(csvContent.get(i).get("点击率").replaceAll("%", "")) / 100);
                vo.setOrderCount(Integer.parseInt(csvContent.get(i).get("订单量")));
                vo.setOrderSaleCount(Integer.parseInt(csvContent.get(i).get("销售金额(指数)")));
                vo.setTraderRate(Double.valueOf(csvContent.get(i).get("搜索成交转化率").replaceAll("%", "")) / 100);
                String s = vo.getKeyword() + vo.getRecordStartTime() + vo.getRecordEndTime();
                if (!keywordMap.containsKey(s)) {
                    keywordMap.put(s, 0);
                    keywordsDOS.add(vo);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                continue;
            }


        }
        return 0;
    }
}
