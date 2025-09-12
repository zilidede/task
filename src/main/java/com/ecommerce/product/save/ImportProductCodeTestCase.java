package com.ecommerce.product.save;

import com.ecommerce.product.main.ImportProductSpu;

import java.sql.SQLException;

/**
 *  导入商品信息测试用例
 * @author zl
 *
 */

public class ImportProductCodeTestCase {
    public static void main(String[] args) throws Exception {
        ImportProductCode importProductCode = new ImportProductCode();
        importProductCode.readExcel("./data/productContent/商品资料/");
        importProductCode.saveToDB();
    }
}
