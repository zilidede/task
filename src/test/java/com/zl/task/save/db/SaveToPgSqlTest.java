package com.zl.task.save.db;

import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

public class SaveToPgSqlTest {


    @Test
    public void cycleSave() throws Exception {

        SaveToPgSql.cycleSave(3);
    }

    @Test
    public void saveCityWeatherToPgSql() throws Exception {
        SaveToPgSql.saveCityWeatherToPgSql("D:\\data\\back\\day\\2025-06-26\\");
    }

    @Test
    public void saveOceanEngineKeyWordsToPgSql() throws SQLException, IOException, ParseException {

    }


    @org.junit.jupiter.api.Test
    void testSaveCityWeatherToPgSql() {
    }
}