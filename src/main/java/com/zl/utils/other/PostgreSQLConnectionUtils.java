package com.zl.utils.other;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @Description:
 * @Param: PostgreSQL 通过jdbc连接PostgreSQL数据库
 * @Auther: zl
 * @Date: 2020/2/25 20:37
 */
public class PostgreSQLConnectionUtils {
    private Connection conn = null;
    private final String user;
    private final String password;


    public PostgreSQLConnectionUtils(String url, String className, String user, String password) {
        this.user = user;
        this.password = password;
        connect(url, className);

    }

    public static void main(String[] args) {

    }

    public boolean connect(String url, String className) {
        if (conn != null)
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        try {
            Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        //url="jdbc:sqlite://E:\\data\\local\\file.db";
        try {
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }

    public Connection getConn() {
        return conn;
    }

    public boolean close() {
        try {
            if (conn != null) {
                conn.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }
}
