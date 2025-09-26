package com.ecommerce.craw.dy.shop.compass;

import com.ecommerce.dao.generate.*;
import com.ecommerce.impl.ContentProcessor;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ll.drissonPage.page.ChromiumTab;
import com.zl.task.save.parser.ParserJsonToHttpVO;
import com.zl.task.vo.http.HttpVO;
import com.zl.utils.io.FileIoUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 抖店罗盘处理
public class ContentProcessDouYinShopCompassLiveImpl implements ContentProcessor {
    public static void main(String[] args) throws IOException, SQLException {
        ContentProcessor parser = new ContentProcessDouYinShopCompassLiveImpl();
        String filePath="D:\\data\\task\\爬虫\\douYinShopApi\\compass.jinritemai.comCompassApiShopLiveLiveRankBoardListV2\\2025-09-25 19-44-03-243.txt";
        parser.process(filePath);
    }
    private List<AuthorInfoDO> authorInfoDOS=new java.util.ArrayList<>();
    private List<DrainageVideosDO> drainageVideosDOS=new java.util.ArrayList<>();;
    private List<VideoStatsDO> videoStatsDOS=new java.util.ArrayList<>();;
    private List<LivePerformanceHourlyDO> livePerformanceHourlyDOS=new java.util.ArrayList<>();;
    private DrainageVideosDao drainageVideosDao=new DrainageVideosDao();
    private AuthorInfoDao authorInfoDao=new AuthorInfoDao();
    private LivePerformanceHourlyDao livePerformanceHourlyDao=new LivePerformanceHourlyDao();
    private VideoStatsDao videoStatsDao=new VideoStatsDao();
    private Map<String ,List<VideoStatsDO>> videoMaps=new HashMap<>();

    private String savePath;
    public ContentProcessDouYinShopCompassLiveImpl() throws SQLException {

    }
    @Override
    public void process(String filePath) throws IOException {
        HttpVO vo = ParserJsonToHttpVO.parserXHRJson(filePath);
        String json = vo.getResponse().getBody();
        parserOriginalXHR(json);

    }
    public List<String> parserOriginalXHR(String json) throws IOException {
        JsonParser parser = new JsonParser();
        JsonObject object = parser.parse(json).getAsJsonObject();
        JsonArray jsonArray=object.get("data").getAsJsonObject().get("module_data").getAsJsonObject()
                .get("live_trade_flow_rank").getAsJsonObject().get("compass_general_table_value")
                .getAsJsonObject().get("data")
                .getAsJsonArray();
        List<String> urls = new java.util.ArrayList<>();
        for(JsonElement element : jsonArray){
            JsonObject object1=element.getAsJsonObject().get("cell_info").getAsJsonObject()
                            .get("click_pay_rate").getAsJsonObject().get("index_values").getAsJsonObject()
                    .get("extra_value").getAsJsonObject();
            LivePerformanceHourlyDO liveDo=new LivePerformanceHourlyDO();
            liveDo.setClickPayRateLower(object1.get("lower").getAsJsonObject().get("value").getAsDouble());
            liveDo.setClickPayRateUpper(object1.get("upper").getAsJsonObject().get("value").getAsDouble());

            JsonArray objectArr=element.getAsJsonObject().get("cell_info").getAsJsonObject()
                    .get("drainage_video_list").getAsJsonObject().get("video_list").getAsJsonArray();
            //视频信息
            List<VideoStatsDO> videoStatsDOS1=new java.util.ArrayList<>();
            for(JsonElement element1 : objectArr){
                VideoStatsDO videosDO=new VideoStatsDO();
                videosDO.setVideoId(element1.getAsJsonObject().get("video_id").getAsString());
                videosDO.setTitle(element1.getAsJsonObject().get("video_title").getAsString());
                videosDO.setCoverUrl(element1.getAsJsonObject().get("video_cover").getAsString());
                videosDO.setStatus(element1.getAsJsonObject().get("video_status").getAsInt());
                videosDO.setPublishTime(element1.getAsJsonObject().get("publish_time").getAsLong());
                videoStatsDOS.add(videosDO);
                videoStatsDOS1.add(videosDO);
            }
            object1=element.getAsJsonObject().get("cell_info").getAsJsonObject()
                    .get("pay_amt").getAsJsonObject().get("index_values").getAsJsonObject()
                    .get("extra_value").getAsJsonObject();
            liveDo.setPayAmtLower(object1.get("lower").getAsJsonObject().get("value").getAsDouble());
            liveDo.setPayAmtUpper(object1.get("upper").getAsJsonObject().get("value").getAsDouble());

            object1=element.getAsJsonObject().get("cell_info").getAsJsonObject()
                    .get("product_click_cnt").getAsJsonObject().get("index_values").getAsJsonObject()
                    .get("extra_value").getAsJsonObject();
            liveDo.setProductClickCntLower(object1.get("lower").getAsJsonObject().get("value").getAsInt());
            liveDo.setProductClickCntLower(object1.get("upper").getAsJsonObject().get("value").getAsInt());
            object1=element.getAsJsonObject().get("cell_info").getAsJsonObject()
                    .get("room").getAsJsonObject().get("room").getAsJsonObject();
            liveDo.setLiveRoomId(object1.get("live_room_id").getAsString());
            liveDo.setLiveTitle(object1.get("live_room_title").getAsString());
            liveDo.setLiveStartTs(object1.get("live_start_ts").getAsLong());
            try {
                liveDo.setLiveEndTs(object1.get("live_end_ts").getAsLong());
            }
            catch (Exception e){
                liveDo.setLiveEndTs(0L);
            }
            liveDo.setLiveStatus(object1.get("live_status").getAsInt());
           liveDo.setLiveDuration(object1.get("live_duration").getAsInt());
           //作者信息
           AuthorInfoDO authorInfoDO=new AuthorInfoDO();
           object1=object1.get("author").getAsJsonObject();
           authorInfoDO.setAuthorId(object1.get("author_id").getAsString());
           authorInfoDO.setNickName(object1.get("nick_name").getAsString());
           authorInfoDO.setAccountType(0);
           authorInfoDO.setFansCnt(object1.get("fans_cnt").getAsLong());
           authorInfoDO.setCoverUrl(object1.get("cover_url").getAsString());
           authorInfoDO.setShortId(object1.get("short_id").getAsString());
           authorInfoDO.setQrCode(object1.get("qr_code").getAsString());
           authorInfoDOS.add(authorInfoDO);
           liveDo.setAuthorId(authorInfoDO.getAuthorId());
           object1=element.getAsJsonObject().get("cell_info").getAsJsonObject()
                    .get("shop_id").getAsJsonObject().get("value").getAsJsonObject();
           liveDo.setShopId(object1.get("value").getAsLong());
            object1=element.getAsJsonObject().get("cell_info").getAsJsonObject()
                    .get("top_channel").getAsJsonObject().get("value").getAsJsonObject();
            liveDo.setTopChannel(object1.get("value_str").getAsString());
            object1=element.getAsJsonObject().get("cell_info").getAsJsonObject()
                    .get("watch_cnt").getAsJsonObject().get("index_values").getAsJsonObject()
                    .get("extra_value").getAsJsonObject();
            liveDo.setWatchCntLower(object1.get("lower").getAsJsonObject().get("value").getAsInt());
            liveDo.setWatchCntUpper(object1.get("upper").getAsJsonObject().get("value").getAsInt());
            livePerformanceHourlyDOS.add(liveDo);
            videoMaps.put(liveDo.getLiveRoomId(),videoStatsDOS1);
        }
        return urls;
    }
}
