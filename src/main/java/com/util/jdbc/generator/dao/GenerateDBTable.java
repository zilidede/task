package com.util.jdbc.generator.dao;


import com.util.jdbc.generator.bean.DBFieldBean;
import com.util.jdbc.generator.jdbc.SimpleJDBC;

import java.sql.SQLException;
import java.util.List;

/**
 * @className: com.craw.nd.common.generator.dao.bean-> GenerateTable
 * @description: 通过javabean 自动生成各数据库表结构
 * @author: zl
 * @createDate: 2023-01-04 18:29
 * @version: 1.0
 * @todo:
 */
public class GenerateDBTable {
    private final SimpleJDBC sJdbc;

    public GenerateDBTable(SimpleJDBC simpleJDBC) {
        sJdbc = simpleJDBC;
    }

    public boolean GeneratePgSqlTable(List<DBFieldBean> fields, String table) throws SQLException {
        //生成创建表sql
        String sql = String.format("DROP TABLE IF EXISTS \"public\".\"%s\";", table);
        sql = sql + String.format("CREATE TABLE \"public\".\"%s\" (\n", table);
        for (DBFieldBean field : fields) {
            String t = String.format("  \"%s\" %s,", field.getField(), field.getType());
            sql = sql + t;
        }
        sql = sql.substring(0, sql.length() - 1);
        sql = sql +
                ")\n" +
                ";";
        sJdbc.executeSql(sql);
        return true;
    }


}
