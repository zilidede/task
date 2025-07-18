package com.zl.dao.generate;
import java.util.*;
/**
 * @Description:
 * @Param:
 * @Auther: zl
 * @Date: 2025-06-24
 */
public class RelationKeywordScoreDO {
   private Double relationScore=0.0;
   private Double scoreRate=0.0;
   private Integer composIndex=0;
   private Integer searchIndex=0;
   private Integer scoreRank=0;
   private String contentIds="";
   private String relationWordId="";
   private Integer scoreRateRank=0;
   public Double getRelationScore() {
      return relationScore;
   }
   public void setRelationScore(Double relationScore) {
      this.relationScore = relationScore;
   }
   public Double getScoreRate() {
      return scoreRate;
   }
   public void setScoreRate(Double scoreRate) {
      this.scoreRate = scoreRate;
   }
   public Integer getComposIndex() {
      return composIndex;
   }
   public void setComposIndex(Integer composIndex) {
      this.composIndex = composIndex;
   }
   public Integer getSearchIndex() {
      return searchIndex;
   }
   public void setSearchIndex(Integer searchIndex) {
      this.searchIndex = searchIndex;
   }
   public Integer getScoreRank() {
      return scoreRank;
   }
   public void setScoreRank(Integer scoreRank) {
      this.scoreRank = scoreRank;
   }
   public String getContentIds() {
      return contentIds;
   }
   public void setContentIds(String contentIds) {
      this.contentIds = contentIds;
   }
   public String getRelationWordId() {
      return relationWordId;
   }
   public void setRelationWordId(String relationWordId) {
      this.relationWordId = relationWordId;
   }
   public Integer getScoreRateRank() {
      return scoreRateRank;
   }
   public void setScoreRateRank(Integer scoreRateRank) {
      this.scoreRateRank = scoreRateRank;
   }
   
}
