package com.util.jdbc.generator.convert;

/**
 * @Description: 数据类型映射，数据库类型转java类型
 * @Param:
 * @Auther: zl
 * @Date: 2020/4/20 16:40
 */
public class JdbcTypeToJava {
    public static String toJavaType(String dataBase, String columnType) {

        if (dataBase.equals("pgSql")) {
            return pgSqlToJava(columnType);
        } else if (dataBase.equals("mySql")) {
            return mySqlToJava(columnType);
        } else
            return "";
    }

    public static String pgSqlToJava(String columnType) {
        String javaType = "";
        switch (columnType) {
            case "varchar":
            case "text":
                javaType = "String";
                break;
            case "int4":
            case "int2":
                javaType = "Integer";
                break;
            case "bytea":
                javaType = "byte []";
                break;
            case "int8":
                javaType = "Long";
                break;
            case "float8":
                javaType = "Float";
                break;
            case "numeric":
                javaType = "Double";
                break;
            case "date":
                javaType = "Date";
                break;
            case "timestamptz":
                javaType = "Date";
                break;
            default:
                javaType = "";
                break;
        }
        return javaType;
    }

    public static String mySqlToJava(String columnType) {
        String javaType = "";
        switch (columnType) {
            case "VARCHAR":
            case "TEXT":
                javaType = "String";
                break;
            case "INT":
                javaType = "Integer";
                break;
            case "BIGINT":
                javaType = "Long";
                break;
            case "FLOAT":
                javaType = "Float";
                break;
            case "DOUBLE":
                javaType = "Double";
                break;
            case "DATE":
                javaType = "Date";
                break;
            default:
                javaType = "";
                break;
        }
        return javaType;
    }
}
