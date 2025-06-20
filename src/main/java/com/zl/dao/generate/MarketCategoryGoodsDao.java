package com.zl.dao.generate;

import com.zl.config.Config;
import com.zl.dao.DaoService;
import com.zl.dao.ErrorMsg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @Description:
 * @Param:
 * @Auther: zl
 * @Date: 2024-08-23
 */
public class MarketCategoryGoodsDao implements DaoService<MarketCategoryGoodsDO> {
    private static final int QueryTimeout = 30;
    private final ErrorMsg errorMsg;
    private final Connection conn;
    private Statement stmt = null;
    private PreparedStatement pStmt = null;
    private final String tableName = "market_category_goods";

    public MarketCategoryGoodsDao(Connection connection) throws SQLException {
        errorMsg = new ErrorMsg();
        this.conn = connection;
        stmt = conn.createStatement();
        stmt.setQueryTimeout(QueryTimeout);  // set timeout to 30 sec;
    }

    public ErrorMsg getErrorMsg() {
        return errorMsg;
    }

    @Override
    public void doInsert(MarketCategoryGoodsDO vo) throws SQLException {
        String sql = "insert into market_category_goods(goods_name,goods_id,price_band,category_id,goods_sales,record_time)values (?,?,?,?,?,?)";
        pStmt = conn.prepareStatement(sql);
        pStmt.setString(1, vo.getGoodsName());
        pStmt.setLong(2, vo.getGoodsId());
        pStmt.setString(3, vo.getPriceBand());
        pStmt.setInt(4, vo.getId());
        pStmt.setString(5, vo.getGoodsSales());
        pStmt.setString(6, vo.getRecordTime());
        if (pStmt.execute())
            errorMsg.setCode(0);
        else {
            errorMsg.setCode(Config.FAIL_INSERT);
            errorMsg.setMsg(pStmt.toString());
        }
    }

    public void doUpdate(MarketCategoryGoodsDO vo) throws SQLException {
        String sql = "update  market_category_goods SET  goods_name=? , goods_id=? , price_band=? , category_id=? , goods_sales=? , record_time=?WHERE goods_id=?";
        pStmt = conn.prepareStatement(sql);
        pStmt.setString(1, vo.getGoodsName());
        pStmt.setLong(2, vo.getGoodsId());
        pStmt.setString(3, vo.getPriceBand());
        pStmt.setInt(4, vo.getId());
        pStmt.setString(5, vo.getGoodsSales());

        pStmt.setString(6, vo.getRecordTime());
        pStmt.setLong(7, vo.getGoodsId());
        if (pStmt.execute())
            errorMsg.setCode(0);
        else {
            errorMsg.setCode(Config.FAIL_INSERT);
            errorMsg.setMsg(pStmt.toString());
        }
    }

    @Override
    public void doDelete(MarketCategoryGoodsDO vo) throws SQLException {
        String sql = String.format("DELETE FROM  market_category_goods WHERE goods_id=?", vo.getGoodsId());
        pStmt = conn.prepareStatement(sql);
        if (pStmt.execute())
            errorMsg.setCode(0);
        else {
            errorMsg.setCode(Config.FAIL_INSERT);
            errorMsg.setMsg(pStmt.toString());
        }
    }

    @Override
    public void doBatch(List<MarketCategoryGoodsDO> t) throws SQLException {

    }
}
