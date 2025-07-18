package com.zl.task.craw.keyword;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zl.dao.generate.*;
import com.zl.task.vo.http.HttpVO;
import com.zl.task.vo.other.GenericListContainerVO;
import com.zl.utils.jdbc.generator.jdbc.DefaultDatabaseConnect;
import com.zl.utils.time.SimpleDateFormatUtils;
import com.zl.utils.uuid.UUIDGeneratorUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//解析巨量算数关键字json
public class ParserTrendInSightKeywords {

    private  GenericListContainerVO container = new GenericListContainerVO();  //泛型List容器
    private List<ContentDO> contents ; // 内容列表
    private List<RelationKeywordDO> relationKeywords ; // 关联关键字列表
    private List<RelationKeywordScoreDO> relationKeywordScores ; //关联关键字得分列表
    private ContentDao contentDao ;  //内容DAO
    private RelationKeywordDao relationKeywordDao;//关联关键字DAO
    private RelationKeywordScoreDao relationKeywordScoreDao ;//关联关键字得分DAO

    public ParserTrendInSightKeywords() throws SQLException {
        contents = new ArrayList<>();
        relationKeywords = new ArrayList<>();
        relationKeywordScores = new ArrayList<>();
        contentDao = new ContentDao();
        relationKeywordDao = new RelationKeywordDao();
        relationKeywordScoreDao = new RelationKeywordScoreDao();
    }
    public List<String> parserRelationWord(HttpVO httpVO){
        List<String> result = new ArrayList<>();
        JsonParser parser = new JsonParser();
        JsonObject object=null ;
        //request
        String reqBody = httpVO.getRequest().getBody();
        RelationKeywordDO relaKeyWord=new RelationKeywordDO();
        SimpleDateFormatUtils.setDateFormat("yyyyMMdd");
        object = parser.parse(reqBody).getAsJsonObject();
        relaKeyWord.setRootKeyword(object.get("param").getAsJsonObject().get("keyword").getAsString());
        relaKeyWord.setStartDate( SimpleDateFormatUtils.parserDate(object.get("param").getAsJsonObject().get("start_date").getAsString()));
        relaKeyWord.setStartDate( SimpleDateFormatUtils.parserDate(object.get("param").getAsJsonObject().get("end_date").getAsString()));
        relaKeyWord.setDataSource("抖音-巨量算数");
        String response= httpVO.getResponse().getBody();
        object = parser.parse(response).getAsJsonObject();
        JsonArray jsonArray=object.get("relation_word_list").getAsJsonArray();
        for(JsonElement element : jsonArray){
            JsonObject obj = element.getAsJsonObject();
            RelationKeywordScoreDO relationKeywordScore=new RelationKeywordScoreDO();
            RelationKeywordDO relaKeyWord1=new RelationKeywordDO();
            relaKeyWord1.setRelationWord(element.getAsJsonObject().get("relation_word").getAsString());
            relaKeyWord1.setRootKeyword(relaKeyWord.getRootKeyword());
            relaKeyWord1.setStartDate(relaKeyWord.getStartDate());
            relaKeyWord1.setEndDate(relaKeyWord.getEndDate());
            relaKeyWord1.setDataSource(relaKeyWord.getDataSource());
            result.add(relaKeyWord1.getRelationWord());
            relaKeyWord1.setRelationWordId(UUIDGeneratorUtils.generateID(relaKeyWord1.getRootKeyword()+relaKeyWord1.getRelationWord()+relaKeyWord1.getStartDate()+relaKeyWord1.getEndDate()+relaKeyWord1.getDataSource()));

        }
        jsonArray=object.get("search_relation_word_list").getAsJsonArray();
        for(JsonElement element : jsonArray){
            JsonObject obj = element.getAsJsonObject();
            RelationKeywordScoreDO relationKeywordScore=new RelationKeywordScoreDO();
            RelationKeywordDO relaKeyWord1=new RelationKeywordDO();
            relaKeyWord1.setRelationWord(element.getAsJsonObject().get("relation_word").getAsString());
            relaKeyWord1.setRootKeyword(relaKeyWord.getRootKeyword());
            relaKeyWord1.setStartDate(relaKeyWord.getStartDate());
            relaKeyWord1.setEndDate(relaKeyWord.getEndDate());
            relaKeyWord1.setDataSource(relaKeyWord.getDataSource());
            result.add(relaKeyWord1.getRelationWord());
            relaKeyWord1.setRelationWordId(UUIDGeneratorUtils.generateID(relaKeyWord1.getRootKeyword()+relaKeyWord1.getRelationWord()+relaKeyWord1.getStartDate()+relaKeyWord1.getEndDate()+relaKeyWord1.getDataSource()));
            if (obj.has("query_list")) {
                JsonArray queryList = obj.get("query_list").getAsJsonArray();
                for (JsonElement item : queryList) {
                    result.add(item.getAsString());
                }
            }
        }


        return result;
    }

    public   List<String> parser(HttpVO httpVO){
        List<String> result = new ArrayList<>();
            if(httpVO.getUrl().indexOf("index/get_relation_word")>=0){
                result=parserRelationWord(httpVO);
            }
            return result;
    }


    public GenericListContainerVO getContainer() {
        return container;
    }
}
