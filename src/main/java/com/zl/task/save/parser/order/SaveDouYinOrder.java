package com.zl.task.save.parser.order;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zl.dao.HandleDao;
import com.zl.dao.generate.EcommerceOrderDO;
import com.zl.dao.generate.EcommerceOrderDao;
import com.zl.dao.generate.GoodsProfitDO;
import com.zl.dao.generate.GoodsProfitDao;
import com.zl.task.save.parser.ParserFiddlerJson;
import com.zl.task.vo.http.HttpVO;
import com.zl.utils.csv.BatchCSVReader;
import com.zl.utils.excel.ExcelReaderUtils;
import com.zl.utils.io.DiskIoUtils;
import com.zl.utils.io.FileIoUtils;
import com.zl.utils.jdbc.generator.jdbc.DefaultDatabaseConnect;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.DateUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

/**
 * @className: com.craw.nd.service.other.person.Impl.craw.app.douYin.self.live-> AnalysisRealLiveData
 * @description: 讲抖音原始订单csv转化为-douyin——order
 * @author: zl
 * @createDate: 2023-10-21 15:12
 * @version: 1.0
 * @todo: 修改记录：2024-05-15
 * 修改原因：更改导入文件判断条件为创建时间
 */
public class SaveDouYinOrder {
    private static Map<String, Integer> orderMap;

    private HandleDao handleDao;

    private static final String orderDir = "D:\\data\\爬虫\\电商\\抖音\\抖音小店\\订单";
    private final String userDir = "D:\\work\\data\\BaiduSyncdisk\\craw\\电商\\抖音\\抖音小店\\searchlist\\";
    private final String backOrderCsvDir = "S:\\data\\back\\BaiduSyncdisk\\备份\\电商订单\\已导入\\";
    private final EcommerceOrderDao dao;

    public void importCsvOrdersToDB(String dir) throws Exception {
        //将抖音csv订单表导入到数据库EcommerceOrder
        List<String> files = DiskIoUtils.getFileListFromDir(dir);
        if (files == null)
            return;
        SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd");
        //只处理当前日期前的一个月的日期文件
        // 获取当前日期
        LocalDate currentDate = LocalDate.now();
        // 计算一个月前的日期
        LocalDate oneMonthAgo = currentDate.minusMonths(48);
        // 将 LocalDate 转换为 Date
        Date dateOneMonthAgo = java.util.Date.from(oneMonthAgo.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        String date = sf1.format(dateOneMonthAgo);
        //对文件按日期进行排序；
        List<ImportFile> importFiles = new ArrayList<>();
        for (int j = 0; j < files.size(); j++) {
            File file = new File(files.get(j));
            long creationTime = file.lastModified(); // 注意：Java 7没有提供创建时间，这里用的是修改时间作为示例
            Date lastModifiedTime = new Date(creationTime);
            importFiles.add(new ImportFile(files.get(j), lastModifiedTime));
        }
        List<Map<String, String>> cells;
        importFiles.sort(Comparator.comparing(ImportFile -> ImportFile.lastModifiedTime));
        for (ImportFile importFile : importFiles) {
            String filePath = importFile.filePath;
            File file = new File(filePath);
            long creationTime = file.lastModified(); // 注意：Java 7没有提供创建时间，这里用的是修改时间作为示例
            Date lastModifiedTime = new Date(creationTime);
            String formattedTime = sf1.format(new Date(creationTime));
            if (dateOneMonthAgo.compareTo(lastModifiedTime) > 0)
                continue;
            SaveOrder saveOrder = new SaveOrderImpl();
            String s = FilenameUtils.getExtension(filePath); //后缀名；
            if (s.equals("csv"))
                BatchCSVReader.read(filePath, saveOrder);
            else if (s.equals("xlsx")) {
                List<Map<String, String>> maps = ExcelReaderUtils.readExcel(filePath);
                String fileName = Paths.get(filePath).getFileName().toString(); // 获取文件名
                System.out.println("导入文件" + fileName);
                String[] strings1 = fileName.split("-");
                String platform = strings1[0];
                String shop = strings1[1];
                saveOrder.call(platform, shop, maps);

            }
            //移动到备份文件
            Path source = Paths.get(filePath);
            Path destination = Paths.get(backOrderCsvDir + file.getName());
            try {
                Files.move(source, destination, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("File moved successfully");
            } catch (IOException e) {
                System.err.println("Error while moving the file: " + e.getMessage());
            }


        }
    }

    public static void main(String[] args) throws Exception {
        SaveDouYinOrder parser = new SaveDouYinOrder();
        run("");
    }

    public SaveDouYinOrder() throws SQLException {
        orderMap = new HashMap<>();
        dao = new EcommerceOrderDao(DefaultDatabaseConnect.getConn());
    }

    public static void run(String dir) throws IOException, ParseException, SQLException {
        //从抖音原始数据库获取订单号
        //从json获取买家信息和用户标签;
        List<String> files = DiskIoUtils.getFileListFromDir(dir);
        for (int i = 0; i < 0; i++) {
            String content = FileIoUtils.readTxtFile(files.get(i), "utf-8");
            String[] strings = content.split("\\{");
            String url = strings[0];
            content = content.replace(url, "");
            parserSearchListJson(content, SaveOrderImpl.getOrderDOS());
        }
    }

    public static void parserSearchListJson(String content, List<EcommerceOrderDO> orderDOS) {
        if (content.indexOf("error") >= 0)
            return;
        JsonParser parser = new JsonParser();
        JsonObject object = parser.parse(content).getAsJsonObject();
        JsonArray jsonArray = parser.parse(object.get("data").toString()).getAsJsonArray();
        for (int i = 0; i < jsonArray.size(); i++) {
            object = parser.parse(jsonArray.get(i).toString()).getAsJsonObject();
            EcommerceOrderDO orderDO = new EcommerceOrderDO();
            String orderId = object.get("shop_order_id").getAsString();
            if (orderMap.containsKey(orderId))
                continue;
            orderMap.put(orderId, 1);
            orderDO.setOrderId(orderId);
            orderDO.setUserId(object.get("user_id").getAsLong());
            orderDO.setUserNickname(object.get("user_nickname").getAsString());
            JsonObject object1 = object.get("receiver_info").getAsJsonObject();
            String s = object1.get("post_receiver").getAsString() + "&" + object1.get("post_tel").getAsString();
            JsonObject object2 = object1.get("post_addr").getAsJsonObject();
            JsonObject object3 = object2.get("province").getAsJsonObject();
            s = s + object3.get("name").getAsString() + object3.get("id").getAsString();
            object3 = object2.get("province").getAsJsonObject();
            s = s + object3.get("name").getAsString() + object3.get("id").getAsString();
            object3 = object2.get("town").getAsJsonObject();
            s = s + object3.get("name").getAsString() + object3.get("id").getAsString();
            object3 = object2.get("street").getAsJsonObject();
            s = s + object3.get("name").getAsString() + object3.get("id").getAsString() + object2.get("detail");
            orderDO.setBuyeerAddr(s);
            try {
                JsonArray jsonArray1 = object.get("user_profile_tag").getAsJsonArray();
                s = jsonArray1.get(0).getAsJsonObject().get("text").getAsString() + jsonArray1.get(0).getAsJsonObject().get("hover_text").getAsString();
                orderDO.setUserProfileTag(s);
            } catch (Exception ex) {
                ex.printStackTrace();
                orderDO.setUserProfileTag("");
            }

            orderDOS.add(orderDO);
        }

    }

    class ImportFile {
        //
        private final String filePath;
        private final Date lastModifiedTime;

        ImportFile(String filePath, Date lastModifiedTime) {
            this.filePath = filePath;
            this.lastModifiedTime = lastModifiedTime;
        }
    }


    public void updateOrderUserInfo(String dir) throws SQLException, IOException {
        //更新用户信息到订单表
        List<EcommerceOrderDO> orderDOS = new ArrayList<>();

        List<String> files = DiskIoUtils.getFileListFromDir(dir);
        for (int i = 0; i < files.size(); i++) {
            parser(files.get(i), orderDOS);
        }
        List<EcommerceOrderDO> unUserOrderList = getUnUserOrderList(orderDOS);
        dao.doBatchUpdateUserInfo(unUserOrderList);

    }

    public List<EcommerceOrderDO> getUnUserOrderList(List<EcommerceOrderDO> orderDOS) throws SQLException {
        // 删除重复的User订单号
        List<EcommerceOrderDO> unUserOrderList = new ArrayList<>();
        Map<Long, String> map = dao.getUserOrderIds();
        for (EcommerceOrderDO orderDO : orderDOS) {
            String orderId = orderDO.getOrderId();
            if (!map.containsKey(orderId))
                unUserOrderList.add(orderDO);
        }
        return unUserOrderList;
    }

    public void parser(String content, List<EcommerceOrderDO> list) throws IOException {

        HttpVO httpVO = ParserFiddlerJson.parserXHRJson(content);
        String url = httpVO.getUrl();
        int iBegin = url.indexOf("oid=");
        if (iBegin > 0) {
            EcommerceOrderDO vo = new EcommerceOrderDO();
            String orderId = url.substring(iBegin + 4, url.indexOf("&verifyFp"));
            String response = httpVO.getResponse().getBody();
            JsonParser parser = new JsonParser();
            JsonObject object = null;
            vo.setOrderId(orderId);
            try {
                object = parser.parse(response).getAsJsonObject();
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            try {
                vo.setUserId(object.getAsJsonObject().get("data").
                        getAsJsonObject().get("id").getAsLong());
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }

            vo.setUserNickname(object.getAsJsonObject().get("data").
                    getAsJsonObject().get("screen_name").getAsString());
            vo.setUserProfileTag(object.getAsJsonObject().get("data").
                    getAsJsonObject().get("avatar_url").getAsString());
            list.add(vo);
        }

    }

    public void updateOrder(String dir) {
        //更新订单信息；
        //更新电商订单状态到数据库
        //更新抖音电商订单售后状态；
        // 更新抖音电商订单交付时间
        // 更新抖音订单快递单号；
        //更新抖音订单用户信息；
        //更新抖音订单来源；

    }

    public void updateOrderProfit() throws SQLException, ParseException {
        //更新规定时间 -订单表利润字段
        Map<Long, Double> unMap = new HashMap<>();
        Double sum = 0.0;
        List<EcommerceOrderDO> orderDOS = handleDao.findOrderPayAmount("2023-08-01");
        List<GoodsProfitDO> goodsProfitDOList = handleDao.findGoodsProfits();
        Map<String, Double> map = new HashMap<>();

        for (EcommerceOrderDO orderDO : orderDOS) {
            GoodsProfitDO vo = null;
            if (orderDO.getOrderId().equals(6927646440131073967L)) {
                System.out.println();
            }
            String[] goodsIds = orderDO.getGoodsIds().split("&");
            Double cost = 0.0;
            GoodsProfitDO costDO = new GoodsProfitDO();
            for (String goods : goodsIds) {
                Long l = Long.parseLong(goods);
                for (GoodsProfitDO goodsProfitDO : goodsProfitDOList) {
                    if (l.equals(goodsProfitDO.getGoodsId())) {
                        Long l1 = orderDO.getOrderSubmitTime().getTime();
                        if (l1 <= goodsProfitDO.getEndSaleTime().getTime() && l1 >= goodsProfitDO.getStartSaleTime().getTime()) {
                            costDO.setOtherCost(Math.max(costDO.getOtherCost(), goodsProfitDO.getOtherCost()));
                            costDO.setAttritionCosts(Math.max(costDO.getAttritionCosts(), goodsProfitDO.getAttritionCosts()));
                            costDO.setAcclaimCost(Math.max(costDO.getAcclaimCost(), goodsProfitDO.getAcclaimCost()));
                            costDO.setGiftsCost(Math.max(costDO.getGiftsCost(), goodsProfitDO.getGiftsCost()));
                            costDO.setExpressCost(Math.max(costDO.getExpressCost(), goodsProfitDO.getExpressCost()));
                            costDO.setTransportInsuranceCost(Math.max(costDO.getTransportInsuranceCost(), goodsProfitDO.getTransportInsuranceCost()));
                            costDO.setPlatformTradePoints(Math.max(costDO.getPlatformTradePoints(), goodsProfitDO.getPlatformTradePoints()));
                            costDO.setBrandTradePoints(Math.max(costDO.getBrandTradePoints(), goodsProfitDO.getBrandTradePoints()));
                            costDO.setOtherTradePoints(Math.max(costDO.getOtherTradePoints(), goodsProfitDO.getOtherTradePoints()));
                            costDO.setAnchorTradePoints(Math.max(costDO.getAnchorTradePoints(), goodsProfitDO.getAnchorTradePoints()));
                            costDO.setBroadcasterTradePoints(Math.max(costDO.getBroadcasterTradePoints(), goodsProfitDO.getBroadcasterTradePoints()));
                            costDO.setWholeSale(goodsProfitDO.getWholeSale() + costDO.getWholeSale());
                            break;
                        }
                    }
                }
            }
            if (costDO.getWholeSale().equals(0.00) && orderDO.getGoodsIds().indexOf("3634512437610466669") > 0) {
                System.out.println();
            }
            cost = calculateCost(orderDO.getOrderPayAmount(), costDO);
            if (cost != 0.0)
                map.put(orderDO.getOrderId(), orderDO.getOrderPayAmount() - cost);
            else {
                String[] goodsIds1 = orderDO.getGoodsIds().split("&");
                for (String goods : goodsIds1) {
                    Long l = Long.parseLong(goods);
                    if (unMap.containsKey(l)) {
                        unMap.put(l, unMap.get(l) + orderDO.getOrderPayAmount());
                    } else {
                        unMap.put(l, orderDO.getOrderPayAmount());
                    }


                    sum = sum + orderDO.getOrderPayAmount();

                }
            }
        }
        System.out.println("未定义的商品成本表 营业额大于100000");
        for (Map.Entry<Long, Double> entry : unMap.entrySet()) {
            if (entry.getValue() > 100000)
                System.out.println(entry.getKey() + "订单金额数:" + entry.getValue());
        }
        System.out.println("涉及的未定义总金额数" + sum);
        List<EcommerceOrderDO> voList = new ArrayList<>();
        for (Map.Entry<String, Double> entry : map.entrySet()) {
            String key = entry.getKey();
            EcommerceOrderDO vo = new EcommerceOrderDO();
            Double value = entry.getValue();
            vo.setOrderId(key);
            vo.setProfit(value);
            voList.add(vo);
        }


    }

    public Double calculateCost(Double revenue, GoodsProfitDO cost) {
        Double d = 0.0;
        d = revenue * (cost.getPlatformTradePoints() + cost.getBrandTradePoints() + cost.getAnchorTradePoints() + cost.getBroadcasterTradePoints() + cost.getOtherTradePoints());
        d = d + cost.getAcclaimCost() + cost.getAttritionCosts() + cost.getExpressCost() + cost.getGiftsCost() + cost.getTransportInsuranceCost() + cost.getOtherCost();
        d = d + cost.getWholeSale();
        return d;
    }

    public void reUpdateGoodsProfit(String excelFile) throws SQLException, ParseException, IOException {

        GoodsProfitDao dao;
        List<GoodsProfitDO> profitDOS;
        dao = new GoodsProfitDao(DefaultDatabaseConnect.getConn());
        profitDOS = new ArrayList<>();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        //更新商品利润表
        List<Map<String, String>> cells = new ArrayList<>();
        int i = 0;
        for (Map<String, String> cell : cells) {
            if (i++ > 0) {
                GoodsProfitDO vo = new GoodsProfitDO();
                vo.setRecordTime(DateUtil.getJavaDate(Double.parseDouble(cell.get("记录时间"))));
                vo.setStartSaleTime(DateUtil.getJavaDate(Double.parseDouble(cell.get("利润开始时间"))));
                vo.setEndSaleTime(DateUtil.getJavaDate(Double.parseDouble(cell.get("利润结束时间"))));
                vo.setGoodsId(Long.parseLong(cell.get("商品ID")));
                vo.setGoodsName(cell.get("商品名称"));
                vo.setWholeSale(Double.parseDouble(cell.get("当前拿货价")));
                vo.setPlatformTradePoints(Double.parseDouble(cell.get("平台扣点")));
                vo.setBrandTradePoints(Double.parseDouble(cell.get("品牌方扣点")));
                vo.setAnchorTradePoints(Double.parseDouble(cell.get("主播扣点")));
                vo.setBroadcasterTradePoints(Double.parseDouble(cell.get("助播扣点")));
                vo.setOtherTradePoints(Double.parseDouble(cell.get("其他扣点")));
                vo.setExpressCost(Double.parseDouble(cell.get("物流成本")));
                vo.setTransportInsuranceCost(Double.parseDouble(cell.get("运费险")));
                vo.setGiftsCost(Double.parseDouble(cell.get("赠品")));
                vo.setAcclaimCost(Double.parseDouble(cell.get("好评返校")));
                vo.setAttritionCosts(Double.parseDouble(cell.get("损耗成本")));
                vo.setAfterSaleRate(Double.parseDouble(cell.get("退货率")));
                vo.setFeeRoi(Double.parseDouble(cell.get("投放roi")));
                vo.setOtherCost(Double.parseDouble(cell.get("其他")));
                profitDOS.add(vo);
            }
        }

    }
}
