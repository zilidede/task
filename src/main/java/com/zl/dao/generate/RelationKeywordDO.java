package com.zl.dao.generate;
import java.util.*;
/**
 * @Description:
 * @Param:
 * @Auther: zl
 * @Date: 2025-06-24
 */
public class RelationKeywordDO {
   private String relationWord="";
   private Date endDate=null;
   private String rootKeyword="";
   private String dataSource="";
   private Date startDate=null;
   private String relationWordId="";
   public String getRelationWord() {
      return relationWord;
   }
   public void setRelationWord(String relationWord) {
      this.relationWord = relationWord;
   }
   public Date getEndDate() {
      return endDate;
   }
   public void setEndDate(Date endDate) {
      this.endDate = endDate;
   }
   public String getRootKeyword() {
      return rootKeyword;
   }
   public void setRootKeyword(String rootKeyword) {
      this.rootKeyword = rootKeyword;
   }
   public String getDataSource() {
      return dataSource;
   }
   public void setDataSource(String dataSource) {
      this.dataSource = dataSource;
   }
   public Date getStartDate() {
      return startDate;
   }
   public void setStartDate(Date startDate) {
      this.startDate = startDate;
   }
   public String getRelationWordId() {
      return relationWordId;
   }
   public void setRelationWordId(String relationWordId) {
      this.relationWordId = relationWordId;
   }
   
}
