package com.util.jdbc.generator.bean;


import com.util.jdbc.generator.jdbc.SimpleJDBC;

/**
 * @className: com.craw.nd.common.generator.bean-> DBType
 * @description:
 * @author: zl
 * @createDate: 2023-02-11 16:32
 * @version: 1.0
 * @todo:
 */
public class DBType {
    private SimpleJDBC simpleJDBC;
    private String catalog;
    private String dataBase;
    private String tableName;

    public SimpleJDBC getSimpleJDBC() {
        return simpleJDBC;
    }

    public void setSimpleJDBC(SimpleJDBC simpleJDBC) {
        this.simpleJDBC = simpleJDBC;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getDataBase() {
        return dataBase;
    }

    public void setDataBase(String dataBase) {
        this.dataBase = dataBase;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
