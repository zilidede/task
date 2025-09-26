package com.ecommerce.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ll.drissonPage.page.ChromiumTab;
import com.zl.task.save.parser.ParserJsonToHttpVO;
import com.zl.task.vo.http.HttpVO;
import com.zl.task.vo.task.taskResource.DefaultTaskResourceCrawTabList;
import com.zl.utils.io.FileIoUtils;

import java.io.IOException;
import java.util.List;

// 抖音web账号主页内容XHR处理
public class ContentProcessDouYinWebAccountMainImpl implements ContentProcessor {
    private ChromiumTab tab;
    private String savePath;
    public ContentProcessDouYinWebAccountMainImpl(){

        tab= DefaultTaskResourceCrawTabList.getTabList().get(2);
        savePath="D:\\data\\物品库\\下载库\\";
    }
    @Override
    public void process(String filePath) throws IOException {
        HttpVO vo = ParserJsonToHttpVO.parserXHRJson(filePath);
        String json = vo.getResponse().getBody();
        parserOriginalVideoUrls(json);

    }
    public List<String> parserOriginalVideoUrls(String json) throws IOException {
        JsonParser parser = new JsonParser();
        JsonObject object = parser.parse(json).getAsJsonObject();
        JsonArray jsonArray=object.get("aweme_list").getAsJsonArray();
        List<String> urls = new java.util.ArrayList<>();
        for(JsonElement element : jsonArray){
            String videoId=element.getAsJsonObject().get("aweme_id").getAsString();
            String uid=videoId+"-"+element.getAsJsonObject().get("author").getAsJsonObject().get("uid").getAsString()+
                    element.getAsJsonObject().get("author").getAsJsonObject().get("nickname").getAsString();
            String playAddrUrl=element.getAsJsonObject().get("video").getAsJsonObject().get("play_addr").getAsJsonObject().get("uri").getAsString();
            String url="https://www.douyin.com/aweme/v1/play/?video_id="+playAddrUrl;
            urls.add(url);
            String filePath=savePath+uid+".mp4";
            if(!FileIoUtils.fileExists(filePath))
                tab.download().download(url, savePath, uid+".mp4");
        }
        return urls;
    }
}
