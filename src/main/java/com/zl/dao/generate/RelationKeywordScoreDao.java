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
public class RelationKeywordScoreDao implements DaoService<RelationKeywordScoreDO>{

    private ErrorMsg errorMsg;

    public RelationKeywordScoreDao() throws SQLException {
        errorMsg=new ErrorMsg();

    }    public ErrorMsg getErrorMsg() {
        return errorMsg;
    }
@Override
    public void doInsert(RelationKeywordScoreDO vo) throws SQLException {
        String sql = "insert into relation_keyword_score(content_ids,relation_word_id,search_index,relation_score,score_rate,score_rate_rank,compos_index,score_rank)values (?,?,?,?,?,?,?,?)";
    try (Connection conn = ConnectionPool.getConnection(); PreparedStatement pStmt = conn.prepareStatement(sql)) {
        pStmt.setString(1, vo.getContentIds());
        pStmt.setString(2, vo.getRelationWordId());
        pStmt.setInt(3, vo.getSearchIndex());
        pStmt.setDouble(4, vo.getRelationScore());
        pStmt.setDouble(5, vo.getScoreRate());
        pStmt.setInt(6, vo.getScoreRateRank());
        pStmt.setInt(7, vo.getComposIndex());
        pStmt.setInt(8, vo.getScoreRank());
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
    public void doUpdate(RelationKeywordScoreDO vo) throws SQLException {
        String sql = "update  relation_keyword_score SET  content_ids=? , relation_word_id=? , search_index=? , relation_score=? , score_rate=? , score_rate_rank=? , compos_index=? , score_rank=?WHERE relation_word_id=?";
        try (Connection conn = ConnectionPool.getConnection(); PreparedStatement pStmt = conn.prepareStatement(sql)) {
            pStmt.setString(1, vo.getContentIds());
            pStmt.setString(2, vo.getRelationWordId());
            pStmt.setInt(3, vo.getSearchIndex());
            pStmt.setDouble(4, vo.getRelationScore());
            pStmt.setDouble(5, vo.getScoreRate());
            pStmt.setInt(6, vo.getScoreRateRank());
            pStmt.setInt(7, vo.getComposIndex());
            pStmt.setInt(8, vo.getScoreRank());
            pStmt.setString(9, vo.getRelationWordId());
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
    public void doDelete(RelationKeywordScoreDO vo) throws SQLException {
        String sql =String.format("DELETE FROM  relation_keyword_score WHERE relation_word_id=?",vo.getRelationWordId());
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
    public void doBatch(List<RelationKeywordScoreDO> t) throws SQLException {

    }
}
