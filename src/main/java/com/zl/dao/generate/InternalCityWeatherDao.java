package com.zl.dao.generate;


import com.zl.dao.DaoService;
import com.zl.dao.ErrorMsg;

import java.sql.*;
import java.util.List;

/**
 * @className: com.craw.nd.dao.generate-> InternalCityWeatherDao
 * @description:
 * @author: zl
 * @createDate: 2023-02-08 15:23
 * @version: 1.0
 * @todo:
 */
public class InternalCityWeatherDao implements DaoService<InternalCityWeatherDO> {
    private static final int QueryTimeout = 30;
    private final Connection conn;
    private Statement stmt = null;
    private PreparedStatement pStmt = null;
    private final ErrorMsg errorMsg;

    public InternalCityWeatherDao(Connection connection) throws SQLException {
        errorMsg = new ErrorMsg();
        this.conn = connection;
        stmt = conn.createStatement();
        stmt.setQueryTimeout(QueryTimeout);  // set timeout to 30 sec;

    }

    public ErrorMsg getErrorMsg() {
        return errorMsg;
    }

    @Override
    public void doInsert(InternalCityWeatherDO internalCityWeatherDO) throws SQLException {
        String sql = "insert into internal_city_weather(city_name,day_meteorological,max_air_temperature,day_wind,night_meteorological,night_wind,record_time,min_air_temperature)values (?,?,?,?,?,?,?,?)";
        pStmt = conn.prepareStatement(sql);
        pStmt.setString(1, internalCityWeatherDO.getCityName());
        pStmt.setString(2, internalCityWeatherDO.getDayMeteorological());
        pStmt.setInt(3, internalCityWeatherDO.getMaxAirTemperature());
        pStmt.setString(4, internalCityWeatherDO.getDayWind());
        pStmt.setString(5, internalCityWeatherDO.getNightMeteorological());
        pStmt.setString(6, internalCityWeatherDO.getNightWind());
        Timestamp ts = new Timestamp(internalCityWeatherDO.getRecordTime().getTime());
        pStmt.setTimestamp(7, ts);
        pStmt.setInt(8, internalCityWeatherDO.getMinAirTemperature());
        if (pStmt.execute())
            errorMsg.setCode(0);
        else {

            errorMsg.setMsg(pStmt.toString());
        }

    }

    @Override
    public void doUpdate(InternalCityWeatherDO internalCityWeatherDO) throws SQLException {
        String sql = "insert into internal_city_weather(city_name,day_meteorological,max_air_temperature,day_wind,night_meteorological,night_wind,record_time,min_air_temperature)values (?,?,?,?,?,?,?,?)" +
                "ON conflict(record_time,city_name)" +
                "DO UPDATE SET day_meteorological=?, night_meteorological=?,day_wind=?, night_wind=?,max_air_temperature=?, min_air_temperature=? ";
        pStmt = conn.prepareStatement(sql);
        pStmt.setString(1, internalCityWeatherDO.getCityName());
        pStmt.setString(2, internalCityWeatherDO.getDayMeteorological());
        pStmt.setInt(3, internalCityWeatherDO.getMaxAirTemperature());
        pStmt.setString(4, internalCityWeatherDO.getDayWind());
        pStmt.setString(5, internalCityWeatherDO.getNightMeteorological());
        pStmt.setString(6, internalCityWeatherDO.getNightWind());
        Timestamp ts = new Timestamp(internalCityWeatherDO.getRecordTime().getTime());
        pStmt.setTimestamp(7, ts);
        pStmt.setInt(8, internalCityWeatherDO.getMinAirTemperature());
        pStmt.setString(9, internalCityWeatherDO.getDayMeteorological());
        pStmt.setString(10, internalCityWeatherDO.getNightMeteorological());
        pStmt.setString(11, internalCityWeatherDO.getDayWind());
        pStmt.setString(12, internalCityWeatherDO.getNightWind());
        pStmt.setInt(13, internalCityWeatherDO.getMaxAirTemperature());
        pStmt.setInt(14, internalCityWeatherDO.getMinAirTemperature());
        if (pStmt.execute())
            errorMsg.setCode(0);
        else {

            errorMsg.setMsg(pStmt.toString());
        }
    }

    @Override
    public void doDelete(InternalCityWeatherDO internalCityWeatherDO) {

    }

    @Override
    public void doBatch(List<InternalCityWeatherDO> t) throws SQLException {

    }

    //handle
    public Integer findDateCityWeathers(String date) throws SQLException {
        Integer count = -1;
        String sql = String.format("select count(city_name) from internal_city_weather where record_time='%s'", date);
        pStmt = conn.prepareStatement(sql);
        ResultSet set = pStmt.executeQuery();
        if (set.next()) {
            count = set.getInt("count");
        }
        return count;
    }
}
