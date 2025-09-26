package com.zl.dao.generate;
import com.zl.config.Config;
import com.zl.dao.DaoService;
import com.zl.dao.ErrorMsg;
import java.sql.*;
import java.util.List;

import com.util.jdbc.hikariCP.ConnectionPool;

/**
 * @Description:
 * @Param:
 * @Auther: zl
 * @Date: 2025-06-24
 */
public class RelationKeywordDao implements DaoService<RelationKeywordDO>{
    private static int QueryTimeout = 30;
    private ErrorMsg errorMsg;

    private String tableName = "relation_keyword";
    public RelationKeywordDao() throws SQLException {
        errorMsg=new ErrorMsg();
        // set timeout to 30 sec;
    }    public ErrorMsg getErrorMsg() {
        return errorMsg;
    }
@Override
    public void doInsert(RelationKeywordDO vo) throws SQLException {
        String sql = "insert into relation_keyword(end_date,relation_word_id,root_keyword,relation_word,data_source,start_date)values (?,?,?,?,?,?)";
    try (Connection conn = ConnectionPool.getConnection(); PreparedStatement pStmt = conn.prepareStatement(sql)) {
        Timestamp ts = new Timestamp(vo.getEndDate().getTime());
        pStmt.setTimestamp(1, ts);
        pStmt.setString(2, vo.getRelationWordId());
        pStmt.setString(3, vo.getRootKeyword());
        pStmt.setString(4, vo.getRelationWord());
        pStmt.setString(5, vo.getDataSource());
        ts = new Timestamp(vo.getStartDate().getTime());
        pStmt.setTimestamp(6, ts);
        if(pStmt.execute())
            errorMsg.setCode(0);
        else{
            errorMsg.setCode(Config.FAIL_INSERT);
            errorMsg.setMsg(pStmt.toString());
        }
    } catch (SQLException e) {
        // 异常处理
    }

    }
    public void doUpdate(RelationKeywordDO vo) throws SQLException {
        String sql = "update  relation_keyword SET  end_date=? , relation_word_id=? , root_keyword=? , relation_word=? , data_source=? , start_date=?WHERE relation_word_id=?";
        try (Connection conn = ConnectionPool.getConnection(); PreparedStatement pStmt = conn.prepareStatement(sql)) {
            Timestamp ts = new Timestamp(vo.getEndDate().getTime());
            pStmt.setTimestamp(1, ts);
            pStmt.setString(2, vo.getRelationWordId());
            pStmt.setString(3, vo.getRootKeyword());
            pStmt.setString(4, vo.getRelationWord());
            pStmt.setString(5, vo.getDataSource());
            ts = new Timestamp(vo.getStartDate().getTime());
            pStmt.setTimestamp(6, ts);
            pStmt.setString(7, vo.getRelationWordId());
            if(pStmt.execute())
                errorMsg.setCode(0);
            else{
                errorMsg.setCode(Config.FAIL_INSERT);
                errorMsg.setMsg(pStmt.toString());
            }
        } catch (SQLException e) {
            // 异常处理
        }

    }@Override
    public void doDelete(RelationKeywordDO vo) throws SQLException {
        String sql =String.format("DELETE FROM  relation_keyword WHERE relation_word_id=?",vo.getRelationWordId());
        try (Connection conn = ConnectionPool.getConnection(); PreparedStatement pStmt = conn.prepareStatement(sql)) {
            if(pStmt.execute())
                errorMsg.setCode(0);
            else{
                errorMsg.setCode(Config.FAIL_INSERT);
                errorMsg.setMsg(pStmt.toString());
            }
        } catch (SQLException e) {
            // 异常处理
        }

    }

    @Override
    public void doBatch(List<RelationKeywordDO> t) throws SQLException {

    }
}
