package com.zl.dao.generate;

import com.zl.dao.DaoService;
import com.zl.dao.ErrorMsg;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Param:
 * @Auther: zl
 * @Date: 2024-07-23
 */
public class LocalTaskDao implements DaoService<LocalTaskDO> {
    private static final int QueryTimeout = 30;
    private final ErrorMsg errorMsg;
    private final Connection conn;
    private Statement stmt = null;
    private PreparedStatement pStmt = null;
    private final String tableName = "local_task";

    public LocalTaskDao(Connection connection) throws SQLException {
        errorMsg = new ErrorMsg();
        this.conn = connection;
        stmt = conn.createStatement();
        stmt.setQueryTimeout(QueryTimeout);  // set timeout to 30 sec;
    }

    public ErrorMsg getErrorMsg() {
        return errorMsg;
    }

    @Override
    public void doInsert(LocalTaskDO vo) throws SQLException {
        String sql = "insert into task(start_time,name,id,content,status)values (?,?,?,?,?)";
        pStmt = conn.prepareStatement(sql);
        Timestamp ts = new Timestamp(vo.getStartTime().getTime());
        pStmt.setTimestamp(1, ts);
        pStmt.setString(2, vo.getName());
        pStmt.setString(3, vo.getId());
        pStmt.setString(4, vo.getContent());
        pStmt.setInt(5, vo.getStatus());
        if (pStmt.execute())
            errorMsg.setCode(0);
        else {
            // errorMsg.setCode(Config.FAIL_INSERT);
            errorMsg.setMsg(pStmt.toString());
        }
    }

    public void doUpdate(LocalTaskDO vo) throws SQLException {
        String sql = "update  task SET   name=?  , content=? , status=? , end_time=? , exe_time=?WHERE id=?";
        pStmt = conn.prepareStatement(sql);
        pStmt.setString(1, vo.getName());
        pStmt.setString(2, vo.getContent());
        pStmt.setInt(3, vo.getStatus());
        Timestamp ts = new Timestamp(vo.getEndTime().getTime());
        pStmt.setTimestamp(4, ts);
        ts = new Timestamp(vo.getExecuteTime().getTime());
        pStmt.setTimestamp(5, ts);
        pStmt.setString(6, vo.getId());
        if (pStmt.execute())
            errorMsg.setCode(0);
        else {
            //errorMsg.setCode(Config.FAIL_INSERT);
            errorMsg.setMsg(pStmt.toString());
        }
    }

    @Override
    public void doDelete(LocalTaskDO vo) throws SQLException {
        String sql = String.format("DELETE FROM  task WHERE end_time=?", vo.getEndTime());
        pStmt = conn.prepareStatement(sql);
        if (pStmt.execute())
            errorMsg.setCode(0);
        else {
            //errorMsg.setCode(Config.FAIL_INSERT);
            errorMsg.setMsg(pStmt.toString());
        }
    }

    @Override
    public void doBatch(List<LocalTaskDO> t) throws SQLException {

    }

    //find
    public List<LocalTaskDO> findUnExeTask() throws SQLException {
        List<LocalTaskDO> result = new ArrayList<>();
        String sql = "select * from task where  status =0";
        pStmt = conn.prepareStatement(sql);
        ResultSet set = pStmt.executeQuery();
        while (set.next()) {
            LocalTaskDO vo = new LocalTaskDO();
            vo.setName(set.getString("name"));
            vo.setContent(set.getString("content"));
            vo.setId(set.getString("id"));
            result.add(vo);

        }
        return result;
    }
}
