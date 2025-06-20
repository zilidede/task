package com.zl.utils.jdbc.generator.jdbc;


import com.zl.utils.io.FileIoUtils;
import com.zl.utils.jdbc.generator.convert.JdbcTypeToJava;

import java.io.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


/**
 * @Description: 简单操作jdbc-连接 -增删改查。 测试用例使用到数据库-local-potgreSql/test
 * @Param:
 * @Auther: zl
 * @Date: 2020/3/15 15:15
 */
public class SimpleJDBC {
    private final LinkedList<Connection> connPool = new LinkedList<>();
    private Connection conn;
    private String url;
    private String schema;

    public SimpleJDBC(String className, String url, String user, String password, String schema) {
        connect(className, url, user, password, schema);
    }

    public static void testPgsql() {
        String className = "org.postgresql.Driver";
        String url = "jdbc:postgresql://127.0.0.1:5432/test";
        String user = "postgres";
        String password = "123456";
        SimpleJDBC obj = new SimpleJDBC(className, url, user, password, "test");
        try {
            //obj.callStmt();
            obj.commit();
            obj.addData();
            obj.updateData();
            obj.selectData();
            obj.deleteData();
            obj.stmtBatchExecute();
            obj.pamBatchExecute();
            obj.blobExecute();
            obj.getBytea();
            obj.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void testMysql() {
        String className = "org.postgresql.Driver";
        String url = "jdbc:postgresql://127.0.0.1:5432/test";
        String user = "postgres";
        String password = "123456";

        SimpleJDBC obj = new SimpleJDBC(className, url, user, password, "test");
        try {
            //obj.callStmt();
            obj.commit();
            obj.addData();
            obj.updateData();
            obj.selectData();
            obj.deleteData();
            obj.stmtBatchExecute();
            obj.pamBatchExecute();
            obj.blobExecute();
            obj.getBytea();
            obj.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    //获取数据库信息

    public static void main(String[] args) {

    }

    public Connection connect(String className, String url, String user, String password, String schema) {
        //连接数据库
        this.url = url;
        this.schema = schema;
        if (conn != null)
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        try {
            Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        //url="jdbc:sqlite://E:\\data\\local\\file.db";
        System.out.println("正在连接数据库......");
        try {
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return conn;

    }

    private boolean close() {
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

    /**
     * a catalog name; must match the catalog name as it is stored in the database; "" retrieves those without a catalog; null means that the catalog name should not be used to narrow the search
     */
    protected String catalog() {
        return null;
    }

    /**
     * a table name pattern; must match the table name as it is stored in the database
     */
    protected String tableNamePattern() {
        return "%";
    }

    protected String[] types() {
        return new String[]{"TABLE", "VIEW"};
    }

    protected String schemaPattern() {
        //mysql的schemaPattern为数据库名称
        return "%";
    }

    public List<String> listAllTables(String catalog) {
        if (conn == null) {
            return null;
        }

        List<String> result = new ArrayList<>();
        ResultSet rs = null;
        try {
            //参数1 int resultSetType
            //ResultSet.TYPE_FORWORD_ONLY 结果集的游标只能向下滚动。
            //ResultSet.TYPE_SCROLL_INSENSITIVE 结果集的游标可以上下移动，当数据库变化时，当前结果集不变。
            //ResultSet.TYPE_SCROLL_SENSITIVE 返回可滚动的结果集，当数据库变化时，当前结果集同步改变。
            //参数2 int resultSetConcurrency
            //ResultSet.CONCUR_READ_ONLY 不能用结果集更新数据库中的表。
            //ResultSet.CONCUR_UPDATETABLE 能用结果集更新数据库中的表
            conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            DatabaseMetaData meta = conn.getMetaData();
            //目录名称; 数据库名; 表名称; 表类型;
            rs = meta.getTables(catalog, schemaPattern(), "%", types());
            while (rs.next()) {
                result.add(rs.getString("TABLE_NAME"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public TableInfo getTableInfo(String catalog, String dataBase, String tableName) {
        TableInfo result = new TableInfo();
        result.setTableName(tableName);
        result.setFieldsMap(listAllFields(catalog, dataBase, tableName));
        String primaryKey = primaryKey(catalog, tableName);
        if (primaryKey.equals("")) {
            for (Object key : result.getFieldsMap().keySet()) {
                String lKey = (String) key;
                primaryKey = lKey;
                break;
            }

        }
        result.setPrimaryKey(primaryKey);
        return result;
    }

    public String primaryKey(String catalog, String tableName) {
        String primaryKey = "";
        try {
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet pkInfo = metaData.getPrimaryKeys(catalog, null, tableName);
            while (pkInfo.next()) {
                primaryKey = pkInfo.getString("COLUMN_NAME");
            }

        } catch (Exception e) {

        }
        // 数据库的所有数据
        // 获取表的主键名字
        return primaryKey;
    }

    public HashMap<String, String> listAllFields(String catalog, String dataBase, String tableName) {
        if (conn == null) {
            return null;
        }
        HashMap<String, String> result = new HashMap<>();
        ResultSet rs = null;
        try {
            DatabaseMetaData meta = conn.getMetaData();
            rs = meta.getColumns(catalog, schemaPattern(), tableName.trim(), null);
            while (rs.next()) {
                String typeName = JdbcTypeToJava.toJavaType(dataBase, rs.getString("TYPE_NAME"));
                String columnName = rs.getString("COLUMN_NAME");
                if (!typeName.equals(""))
                    result.put(columnName, typeName);
                //int i=new Integer(rs.getString("DATA_TYPE"));
                //System.out.println(rs.getString("TYPE_NAME"));
            }
        } catch (Exception e) {

        }
        return result;

    }

    private boolean addData() throws SQLException {
        String sql = "insert into  test (id,name) values(?,?)";
        PreparedStatement pStmt = conn.prepareStatement(sql);
        pStmt.setInt(1, 1);
        pStmt.setString(2, "zili");
        return pStmt.execute();
    }

    private void updateData() throws SQLException {
        String sql = "update test SET name =? WHERE id=?";
        PreparedStatement pStmt = conn.prepareStatement(sql);
        pStmt.setString(1, "pzl");
        pStmt.setInt(2, 1);
        pStmt.execute();
    }

    private ResultSet selectData() throws SQLException {
        String sql = "select  name from test  WHERE id=?";
        PreparedStatement pStmt = conn.prepareStatement(sql);
        pStmt.setInt(1, 1);
        ResultSet set = pStmt.executeQuery();
        if (set.next())
            System.out.println("name: " + set.getString("name"));
        return set;
    }

    private boolean deleteData() throws SQLException {
        String sql = "delete from  test  WHERE id=?";
        PreparedStatement pStmt = conn.prepareStatement(sql);
        pStmt.setInt(1, 1);
        return pStmt.execute();
    }

    public boolean executeSql(String sql) throws SQLException {
        PreparedStatement pStmt = conn.prepareStatement(sql);
        return pStmt.execute();
    }

    private boolean stmtBatchExecute() throws SQLException {
        // 批处理两种方式
        // Statement 不同的sql语句批量执行。
        Statement stmt = conn.createStatement();
        String sql = "insert into  test (id,name) values(1,'zl')";
        stmt.addBatch(sql);
        sql = "update   test SET name='pzl' WHERE id=1 ";
        stmt.addBatch(sql);
        stmt.executeBatch();
        stmt.clearBatch();

        return true;
    }

    private void pamBatchExecute() throws SQLException {
        // PreparedStatement
        String sql = "insert into  test (id,name) values(?,?)";
        PreparedStatement pStmt = conn.prepareStatement(sql);
        for (int i = 2; i < 10; i++) {
            pStmt.setInt(1, i);
            pStmt.setString(2, "pzl");
            pStmt.addBatch();
        }
        pStmt.executeBatch();
        pStmt.clearBatch();
    }

    private void blobExecute() throws SQLException, FileNotFoundException {
        //二进制文本 imgData->c:/test/img
        String filePath = "./data/img/1.jpg";
        byte[] imgData = FileIoUtils.fileToByte(filePath);
        FileInputStream fileStream = new FileInputStream(new File(filePath));
        String sql = "insert into  test (id,name,img_data) values(?,?,?)";
        PreparedStatement pStmt = conn.prepareStatement(sql);
        pStmt.setInt(1, 11);
        pStmt.setString(2, "zili");
        pStmt.setBinaryStream(3, fileStream);
        pStmt.executeUpdate();
    }

    private void getBytea() throws SQLException {
        //获取二进制数据
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            String sql = "select img_data from test where id = 11";
            rs = stmt.executeQuery(sql);
            System.out.println("sql=" + sql);
            while (rs.next()) {
                System.out.println(rs.getMetaData().getColumnTypeName(1));
                OutputStream ops = null;
                InputStream ips = null;
                File file = new File("./data/img/2.jpg");
                try {
                    ips = rs.getBinaryStream(1);
                    byte[] buffer = new byte[ips.available()];//or other value like 1024
                    ops = new FileOutputStream(file);
                    for (int i; (i = ips.read(buffer)) > 0; ) {
                        ops.write(buffer, 0, i);
                        ops.flush();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace(System.out);
                } finally {
                    ips.close();
                    ops.close();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace(System.out);
            }
        }
    }

    private void callStmt() throws SQLException {
        //调用存储过程-未解决
        Connection connection = null;
        CallableStatement callableStatement = null;
        callableStatement = conn.prepareCall("{call getName(?)}");
        callableStatement.setInt(1, 1);
        //注册第2个参数,类型是VARCHAR
        // callableStatement.registerOutParameter(2, Types.VARCHAR);
        //  callableStatement.execute();

        //获取传出参数[获取存储过程里的值]
        String result = callableStatement.getString(1);
        System.out.println(result);

    }

    private void commit() {
        //开启事务
        PreparedStatement pStmt = null;
        try {
            conn.setAutoCommit(false);
            String sql = "insert into  test (id,name) values(?,?)";
            pStmt = conn.prepareStatement(sql);
            pStmt.setInt(1, 12);
            pStmt.setString(2, "zili");
            pStmt.executeUpdate();
            sql = "update test SET name =? WHERE id=?";
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, "pzl");
            pStmt.setInt(2, 12);
            pStmt.executeUpdate();
            conn.commit();
            conn.setAutoCommit(true);

        } catch (SQLException e) {
            try {
                conn.rollback();
                conn.setAutoCommit(true);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }

        }


    }

    private void connectPool(String className, String url, String user, String password, int poolCount) {
        try {
            Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();

        }
        //url="jdbc:sqlite://E:\\data\\local\\file.db";
        System.out.println("正在连接Sqlite数据库......");
        try {
            for (int i = 0; i < poolCount; i++) {
                conn = DriverManager.getConnection(url, user, password);
                connPool.add(conn);
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    public Connection getConnection() {
        if (connPool.size() > 0) {
            final Connection connection = connPool.removeFirst();
            //看看池的大小
            System.out.println(connPool.size());
            //返回一个动态代理对象
            return (Connection) Proxy.newProxyInstance(SimpleJDBC.class.getClassLoader(), connection.getClass().getInterfaces(), new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                    //如果不是调用close方法，就按照正常的来调用
                    if (!method.getName().equals("close")) {
                        method.invoke(connection, args);
                    } else {

                        //进到这里来，说明调用的是close方法
                        connPool.add(connection);

                        //再看看池的大小
                        System.out.println(connPool.size());

                    }
                    return null;
                }

            });
        }
        return null;
    }
}
