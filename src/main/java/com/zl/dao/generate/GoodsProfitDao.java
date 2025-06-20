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
 * @Date: 2024-03-14
 */
public class GoodsProfitDao implements DaoService<GoodsProfitDO> {
    private static final int QueryTimeout = 30;
    private final ErrorMsg errorMsg;
    private final Connection conn;
    private Statement stmt = null;
    private PreparedStatement pStmt = null;
    private final String tableName = "goods_profit";

    public GoodsProfitDao(Connection connection) throws SQLException {
        errorMsg = new ErrorMsg();
        this.conn = connection;
        stmt = conn.createStatement();
        stmt.setQueryTimeout(QueryTimeout);  // set timeout to 30 sec;
    }

    public ErrorMsg getErrorMsg() {
        return errorMsg;
    }

    @Override
    public void doInsert(GoodsProfitDO vo) throws SQLException {
        String sql = "insert into goods_profit(acclaim_cost,fee_roi,other_trade_points,attrition_costs,gifts_cost,transport_insurance_cost,goods_id,goods_name,platform_trade_points,whole_sale_cost,express_cost,brand_trade_points,anchor_trade_points,broadcaster_trade_points,after_sale_rate,other_cost,record_time,start_sale_time,end_sale_time)values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        pStmt = conn.prepareStatement(sql);
        pStmt.setDouble(1, vo.getAcclaimCost());
        pStmt.setDouble(2, vo.getFeeRoi());
        pStmt.setDouble(3, vo.getOtherTradePoints());
        pStmt.setDouble(4, vo.getAttritionCosts());
        pStmt.setDouble(5, vo.getGiftsCost());
        pStmt.setDouble(6, vo.getTransportInsuranceCost());
        pStmt.setLong(7, vo.getGoodsId());
        pStmt.setString(8, vo.getGoodsName());
        pStmt.setDouble(9, vo.getPlatformTradePoints());
        pStmt.setDouble(10, vo.getWholeSale());
        pStmt.setDouble(11, vo.getExpressCost());
        pStmt.setDouble(12, vo.getBrandTradePoints());
        pStmt.setDouble(13, vo.getAnchorTradePoints());
        pStmt.setDouble(14, vo.getBroadcasterTradePoints());
        pStmt.setDouble(15, vo.getAfterSaleRate());
        pStmt.setDouble(16, vo.getOtherCost());
        Timestamp ts = new Timestamp(vo.getRecordTime().getTime());
        pStmt.setTimestamp(17, ts);
        ts = new Timestamp(vo.getStartSaleTime().getTime());
        pStmt.setTimestamp(18, ts);
        ts = new Timestamp(vo.getEndSaleTime().getTime());
        pStmt.setTimestamp(19, ts);
        if (pStmt.execute())
            errorMsg.setCode(0);
        else {
            errorMsg.setCode(Config.FAIL_INSERT);
            errorMsg.setMsg(pStmt.toString());
        }
    }

    public void doUpdate(GoodsProfitDO vo) throws SQLException {
        String sql = "update  goods_profit SET  acclaim_cost=? , fee_roi=? , other_trade_points=? , attrition_costs=? , gifts_cost=? , transport_insurance_cost=? , goods_id=? , goods_main_img=? , platform_trade_points=? , whole_sale=? , express_cost=? , brand_trade_points=? , anchor_trade_points=? , broadcaster_trade_points=? , after_sale_rate=?WHERE end_sale_time=?";
        pStmt = conn.prepareStatement(sql);
        pStmt.setDouble(1, vo.getAcclaimCost());
        pStmt.setDouble(2, vo.getFeeRoi());
        pStmt.setDouble(3, vo.getOtherTradePoints());
        pStmt.setDouble(4, vo.getAttritionCosts());
        pStmt.setDouble(5, vo.getGiftsCost());
        pStmt.setDouble(6, vo.getTransportInsuranceCost());
        pStmt.setLong(7, vo.getGoodsId());
        pStmt.setString(8, vo.getGoodsMainImg());
        pStmt.setDouble(9, vo.getPlatformTradePoints());
        pStmt.setDouble(10, vo.getWholeSale());
        pStmt.setDouble(11, vo.getExpressCost());
        pStmt.setDouble(12, vo.getBrandTradePoints());
        pStmt.setDouble(13, vo.getAnchorTradePoints());
        pStmt.setDouble(14, vo.getBroadcasterTradePoints());
        pStmt.setDouble(15, vo.getAfterSaleRate());
        if (pStmt.execute())
            errorMsg.setCode(0);
        else {
            errorMsg.setCode(Config.FAIL_INSERT);
            errorMsg.setMsg(pStmt.toString());
        }
    }

    @Override
    public void doDelete(GoodsProfitDO vo) throws SQLException {
        String sql = String.format("DELETE FROM  goods_profit WHERE end_sale_time=?", vo.getEndSaleTime());
        pStmt = conn.prepareStatement(sql);
        if (pStmt.execute())
            errorMsg.setCode(0);
        else {
            errorMsg.setCode(Config.FAIL_INSERT);
            errorMsg.setMsg(pStmt.toString());
        }
    }

    @Override
    public void doBatch(List<GoodsProfitDO> t) throws SQLException {

    }
}
