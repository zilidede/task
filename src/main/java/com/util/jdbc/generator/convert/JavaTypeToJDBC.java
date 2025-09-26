package com.util.jdbc.generator.convert;

/**
 * @className: com.craw.nd.common.jdbc-> JavaTypeToJDBC
 * @description: java类型转jdbc
 * @author: zl
 * @createDate: 2023-01-04 19:40
 * @version: 1.0
 * @todo:
 */
public class JavaTypeToJDBC {
    public static String javaTpPgSql(String javaType) {
        String pgSqlType = "";
        switch (javaType) {
            case "String":
                pgSqlType = "varchar(255)";
                break;
            case "Boolean":
                pgSqlType = "bool";
                break;
            case "Integer":
                pgSqlType = "int4";
                break;
            case "Long":
                pgSqlType = "int8";
                break;
            case "Short":
                pgSqlType = "int2";
                break;
            case "Float":
                pgSqlType = "Float";
                break;
            case "Double":
                pgSqlType = "numeric(32)";
                break;
            case "Date":
                pgSqlType = "date";
                break;

            default:
                pgSqlType = "";
                break;
        }
        return pgSqlType;
    }
}
