package com.zl.dao.generate;


import com.zl.config.Config;
import com.zl.dao.DaoService;
import com.zl.dao.ErrorMsg;
import com.zl.utils.jdbc.hikariCP.ConnectionPool;

import java.sql.*;
import java.util.List;

/**
 * @Description:
 * @Param:
 * @Auther: zl
 * @Date: 2023-12-14
 */
public class OceanengineSearchKeywordsDao implements DaoService<OceanengineSearchKeywordsDO> {
   ;
    private final ErrorMsg errorMsg;



    private final String tableName = "oceanengine_search_keywords";

    public OceanengineSearchKeywordsDao() throws SQLException {
        errorMsg = new ErrorMsg();
    }

    public ErrorMsg getErrorMsg() {
        return errorMsg;
    }

    @Override
    public void doInsert(OceanengineSearchKeywordsDO vo) throws SQLException {
        String sql = "insert into oceanengine_search_keywords" +
                " (record_end_time,order_count,record_start_time,click_rate,trader_rate,key_word_detail_path,running_count,keyword,search_count,order_sale_count)values (?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = ConnectionPool.getConnection(); PreparedStatement pStmt = conn.prepareStatement(sql)) {
            Timestamp ts = new Timestamp(vo.getRecordEndTime().getTime());
            pStmt.setTimestamp(1, ts);
            pStmt.setInt(2, vo.getOrderCount());
            ts = new Timestamp(vo.getRecordStartTime().getTime());
            pStmt.setTimestamp(3, ts);
            pStmt.setDouble(4, vo.getClickRate());
            pStmt.setDouble(5, vo.getTraderRate());
            pStmt.setString(6, vo.getKeyWordDetailPath());
            pStmt.setInt(7, vo.getRunningCount());
            pStmt.setString(8, vo.getKeyword());
            pStmt.setInt(9, vo.getSearchCount());
            pStmt.setInt(10, vo.getOrderSaleCount());
            if (pStmt.execute())
                errorMsg.setCode(0);
            else {
                errorMsg.setCode(Config.FAIL_INSERT);
                errorMsg.setMsg(pStmt.toString());
            }
        } catch (SQLException e) {
            // 异常处理
        }

    }

    public void doUpdate(OceanengineSearchKeywordsDO vo) throws SQLException {
        String sql = "update  oceanengine_search_keywords SET  record_end_time=? , order_count=? , record_start_time=? , click_rate=? , trader_rate=? , key_word_detail_path=? , running_count=? , keyword=? , search_count=? , order_sale_count=?WHERE record_end_time=?";
        try (Connection conn = ConnectionPool.getConnection(); PreparedStatement pStmt = conn.prepareStatement(sql)) {
            Timestamp ts = new Timestamp(vo.getRecordEndTime().getTime());
            pStmt.setTimestamp(1, ts);
            pStmt.setInt(2, vo.getOrderCount());
            ts = new Timestamp(vo.getRecordStartTime().getTime());
            pStmt.setTimestamp(3, ts);
            pStmt.setDouble(4, vo.getClickRate());
            pStmt.setDouble(5, vo.getTraderRate());
            pStmt.setString(6, vo.getKeyWordDetailPath());
            pStmt.setInt(7, vo.getRunningCount());
            pStmt.setString(8, vo.getKeyword());
            pStmt.setInt(9, vo.getSearchCount());
            pStmt.setInt(10, vo.getOrderSaleCount());
            ts = new Timestamp(vo.getRecordEndTime().getTime());
            pStmt.setTimestamp(11, ts);
            if (pStmt.execute())
                errorMsg.setCode(0);
            else {
                errorMsg.setCode(Config.FAIL_INSERT);
                errorMsg.setMsg(pStmt.toString());
            }
        } catch (SQLException e) {
            // 异常处理
        }

    }

    @Override
    public void doDelete(OceanengineSearchKeywordsDO vo) throws SQLException {
        String sql = String.format("DELETE FROM  douyin_oceanengine_search_keywords WHERE record_end_time=?", vo.getRecordEndTime());
        try (Connection conn = ConnectionPool.getConnection(); PreparedStatement pStmt = conn.prepareStatement(sql)) {
            if (pStmt.execute())
                errorMsg.setCode(0);
            else {
                errorMsg.setCode(Config.FAIL_INSERT);
                errorMsg.setMsg(pStmt.toString());
            }
        } catch (SQLException e) {
            // 异常处理
        }

    }

    @Override
    public void doBatch(List<OceanengineSearchKeywordsDO> t) throws SQLException {

    }

    // find
    public int findOrderCount(OceanengineSearchKeywordsDO vo) throws SQLException {
        //查找最近七天词根相关搜索词的总订单数
        String key = "%" + vo.getKeyword() + "%";
        String sql = "select sum(order_count) from oceanengine_search_keywords where  keyword like ? and  record_start_time = ?::date";
        try (Connection conn = ConnectionPool.getConnection(); PreparedStatement pStmt = conn.prepareStatement(sql)) {
            pStmt.setString(1, key);
            pStmt.setString(2, "2024-12-26");
            // 执行查询
            ResultSet rs = pStmt.executeQuery();
            // 处理结果集
            if (rs.next()) {
                // 获取结果
                int orderCount = rs.getInt("sum");
                return orderCount;
            }
        } catch (SQLException e) {
            // 异常处理
        }
        return -1;
    }
}
