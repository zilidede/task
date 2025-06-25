package com.zl.task.save.parser.douYinWeb;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zl.dao.generate.SugKeywordDO;
import com.zl.task.vo.http.HttpVO;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// 解析抖音web搜索下拉框词
public class ParserDouYinWebSug {
    private List<SugKeywordDO> sugs;
    public ParserDouYinWebSug() throws SQLException {

    }
    public List<String> parser(HttpVO httpVO) throws UnsupportedEncodingException {
        List<String> result = new ArrayList<>();
        String url=httpVO.getUrl();
        url= URLDecoder.decode( url, "UTF-8" );
        Integer beginIndex=url.indexOf("keyword=")+8;
        Integer endIndex=url.indexOf("&source");
        String keyword=url.substring(beginIndex,endIndex);
        JsonParser parser = new JsonParser();
        JsonObject object = parser.parse(httpVO.getResponse().getBody()).getAsJsonObject();
        JsonArray jsonArray=object.get("sug_list").getAsJsonArray();
        int i=0;
        for(JsonElement element : jsonArray){
            SugKeywordDO sug=new SugKeywordDO();
            sug.setRecordTime(new Date());
            sug.setRootKeyword(keyword);
            String relaKeyword=element.getAsJsonObject().get("content").getAsString();
            sug.setRelationKeyword(relaKeyword);
            sug.setRelationRank(++i);
            sug.setDataSource("抖音-web网页");
            sugs.add(sug);
            result.add(relaKeyword);
        }
        return result;
    }

    public List<SugKeywordDO> getSugs() {
        return sugs;
    }

    public void setSugs(List<SugKeywordDO> sugs) {
        this.sugs = sugs;
    }
}
