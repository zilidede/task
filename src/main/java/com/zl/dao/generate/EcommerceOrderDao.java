package com.zl.dao.generate;


import com.zl.config.Config;
import com.zl.dao.DaoService;
import com.zl.dao.ErrorMsg;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Param:
 * @Auther: zl
 * @Date: 2023-10-21
 */
public class EcommerceOrderDao implements DaoService<EcommerceOrderDO> {
    private static final int QueryTimeout = 30;
    private final ErrorMsg errorMsg;
    private final Connection conn;
    private Statement stmt = null;
    private PreparedStatement pStmt = null;
    private final String tableName = "ecommerce_order";

    public EcommerceOrderDao(Connection connection) throws SQLException {
        errorMsg = new ErrorMsg();
        this.conn = connection;
        stmt = conn.createStatement();
        stmt.setQueryTimeout(QueryTimeout);  // set timeout to 30 sec;
    }

    public ErrorMsg getErrorMsg() {
        return errorMsg;
    }

    @Override
    public void doInsert(EcommerceOrderDO vo) throws SQLException {
        String sql = "insert into ecommerce_order(order_finish_time,order_pay_amount,order_submit_time,goods_ids,user_profile_tag,child_order_id,goods_sku,buyers_info,shop_id,user_id,buyeer_addr,order_source,user_nickname,order_id, after_sale,order_deliver_time,goods_count,platform)values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)" +
                "ON conflict(child_order_id,order_submit_time)" +
                "DO UPDATE SET after_sale=?,order_deliver_time=?,order_pay_amount=?";
        pStmt = conn.prepareStatement(sql);
        Timestamp ts = new Timestamp(vo.getOrderFinishTime().getTime());
        pStmt.setTimestamp(1, ts);
        pStmt.setDouble(2, vo.getOrderPayAmount());
        ts = new Timestamp(vo.getOrderSubmitTime().getTime());
        pStmt.setTimestamp(3, ts);
        pStmt.setString(4, vo.getGoodsIds());
        pStmt.setString(5, vo.getUserProfileTag());
        pStmt.setString(6, vo.getChildOrderId());
        pStmt.setString(7, vo.getGoodsSku());
        pStmt.setString(8, vo.getBuyersInfo());
        pStmt.setString(9, vo.getShopId());
        pStmt.setLong(10, vo.getUserId());
        pStmt.setString(11, vo.getBuyeerAddr());
        pStmt.setString(12, vo.getOrderSource());
        pStmt.setString(13, vo.getUserNickname());
        pStmt.setString(14, vo.getOrderId());
        pStmt.setString(15, vo.getAfterSale());
        ts = new Timestamp(vo.getOrderDeliverTime().getTime());
        pStmt.setTimestamp(16, ts);
        pStmt.setString(17, vo.getGoodsCount());
        pStmt.setString(18, vo.getPlatform());
        pStmt.setString(19, vo.getAfterSale());
        ts = new Timestamp(vo.getOrderDeliverTime().getTime());
        pStmt.setTimestamp(20, ts);
        pStmt.setDouble(21, vo.getOrderPayAmount());
        if (pStmt.execute())
            errorMsg.setCode(0);
        else {
            errorMsg.setCode(Config.FAIL_INSERT);
            errorMsg.setMsg(pStmt.toString());
        }
    }

    public void doUpdate(EcommerceOrderDO vo) throws SQLException {
        String sql = "update  ecommerce_order SET   profit_amount=?WHERE order_id=?";
        pStmt = conn.prepareStatement(sql);
        pStmt.setDouble(1, vo.getProfit());
        pStmt.setString(2, vo.getOrderId());
        try {
            pStmt.execute();
            errorMsg.setCode(0);
        } catch (Exception ex) {
            errorMsg.setCode(Config.FAIL_UPDATE);
            errorMsg.setMsg(pStmt.toString());
        }

    }

    @Override
    public void doDelete(EcommerceOrderDO vo) throws SQLException {
        String sql = String.format("DELETE FROM  ecommerce_order WHERE order_id=?", vo.getOrderId());
        pStmt = conn.prepareStatement(sql);
        if (pStmt.execute())
            errorMsg.setCode(0);
        else {
            errorMsg.setCode(Config.FAIL_INSERT);
            errorMsg.setMsg(pStmt.toString());
        }
    }

    @Override
    public void doBatch(List<EcommerceOrderDO> t) throws SQLException {

    }

    //handle
    public List<String> getUnUserOrderIds(String shopId, String submitTime) throws SQLException {
        List<String> list = new ArrayList<>();
        String sql = String.format("select distinct(order_id) from ecommerce_order where  user_nickname ='' and shop_id ='%s'  and order_submit_time>'%s'", shopId, submitTime);
        pStmt = conn.prepareStatement(sql);
        ResultSet set = pStmt.executeQuery();
        while (set.next()) {
            list.add(set.getString("order_id"));
        }
        return list;
    }

    public Map<Long, String> getUserOrderIds() throws SQLException {
        Map<Long, String> map = new HashMap<>();
        String sql = "select distinct(order_id),user_nickname from ecommerce_order where  user_nickname !=''";
        pStmt = conn.prepareStatement(sql);
        ResultSet set = pStmt.executeQuery();
        while (set.next()) {
            map.put(set.getLong("order_id"), "");
        }
        return map;
    }

    public int doUpdateUserInfo(EcommerceOrderDO vo) throws SQLException {
        String sql = "update  ecommerce_order set user_id=?,user_nickname=?,user_profile_tag=? where order_id=?";
        pStmt = conn.prepareStatement(sql);
        pStmt.setLong(1, vo.getUserId());
        pStmt.setString(2, vo.getUserNickname());
        pStmt.setString(3, vo.getUserProfileTag());
        pStmt.setString(4, vo.getOrderId());
        try {
            pStmt.execute();
            errorMsg.setCode(0);
        } catch (Exception ex) {
            errorMsg.setCode(Config.FAIL_UPDATE);
            errorMsg.setMsg(pStmt.toString());
        }

        return errorMsg.getCode();
    }

    public int doBatchUpdateUserInfo(List<EcommerceOrderDO> list) throws SQLException {
        conn.setAutoCommit(false);
        // 创建PreparedStatement对象
        String sql = "update  ecommerce_order set user_id=?,user_nickname=?,user_profile_tag=? where order_id=?";

        pStmt = conn.prepareStatement(sql);
        try {
            // 准备多条记录的数据
            int batchSize = list.size(); // 假设有5条记录要插入
            for (EcommerceOrderDO vo : list) {
                pStmt.setLong(1, vo.getUserId());
                pStmt.setString(2, vo.getUserNickname());
                pStmt.setString(3, vo.getUserProfileTag());
                pStmt.setString(4, vo.getOrderId());
                // 添加当前操作到批处理中
                pStmt.addBatch();
            }
            // 执行批处理中的所有操作
            int[] updateCounts = pStmt.executeBatch();

            // 提交事务
            conn.commit();

            System.out.println("Rows affected: " + updateCounts.length);
        } catch (SQLException e) {
            e.printStackTrace();
            // 如果有异常发生，回滚事务
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return 0;
    }
}
