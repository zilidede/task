package com.zl.task.save.parser.account;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zl.dao.generate.LiveMinRecordDO;
import com.zl.dao.generate.LiveMinRecordDao;
import com.zl.dao.generate.LiveRecordDO;
import com.zl.dao.generate.LiveRecordDao;
import com.zl.task.impl.SaveServiceImpl;
import com.zl.task.save.base.SaveXHRImpl;
import com.zl.task.save.parser.ParserJsonToHttpVO;
import com.zl.task.vo.http.HttpVO;
import com.zl.utils.io.DiskIoUtils;
import com.util.jdbc.generator.jdbc.DefaultDatabaseConnect;
import com.zl.utils.time.SimpleDateFormatUtils;
import com.zl.utils.time.TimeUtils;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SaveHuiTunAccount extends SaveXHRImpl<LiveRecordDO> {
    private final LiveRecordDao daoService = new LiveRecordDao(DefaultDatabaseConnect.getConn());
    private final LiveMinRecordDao liveMinRecordDao = new LiveMinRecordDao(DefaultDatabaseConnect.getConn());
    private final List<LiveRecordDO> liveRecordDOS = new ArrayList<>();
    private final List<LiveMinRecordDO> liveMinRecordDOS = new ArrayList<>();
    private final SaveServiceImpl saveService = new SaveServiceImpl();

    public SaveHuiTunAccount() throws SQLException {
    }

    public static void main(String[] args) throws Exception {
        SaveHuiTunAccount saver = new SaveHuiTunAccount();
        saver.save("S:\\data\\task\\爬虫\\huiTunLive");
    }

    @Override
    public void save(String sDir) throws Exception {
        //
        String dir = sDir + "\\liveV2RecordT";
        List<String> files = DiskIoUtils.getFileListFromDir(dir);
        for (String file : files) {
            HttpVO httpVO = ParserJsonToHttpVO.parserXHRJson(file);
            String url = httpVO.getUrl();
            String json = httpVO.getResponse().getBody();
            parserLiveRecordJson(json, httpVO);
        }

        saveService.savePgSql(daoService, liveRecordDOS);
        liveRecordDOS.clear();
        //
        dir = sDir + "\\liveRoomInfoChartT";
        files = DiskIoUtils.getFileListFromDir(dir);
        for (String file : files) {
            HttpVO httpVO = ParserJsonToHttpVO.parserXHRJson(file);
            String url = httpVO.getUrl();
            String json = httpVO.getResponse().getBody();
            parserLiveMinRecordJson(json, httpVO);
        }
        saveService.savePgSql(liveMinRecordDao, liveMinRecordDOS);
        liveMinRecordDOS.clear();

    }

    public void parserLiveGoodsJson(String json, HttpVO httpVO) {

    }

    public void parserLiveRecordJson(String json, HttpVO httpVO) {
        JsonParser parser = new JsonParser();
        JsonObject object = null;
        JsonArray jsonArray = null;
        SimpleDateFormatUtils.setDateFormat("yyyy/MM/dd HH:mm:ss");
        if (json.equals(""))
            return;
        object = parser.parse(json).getAsJsonObject();
        try {
            jsonArray = object.get("data").getAsJsonArray();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        if (jsonArray.size() < 1)
            return;
        String temp = "";
        for (int i = 0; i < jsonArray.size(); i++) {
            LiveRecordDO vo = new LiveRecordDO();
            vo.setRoomId(Long.parseLong(String.valueOf(jsonArray.get(i).getAsJsonObject().get("roomId").getAsString())));
            vo.setCoverUrl(jsonArray.get(i).getAsJsonObject().get("coverUrl").getAsString());
            JsonElement element = jsonArray.get(i).getAsJsonObject().get("title");
            if (element.isJsonNull()) {
                vo.setTitle("");
            } else
                vo.setTitle(element.getAsString());
            vo.setStartLive(SimpleDateFormatUtils.parserDate(jsonArray.get(i).getAsJsonObject().get("startLive").getAsString() + ":00"));
            try {
                vo.setLiveTime(TimeUtils.convertToSecond(jsonArray.get(i).getAsJsonObject().get("liveTime").getAsString()));
            } catch (Exception ex) {
                ex.printStackTrace();
                vo.setLiveTime(0);
            }

            element = jsonArray.get(i).getAsJsonObject().get("userNum");
            if (element.isJsonNull()) {
                temp = "0";
            } else
                temp = jsonArray.get(i).getAsJsonObject().get("userNum").getAsString();
            int iValue = 0;
            if (temp.indexOf("万") > 0) {
                temp = temp.replace("万", "");
                iValue = (int) Double.parseDouble(temp) * 10000;
                vo.setUserNum(iValue); //人气峰值；
            }
            element = jsonArray.get(i).getAsJsonObject().get("totalUser");
            if (element.isJsonNull()) {
                temp = "0";
            } else
                temp = jsonArray.get(i).getAsJsonObject().get("totalUser").getAsString();
            if (temp.indexOf("万") > 0) {
                temp = temp.replace("万", "");
                iValue = (int) Double.parseDouble(temp) * 10000;
                vo.setTotalUser(iValue);
            }
            element = jsonArray.get(i).getAsJsonObject().get("avgUserDuration");
            if (element.isJsonNull()) {
                vo.setAvgUserDuration(0);
            } else
                vo.setAvgUserDuration(TimeUtils.convertToSecond(jsonArray.get(i).getAsJsonObject().get("avgUserDuration").getAsString()));


            double salesAverage = 0.0;
            element = jsonArray.get(i).getAsJsonObject().get("sales");
            if (element.isJsonNull() || element.getAsString().equals("0")) {
                vo.setSales(0);
            } else {
                try {
                    salesAverage = convertToNumeric(jsonArray.get(i).getAsJsonObject().get("sales").getAsString());
                    vo.setSales((int) salesAverage);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
            double gmvAverage = 0.0;
            element = jsonArray.get(i).getAsJsonObject().get("gmv");
            if (element.isJsonNull() || element.getAsString().equals("0")) {
                vo.setGmv(0.0);
            } else {
                gmvAverage = convertToNumeric(jsonArray.get(i).getAsJsonObject().get("gmv").getAsString());
                vo.setGmv(gmvAverage);
            }


            try {
                vo.setUserCount(jsonArray.get(i).getAsJsonObject().get("userCount").getAsInt());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                temp = jsonArray.get(i).getAsJsonObject().get("categories").getAsJsonArray().toString();
            } catch (Exception ex) {
                ex.printStackTrace();
                temp = "";
            }
            vo.setCategories(temp);
            Double d;
            try {
                d = jsonArray.get(i).getAsJsonObject().get("uvVal").getAsDouble();
            } catch (Exception ex) {
                ex.printStackTrace();
                d = 0.0;
            }
            vo.setUvVal(d);
            try {
                vo.setFansInc((int) parseValue(jsonArray.get(i).getAsJsonObject().get("fansInc").getAsString()));
            } catch (Exception ex) {
                ex.printStackTrace();
                vo.setFansInc(0);
            }

            vo.setLiveTrafficMap(jsonArray.get(i).getAsJsonObject().get("liveTrafficMap").getAsJsonObject().toString());
            vo.setPlatform("灰豚");
            liveRecordDOS.add(vo);
        }
    }

    public void parserLiveMinRecordJson(String json, HttpVO vo) {
        JsonParser parser = new JsonParser();
        JsonObject object = null;
        JsonArray jsonArray = null;
        SimpleDateFormatUtils.setDateFormat("yyyy-MM-dd HH:mm:ss");
        String decodedURL = "";
        LiveMinRecordDO liveMinRecordDO = new LiveMinRecordDO();
        try {
            decodedURL = URLDecoder.decode(vo.getUrl(), StandardCharsets.UTF_8);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        String temp = decodedURL.substring(decodedURL.indexOf("roomId=") + 7, decodedURL.indexOf("&uid"));
        liveMinRecordDO.setRoomId(Long.parseLong(temp));
        temp = decodedURL.substring(decodedURL.indexOf("uid=") + 4, decodedURL.indexOf("&tags"));
        liveMinRecordDO.setAuthorId(Long.parseLong(temp));
        try {
            object = parser.parse(json).getAsJsonObject();

        } catch (Exception ex) {
            ex.printStackTrace();
            return;

        }
        //fans
        Map<String, Integer> fansMap = new HashMap<>();
        jsonArray = object.get("data").getAsJsonObject().get("converList").getAsJsonArray();

        for (int i = 0; i < jsonArray.size(); i++) {
            fansMap.put(jsonArray.get(i).getAsJsonObject().get("date").getAsString(), jsonArray.get(i).getAsJsonObject().get("fans").getAsInt());
        }
        //
        jsonArray = object.get("data").getAsJsonObject().get("flowList").getAsJsonArray();

        for (int i = 0; i < jsonArray.size(); i++) {
            LiveMinRecordDO vo1 = new LiveMinRecordDO();
            vo1.setRoomId(liveMinRecordDO.getRoomId());
            vo1.setAuthorId(liveMinRecordDO.getAuthorId());
            temp = jsonArray.get(i).getAsJsonObject().get("date").getAsString();
            vo1.setFansInc(fansMap.get(temp));
            vo1.setRecordTime(SimpleDateFormatUtils.parserDate(temp + ":00"));
            try {
                vo1.setLeaveUcnt(jsonArray.get(i).getAsJsonObject().get("outInc").getAsInt());
            } catch (Exception ex) {
                ex.printStackTrace();
                vo1.setLeaveUcnt(0);
            }
            try {
                vo1.setOnlineUserCnt(jsonArray.get(i).getAsJsonObject().get("maxUserCount").getAsInt());
            } catch (Exception ex) {
                ex.printStackTrace();
                vo1.setOnlineUserCnt(0);
            }
            try {
                vo1.setWatchUcnt(jsonArray.get(i).getAsJsonObject().get("watchInc").getAsInt());
            } catch (Exception ex) {
                ex.printStackTrace();
                vo1.setWatchUcnt(0);
            }
            liveMinRecordDOS.add(vo1);

        }


    }

    public double convertToNumeric(String value) {
        if (value == null || value.isEmpty()) {
            return 0;
        }
        if (value.equals("--")) {
            return 0;
        }
        value = value.replace("+", "");


        String[] parts = value.split("-");
        if (parts.length == 1) {
            if (value.indexOf("亿") >= 0)
                return parseValue(value);
            throw new IllegalArgumentException("Invalid format: " + value);
        }
        if (parts.length == 2) {
            try {
                double lowerBound = parseValue(parts[0]);
                double upperBound = parseValue(parts[1]);
                return (lowerBound + upperBound) / 2;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }


        return -1;


    }

    private double parseValue(String value) {
        if (value == null || value.isEmpty()) {
            return 0;
        }
        double multiplier = 1;
        if (value.endsWith("kw")) {
            multiplier = 10000000; // 千万
            value = value.substring(0, value.length() - 2);
        } else if (value.endsWith("k")) {
            multiplier = 1000; // 千
            value = value.substring(0, value.length() - 1);
        } else if (value.endsWith("w")) {
            multiplier = 10000; // 万
            value = value.substring(0, value.length() - 1);
        } else if (value.endsWith("亿")) {
            multiplier = 100000000; // 万
            value = value.substring(0, value.length() - 1);
        }

        try {
            return Double.parseDouble(value) * multiplier;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0; // 或者根据需求返回其他默认值
        }
    }

}
