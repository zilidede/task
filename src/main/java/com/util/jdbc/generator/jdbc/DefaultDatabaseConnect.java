package com.util.jdbc.generator.jdbc;


import com.util.jdbc.generator.GenerateDaoCode;
import com.util.jdbc.hikariCP.ConnectionPool;
import com.zl.utils.other.Ini4jUtils;
import com.zl.utils.other.PostgreSQLConnectionUtils;

import java.sql.Connection;

/**
 * @Classname DatabaseConnect
 * @Description TODO
 * @Date 2020/10/3 11:23
 * @Created by Administrator
 */
public class DefaultDatabaseConnect {
    public static Connection getConn() {
        return ConnectionPool.getConnection();
    }
    public static Connection getConn1() {
        Ini4jUtils.loadIni("./data/config/config.ini");
        Ini4jUtils.setSectionValue("database");
        Connection connection = null;
        try {
            String url = Ini4jUtils.readIni("url");
            String className = Ini4jUtils.readIni("className");
            String user = Ini4jUtils.readIni("user");
            String password = Ini4jUtils.readIni("password");
            //
            // password="3q9w2i3lAs+" ;//小耿小新数据库
            PostgreSQLConnectionUtils postgreSQLConnectionUtils = new PostgreSQLConnectionUtils(url, className, user, password);
            connection = postgreSQLConnectionUtils.getConn();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return connection;
    }

    public static SimpleJDBC getPGJDBC() {
        String generateFileDir = "C:\\work\\craw\\src\\main\\java\\com\\craw\\nd\\dao\\generate";
        String packageName = "com.craw.nd.dao.generate";
        String url = "jdbc:postgresql://127.0.0.1:5432/postgres";
        String className = "org.postgresql.Driver";
        String user = "postgres";
        String password = "332500asdqwe";
        String database = "com/";
        SimpleJDBC simpleJDBC = new SimpleJDBC(className, url, user, password, database);
        return simpleJDBC;
    }

    private static void getMySQLConn() {
        String generateFileDir = "C:\\work\\it\\project\\iApp\\iAccount\\src\\main\\java\\com\\app\\dao\\generate";
        String packageName = "com.app.dao.generate";
        String className = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/app?serverTimezone=UTC";
        String user = "postgres";
        String password = "332500";
        String database = "com/app";
        String catalog = "app";
        SimpleJDBC simpleJDBC = new SimpleJDBC(className, url, user, password, database);
        GenerateDaoCode obj = new GenerateDaoCode(generateFileDir, packageName);
        obj.codeGeneration(catalog, "mySql", simpleJDBC);
    }

}
