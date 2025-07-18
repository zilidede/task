package com.zl.dao.generate;
import java.util.*;
/**
 * @Description:
 * @Param:
 * @Auther: zl
 * @Date: 2025-07-18
 */
public class TrendinsightKeywordsDO {
   private Integer keywordSearchIndex=0;
   private Date recordTime=null;
   private String keywordContentList="";
   private String keywordSearchPointList="";
   private String keywordInfluScope="";
   private Integer keywordContentIndex=0;
   private String keyword="";
   public Integer getKeywordSearchIndex() {
      return keywordSearchIndex;
   }
   public void setKeywordSearchIndex(Integer keywordSearchIndex) {
      this.keywordSearchIndex = keywordSearchIndex;
   }
   public Date getRecordTime() {
      return recordTime;
   }
   public void setRecordTime(Date recordTime) {
      this.recordTime = recordTime;
   }
   public String getKeywordContentList() {
      return keywordContentList;
   }
   public void setKeywordContentList(String keywordContentList) {
      this.keywordContentList = keywordContentList;
   }
   public String getKeywordSearchPointList() {
      return keywordSearchPointList;
   }
   public void setKeywordSearchPointList(String keywordSearchPointList) {
      this.keywordSearchPointList = keywordSearchPointList;
   }
   public String getKeywordInfluScope() {
      return keywordInfluScope;
   }
   public void setKeywordInfluScope(String keywordInfluScope) {
      this.keywordInfluScope = keywordInfluScope;
   }
   public Integer getKeywordContentIndex() {
      return keywordContentIndex;
   }
   public void setKeywordContentIndex(Integer keywordContentIndex) {
      this.keywordContentIndex = keywordContentIndex;
   }
   public String getKeyword() {
      return keyword;
   }
   public void setKeyword(String keyword) {
      this.keyword = keyword;
   }
   
}
