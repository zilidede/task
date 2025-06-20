package com.zl.dao.generate;

import com.zl.utils.jdbc.generator.jdbc.DefaultDatabaseConnect;
import org.junit.Test;

import java.sql.SQLException;

public class EcommerceOrderDaoTest {

    @Test
    public void getUnUserOrderIds() {
        try {
            EcommerceOrderDao dao = new EcommerceOrderDao(DefaultDatabaseConnect.getConn());
            // dao.getUnUserOrderIds("2019-08-01");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}