package com.ecommerce.product.save;

import com.ecommerce.dao.generate.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.zl.task.impl.SaveService;
import com.zl.task.impl.SaveServiceImpl;
import com.zl.utils.excel.ExcelReader;
import com.zl.utils.io.DiskIoUtils;
import com.zl.utils.log.LoggerUtils;

import java.sql.SQLException;
import java.util.*;

import static com.ecommerce.product.save.SkuGenerator.generateSku;

/**
 *  将聚水潭商品码导入到数据库
 * @author zl
 *
 */
public class ImportProductCode {
    public static void main(String[] args) {

    }
    private String localProductImgPath="D:\\data\\物品库\\商品库\\";
    private List<ProductSpuDO> spuDOS;
    private List<ProductSkuDO> skuDOS;
    private List<ProductPlatformDO> platformDOS;
    private List<PlatformSkuMapDO> skuMapDOS;
    private ProductPlatformDao productPlatformDao;
    private ProductSkuDao productSkuDao;
    private ProductSpuDao productSpuDao;
    private PlatformSkuMapDao platformSkuMapDao;
    ImportProductCode() throws SQLException {
        productPlatformDao = new ProductPlatformDao();
        productSkuDao = new ProductSkuDao();
        productSpuDao = new ProductSpuDao();
        platformSkuMapDao = new PlatformSkuMapDao();
        spuDOS = new ArrayList<>();
        skuDOS = new ArrayList<>();
        platformDOS = new ArrayList<>();
        skuMapDOS = new ArrayList<>();
    }
    public  void readExcel(String path) throws Exception {

        //聚水潭-商品款式spu
        String spuPath = path+"那度-聚水潭-商品款式信息.xlsx";
        List<Map<String, String>> spuData = ExcelReader.readExcel(spuPath);
        // 创建Gson对象
        Gson gson = new Gson();
        for (Map<String, String> row : spuData){
            ProductSpuDO spuDO = new ProductSpuDO();
            spuDO.setSpuId(row.get("图片"));
            spuDO.setTitle(row.get("商品名称"));
            spuDO.setCreateTime(new Date());
            spuDO.setUpdateTime(new Date());
            spuDO.setStatus(1);
            String imgDir=localProductImgPath+spuDO.getSpuId()+"\\";
            DiskIoUtils.createDir(imgDir);
            spuDO.setLocalDataImg(imgDir);
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("mainImgUrl", row.get("主图链接"));
            // 转换为JSON字符串
            String json = gson.toJson(jsonObject);
            spuDO.setImageGallery(json);
            spuDOS.add(spuDO);
        }
        //抖音平台sku
        String platformSkuPath = path+"抖音&真维斯官方旗舰店&商品表.xlsx";
        List<Map<String, String>> platformSkuData = ExcelReader.readExcel(platformSkuPath);
        Map<Long ,Integer> map=new HashMap<>();
        for (Map<String, String> row : platformSkuData) {
            ProductPlatformDO platformDO = new ProductPlatformDO();
            platformDO.setPlatformProductId(Long.parseLong(row.get("商品ID").replace("", "")));
            String spu=row.get("商家SKU编码");
            String [] strings=spu.split("-");
            if(strings.length==1)
                platformDO.setSpuId(spu);
            else {
                String sku = row.get("商品规格");
                String skus = sku.split("，")[0].split(":")[1];
                String spuId="";
                try {
                     spuId = SkuProcessor.process(spu);
                }
                catch (Exception e) {
                    e.printStackTrace();
                    LoggerUtils.logger.error("商品ID"+platformDO.getPlatformProductId()+"商品编码错误"+spu);
                    continue;
                }

                platformDO.setSpuId(spuId);
            }
            platformDO.setPlatformName("抖音");
            platformDO.setCustomTitle(row.get("商品名称"));
            String catId=row.get("一级类目")+"&"+row.get("二级类目")+"&"+row.get("三级类目")+"&"+row.get("四级类目");
            platformDO.setPlatformCatId(catId);
            platformDO.setPlatformPrice(Double.parseDouble(row.get("商品价格")));
            platformDO.setStatus(1);
            platformDO.setLink(row.get("商品链接"));
            platformDO.setCreateTime(new Date());
            platformDO.setUpdateTime(new Date());
            platformDOS.add(platformDO);
            PlatformSkuMapDO skuMapDO = new PlatformSkuMapDO();
            skuMapDO.setSkuId(row.get("商家SKU编码"));
            skuMapDO.setPlatformName("抖音");
            skuMapDO.setPlatformSkuCode(row.get("规格ID（SKUID）"));
            skuMapDO.setCreateTime(new Date());
            skuMapDO.setUpdateTime(new Date());
            skuMapDOS.add(skuMapDO);
        }
        // 聚水潭-商品资料 sku
        //
        String skuPath = path+"那度-聚水潭-商品资料.xlsx";
        List<Map<String, String>> skuData = ExcelReader.readExcel(skuPath);
        for(Map<String, String> row : skuData){
            ProductSkuDO skuDO = new ProductSkuDO();
            String skuCode=row.get("商品编码");
            skuCode=skuCode.replace(row.get("颜色及规格"),"");
            skuCode=skuCode.replace("-","");
            String sku="";
            String spil=row.get("供应商名称");
            if(spil.equals("")){
                spil="自发";
            }
            try {
                sku = generateSku(
                        spil,  // 供应商名称
                        skuCode,  // 货号
                        row.get("颜色及规格")     //规格
                );
            }
            catch (Exception e) {
                e.printStackTrace();
                LoggerUtils.logger.error(skuCode);
                continue;
            }
            skuDO.setSkuId(sku);
            skuDO.setSupplierName(row.get("供应商名称"));
            skuDO.setCreateTime(new Date());
            skuDO.setUpdateTime(new Date());
            skuDO.setSkuImagePath(row.get("图片"));
            skuDOS.add(skuDO);
        }
        //更新sku和平台sku映射




    }
    public void convert() throws Exception {

    }
    public void saveToDB() throws SQLException {
        SaveService saveService= new SaveServiceImpl();
        saveService.savePgSql(productSpuDao, spuDOS); //保存spu
      //  saveService.savePgSql(productPlatformDao, platformDOS); //保存平台sku
      //  saveService.savePgSql(productSkuDao, skuDOS); //保存商品sku
      //  saveService.savePgSql(platformSkuMapDao, skuMapDOS); //保存唯一skuid 对应平台sku映射
      //



    }
}
