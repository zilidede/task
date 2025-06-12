package com.zl.utils.jdbc.generator.jdbc;

import java.util.HashMap;

/**
 * @Description:
 * @Param:
 * @Auther: zl
 * @Date: 2020/4/27 16:12
 */
public class TableInfo {
    private String tableName;
    private HashMap<String, String> fieldsMap;
    private String primaryKey;

    public HashMap<String, String> getFieldsMap() {
        return fieldsMap;
    }

    public void setFieldsMap(HashMap<String, String> fieldsMap) {
        this.fieldsMap = fieldsMap;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

}
