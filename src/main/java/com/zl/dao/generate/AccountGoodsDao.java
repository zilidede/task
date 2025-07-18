package com.zl.dao.generate;

import com.zl.config.Config;
import com.zl.dao.DaoService;
import com.zl.dao.ErrorMsg;

import java.sql.*;
import java.util.List;

/**
 * @Description:
 * @Param:
 * @Auther: zl
 * @Date: 2025-03-01
 */
public class AccountGoodsDao implements DaoService<AccountGoodsDO> {
    private static final int QueryTimeout = 30;
    private final ErrorMsg errorMsg;
    private final Connection conn;
    private Statement stmt = null;
    private PreparedStatement pStmt = null;
    private final String tableName = "account_goods";

    public AccountGoodsDao(Connection connection) throws SQLException {
        errorMsg = new ErrorMsg();
        this.conn = connection;
        stmt = conn.createStatement();
        stmt.setQueryTimeout(QueryTimeout);  // set timeout to 30 sec;
    }

    public ErrorMsg getErrorMsg() {
        return errorMsg;
    }

    @Override
    public void doInsert(AccountGoodsDO vo) throws SQLException {
        String sql = "insert into account_goods(account_type,account_id,category_id,live_record_path,live_features,goods_id,dou_yin_id,price_domain,record_time,goods_features,fans)values (?,?,?,?,?,?,?,?,?,?,?)";
        pStmt = conn.prepareStatement(sql);
        pStmt.setString(1, vo.getAccountType());
        pStmt.setInt(2, vo.getAccountId());
        pStmt.setInt(3, vo.getCategoryId());
        pStmt.setString(4, vo.getLiveRecordPath());
        pStmt.setString(5, vo.getLiveFeatures());
        pStmt.setInt(6, vo.getGoodsId());
        pStmt.setInt(7, vo.getDouYinId());
        pStmt.setString(8, vo.getPriceDomain());
        Timestamp ts = new Timestamp(vo.getRecordTime().getTime());
        pStmt.setTimestamp(9, ts);
        pStmt.setString(10, vo.getGoodsFeatures());
        pStmt.setInt(11, vo.getFans());
        if (pStmt.execute())
            errorMsg.setCode(0);
        else {
            errorMsg.setCode(Config.FAIL_INSERT);
            errorMsg.setMsg(pStmt.toString());
        }
    }

    public void doUpdate(AccountGoodsDO vo) throws SQLException {
        String sql = "update  account_goods SET  account_type=? , account_id=? , category_id=? , live_record_path=? , live_features=? , goods_id=? , dou_yin_id=? , price_domain=? , record_time=? , goods_features=? , fans=?WHERE account_type=?";
        pStmt = conn.prepareStatement(sql);
        pStmt.setString(1, vo.getAccountType());
        pStmt.setInt(2, vo.getAccountId());
        pStmt.setInt(3, vo.getCategoryId());
        pStmt.setString(4, vo.getLiveRecordPath());
        pStmt.setString(5, vo.getLiveFeatures());
        pStmt.setInt(6, vo.getGoodsId());
        pStmt.setInt(7, vo.getDouYinId());
        pStmt.setString(8, vo.getPriceDomain());
        Timestamp ts = new Timestamp(vo.getRecordTime().getTime());
        pStmt.setTimestamp(9, ts);
        pStmt.setString(10, vo.getGoodsFeatures());
        pStmt.setInt(11, vo.getFans());
        pStmt.setString(12, vo.getAccountType());
        if (pStmt.execute())
            errorMsg.setCode(0);
        else {
            errorMsg.setCode(Config.FAIL_INSERT);
            errorMsg.setMsg(pStmt.toString());
        }
    }

    @Override
    public void doDelete(AccountGoodsDO vo) throws SQLException {
        String sql = String.format("DELETE FROM  account_goods WHERE account_type=?", vo.getAccountType());
        pStmt = conn.prepareStatement(sql);
        if (pStmt.execute())
            errorMsg.setCode(0);
        else {
            errorMsg.setCode(Config.FAIL_INSERT);
            errorMsg.setMsg(pStmt.toString());
        }
    }

    @Override
    public void doBatch(List<AccountGoodsDO> t) throws SQLException {

    }
}
