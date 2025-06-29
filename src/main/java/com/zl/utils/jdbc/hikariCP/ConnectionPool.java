package com.zl.utils.jdbc.hikariCP;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zl.utils.other.Ini4jUtils;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPool {
    // 使用 volatile 保证多线程可见性
    private static volatile HikariDataSource dataSource;

    // 私有构造防止实例化
    private ConnectionPool() {}

    /**
     * 初始化连接池（线程安全）
     */
    private static void initializePool() {
        if (dataSource == null) {
            synchronized (ConnectionPool.class) {
                if (dataSource == null) {
                    try {
                        // 加载配置文件
                        Ini4jUtils.loadIni("./data/config/config.ini");
                        Ini4jUtils.setSectionValue("database");
                        // 获取配置参数
                        String jdbcUrl = Ini4jUtils.readIni("url");
                        String driverClass = Ini4jUtils.readIni("className");
                        String username = Ini4jUtils.readIni("user");
                        String password = Ini4jUtils.readIni("password");

                        // 创建 HikariCP 配置
                        HikariConfig config = new HikariConfig();
                        config.setJdbcUrl(jdbcUrl);
                        config.setDriverClassName(driverClass);
                        config.setUsername(username);
                        config.setPassword(password);

                        // 优化连接池参数
                        config.setPoolName("App-PostgreSQL-Pool");
                        config.setMaximumPoolSize(20);          // 最大连接数
                        config.setMinimumIdle(5);               // 最小空闲连接
                        config.setConnectionTimeout(30000);     // 连接超时30s
                        config.setIdleTimeout(600000);          // 空闲连接超时10分钟
                        config.setMaxLifetime(1800000);         // 连接最大生命周期30分钟
                        config.setLeakDetectionThreshold(5000); // 泄漏检测5s

                        // PostgreSQL 优化参数
                       // config.addDataSourceProperty("prepStmtCacheSize", 250);
                      //  config.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
                       // config.addDataSourceProperty("useServerPrepStmts", true);

                        dataSource = new HikariDataSource(config);

                        // 添加关闭钩子
                        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                            if (dataSource != null) {
                                dataSource.close();
                                System.out.println("连接池已安全关闭");
                            }
                        }));
                    } catch (Exception e) {
                        throw new RuntimeException("连接池初始化失败", e);
                    }
                }
            }
        }
    }

    /**
     * 从连接池获取数据库连接
     * @return 数据库连接对象
     */
    public static Connection getConnection() {
        if (dataSource == null) {
            initializePool();
        }

        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("获取数据库连接失败", e);
        }
    }

    /**
     * 安全关闭连接池（应用关闭时调用）
     */
    public static void shutdownPool() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            dataSource = null;
        }
    }
}