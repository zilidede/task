package com.zl.dao;


import com.zl.dao.generate.EcommerceOrderDO;
import com.zl.dao.generate.GoodsProfitDO;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @className: com.craw.nd.dao.manual-> HandleDao
 * @description: //手动dao层
 * @author: zl
 * @createDate: 2023-11-15 17:03
 * @version: 1.0
 * @todo:
 */
public class HandleDao {
    private static final int QueryTimeout = 30;
    private final Connection conn;
    private Statement stmt = null;
    private PreparedStatement pStmt = null;
    private final SimpleDateFormat sf;

    public HandleDao(Connection connection) throws SQLException {
        this.conn = connection;
        stmt = conn.createStatement();
        stmt.setQueryTimeout(QueryTimeout);  // set timeout to 30 sec;
        sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    public List<String> findCompleDayTaskList(String date) throws SQLException {
        //查找任务队列中list的完成的每日任务；
        List<String> result = new ArrayList<>();
        String sql = String.format("select content from task where  start_time >='%s' and name='爬取每日抖店罗盘商品榜单' and status=3 ", date);
        pStmt = conn.prepareStatement(sql);
        ResultSet set = pStmt.executeQuery();
        while (set.next()) {
            result.add(set.getString("content"));
        }
        return result;
    }

    public List<EcommerceOrderDO> findOrderPayAmount(String date) throws SQLException, ParseException {
        List<EcommerceOrderDO> ecommerceOrderDOS = new ArrayList<>();
        String sql = String.format("select * from  ecommerce_order where  profit_amount=0.00 and order_submit_time>'%s' ", date);
        pStmt = conn.prepareStatement(sql);
        ResultSet set = pStmt.executeQuery();
        while (set.next()) {
            EcommerceOrderDO vo = new EcommerceOrderDO();
            String l = set.getString("order_id");
            String childOrderId = set.getString("child_order_id");
            String goodsCount = set.getString("goods_count");
            String goodsIds = set.getString("goods_ids");
            Double orderPayAmount = set.getDouble("order_pay_amount");
            Long orderSubmitTime = set.getTimestamp("order_submit_time").getTime();
            vo.setOrderId(l);
            vo.setChildOrderId(childOrderId);
            vo.setGoodsIds(goodsIds);
            vo.setGoodsCount(goodsCount);
            vo.setOrderPayAmount(orderPayAmount);
            vo.setOrderSubmitTime(new Date(orderSubmitTime));
            ecommerceOrderDOS.add(vo);
        }
        return ecommerceOrderDOS;
    }

    public List<GoodsProfitDO> findGoodsProfits() throws SQLException, ParseException {
        // 从goods_profit表中寻找
        List<GoodsProfitDO> list = new ArrayList<>();
        String sql = "select * from  goods_profit";
        pStmt = conn.prepareStatement(sql);
        ResultSet set = pStmt.executeQuery();
        while (set.next()) {
            Long l = set.getLong("goods_id");
            GoodsProfitDO goodsProfitDO = new GoodsProfitDO();
            goodsProfitDO.setGoodsId(l);
            goodsProfitDO.setWholeSale(set.getDouble("whole_sale_cost"));
            goodsProfitDO.setPlatformTradePoints(set.getDouble("platform_trade_points"));
            goodsProfitDO.setBrandTradePoints(set.getDouble("brand_trade_points"));
            goodsProfitDO.setAnchorTradePoints(set.getDouble("anchor_trade_points"));
            goodsProfitDO.setOtherTradePoints(set.getDouble("other_trade_points"));
            goodsProfitDO.setBroadcasterTradePoints(set.getDouble("broadcaster_trade_points"));
            goodsProfitDO.setExpressCost(set.getDouble("express_cost"));
            goodsProfitDO.setTransportInsuranceCost(set.getDouble("transport_insurance_cost"));
            goodsProfitDO.setGiftsCost(set.getDouble("gifts_cost"));
            goodsProfitDO.setAcclaimCost(set.getDouble("acclaim_cost"));
            goodsProfitDO.setAttritionCosts(set.getDouble("attrition_costs"));
            goodsProfitDO.setOtherCost(set.getDouble("other_cost"));
            goodsProfitDO.setAfterSaleRate(set.getDouble("after_sale_rate"));
            goodsProfitDO.setFeeRoi(set.getDouble("fee_roi"));

            goodsProfitDO.setStartSaleTime(set.getTimestamp("start_sale_time"));

            goodsProfitDO.setEndSaleTime(set.getTimestamp("end_sale_time"));
            list.add(goodsProfitDO);
        }
        return list;
    }

    public void findOcSearchKeywordsDetail(Map<String, Integer> map) throws SQLException {
        // 从巨量云图搜索词详情寻找

        String sql = "select * from  douyin_oceanengine_search_keywords_detail";
        pStmt = conn.prepareStatement(sql);
        ResultSet set = pStmt.executeQuery();
        while (set.next()) {
            String s = set.getString("keyword");
            map.put(s, 0);
        }

    }

    public List<Integer> findUnCrawCategoryIdsFromGoodsType(Map<Integer, Integer> categoryIds) throws SQLException {
        //从goods_type 找未爬取商品类目id
        List<Integer> unCrawCategoryIds = new ArrayList<>();
        String sql = "select * from goods_type where level=1 or level=2";
        pStmt = conn.prepareStatement(sql);
        ResultSet set = pStmt.executeQuery();
        while (set.next()) {
            Integer id = set.getInt("id");
            if (!categoryIds.containsKey(id)) {
                unCrawCategoryIds.add(id);
            }
        }
        return unCrawCategoryIds;
    }

    public void findCategoryIdsFromGoodsType(Map<Integer, String> categoryIds) throws SQLException {
        //从goods_type 找未爬取商品类目id

        String sql = "select * from goods_type";
        pStmt = conn.prepareStatement(sql);
        ResultSet set = pStmt.executeQuery();
        while (set.next()) {
            String s = set.getString("industry_name") +
                    "-" + set.getString("industry_first_category_name")
                    + "-" + set.getString("industry_second_category_name")
                    + "-" + set.getString("industry_three_category_name")
                    + "-" + set.getString("industry_four_category_name");
            s = s.replaceAll("\\\"", "");
            String[] strings = s.split("-");
            categoryIds.put(set.getInt("id"), s);
        }

    }

    public List<EcommerceOrderDO> findDouYinShopOriginalOrderIds() throws SQLException, ParseException {
        //寻找抖音原始订单id列表(不存在于douyin_order表);
        String sql = "select order_id,order_child_id,order_submit_time,\"订单完成时间\" from original_douyin_shop_order where order_submit_time>'2022-01-01'";
        pStmt = conn.prepareStatement(sql);
        ResultSet set = pStmt.executeQuery();
        Map<Long, EcommerceOrderDO> map = new HashMap<>();
        List<EcommerceOrderDO> orders = new ArrayList<>();
        while (set.next()) {
            Long orderId = set.getLong("order_id");
            if (map.containsKey(orderId)) {
                EcommerceOrderDO vo1 = map.get(orderId);
                //  if(vo1.getOrderId().equals(4838422092233398240l)){
                // String s2=set.getString("order_child_id");
                // }
                //  vo1.setChildOrderId(vo1.getChildOrderId()+" "+set.getString("order_child_id"));
            } else {

            }
            EcommerceOrderDO vo = new EcommerceOrderDO();

            vo.setOrderId(set.getString("order_id"));
            //   vo.setChildOrderId(set.getString("order_child_id"));
            String s = set.getString("订单完成时间");

            if (s == null) {
                s = "1997-07-27 00:00:00";
            }
            vo.setOrderFinishTime(sf.parse(s));
            s = set.getString("order_submit_time");
            vo.setOrderSubmitTime(sf.parse(s));
            map.put(orderId, vo);
        }
        for (Map.Entry<Long, EcommerceOrderDO> entry : map.entrySet()) {
            orders.add(entry.getValue());
        }
        return orders;
    }

    public List<String> findDouYinOrderId(String startTime, String endTime) throws SQLException {
        //寻找抖音订单id列表
        List<String> orders = new ArrayList<>();
        if (startTime.equals("")) {
            String sql = "select order_id FROM  url";
            pStmt = conn.prepareStatement(sql);
            ResultSet set = pStmt.executeQuery();
            while (set.next()) {
                orders.add(set.getString("order_id"));
            }
        }
        return orders;
    }

    public List<String> findDouYinDistinctOrderId() throws SQLException {
        //寻找抖音订单id列表
        Map<String, Integer> map = findDouYinNickNameDistinctOrderId();
        List<String> orders = new ArrayList<>();
        String sql = "select distinct(order_id) from original_douyin_shop_order";
        pStmt = conn.prepareStatement(sql);
        ResultSet set = pStmt.executeQuery();
        while (set.next()) {
            String order = set.getString("order_id");
            if (!map.containsKey(order))
                orders.add(set.getString("order_id"));
        }
        return orders;
    }

    public Map<String, Integer> findDouYinNickNameDistinctOrderId() throws SQLException {
        //寻找抖音订单id列表
        Map<String, Integer> orders = new HashMap();
        String sql = "select distinct(order_id) from douyin_order_user";
        pStmt = conn.prepareStatement(sql);
        ResultSet set = pStmt.executeQuery();
        while (set.next()) {
            orders.put(set.getString("order_id"), 0);
        }
        return orders;
    }
}
