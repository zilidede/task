package com.zl.dao.generate;

import com.zl.config.Config;
import com.zl.dao.DaoService;
import com.zl.dao.ErrorMsg;
import com.zl.utils.jdbc.hikariCP.ConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Param:
 * @Auther: zl
 * @Date: 2025-06-07
 */
public class CityWeatherDao implements DaoService<CityWeatherDO> {
    private static final int QueryTimeout = 30;
    private final ErrorMsg errorMsg;

    private final String tableName = "city_weather";

    public CityWeatherDao() throws SQLException {
        errorMsg = new ErrorMsg();

    }

    public ErrorMsg getErrorMsg() {
        return errorMsg;
    }

    @Override
    public void doInsert(CityWeatherDO vo) throws SQLException {

        String sql = "insert into city_weather(precipitation,city_name,last_update,wind_scale,temperature,humidity,wind_direction_degree,wind_speed,feelst,wind_direction,pressure,city_id)values (?,?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = ConnectionPool.getConnection(); PreparedStatement pStmt = conn.prepareStatement(sql)) {
            pStmt.setDouble(1, vo.getPrecipitation());
            pStmt.setString(2, vo.getCityName());
            Timestamp ts = new Timestamp(vo.getLastUpdate().getTime());
            pStmt.setTimestamp(3, ts);
            pStmt.setString(4, vo.getWindScale());
            pStmt.setDouble(5, vo.getTemperature());
            pStmt.setDouble(6, vo.getHumidity());
            pStmt.setDouble(7, vo.getWindDirectionDegree());
            pStmt.setDouble(8, vo.getWindSpeed());
            pStmt.setDouble(9, vo.getFeelst());
            pStmt.setString(10, vo.getWindDirection());
            pStmt.setDouble(11, vo.getPressure());
            pStmt.setString(12, vo.getCityId());
            if (pStmt.execute())
                errorMsg.setCode(0);
            else {
                errorMsg.setCode(Config.FAIL_INSERT);
                errorMsg.setMsg(pStmt.toString());
            }
        } catch (SQLException e) {
            // 异常处理
        }

    }

    public void doUpdate(CityWeatherDO vo) throws SQLException {
        String sql = "update  city_weather SET  precipitation=? , city_name=? , last_update=? , wind_scale=? , temperature=? , humidity=? , wind_direction_degree=? , wind_speed=? , feelst=? , wind_direction=? , pressure=? , city_id=?WHERE last_update=?";
        try (Connection conn = ConnectionPool.getConnection(); PreparedStatement pStmt = conn.prepareStatement(sql)) {
            pStmt.setDouble(1, vo.getPrecipitation());
            pStmt.setString(2, vo.getCityName());
            Timestamp ts = new Timestamp(vo.getLastUpdate().getTime());
            pStmt.setTimestamp(3, ts);
            pStmt.setString(4, vo.getWindScale());
            pStmt.setDouble(5, vo.getTemperature());
            pStmt.setDouble(6, vo.getHumidity());
            pStmt.setDouble(7, vo.getWindDirectionDegree());
            pStmt.setDouble(8, vo.getWindSpeed());
            pStmt.setDouble(9, vo.getFeelst());
            pStmt.setString(10, vo.getWindDirection());
            pStmt.setDouble(11, vo.getPressure());
            pStmt.setString(12, vo.getCityId());
            ts = new Timestamp(vo.getLastUpdate().getTime());
            pStmt.setTimestamp(13, ts);
            if (pStmt.execute())
                errorMsg.setCode(0);
            else {
                errorMsg.setCode(Config.FAIL_INSERT);
                errorMsg.setMsg(pStmt.toString());
            }
        } catch (SQLException e) {
            // 异常处理
        }

    }

    @Override
    public void doDelete(CityWeatherDO vo) throws SQLException {
        String sql = String.format("DELETE FROM  city_weather WHERE last_update=?", vo.getLastUpdate());
        try (Connection conn = ConnectionPool.getConnection(); PreparedStatement pStmt = conn.prepareStatement(sql)) {
            if (pStmt.execute())
                errorMsg.setCode(0);
            else {
                errorMsg.setCode(Config.FAIL_INSERT);
                errorMsg.setMsg(pStmt.toString());
            }
        } catch (SQLException e) {
            // 异常处理
        }

    }

    @Override
    public void doBatch(List<CityWeatherDO> t) throws SQLException {

    }


    public List<String> findCityIds() throws SQLException {
        List<String> list = new ArrayList<>();
        String sql = "select distinct city_id from city_weather";
        try (Connection conn = ConnectionPool.getConnection(); PreparedStatement pStmt = conn.prepareStatement(sql)) {
            ResultSet set = pStmt.executeQuery();
            while (set.next()) {
                list.add(set.getString("city_id"));
            }
        } catch (SQLException e) {
            // 异常处理
        }
        return list;
    }
    public List<String> findCityNames() throws SQLException {
        List<String> list = new ArrayList<>();
        String sql = "select distinct city_name from city_weather";
        try (Connection conn = ConnectionPool.getConnection(); PreparedStatement pStmt = conn.prepareStatement(sql)) {
            ResultSet set = pStmt.executeQuery();
            while (set.next()) {
                list.add(set.getString("city_name"));
            }
        } catch (SQLException e) {
            // 异常处理
        }
        return list;
    }
}
