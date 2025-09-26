package com.util.jdbc.generator.bean;

/**
 * @className: com.craw.nd.common.generator.dao.bean-> DBType
 * @description: 数据库字段 -数据结构格式
 * @author: zl
 * @createDate: 2023-01-04 18:44
 * @version: 1.0
 * @todo:
 */
public class DBFieldBean {
    private String field;
    private String type;
    private boolean isPrimaryKey;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        isPrimaryKey = primaryKey;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
