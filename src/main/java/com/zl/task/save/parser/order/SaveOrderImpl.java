package com.zl.task.save.parser.order;


import com.zl.dao.generate.EcommerceOrderDO;
import com.zl.dao.generate.EcommerceOrderDao;
import com.zl.task.impl.SaveServiceImpl;
import com.zl.utils.csv.BatchCSVReader;
import com.zl.utils.jdbc.generator.jdbc.DefaultDatabaseConnect;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class SaveOrderImpl implements SaveOrder {
    private static final Integer MAX_SAVE_COUNT = 1000;
    private static final List<EcommerceOrderDO> orderDOS = new ArrayList<>();
    private HashMap<String, String> tMaps = new HashMap<>();
    private static EcommerceOrderDao douyinOrderDao;
    private static SaveServiceImpl saveService;
    private final SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private int i = 0;

    public SaveOrderImpl() throws SQLException {
        saveService = new SaveServiceImpl();
        douyinOrderDao = new EcommerceOrderDao(DefaultDatabaseConnect.getConn());
        i = 0;
    }

    public static List<EcommerceOrderDO> getOrderDOS() {
        return orderDOS;
    }

    @Override
    public void call(String platform, String shop, List<Map<String, String>> maps) throws ParseException {
        if (platform.equals("小红书")) {
            callRedBook(shop, maps);
        } else if (platform.equals("视频号")) {
            callWeChat(shop, maps);
        } else if (platform.equals("抖音")) {
            callDouYin(shop);
        }
    }

    public void callDouYin(String shop) throws ParseException {
        //回调函数 抖音订单csv导入数据库
        List<Map<String, String>> cells = BatchCSVReader.getMaps();
        Map<String, Integer> map = new HashMap<>();
        for (int i = 1; i < cells.size(); i++) {
            EcommerceOrderDO orderDO = null;
            orderDO = setDouYinOrderCell(cells.get(i), shop);
            orderDOS.add(orderDO);
            map.put(orderDO.getOrderId(), 0);
        }
        //去重；
        Set set = new HashSet();
        List newList = new ArrayList();
        for (Iterator iter = orderDOS.iterator(); iter.hasNext(); ) {
            Object element = iter.next();
            if (set.add(element))
                newList.add(element);
        }
        orderDOS.clear();
        orderDOS.addAll(newList);
        if (orderDOS.size() > MAX_SAVE_COUNT) {

            saveService.savePgSql(douyinOrderDao, SaveOrderImpl.getOrderDOS());
        }
        cells.clear();
        System.out.println("订单总数:" + orderDOS.size());
    }

    public void callRedBook(String shop, List<Map<String, String>> map) throws ParseException {
        // 小红书订单excel导入数据库
        tMaps = new HashMap<>();
        for (int i = 0; i < map.size(); i++) {
            EcommerceOrderDO orderDO = null;
            orderDO = setRedBookOrderCell(map.get(i), shop);
            orderDOS.add(orderDO);
            tMaps.put(orderDO.getOrderId(), orderDO.getGoodsSku());
        }
        //相同订单需修改子订单编号
        int i = 0;
        for (EcommerceOrderDO orderDO : orderDOS) {
            if (tMaps.containsKey(orderDO.getChildOrderId())) {
                String s = tMaps.get(orderDO.getChildOrderId());
                if (!s.equals(orderDO.getGoodsSku())) {
                    String childOrderId = orderDO.getChildOrderId() + i;
                    while (tMaps.containsKey(childOrderId)) {
                        childOrderId = orderDO.getChildOrderId() + i++;
                    }
                    i++;
                    orderDO.setChildOrderId(childOrderId);
                }

            }
        }
        //去重；
        Set set = new HashSet();
        List newList = new ArrayList();
        for (Iterator iter = orderDOS.iterator(); iter.hasNext(); ) {
            Object element = iter.next();
            if (set.add(element))
                newList.add(element);
        }
        orderDOS.clear();
        orderDOS.addAll(newList);

        saveService.savePgSql(douyinOrderDao, SaveOrderImpl.getOrderDOS());
        orderDOS.clear();
        map.clear();
        System.out.println("订单总数:" + orderDOS.size());
    }

    public void callWeChat(String shop, List<Map<String, String>> map) throws ParseException {
        // 视频号订单excel导入数据库

        tMaps = new HashMap<>();
        for (int i = 0; i < map.size(); i++) {
            EcommerceOrderDO orderDO = null;
            orderDO = setWeChatOrderCell(map.get(i), shop);
            orderDOS.add(orderDO);
            tMaps.put(orderDO.getOrderId(), orderDO.getGoodsSku());
        }
        //相同订单需修改子订单编号
        int i = 0;
        for (EcommerceOrderDO orderDO : orderDOS) {
            if (tMaps.containsKey(orderDO.getChildOrderId())) {
                String s = tMaps.get(orderDO.getChildOrderId());
                if (!s.equals(orderDO.getGoodsSku())) {
                    String childOrderId = orderDO.getChildOrderId() + i;
                    while (tMaps.containsKey(childOrderId)) {
                        childOrderId = orderDO.getChildOrderId() + i++;
                    }
                    i++;
                    orderDO.setChildOrderId(childOrderId);
                }

            }
        }
        //去重；
        Set set = new HashSet();
        List newList = new ArrayList();
        for (Iterator iter = orderDOS.iterator(); iter.hasNext(); ) {
            Object element = iter.next();
            if (set.add(element))
                newList.add(element);
        }
        orderDOS.clear();
        orderDOS.addAll(newList);
        saveService.savePgSql(douyinOrderDao, SaveOrderImpl.getOrderDOS());

        System.out.println("订单总数:" + orderDOS.size());

    }

    public EcommerceOrderDO setWeChatOrderCell(Map<String, String> map, String shop) throws ParseException {
        EcommerceOrderDO orderDO = new EcommerceOrderDO();
        orderDO.setShopId(shop);
        orderDO.setPlatform("视频号");
        orderDO.setOrderId(map.get("订单号").trim());
        orderDO.setChildOrderId(map.get("订单号"));
        orderDO.setOrderSubmitTime(sf.parse(map.get("下单时间").trim()));
        String temp = map.get("支付时间").trim();
        if (temp.equals("")) {
            orderDO.setOrderFinishTime(sf.parse("1997-07-27 00:00:00"));
        } else
            orderDO.setOrderFinishTime(sf.parse(map.get("支付时间").trim()));

        temp = map.get("商品发货").trim(); //4325502573202412063147720272
        if (temp.equals("未发货")) {
            orderDO.setOrderDeliverTime(sf.parse("1997-07-27 00:00:00"));
        } else if (temp.equals("已发货"))
            orderDO.setOrderDeliverTime(sf.parse("2000-07-27 00:00:00"));
        try {
            orderDO.setAfterSale(map.get("商品售后").trim());
        } catch (Exception ex) {
            orderDO.setAfterSale("");
        }
        if (orderDO.getAfterSale().equals(" ") || orderDO.getAfterSale().equals("无") || orderDO.getAfterSale().equals("用户取消申请")) {
            orderDO.setAfterSale("无售后");
        }
        orderDO.setGoodsCount(map.get("商品数量").trim());
        orderDO.setBuyersInfo(map.get("收件人姓名").trim() + "&" + map.get("收件人手机").trim());
        orderDO.setBuyeerAddr(map.get("省").trim() + "&" + map.get("市").trim() + "&" + map.get("区").trim() + "&" + map.get("收件人地址").trim());
        orderDO.setGoodsIds(map.get("商品编码(平台)").trim());
        orderDO.setGoodsSku(map.get("SKU编码(自定义)").trim());
        orderDO.setOrderSource(map.get("来源渠道").trim() + "&" + map.get("账号昵称").trim());
        orderDO.setOrderPayAmount(Double.parseDouble(map.get("订单实际支付金额").replace(",", "").trim()));
        if (orderDO.getOrderSource().equals("&")) {
            if (orderDO.getGoodsSku().indexOf("-") < 0)
                orderDO.setOrderPayAmount(0.00);
        }
        orderDO.setShopId(shop);
        orderDO.setUserId(0L);
        orderDO.setUserProfileTag("");
        orderDO.setUserNickname("");
        //debug
        return orderDO;
    }

    public EcommerceOrderDO setRedBookOrderCell(Map<String, String> map, String shop) throws ParseException {
        EcommerceOrderDO orderDO = new EcommerceOrderDO();
        orderDO.setShopId(shop);
        orderDO.setPlatform("小红书");
        orderDO.setOrderId(map.get("订单号").trim());
        orderDO.setGoodsSku(map.get("SKU名称").trim());
        orderDO.setChildOrderId(map.get("订单号"));
        orderDO.setOrderSubmitTime(sf.parse(map.get("订单创建时间").trim()));
        String temp = map.get("支付时间").trim();
        if (temp.equals("")) {
            orderDO.setOrderFinishTime(sf.parse("1997-07-27 00:00:00"));
        } else
            orderDO.setOrderFinishTime(sf.parse(map.get("支付时间").trim()));

        temp = map.get("订单发货时间").trim();
        if (temp.equals("")) {
            orderDO.setOrderDeliverTime(sf.parse("1997-07-27 00:00:00"));
        } else
            orderDO.setOrderDeliverTime(sf.parse(map.get("订单发货时间").trim()));
        orderDO.setAfterSale(map.get("订单状态").trim() + "&" + map.get("售后状态").trim());
        orderDO.setGoodsCount("1");
        orderDO.setBuyersInfo(map.get("收件人姓名").trim() + "&" + map.get("收件人电话").trim());
        orderDO.setBuyeerAddr(map.get("省").trim() + "&" + map.get("市").trim() + "&" + map.get("区").trim() + "&" + map.get("收件人地址").trim());
        orderDO.setGoodsIds("");
        orderDO.setGoodsSku(map.get("SKU名称").trim());
        orderDO.setOrderSource(map.get("达人名称(待下线)").trim());
        orderDO.setOrderPayAmount(Double.parseDouble(map.get("用户应付金额(元)").replace(",", "").trim()));
        if (orderDO.getOrderPayAmount().equals(0.00)) {
            orderDO.setOrderPayAmount(Double.parseDouble(map.get("商品总价(元)").replace(",", "").trim()));
        }
        orderDO.setShopId(shop);
        orderDO.setUserId(0L);
        orderDO.setUserProfileTag("");
        orderDO.setUserNickname("");
        return orderDO;
    }

    public EcommerceOrderDO setDouYinOrderCell(Map<String, String> map, String shop) throws ParseException {
        EcommerceOrderDO orderDO = new EcommerceOrderDO();
        orderDO.setOrderId(map.get("主订单编号").trim());
        orderDO.setChildOrderId(map.get("子订单编号"));
        orderDO.setOrderSubmitTime(sf.parse(map.get("订单提交时间").trim()));
        String temp = map.get("支付完成时间").trim();
        if (temp.equals("")) {
            orderDO.setOrderFinishTime(sf.parse("1997-07-27 00:00:00"));
        } else
            orderDO.setOrderFinishTime(sf.parse(map.get("支付完成时间").trim()));

        temp = map.get("发货时间").trim();
        if (temp.equals("")) {
            orderDO.setOrderDeliverTime(sf.parse("1997-07-27 00:00:00"));
        } else
            orderDO.setOrderDeliverTime(sf.parse(map.get("发货时间").trim()));
        orderDO.setAfterSale(map.get("售后状态").trim());
        orderDO.setGoodsCount(map.get("商品数量").trim());
        orderDO.setBuyersInfo(map.get("收件人").trim() + "&" + map.get("收件人手机号").trim());
        orderDO.setBuyeerAddr(map.get("省").trim() + "&" + map.get("市").trim() + "&" + map.get("区").trim() + "&" + map.get("街道").trim() + "&" + map.get("详细地址").trim());
        orderDO.setGoodsIds(map.get("商品ID").trim());
        orderDO.setGoodsSku(map.get("商品规格").trim());
        orderDO.setOrderSource(map.get("APP渠道").trim() + "&" + map.get("广告渠道").trim() + "&" + map.get("流量类型").trim() + "&" + map.get("流量体裁").trim() + "&" + map.get("流量渠道").trim() + "&" + map.get("达人ID").trim() + "&" + map.get("达人昵称").trim());
        orderDO.setOrderPayAmount(Double.parseDouble(map.get("订单应付金额").replace(",", "").trim()));
        orderDO.setShopId(shop);
        orderDO.setUserId(0L);
        orderDO.setUserProfileTag("");
        orderDO.setUserNickname("");
        orderDO.setPlatform("抖音");
        if (orderDO.getAfterSale().equals("-")) {
            orderDO.setAfterSale("无售后");
        }
        return orderDO;
    }

}
