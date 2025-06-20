package com.zl.task.executor.save.order;

import com.zl.task.save.parser.order.SaveDouYinOrder;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

public class SaveDouYinOrderTest {

    @Test
    public void updateOrderUserInfo() throws SQLException, ParseException, IOException {
        SaveDouYinOrder saver = new SaveDouYinOrder();
        saver.updateOrderUserInfo("D:\\data\\爬虫\\电商\\抖音\\抖音小店\\order\\getuserbyorder\\");
    }
}