package com.zl.task.save.parser.list;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zl.dao.generate.HourLiveRankDO;
import com.zl.dao.generate.HourLiveRankDao;
import com.zl.task.impl.SaveServiceImpl;
import com.zl.task.save.base.SaveXHRImpl;
import com.zl.task.save.parser.ParserJsonToHttpVO;
import com.zl.task.vo.http.HttpVO;
import com.zl.utils.io.DiskIoUtils;
import com.util.jdbc.generator.jdbc.DefaultDatabaseConnect;
import com.zl.utils.log.LoggerUtils;
import com.zl.utils.time.SimpleDateFormatUtils;
import com.zl.utils.time.TimeUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SaveHourLiveList extends SaveXHRImpl<HourLiveRankDO> {

    private final HourLiveRankDao daoService = new HourLiveRankDao(DefaultDatabaseConnect.getConn());
    private final List<HourLiveRankDO> hourLiveRankDOS = new ArrayList<>();
    private final SaveServiceImpl saveService = new SaveServiceImpl();

    public SaveHourLiveList() throws SQLException {

    }

    public static void main(String[] args) throws Exception {
        SaveHourLiveList saveHourLiveList = new SaveHourLiveList();
        saveHourLiveList.save("S:\\data\\task\\爬虫\\list");
    }

    @Override
    public void save(String sDir) throws Exception {
        String dir = sDir + "\\boardList";
        List<String> files = DiskIoUtils.getFileListFromDir(dir);
        for (String file : files) {
            HttpVO httpVO = ParserJsonToHttpVO.parserXHRJson(file);
            String url = httpVO.getUrl();
            String json = httpVO.getResponse().getBody();
            List<HourLiveRankDO> result = parserJson(json, httpVO);
            if (result == null) {
                LoggerUtils.logger.error("boardList文件解析失败" + file);
            }

        }

        saveService.savePgSql(daoService, hourLiveRankDOS);
        hourLiveRankDOS.clear();

    }

    @Override
    public List<HourLiveRankDO> parserJson(String json, HttpVO vo) throws UnsupportedEncodingException {

        JsonParser parser = new JsonParser();
        JsonObject object = null;
        JsonArray jsonArray = null;
        SimpleDateFormatUtils.setDateFormat("yyyy/MM/dd HH:mm:ss");
        String decodedURL = "";
        HourLiveRankDO hourLiveRankDO = new HourLiveRankDO();
        try {
            decodedURL = URLDecoder.decode(vo.getUrl(), StandardCharsets.UTF_8);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        String temp = decodedURL.substring(decodedURL.indexOf("begin_date") + 11, decodedURL.indexOf("&end_date"));
        hourLiveRankDO.setStartTime(SimpleDateFormatUtils.parserDate(temp));
        temp = decodedURL.substring(decodedURL.indexOf("end_date") + 9, decodedURL.indexOf("&date_type"));
        hourLiveRankDO.setEndTime(SimpleDateFormatUtils.parserDate(temp));
        temp = decodedURL.substring(decodedURL.indexOf("category_id") + 12, decodedURL.indexOf("&brand_type"));
        hourLiveRankDO.setCategoryId(Integer.parseInt(temp));
        try {
            object = parser.parse(json).getAsJsonObject();
            jsonArray = object.get("data").getAsJsonObject().get("board_list").getAsJsonArray();
        } catch (Exception ex) {
            ex.printStackTrace();

            return null;
        }
        for (int i = 0; i < jsonArray.size(); i++) {
            HourLiveRankDO liveRankDO = new HourLiveRankDO();
            liveRankDO.setCategoryId(hourLiveRankDO.getCategoryId());
            liveRankDO.setEndTime(hourLiveRankDO.getEndTime());
            liveRankDO.setStartTime(hourLiveRankDO.getStartTime());
            liveRankDO.setAuthorId(jsonArray.get(i).getAsJsonObject().get("author_id").getAsLong());
            liveRankDO.setNickname(jsonArray.get(i).getAsJsonObject().get("nickname").getAsString());
            temp = jsonArray.get(i).getAsJsonObject().get("fans_ucnt_p1d").getAsString();
            if (temp.indexOf("万") > 0) {
                temp = temp.replace("万", "");
                Double d = Double.parseDouble(temp) * 10000;
                liveRankDO.setFansUcntP1d(d.intValue());
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(hourLiveRankDO.getStartTime());
            liveRankDO.setLiveDuration(TimeUtils.convertToSeconds(jsonArray.get(i).getAsJsonObject().get("live_start_time").getAsString()));
            String temp1 = calendar.get(Calendar.YEAR) + "/" + jsonArray.get(i).getAsJsonObject().get("live_start_time").getAsString() + ":00";
            liveRankDO.setLiveStartTime(SimpleDateFormatUtils.parserDate(temp1));
            Double val = 0.0;
            JsonArray jsonArray1 = jsonArray.get(i).getAsJsonObject().get("pay_amt").getAsJsonObject().get("value_range").getAsJsonArray();
            for (int j = 0; j < jsonArray1.size(); j++) {
                val = (+jsonArray1.get(j).getAsJsonObject().get("value").getAsDouble());
            }
            val = val / 2000;
            liveRankDO.setPayAmt(val);
            val = 0.0;
            jsonArray1 = jsonArray.get(i).getAsJsonObject().get("product_click_cnt").getAsJsonObject().get("value_range").getAsJsonArray();
            for (int j = 0; j < jsonArray1.size(); j++) {
                val = (+jsonArray1.get(j).getAsJsonObject().get("value").getAsDouble());
            }
            val = val / 2;
            liveRankDO.setProductClickCnt((int) Math.floor(val));
            liveRankDO.setRoomId(jsonArray.get(i).getAsJsonObject().get("room_id").getAsLong());
            liveRankDO.setRoomTitle(jsonArray.get(i).getAsJsonObject().get("room_title").getAsString());
            int iVal = 0;
            jsonArray1 = jsonArray.get(i).getAsJsonObject().get("watch_cnt").getAsJsonObject().get("value_range").getAsJsonArray();
            for (int j = 0; j < jsonArray1.size(); j++) {
                iVal = (+jsonArray1.get(j).getAsJsonObject().get("value").getAsInt());
            }
            iVal = iVal / 2;
            liveRankDO.setWatchCnt(iVal);
            hourLiveRankDOS.add(liveRankDO);
        }
        return hourLiveRankDOS;
    }


}
