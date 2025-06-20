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
public class MarketCategoryDao implements DaoService<MarketCategoryDO> {
    private static final int QueryTimeout = 30;
    private final ErrorMsg errorMsg;
    private final Connection conn;
    private Statement stmt = null;
    private PreparedStatement pStmt = null;
    private final String tableName = "market_category";

    public MarketCategoryDao(Connection connection) throws SQLException {
        errorMsg = new ErrorMsg();
        this.conn = connection;
        stmt = conn.createStatement();
        stmt.setQueryTimeout(QueryTimeout);  // set timeout to 30 sec;
    }

    public ErrorMsg getErrorMsg() {
        return errorMsg;
    }

    @Override
    public void doInsert(MarketCategoryDO vo) throws SQLException {
        String sql = "insert into market_category(cate_pay_amt_ratio,cate_per_pay_combo_cnt,cate_pay_cnt,cate_pay_amt,cate_pay_product_cnt,cate_online_product_cnt,cate_per_pay_ucnt,channel,price_band,category_id,record_time,cate_pay_combo_cnt,cate_pay_ucnt)values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        pStmt = conn.prepareStatement(sql);
        pStmt.setString(1, vo.getCatePayAmtRatio());
        pStmt.setString(2, vo.getCatePerPayComboCnt());
        pStmt.setString(3, vo.getCatePayCnt());
        pStmt.setString(4, vo.getCatePayAmt());
        pStmt.setString(5, vo.getCatePayProductCnt());
        pStmt.setString(6, vo.getCateOnlineProductCnt());
        pStmt.setString(7, vo.getCatePerPayUcnt());
        pStmt.setString(8, vo.getChannel());
        pStmt.setString(9, vo.getPriceBand());
        pStmt.setInt(10, vo.getId());
        pStmt.setString(11, vo.getRecordTime());
        pStmt.setString(12, vo.getCatePayComboCnt());
        pStmt.setString(13, vo.getCatePayUcnt());
        if (pStmt.execute())
            errorMsg.setCode(0);
        else {
            errorMsg.setCode(Config.FAIL_INSERT);
            errorMsg.setMsg(pStmt.toString());
        }
    }

    public void doUpdate(MarketCategoryDO vo) throws SQLException {
        String sql = "update  market_category SET  cate_pay_amt_ratio=? , cate_per_pay_combo_cnt=? , cate_pay_cnt=? , cate_pay_amt=? , cate_pay_product_cnt=? , cate_online_product_cnt=? , cate_per_pay_ucnt=? , channel=? , price_band=? , id=? , record_time=? , cate_pay_combo_cnt=?WHERE cate_pay_amt_ratio=?";
        pStmt = conn.prepareStatement(sql);
        pStmt.setString(1, vo.getCatePayAmtRatio());
        pStmt.setString(2, vo.getCatePerPayComboCnt());
        pStmt.setString(3, vo.getCatePayCnt());
        pStmt.setString(4, vo.getCatePayAmt());
        pStmt.setString(5, vo.getCatePayProductCnt());
        pStmt.setString(6, vo.getCateOnlineProductCnt());
        pStmt.setString(7, vo.getCatePerPayUcnt());
        pStmt.setString(8, vo.getChannel());
        pStmt.setString(9, vo.getPriceBand());
        pStmt.setInt(10, vo.getId());

        pStmt.setString(11, vo.getRecordTime());
        pStmt.setString(12, vo.getCatePayComboCnt());
        pStmt.setString(13, vo.getCatePayAmtRatio());
        if (pStmt.execute())
            errorMsg.setCode(0);
        else {
            errorMsg.setCode(Config.FAIL_INSERT);
            errorMsg.setMsg(pStmt.toString());
        }
    }

    @Override
    public void doDelete(MarketCategoryDO vo) throws SQLException {
        String sql = String.format("DELETE FROM  market_category WHERE cate_pay_amt_ratio=?", vo.getCatePayAmtRatio());
        pStmt = conn.prepareStatement(sql);
        if (pStmt.execute())
            errorMsg.setCode(0);
        else {
            errorMsg.setCode(Config.FAIL_INSERT);
            errorMsg.setMsg(pStmt.toString());
        }
    }

    @Override
    public void doBatch(List<MarketCategoryDO> t) throws SQLException {

    }
}
