package com.zl.dao.generate;
import com.zl.config.Config;
import com.zl.dao.DaoService;
import com.zl.dao.ErrorMsg;
import java.util.List;
import com.zl.utils.jdbc.hikariCP.ConnectionPool;
import java.sql.*;import com.zl.dao.generate.TrendinsightKeywordsDO;
/**
 * @Description:
 * @Param:
 * @Auther: zl
 * @Date: 2025-07-18
 */
public class TrendinsightKeywordsDao implements DaoService<TrendinsightKeywordsDO>{
    private ErrorMsg errorMsg;

    public TrendinsightKeywordsDao() throws SQLException {
        errorMsg=new ErrorMsg();
    }
    public ErrorMsg getErrorMsg() {
        return errorMsg;
    }
@Override
    public void doInsert(TrendinsightKeywordsDO vo) throws SQLException {
        String sql = "insert into trendinsight_keywords(keyword_search_point_list,keyword_content_list,keyword_search_index,keyword,record_time,keyword_content_index,keyword_influ_scope)values (?,?,?,?,?,?,?)";
        try (Connection conn = ConnectionPool.getConnection(); PreparedStatement pStmt = conn.prepareStatement(sql)) {
        pStmt.setString(1, vo.getKeywordSearchPointList());
        pStmt.setString(2, vo.getKeywordContentList());
        pStmt.setInt(3, vo.getKeywordSearchIndex());
        pStmt.setString(4, vo.getKeyword());
Timestamp ts = new Timestamp(vo.getRecordTime().getTime());
        pStmt.setTimestamp(5, ts);
        pStmt.setInt(6, vo.getKeywordContentIndex());
        pStmt.setString(7, vo.getKeywordInfluScope());
        if(pStmt.execute())
            errorMsg.setCode(0);
        else{
            errorMsg.setCode(Config.FAIL_INSERT);
            errorMsg.setMsg(pStmt.toString());
        }
 } catch (SQLException e) {
            // 异常处理
        }    }@Override
     public void doUpdate(TrendinsightKeywordsDO vo) throws SQLException {
    }@Override
    public void doDelete(TrendinsightKeywordsDO vo) throws SQLException {
        }
@Override
    public void doBatch(List<TrendinsightKeywordsDO> vo) throws SQLException {
        }
   
}
