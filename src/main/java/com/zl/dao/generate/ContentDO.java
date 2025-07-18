package com.zl.dao.generate;
import java.util.*;
/**
 * @Description:
 * @Param:
 * @Auther: zl
 * @Date: 2025-06-24
 */
public class ContentDO {
   private Long itemId=0l;
   private String localPath="";
   private String title="";
   private String dataSource="";
   public Long getItemId() {
      return itemId;
   }
   public void setItemId(Long itemId) {
      this.itemId = itemId;
   }
   public String getLocalPath() {
      return localPath;
   }
   public void setLocalPath(String localPath) {
      this.localPath = localPath;
   }
   public String getTitle() {
      return title;
   }
   public void setTitle(String title) {
      this.title = title;
   }
   public String getDataSource() {
      return dataSource;
   }
   public void setDataSource(String dataSource) {
      this.dataSource = dataSource;
   }
   
}
