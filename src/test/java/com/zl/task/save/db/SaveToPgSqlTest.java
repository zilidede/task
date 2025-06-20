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
        SaveToPgSql.saveCityWeatherToPgSql("S:\\data\\task\\爬虫\\weather\\");
    }

    @Test
    public void saveOceanEngineKeyWordsToPgSql() throws SQLException, IOException, ParseException {

    }


}