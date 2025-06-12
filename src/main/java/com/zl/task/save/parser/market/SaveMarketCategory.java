package com.zl.task.save.parser.market;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zl.dao.generate.MarketCategoryDO;
import com.zl.dao.generate.MarketCategoryDao;
import com.zl.task.impl.SaveServiceImpl;
import com.zl.task.save.parser.ParserFiddlerJson;
import com.zl.task.vo.http.HttpVO;
import com.zl.utils.io.DiskIoUtils;
import com.zl.utils.jdbc.generator.jdbc.DefaultDatabaseConnect;
import com.zl.utils.unicode.UnicodeToChinese;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//
//日志：
/*
2024-08-23 ：解析MarketCategory json 数据，插入数据库
 json 来源： market任务 ：xhr=compass_api/shop/product_lander/lander/interface
 */
public class SaveMarketCategory {
    private final SaveServiceImpl saveService = new SaveServiceImpl();
    private final MarketCategoryDao daoService = new MarketCategoryDao(DefaultDatabaseConnect.getConn());
    private final List<MarketCategoryDO> marketCategoryDOList = new ArrayList<>();

    public SaveMarketCategory() throws SQLException {
    }

    public static void main(String[] args) throws Exception {
        SaveMarketCategory saver = new SaveMarketCategory();
        saver.save("");
    }

    public void save(String sdir) throws Exception {
        //价格分布
        // String dir="D:\\data\\百度云\\BaiduSyncdisk\\2024-08-25\\market-zl\\compassApiShopProductProductChanceMarketCategoryOverviewPriceBandDistribution";
        String dir = sdir + "\\compassApiShopProductProductChanceMarketCategoryOverviewPriceBandDistribution";
        List<String> files = DiskIoUtils.getFileListFromDir(dir);
        for (String file : files) {
            HttpVO httpVO = ParserFiddlerJson.parserXHRJson(file);
            String url = httpVO.getUrl();
            String json = httpVO.getResponse().getBody();
            try {
                parserJson(json, parserUrl(url));
            } catch (Exception ex) {
                continue;
            }

        }

        saveService.savePgSql(daoService, marketCategoryDOList);
        marketCategoryDOList.clear();
        //综合 子类目，各渠道综合，各渠道列表；。
        dir = sdir + "\\compassApiShopProductLanderLanderInterface";
        files = DiskIoUtils.getFileListFromDir(dir);
        for (String file : files) {
            HttpVO httpVO = ParserFiddlerJson.parserXHRJson(file);
            String url = httpVO.getUrl();
            String json = httpVO.getResponse().getBody();
            if (json.indexOf("live_children") >= 0)
                parserChannelJson(json, parserUrl(url, httpVO.getRequest().getBody()));
            else {
                parserJson(json, parserUrl(url, httpVO.getRequest().getBody()));
            }
            System.out.println(file);
        }

        saveService.savePgSql(daoService, marketCategoryDOList);
        //

    }

    public MarketCategoryDO parserUrl(String url) throws UnsupportedEncodingException {
        // 时间 -，catagoryId, -价格域
        String decodedString = "";
        try {
            decodedString = URLDecoder.decode(url, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        String[] strings = decodedString.split("\\&");
        String begin = strings[6].substring(strings[6].indexOf("begin_date=") + 11);
        String end = begin + "-" + strings[7].substring(strings[7].indexOf("end_date=") + 9);
        String id = strings[4].substring(strings[4].indexOf("category_id=") + 12);
        id = UnicodeToChinese.toChinese(id);
        if (id.indexOf(",") >= 0) {
            String[] ids = id.split(",");
            id = ids[ids.length - 1];
        }

        String price = "";
        try {
            price = strings[6].substring(strings[6].indexOf("price_band=") + 11);
        } catch (Exception e) {

        }
        MarketCategoryDO vo = new MarketCategoryDO();
        vo.setPriceBand(price);
        vo.setRecordTime(end);
        vo.setId(Integer.parseInt(id));
        return vo;
    }

    public MarketCategoryDO parserUrl(String url, String json) throws UnsupportedEncodingException {
        // 时间 -，catagoryId, -价格域
        if (json.indexOf("request_body") < 0) {
            return null;
        }

        JsonParser parser = new JsonParser();
        JsonObject object = null;
        JsonArray jsonArray = null;
        String jsonString = "";
        try {
            object = parser.parse(json).getAsJsonObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            jsonString = object.getAsJsonObject().get("rpc_execute_info").
                    getAsJsonObject().get("request_body").getAsString().replaceAll("\n", "");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


        String decodedString = jsonString.substring(jsonString.indexOf("begin_date") + 13, jsonString.indexOf("end_date") - 3) + "-" + jsonString.substring(jsonString.indexOf("end_date") + 11, jsonString.indexOf("date_type") - 2);
        MarketCategoryDO vo = new MarketCategoryDO();
        vo.setRecordTime(decodedString);
        try {
            vo.setId(getId(jsonString));
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

        vo.setPriceBand("all");
        vo.setChannel("all");
        if (jsonString.indexOf("content_type") > 0) {
            String channel = jsonString.substring(jsonString.indexOf("content_type") + 15, jsonString.indexOf("page_info") - 2);
            channel = channel.replaceAll("\"", "").replaceAll(",", "").replaceAll(" ", "");

            vo.setChannel(channel);
        }
        return vo;
    }

    public Integer getId(String jsonString) {
        String id = "";
        String[] ids;
        try {
            id = jsonString.substring(jsonString.indexOf("category_id") + 13, jsonString.indexOf("index_selected") - 2);
            ids = id.split("\"");
            id = ids[1];
        } catch (Exception e) {
            try {
                id = jsonString.substring(jsonString.indexOf("category_id") + 13, jsonString.length() - 2);
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }

            ids = id.split("\"");
            id = ids[1];
        }
        if (id.indexOf(",") >= 0) {
            ids = id.split(",");
            id = ids[ids.length - 1];
        }
        id = id.replaceAll("\"", "").replaceAll(",", "").replaceAll(" ", "");

        int i = Integer.parseInt(id);
        if (i == 2000936957) {
            System.out.println();
        }
        return i;
    }

    public void parserChannelJson(String json, MarketCategoryDO vo1) throws Exception {
        if (vo1 == null)
            return;
        if (json.indexOf("参数校验失败") > 0) {
            return;
        }
        JsonParser parser = new JsonParser();
        JsonObject object = null;
        JsonArray jsonArray = null;
        try {
            object = parser.parse(json).getAsJsonObject();
            jsonArray = object.get("data").getAsJsonObject().get("rpc_data").getAsJsonObject().get("data").getAsJsonArray();
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
        JsonObject jsons = jsonArray.get(0).getAsJsonObject().get("cell_info").getAsJsonObject();
        Iterator<String> keys = jsons.keySet().iterator();
        for (Iterator<String> it = keys; it.hasNext(); ) {
            String key = it.next();
            MarketCategoryDO vo = new MarketCategoryDO();
            vo.setChannel(key);
            vo.setPriceBand("all");
            vo.setId(vo1.getId());
            vo.setRecordTime(vo1.getRecordTime());
            JsonObject object1 = jsons.get(key).getAsJsonObject().get(key + "_children").getAsJsonObject().get("children").getAsJsonArray().get(0)
                    .getAsJsonObject().get("cell_info").getAsJsonObject().get("cate_pay_amt").getAsJsonObject().get("cate_pay_amt_index_values").getAsJsonObject();
            vo.setCatePayAmt(parserField(object1, true));
            try {
                String s1 = object1.get("index_values").getAsJsonObject().get("value_ratio").getAsJsonObject().get("value").getAsString();
                s1 = s1.substring(0, 6);
                vo.setCatePayAmtRatio(s1);

            } catch (Exception ex) {
                ex.printStackTrace();
                vo.setCatePayAmtRatio("0");
            }
            marketCategoryDOList.add(vo);


        }

    }

    public void parserJson(String json, MarketCategoryDO vo1) throws Exception {
        if (vo1 == null)
            return;
        if (json.indexOf("参数校验失败") > 0) {
            return;
        }
        JsonParser parser = new JsonParser();
        JsonObject object = null;
        JsonArray jsonArray = null;
        try {
            object = parser.parse(json).getAsJsonObject();
            jsonArray = object.get("data").getAsJsonObject().get("rpc_data").getAsJsonObject().get("data").getAsJsonArray();
        } catch (Exception e) {
            // e.printStackTrace();
            try {
                jsonArray = object.get("data").getAsJsonArray();
            } catch (Exception ex) {
                ex.printStackTrace();
                return;
            }


        }
        JsonObject object1 = null;
        String temp = "";
        if (jsonArray.size() > 1)

            for (int i = 0; i < jsonArray.size(); i++) {
                MarketCategoryDO vo = new MarketCategoryDO();
                JsonObject jsonObject = jsonArray.get(i).getAsJsonObject().get("cell_info").getAsJsonObject();
                try {
                    //综合
                    temp = jsonObject.get("operate_info").getAsJsonObject().get("cate_id_value").getAsJsonObject().get("value").getAsJsonObject().get("value_str").getAsString();
                    vo.setChannel("all");
                    String temp1 = vo1.getId() + "," + temp;
                    vo.setId(vo1.getId());
                } catch (Exception e) {
                    // 渠道流量数据
                    vo.setId(vo1.getId());
                    try {
                        temp = jsonObject.get("channel_name").getAsJsonObject().get("channel_name_value").getAsJsonObject().get("value").getAsJsonObject().get("value_str").getAsString();
                        vo.setChannel(vo1.getChannel() + "-" + temp);
                    } catch (Exception ex) {
                        vo.setChannel("all");
                    }
                }
                try {
                    temp = jsonObject.get("price_band").getAsJsonObject().get("price_band_value").getAsJsonObject().get("value").getAsJsonObject().get("value_str").getAsString();
                    vo.setPriceBand(temp);
                } catch (Exception e) {
                    vo.setPriceBand("all");
                }
                vo.setRecordTime(vo1.getRecordTime());
                object1 = jsonObject.get("cate_pay_amt").getAsJsonObject().get("cate_pay_amt_index_values").getAsJsonObject();
                vo.setCatePayAmt(parserField(object1, true));
                object1 = jsonObject.get("cate_pay_amt_ratio").getAsJsonObject().get("cate_pay_amt_ratio_index_values").getAsJsonObject();
                vo.setCatePayAmtRatio(parserField(object1, false));
                object1 = jsonObject.get("cate_pay_cnt").getAsJsonObject().get("cate_pay_cnt_index_values").getAsJsonObject();
                vo.setCatePayCnt(parserField(object1, false));
                object1 = jsonObject.get("cate_pay_combo_cnt").getAsJsonObject().get("cate_pay_combo_cnt_index_values").getAsJsonObject();
                vo.setCatePayComboCnt(parserField(object1, false));
                object1 = jsonObject.get("cate_pay_product_cnt").getAsJsonObject().get("cate_pay_product_cnt_index_values").getAsJsonObject();
                vo.setCatePayProductCnt(parserField(object1, false));
                object1 = jsonObject.get("cate_per_pay_combo_cnt").getAsJsonObject().get("cate_per_pay_combo_cnt_index_values").getAsJsonObject();
                vo.setCatePerPayComboCnt(parserField(object1, true));
                object1 = jsonObject.get("cate_per_pay_ucnt").getAsJsonObject().get("cate_per_pay_ucnt_index_values").getAsJsonObject();
                vo.setCatePerPayUcnt(parserField(object1, true));
                object1 = jsonObject.get("cate_pay_ucnt").getAsJsonObject().get("cate_pay_ucnt_index_values").getAsJsonObject();
                vo.setCatePayUcnt(parserField(object1, false));
                marketCategoryDOList.add(vo);
            }
    }

    public String parserField(JsonObject jsonObject, boolean flag) throws Exception {
        //flas =特殊处理
        String s = "";
        String trend = "";
        JsonObject object1 = jsonObject.get("index_values").getAsJsonObject().get("extra_value").getAsJsonObject();
        try {
            if (!flag)
                s = object1.get("lower_range").getAsJsonObject().get("value").getAsString() + "-" + object1.get("upper_range").getAsJsonObject().get("value").getAsString();
            else {
                s = String.valueOf(object1.get("lower_range").getAsJsonObject().get("value").getAsLong() / 100);
                s = s + "-" + object1.get("upper_range").getAsJsonObject().get("value").getAsLong() / 100;
            }
            //trend;对比上周期
            try {
                trend = jsonObject.get("index_values").getAsJsonObject().get("out_period_ratio").getAsJsonObject().get("value").getAsString();
                trend = trend.substring(0, 6);
            } catch (Exception ex) {

            }
            s = s + "&" + trend;

        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }

        return s;
    }
}
