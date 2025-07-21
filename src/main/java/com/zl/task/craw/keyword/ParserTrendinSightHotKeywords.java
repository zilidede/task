package com.zl.task.craw.keyword;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zl.dao.generate.ContentDO;
import com.zl.dao.generate.ContentDao;
import com.zl.dao.generate.TrendinsightKeywordsDO;
import com.zl.dao.generate.TrendinsightKeywordsDao;
import com.zl.task.impl.SaveService;
import com.zl.task.impl.SaveServiceImpl;
import com.zl.task.vo.http.HttpVO;
import com.zl.task.vo.other.GenericListContainerVO;
import com.zl.utils.time.SimpleDateFormatUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

////解析巨量算数关键字json indexGetMultiKeywordHotTrend
public class ParserTrendinSightHotKeywords  {
    private GenericListContainerVO container;
    private TrendinsightKeywordsDao dao;
    private ContentDao contentDao;
    private List<TrendinsightKeywordsDO> list;
    private List<ContentDO> contentDOList;
    private SaveService saveService;
    public ParserTrendinSightHotKeywords() throws SQLException {
        contentDao=new ContentDao();
        dao=new TrendinsightKeywordsDao();
        container=new GenericListContainerVO();
        list=new ArrayList<>();
        contentDOList=new ArrayList<>();
        saveService=new SaveServiceImpl();


    }
    public void parserHotKeywords(HttpVO vo){
        List<String> result = new ArrayList<>();
        JsonParser parser = new JsonParser();
        JsonObject object=null ;
        //request
        String reBody=vo.getRequest().getBody();
        object =parser.parse(reBody).getAsJsonObject();
        String scopeInfluence="全国";
        try {
             scopeInfluence = object.get("region").getAsJsonArray().get(0).getAsString();
        }
        catch (Exception ex){
            scopeInfluence="全国";
        }

        //response
        String reqBody = vo.getResponse().getBody();
        String rootKeyword="";
        SimpleDateFormatUtils.setDateFormat("yyyyMMdd");
        parser = new JsonParser();
        JsonArray jsonArray=null;
       object = null;
        Map<String,TrendinsightKeywordsDO> map=new HashMap<String,TrendinsightKeywordsDO>();
        object =parser.parse(reqBody).getAsJsonObject();
        rootKeyword=object.get("hot_list").getAsJsonArray().get(0).getAsJsonObject().get("keyword").getAsString();
        //综合指数
        jsonArray = object.get("hot_list").getAsJsonArray().get(0).getAsJsonObject().get("hot_list").getAsJsonArray();
        for(int i=0;i<jsonArray.size();i++){
            TrendinsightKeywordsDO tkDo=new TrendinsightKeywordsDO();
            tkDo.setKeywordInfluScope(scopeInfluence);
            tkDo.setKeyword(rootKeyword);
            String sDate=jsonArray.get(i).getAsJsonObject().get("datetime").getAsString();
            tkDo.setRecordTime(SimpleDateFormatUtils.parserDate(sDate));
            tkDo.setKeywordContentIndex(Integer.parseInt(jsonArray.get(i).getAsJsonObject().get("index").getAsString()));
            map.put(sDate+rootKeyword, tkDo);
        }
        //搜索指数
        jsonArray = object.get("hot_list").getAsJsonArray().get(0).getAsJsonObject().get("search_hot_list").getAsJsonArray();
        for(int i=0;i<jsonArray.size();i++){
            String sDate=jsonArray.get(i).getAsJsonObject().get("datetime").getAsString();
            Integer searchIndex=Integer.parseInt(jsonArray.get(i).getAsJsonObject().get("index").getAsString());
            TrendinsightKeywordsDO tkDo=map.get(sDate+rootKeyword);
            tkDo.setKeywordSearchIndex(searchIndex);
        }

        //内容表
        jsonArray = object.get("hot_list").getAsJsonArray().get(0).getAsJsonObject().get("top_point_list").getAsJsonArray();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject object1=jsonArray.get(i).getAsJsonObject();
            String sDate=object1.get("date").getAsString();
            TrendinsightKeywordsDO tkDo=map.get(sDate+rootKeyword);
            //添加内容表
            String content="";
            JsonArray jsonArray1=object1.get("content_list").getAsJsonArray();
            for (int j = 0; j < jsonArray1.size(); j++) {
                JsonObject object2=jsonArray1.get(j).getAsJsonObject();
                ContentDO contentDO=new ContentDO();
                contentDO.setItemId(object2.get("item_id").getAsLong());
                contentDO.setDataSource("抖音");
                contentDO.setTitle(object2.get("title").getAsString());
                contentDOList.add(contentDO);
                content=object2.get("item_id").getAsString()+"&"+content;
            }
            tkDo.setKeywordContentList( content);
        }
        jsonArray = object.get("hot_list").getAsJsonArray().get(0).getAsJsonObject().get("search_top_point_list").getAsJsonArray();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject object1=jsonArray.get(i).getAsJsonObject();
            String sDate=object1.get("date").getAsString();
            TrendinsightKeywordsDO tkDo=map.get(sDate+rootKeyword);
            //添加内容表
            String content="";
            JsonArray jsonArray1=object1.get("query_list").getAsJsonArray();
            for (int j = 0; j < jsonArray1.size(); j++) {
                content=jsonArray1.get(j).getAsString()+"&"+content;
            }
            tkDo.setKeywordSearchPointList( content);
        }

        map.forEach((key, value) -> {
            // 在这里处理 key 和 value
            list.add(value);
        });

        System.out.println("解析成功");

    }


    public  void save() throws Exception {
        saveService.savePgSql(contentDao, contentDOList);
        saveService.savePgSql(dao, list);
    }
}
