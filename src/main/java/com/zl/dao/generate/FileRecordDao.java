package com.zl.dao.generate;

import com.zl.config.Config;
import com.zl.dao.DaoService;
import com.zl.dao.ErrorMsg;

import java.sql.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Param:
 * @Auther: zl
 * @Date: 2025-06-11
 */
public class FileRecordDao implements DaoService<FileRecordDO> {
    private static int QueryTimeout = 30;
    private ErrorMsg errorMsg;
    private Connection conn;
    private Statement stmt = null;
    private PreparedStatement pStmt = null;
    private String tableName = "file_record";

    public FileRecordDao(Connection connection) throws SQLException {
        errorMsg = new ErrorMsg();
        this.conn = connection;
        stmt = conn.createStatement();
        stmt.setQueryTimeout(QueryTimeout);  // set timeout to 30 sec;
    }

    public ErrorMsg getErrorMsg() {
        return errorMsg;
    }

    @Override
    public void doInsert(FileRecordDO vo) throws SQLException {
        String sql = "insert into file_record(file_status,file_md5,file_name,file_local_path,file_last_update)values (?,?,?,?,?)";
        pStmt = conn.prepareStatement(sql);
        pStmt.setInt(1, vo.getFileStatus());
        pStmt.setString(2, vo.getFileMd5());
        pStmt.setString(3, vo.getFileName());
        pStmt.setString(4, vo.getFileLocalPath());
        pStmt.setObject(5, vo.getFileLastUpdate());
        if (pStmt.execute())
            errorMsg.setCode(0);
        else {
            errorMsg.setCode(Config.FAIL_INSERT);
            errorMsg.setMsg(pStmt.toString());
        }
    }

    public void doUpdate(FileRecordDO vo) throws SQLException {
        String sql = "update  file_record SET  file_status=? , file_md5=? , file_name=? , file_local_path=? , file_last_update=?WHERE file_status=?";
        pStmt = conn.prepareStatement(sql);
        pStmt.setInt(1, vo.getFileStatus());
        pStmt.setString(2, vo.getFileMd5());
        pStmt.setString(3, vo.getFileName());
        pStmt.setString(4, vo.getFileLocalPath());
        pStmt.setObject(5, vo.getFileLastUpdate());
        pStmt.setInt(6, vo.getFileStatus());
        if (pStmt.execute())
            errorMsg.setCode(0);
        else {
            errorMsg.setCode(Config.FAIL_INSERT);
            errorMsg.setMsg(pStmt.toString());
        }
    }

    @Override
    public void doDelete(FileRecordDO vo) throws SQLException {
        String sql = String.format("DELETE FROM  file_record WHERE file_status=?", vo.getFileStatus());
        pStmt = conn.prepareStatement(sql);
        if (pStmt.execute())
            errorMsg.setCode(0);
        else {
            errorMsg.setCode(Config.FAIL_INSERT);
            errorMsg.setMsg(pStmt.toString());
        }
    }

    //批量插入
    @Override
    public void doBatch(List<FileRecordDO> fileRecordDO) throws SQLException {
        String sql = "insert into file_record(file_status,file_md5,file_name,file_local_path,file_last_update)values (?,?,?,?,?)";

        try (PreparedStatement pStmt = conn.prepareStatement(sql)) {

            // 开启事务（如果尚未自动提交）
            boolean autoCommit = conn.getAutoCommit();
            if (autoCommit) {
                conn.setAutoCommit(false);
            }
            for (FileRecordDO vo : fileRecordDO) {
                pStmt.setInt(1, vo.getFileStatus());
                pStmt.setString(2, vo.getFileMd5());
                pStmt.setString(3, vo.getFileName());
                pStmt.setString(4, vo.getFileLocalPath());
                pStmt.setObject(5, vo.getFileLastUpdate());
                pStmt.addBatch();  // 添加到批处理
            }
            pStmt.executeBatch();  // 执行批量插入
            conn.commit();      // 提交事务

        } catch (Exception e) {
            conn.rollback();    // 出错回滚
            throw e;
        }
    }

    // 查询指定时间范围内的文件路径
    public List<String> findFilePathsByTimeRange(OffsetDateTime startTime, OffsetDateTime endTime) throws SQLException {
        // 构造带参数占位符的 SQL（使用 BETWEEN 包含两端时间）
        String sql = "SELECT file_local_path FROM file_record " +
                "WHERE file_status>0 and file_last_update BETWEEN ? AND ? ";

        pStmt = conn.prepareStatement(sql);
        // 设置参数值（注意时间格式需与数据库匹配）
        pStmt.setObject(1, startTime);  // 开始时间
        pStmt.setObject(2, endTime);    // 结束时间
        ResultSet rs = pStmt.executeQuery();
        List<String> list = new ArrayList<>();

        while (rs.next()) {
            // 获取文件本地路径
            String filePath = rs.getString("file_local_path");
            list.add(filePath);
        }

        return list;
    }

    /**
     * 批量更新文件状态
     *
     * @param updates 文件更新信息列表
     * @throws SQLException 如果发生数据库错误
     */
    public void batchUpdateFileStatus(List<FileRecordDO> updates) throws SQLException {
        String sql = "UPDATE file_record SET file_status = ? WHERE file_md5 = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            // 开启事务（如果尚未自动提交）
            boolean autoCommit = conn.getAutoCommit();
            if (autoCommit) {
                conn.setAutoCommit(false);
            }

            for (FileRecordDO update : updates) {
                ps.setInt(1, update.getFileStatus());  // 设置 file_status
                ps.setString(2, update.getFileMd5());  // 设置 file_md5

                ps.addBatch();  // 添加到批处理
            }

            ps.executeBatch();  // 执行批量更新
            conn.commit();      // 提交事务

        } catch (SQLException e) {
            conn.rollback();    // 出错回滚
            throw e;
        }
    }


}
