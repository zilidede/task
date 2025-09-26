package com.zl.task.save.parser.market;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zl.dao.generate.MarketCategoryGoodsDO;
import com.zl.dao.generate.MarketCategoryGoodsDao;
import com.zl.task.impl.SaveServiceImpl;
import com.zl.task.save.parser.ParserJsonToHttpVO;
import com.zl.task.vo.http.HttpVO;
import com.zl.utils.io.DiskIoUtils;
import com.util.jdbc.generator.jdbc.DefaultDatabaseConnect;
import com.zl.utils.unicode.UnicodeToChinese;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//
//日志：
/*
2024-08-23 ：解析MarketCategory json 数据，插入数据库
json 来源： market任务 ：xhr=compassApiShopProductProductChanceMarketCategoryOverviewPriceAnalysisProduct
 */
public class SaveMarketCategoryGoods {
    private final SaveServiceImpl saveService = new SaveServiceImpl();
    private final MarketCategoryGoodsDao daoService = new MarketCategoryGoodsDao(DefaultDatabaseConnect.getConn());
    private final List<MarketCategoryGoodsDO> marketCategoryDOList = new ArrayList<>();

    public SaveMarketCategoryGoods() throws SQLException {
    }

    public static void main(String[] args) throws Exception {
        SaveMarketCategoryGoods saver = new SaveMarketCategoryGoods();
        saver.save("");
    }

    public void save(String sdir) throws Exception {
        String dir = sdir + "\\compassApiShopProductProductChanceMarketCategoryOverviewPriceAnalysisProduct";
        List<String> files = DiskIoUtils.getFileListFromDir(dir);
        for (String file : files) {
            HttpVO httpVO = ParserJsonToHttpVO.parserXHRJson(file);
            String url = httpVO.getUrl();
            String json = httpVO.getResponse().getBody();
            parserJson(json, parserUrl(url));
        }

        saveService.savePgSql(daoService, marketCategoryDOList);
    }

    public MarketCategoryGoodsDO parserUrl(String url) throws UnsupportedEncodingException {
        // 时间 -，catagoryId, -价格域
        String decodedString = "";
        try {
            decodedString = URLDecoder.decode(url, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        String[] strings = decodedString.split("\\&");
        String begin = strings[0].substring(strings[0].indexOf("begin_date=") + 11);
        String end = begin + "-" + strings[1].substring(strings[1].indexOf("end_date=") + 9);
        String id = strings[5].substring(strings[5].indexOf("category_id=") + 12);
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
        MarketCategoryGoodsDO vo = new MarketCategoryGoodsDO();
        vo.setPriceBand(price);
        vo.setRecordTime(end);
        vo.setId(Integer.parseInt(id));
        return vo;
    }

    public void parserJson(String json, MarketCategoryGoodsDO marketCategoryGoodsDO) {
        JsonParser parser = new JsonParser();
        JsonObject object = null;
        JsonArray jsonArray = null;
        try {
            object = parser.parse(json).getAsJsonObject();
            jsonArray = object.get("data").getAsJsonArray();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        if (jsonArray.size() < 1)
            return;
        for (int i = 0; i < jsonArray.size(); i++) {
            MarketCategoryGoodsDO vo = new MarketCategoryGoodsDO();
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject().get("cell_info").getAsJsonObject();
            String s = String.valueOf(jsonObject.getAsJsonObject().get("cate_pay_amt").
                    getAsJsonObject().get("cate_pay_amt_index_values").
                    getAsJsonObject().get("index_values").
                    getAsJsonObject().get("extra_value")
                    .getAsJsonObject().get("lower_range")
                    .getAsJsonObject().get("value")
                    .getAsInt() / 100);
            s = s + "-" + jsonObject.getAsJsonObject().get("cate_pay_amt").
                    getAsJsonObject().get("cate_pay_amt_index_values").
                    getAsJsonObject().get("index_values").
                    getAsJsonObject().get("extra_value")
                    .getAsJsonObject().get("upper_range")
                    .getAsJsonObject().get("value")
                    .getAsInt() / 100;
            vo.setGoodsSales(s);

            vo.setGoodsId(jsonObject.getAsJsonObject().get("product_info").
                    getAsJsonObject().get("product_id_value").getAsJsonObject().get("value").
                    getAsJsonObject().get("value_str").getAsLong());
            vo.setGoodsName(jsonObject.getAsJsonObject().get("product_info").
                    getAsJsonObject().get("product_name_value").
                    getAsJsonObject().get("value").
                    getAsJsonObject().get("value_str").getAsString());
            vo.setId(marketCategoryGoodsDO.getId());
            vo.setRecordTime(marketCategoryGoodsDO.getRecordTime());
            vo.setPriceBand(marketCategoryGoodsDO.getPriceBand());
            marketCategoryDOList.add(vo);
        }
    }
}
