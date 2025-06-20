package com.zl.dao.generate;

import com.zl.dao.DaoService;
import com.zl.dao.ErrorMsg;

import java.sql.*;
import java.util.List;

/**
 * @Description:
 * @Param:
 * @Auther: zl
 * @Date: 2024-07-12
 */
public class OceanenSearchKeywordsProductDao implements DaoService<OceanenSearchKeywordsProductDO> {
    private static final int QueryTimeout = 30;
    private final ErrorMsg errorMsg;
    private final Connection conn;
    private Statement stmt = null;
    private PreparedStatement pStmt = null;
    private final String tableName = "oceanen_search_keywords_product";

    public OceanenSearchKeywordsProductDao(Connection connection) throws SQLException {
        errorMsg = new ErrorMsg();
        this.conn = connection;
        stmt = conn.createStatement();
        stmt.setQueryTimeout(QueryTimeout);  // set timeout to 30 sec;
    }

    public ErrorMsg getErrorMsg() {
        return errorMsg;
    }

    @Override
    public void doInsert(OceanenSearchKeywordsProductDO vo) throws SQLException {
        String sql = "insert into oceanen_search_keywords_product(prduct_id,product_url,product_click_cnt,prduct_name,product_show_cnt,product_order_cnt,keyword,product_out_link,record_time,product_amount_index)values (?,?,?,?,?,?,?,?,?,?)";
        pStmt = conn.prepareStatement(sql);
        pStmt.setLong(1, vo.getPrductId());
        pStmt.setString(2, vo.getProductUrl());
        pStmt.setInt(3, vo.getProductClickCnt());
        pStmt.setString(4, vo.getPrductName());
        pStmt.setInt(5, vo.getProductShowCnt());
        pStmt.setInt(6, vo.getProductOrderCnt());
        pStmt.setString(7, vo.getKeyword());
        pStmt.setString(8, vo.getRoductOutLink());
        Timestamp ts = new Timestamp(vo.getRecordTime().getTime());
        pStmt.setTimestamp(9, ts);
        pStmt.setInt(10, vo.getProductAmountIndex());
        if (pStmt.execute())
            errorMsg.setCode(0);
        else {

            errorMsg.setMsg(pStmt.toString());
        }
    }

    public void doUpdate(OceanenSearchKeywordsProductDO vo) throws SQLException {
        String sql = "update  oceanen_search_keywords_product SET  prduct_id=? , product_url=? , product_click_cnt=? , prduct_name=? , product_show_cnt=? , product_order_cnt=? , keyword=? , roduct_out_link=? , record_time=? , product_amount_index=?WHERE prduct_id=?";
        pStmt = conn.prepareStatement(sql);
        pStmt.setLong(1, vo.getPrductId());
        pStmt.setString(2, vo.getProductUrl());
        pStmt.setInt(3, vo.getProductClickCnt());
        pStmt.setString(4, vo.getPrductName());
        pStmt.setInt(5, vo.getProductShowCnt());
        pStmt.setInt(6, vo.getProductOrderCnt());
        pStmt.setString(7, vo.getKeyword());
        pStmt.setString(8, vo.getRoductOutLink());
        Timestamp ts = new Timestamp(vo.getRecordTime().getTime());
        pStmt.setTimestamp(9, ts);
        pStmt.setInt(10, vo.getProductAmountIndex());
        pStmt.setLong(11, vo.getPrductId());
        if (pStmt.execute())
            errorMsg.setCode(0);
        else {

            errorMsg.setMsg(pStmt.toString());
        }
    }

    @Override
    public void doDelete(OceanenSearchKeywordsProductDO vo) throws SQLException {
        String sql = String.format("DELETE FROM  oceanen_search_keywords_product WHERE prduct_id=?", vo.getPrductId());
        pStmt = conn.prepareStatement(sql);
        if (pStmt.execute())
            errorMsg.setCode(0);
        else {
            errorMsg.setMsg(pStmt.toString());
        }
    }

    @Override
    public void doBatch(List<OceanenSearchKeywordsProductDO> t) throws SQLException {

    }
}
