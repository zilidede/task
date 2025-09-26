package com.zl.task.save.parser.category;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zl.dao.generate.CategoryTypeDO;
import com.zl.dao.generate.CategoryTypeDao;
import com.zl.task.impl.SaveServiceImpl;
import com.zl.task.save.base.SaveXHRImpl;
import com.zl.task.save.parser.ParserJsonToHttpVO;
import com.zl.task.vo.http.HttpVO;
import com.zl.utils.io.DiskIoUtils;
import com.util.jdbc.generator.jdbc.DefaultDatabaseConnect;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SaveCategoryType extends SaveXHRImpl<CategoryTypeDO> {
    private final CategoryTypeDao daoService = new CategoryTypeDao(DefaultDatabaseConnect.getConn());
    private final List<CategoryTypeDO> list = new ArrayList<>();
    private final SaveServiceImpl saveService = new SaveServiceImpl();

    public SaveCategoryType() throws SQLException {
    }

    public static void main(String[] args) throws Exception {
        SaveCategoryType saver = new SaveCategoryType();
        saver.save("S:\\data\\task\\爬虫\\childCategory");
    }

    @Override
    public void save(String sDir) throws Exception {
        String dir = sDir + "\\";
        List<String> files = DiskIoUtils.getFileListFromDir(dir);
        for (String file : files) {
            HttpVO httpVO = ParserJsonToHttpVO.parserXHRJson(file);
            String url = httpVO.getUrl();
            String json = httpVO.getResponse().getBody();
            parserJson(json, httpVO);
        }

        saveService.savePgSql(daoService, list);
        list.clear();
    }

    @Override
    public List<CategoryTypeDO> parserJson(String json, HttpVO vo) throws UnsupportedEncodingException {
        JsonParser parser = new JsonParser();
        JsonObject object = null;
        JsonArray jsonArray = null;

        String url = vo.getUrl();
        String decodedURL = "";
        try {
            decodedURL = URLDecoder.decode(vo.getUrl(), StandardCharsets.UTF_8);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        String industry = decodedURL.substring(decodedURL.indexOf("industry_id=") + 12, decodedURL.indexOf("&down_to_category_level"));
        CategoryTypeDO vos = new CategoryTypeDO();
        vos.setId(Integer.parseInt(industry));
        vos.setLevel(0);
        list.add(vos);
        try {
            object = parser.parse(json).getAsJsonObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        jsonArray = object.get("data").getAsJsonObject().get("child_category_list").getAsJsonArray();
        for (int i = 0; i < jsonArray.size(); i++) {
            //level1
            CategoryTypeDO vo1 = new CategoryTypeDO();
            vo1.setId(jsonArray.get(i).getAsJsonObject().get("category_id").getAsInt());
            vo1.setIndustryName(industry);
            vo1.setLevel(jsonArray.get(i).getAsJsonObject().get("category_level").getAsInt());
            vo1.setIndustryFirstCategoryName(jsonArray.get(i).getAsJsonObject().get("category_name").getAsString());
            list.add(vo1);
            //level2
            JsonArray jsonArray2 = jsonArray.get(i).getAsJsonObject().get("child_category_list").getAsJsonArray();
            for (int j = 0; j < jsonArray2.size(); j++) {
                Integer categoryId = jsonArray2.get(j).getAsJsonObject().get("category_id").getAsInt();
                if (categoryId == 0) {
                    continue;
                }
                CategoryTypeDO vo2 = new CategoryTypeDO();
                vo2.setId(categoryId);
                vo2.setIndustryName(industry);
                vo2.setIndustryFirstCategoryName(vo1.getIndustryFirstCategoryName());
                vo2.setLevel(jsonArray2.get(j).getAsJsonObject().get("category_level").getAsInt());
                vo2.setIndustrySecondCategoryName(jsonArray2.get(j).getAsJsonObject().get("category_name").getAsString());
                list.add(vo2);
                //level3
                JsonArray jsonArray3 = jsonArray2.get(j).getAsJsonObject().get("child_category_list").getAsJsonArray();
                for (int k = 0; k < jsonArray3.size(); k++) {
                    categoryId = jsonArray3.get(k).getAsJsonObject().get("category_id").getAsInt();
                    if (categoryId == 0) {
                        continue;
                    }
                    CategoryTypeDO vo3 = new CategoryTypeDO();
                    vo3.setId(categoryId);
                    vo3.setIndustryName(industry);
                    vo3.setIndustryFirstCategoryName(vo1.getIndustryFirstCategoryName());
                    vo3.setIndustrySecondCategoryName(vo2.getIndustrySecondCategoryName());
                    vo3.setIndustryThreeCategoryName(jsonArray3.get(k).getAsJsonObject().get("category_name").getAsString());
                    vo3.setLevel(jsonArray3.get(k).getAsJsonObject().get("category_level").getAsInt());
                    list.add(vo3);
                    //level4
                    JsonArray jsonArray4 = jsonArray3.get(k).getAsJsonObject().get("child_category_list").getAsJsonArray();
                    for (int l = 0; l < jsonArray4.size(); l++) {
                        categoryId = jsonArray4.get(l).getAsJsonObject().get("category_id").getAsInt();
                        if (categoryId == 0) {
                            continue;
                        }
                        CategoryTypeDO vo4 = new CategoryTypeDO();
                        vo4.setId(categoryId);
                        vo4.setIndustryName(industry);
                        vo4.setIndustryFirstCategoryName(vo1.getIndustryFirstCategoryName());
                        vo4.setIndustrySecondCategoryName(vo2.getIndustrySecondCategoryName());
                        vo4.setIndustryThreeCategoryName(vo3.getIndustryThreeCategoryName());
                        vo4.setIndustryFourCategoryName(jsonArray4.get(l).getAsJsonObject().get("category_name").getAsString());
                        vo4.setLevel(jsonArray4.get(l).getAsJsonObject().get("category_level").getAsInt());
                        list.add(vo4);
                    }
                }

            }

        }
        return super.parserJson(json, vo);
    }
}
