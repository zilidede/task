package com.zl.dao.generate;

import com.zl.dao.DaoService;
import com.zl.dao.ErrorMsg;
import com.util.jdbc.hikariCP.ConnectionPool;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Param:
 * @Auther: zl
 * @Date: 2024-07-12
 */
public class OceanenSearchKeywordsDetailDao implements DaoService<OceanenSearchKeywordsDetailDO> {
    private static final int QueryTimeout = 30;
    private final ErrorMsg errorMsg;
    private Statement stmt = null;

    private final String tableName = "oceanen_search_keywords_detail";
    private final SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");

    public OceanenSearchKeywordsDetailDao() throws SQLException {
        errorMsg = new ErrorMsg();

  }

    public ErrorMsg getErrorMsg() {
        return errorMsg;
    }

    public List<String> getKeywords() throws SQLException {
        //查找任务队列中list的完成的每日任务；
        List<String> result = new ArrayList<>();
        String sql = "select DISTINCT keyword from oceanen_search_keywords_detail";
        try (Connection conn = ConnectionPool.getConnection(); PreparedStatement pStmt = conn.prepareStatement(sql)) {
             ResultSet set = pStmt.executeQuery();
            while (set.next()) {
                result.add(set.getString("keyword"));
            }

        }
        catch (SQLException e){
            errorMsg.setMsg(e.getMessage());
            errorMsg.setCode(-1);
        }

        return result;
    }

    @Override
    public void doInsert(OceanenSearchKeywordsDetailDO vo) throws SQLException {
        String sql = "insert into search_keywords_detail(keyword_count,keyword,record_time,platform_source)values (?,?,?,?)";
        // 获取连接
        try (Connection conn = ConnectionPool.getConnection(); PreparedStatement pStmt = conn.prepareStatement(sql)) {
            pStmt.setInt(1, vo.getKeywordCount());
            pStmt.setString(2, vo.getKeyword());
            Timestamp ts = new Timestamp(vo.getRecordTime().getTime());
            pStmt.setTimestamp(3, ts);
            pStmt.setString(4, vo.getPlatformSource());
            if (pStmt.execute())
                errorMsg.setCode(0);
            else {
                errorMsg.setMsg(pStmt.toString());
            }
        } catch (SQLException e) {
            // 异常处理
        }





    }

    public void doUpdate(OceanenSearchKeywordsDetailDO vo) throws SQLException {
        String sql = "update  oceanen_search_keywords_detail SET  keyword_count=? , keyword=? , record_time=?WHERE keyword_path=?";
        try (Connection conn = ConnectionPool.getConnection(); PreparedStatement pStmt = conn.prepareStatement(sql)) {
            pStmt.setInt(1, vo.getKeywordCount());
            pStmt.setString(2, vo.getKeyword());
            Timestamp ts = new Timestamp(vo.getRecordTime().getTime());
            pStmt.setTimestamp(3, ts);
            pStmt.setInt(4, vo.getKeywordCount());
            if (pStmt.execute())
                errorMsg.setCode(0);
            else {

                errorMsg.setMsg(pStmt.toString());
            }
        } catch (SQLException e) {
            // 异常处理
        }



    }

    @Override
    public void doDelete(OceanenSearchKeywordsDetailDO vo) throws SQLException {
        String sql = String.format("DELETE FROM  oceanen_search_keywords_detail WHERE keyword_count=?", vo.getKeywordCount());
        try (Connection conn = ConnectionPool.getConnection(); PreparedStatement pStmt = conn.prepareStatement(sql)) {
            if (pStmt.execute())
                errorMsg.setCode(0);
            else {

                errorMsg.setMsg(pStmt.toString());
            }
        } catch (SQLException e) {
            // 异常处理
        }

    }

    @Override
    public void doBatch(List<OceanenSearchKeywordsDetailDO> t) throws SQLException {

    }

    public Map<String, Integer> findOcSearchKeywordsDetail(Map<String, Integer> map) throws SQLException {
        map.clear();
        // 从巨量云图搜索词详情寻找
        String sql = "select distinct keyword from search_keywords_detail";
        try (Connection conn = ConnectionPool.getConnection(); PreparedStatement pStmt = conn.prepareStatement(sql)) {
            ResultSet set = pStmt.executeQuery();
            while (set.next()) {
                String s = set.getString("keyword");
                map.put(s, 0);
            }
        } catch (SQLException e) {
            // 异常处理
        }
        return map;
    }

    public int findLikeKeyWordCount(String keyword, String date) throws SQLException {
        //
        String key = "%" + keyword + "%";
        String sql = "SELECT sum(keyword_count) FROM search_keywords_detail WHERE keyword LIKE ? AND record_time = ?::date";
        try (Connection conn = ConnectionPool.getConnection(); PreparedStatement pStmt = conn.prepareStatement(sql)) {
            pStmt.setString(1, key);
            pStmt.setString(2, date);
            // 执行查询
            ResultSet rs = pStmt.executeQuery();
            // 处理结果集
            if (rs.next()) {
                // 获取结果
                int keywordCount = rs.getInt("sum");
                return keywordCount;
            }
        } catch (SQLException e) {
            // 异常处理
        }
        return -1;
    }

}
