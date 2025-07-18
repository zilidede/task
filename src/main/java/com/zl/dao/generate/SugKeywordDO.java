package com.zl.dao.generate;
import java.util.*;
/**
 * @Description:
 * @Param:
 * @Auther: zl
 * @Date: 2025-06-25
 */
public class SugKeywordDO {
   private Date recordTime=null;
   private String rootKeyword="";
   private String relationKeyword="";
   private Integer relationRank=0;
   private String dataSource="";
   public Date getRecordTime() {
      return recordTime;
   }
   public void setRecordTime(Date recordTime) {
      this.recordTime = recordTime;
   }
   public String getRootKeyword() {
      return rootKeyword;
   }
   public void setRootKeyword(String rootKeyword) {
      this.rootKeyword = rootKeyword;
   }
   public String getRelationKeyword() {
      return relationKeyword;
   }
   public void setRelationKeyword(String relationKeyword) {
      this.relationKeyword = relationKeyword;
   }
   public Integer getRelationRank() {
      return relationRank;
   }
   public void setRelationRank(Integer relationRank) {
      this.relationRank = relationRank;
   }
   public String getDataSource() {
      return dataSource;
   }
   public void setDataSource(String dataSource) {
      this.dataSource = dataSource;
   }
   
}
