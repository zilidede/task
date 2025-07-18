package com.zl.dao.generate;
import com.zl.config.Config;
import com.zl.dao.DaoService;
import com.zl.dao.ErrorMsg;
import java.sql.*;
import java.util.List;

import com.zl.dao.generate.SugKeywordDO;
/**
 * @Description:
 * @Param:
 * @Auther: zl
 * @Date: 2025-06-25
 */
public class SugKeywordDao implements DaoService<SugKeywordDO>{
    private static int QueryTimeout = 30;
    private ErrorMsg errorMsg;
    private Connection conn;
    private Statement stmt = null;
    private PreparedStatement pStmt = null;
    private String tableName = "sug_keyword";
    public SugKeywordDao(Connection connection) throws SQLException {
        errorMsg=new ErrorMsg();
        this.conn = connection;
        stmt = conn.createStatement();
        stmt.setQueryTimeout(QueryTimeout);  // set timeout to 30 sec;
    }    public ErrorMsg getErrorMsg() {
        return errorMsg;
    }
@Override
    public void doInsert(SugKeywordDO vo) throws SQLException {
        String sql = "insert into sug_keyword(root_keyword,relation_keyword,relation_rank,record_time,data_source)values (?,?,?,?,?)";
        pStmt = conn.prepareStatement(sql);
        pStmt.setString(1, vo.getRootKeyword());
        pStmt.setString(2, vo.getRelationKeyword());
        pStmt.setInt(3, vo.getRelationRank());
Timestamp ts = new Timestamp(vo.getRecordTime().getTime());
        pStmt.setTimestamp(4, ts);
        pStmt.setString(5, vo.getDataSource());
        if(pStmt.execute())
            errorMsg.setCode(0);
        else{
            errorMsg.setCode(Config.FAIL_INSERT);
            errorMsg.setMsg(pStmt.toString());
        }
    }
    public void doUpdate(SugKeywordDO vo) throws SQLException {
        String sql = "update  sug_keyword SET  root_keyword=? , relation_keyword=? , relation_rank=? , record_time=? , data_source=?WHERE root_keyword=?";
        pStmt = conn.prepareStatement(sql);
        pStmt.setString(1, vo.getRootKeyword());
        pStmt.setString(2, vo.getRelationKeyword());
        pStmt.setInt(3, vo.getRelationRank());
Timestamp ts = new Timestamp(vo.getRecordTime().getTime());
        pStmt.setTimestamp(4, ts);
        pStmt.setString(5, vo.getDataSource());
        pStmt.setString(6, vo.getRootKeyword());
        if(pStmt.execute())
            errorMsg.setCode(0);
        else{
            errorMsg.setCode(Config.FAIL_INSERT);
            errorMsg.setMsg(pStmt.toString());
        }
    }@Override
    public void doDelete(SugKeywordDO vo) throws SQLException {
        String sql =String.format("DELETE FROM  sug_keyword WHERE root_keyword=?",vo.getRootKeyword());
        pStmt = conn.prepareStatement(sql);
        if(pStmt.execute())
            errorMsg.setCode(0);
        else{
            errorMsg.setCode(Config.FAIL_INSERT);
            errorMsg.setMsg(pStmt.toString());
        }
    }

    @Override
    public void doBatch(List<SugKeywordDO> t) throws SQLException {

    }
}
