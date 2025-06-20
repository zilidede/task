package com.zl.dao.generate;

import com.zl.config.Config;
import com.zl.dao.DaoService;
import com.zl.dao.ErrorMsg;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Param:
 * @Auther: zl
 * @Date: 2025-02-21
 */
public class HourLiveRankDao implements DaoService<HourLiveRankDO> {
    private static final int QueryTimeout = 30;
    private final ErrorMsg errorMsg;
    private final Connection conn;
    private Statement stmt = null;
    private PreparedStatement pStmt = null;
    private final String tableName = "hour_live_rank";

    public HourLiveRankDao(Connection connection) throws SQLException {
        errorMsg = new ErrorMsg();
        this.conn = connection;
        stmt = conn.createStatement();
        stmt.setQueryTimeout(QueryTimeout);  // set timeout to 30 sec;
    }

    public ErrorMsg getErrorMsg() {
        return errorMsg;
    }

    @Override
    public void doInsert(HourLiveRankDO vo) throws SQLException {
        String sql = "insert into hour_live_rank(live_start_time,room_id,pay_amt,product_click_cnt,end_time,room_title,start_time,watch_cnt,category_id,fans_ucnt_p1d,live_duration,nickname,author_id)values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        pStmt = conn.prepareStatement(sql);
        Timestamp ts = new Timestamp(vo.getLiveStartTime().getTime());
        pStmt.setTimestamp(1, ts);
        pStmt.setLong(2, vo.getRoomId());
        pStmt.setDouble(3, vo.getPayAmt());
        pStmt.setInt(4, vo.getProductClickCnt());
        ts = new Timestamp(vo.getEndTime().getTime());
        pStmt.setTimestamp(5, ts);
        pStmt.setString(6, vo.getRoomTitle());
        ts = new Timestamp(vo.getStartTime().getTime());
        pStmt.setTimestamp(7, ts);
        pStmt.setInt(8, vo.getWatchCnt());
        pStmt.setInt(9, vo.getCategoryId());
        pStmt.setInt(10, vo.getFansUcntP1d());
        pStmt.setInt(11, vo.getLiveDuration());
        pStmt.setString(12, vo.getNickname());
        pStmt.setLong(13, vo.getAuthorId());
        if (pStmt.execute())
            errorMsg.setCode(0);
        else {
            errorMsg.setCode(Config.FAIL_INSERT);
            errorMsg.setMsg(pStmt.toString());
        }
    }

    public void doUpdate(HourLiveRankDO vo) throws SQLException {
        String sql = "update  hour_live_rank SET  live_start_time=? , room_id=? , pay_amt=? , product_click_cnt=? , end_time=? , room_title=? , start_time=? , watch_cnt=? , category_id=? , fans_ucnt_p1d=? , live_duration=? , nickname=? , author_id=?WHERE start_time=?";
        pStmt = conn.prepareStatement(sql);
        Timestamp ts = new Timestamp(vo.getLiveStartTime().getTime());
        pStmt.setTimestamp(1, ts);
        pStmt.setLong(2, vo.getRoomId());
        pStmt.setDouble(3, vo.getPayAmt());
        pStmt.setInt(4, vo.getProductClickCnt());
        ts = new Timestamp(vo.getEndTime().getTime());
        pStmt.setTimestamp(5, ts);
        pStmt.setString(6, vo.getRoomTitle());
        ts = new Timestamp(vo.getStartTime().getTime());
        pStmt.setTimestamp(7, ts);
        pStmt.setInt(8, vo.getWatchCnt());
        pStmt.setInt(9, vo.getCategoryId());
        pStmt.setInt(10, vo.getFansUcntP1d());
        pStmt.setInt(11, vo.getLiveDuration());
        pStmt.setString(12, vo.getNickname());
        pStmt.setLong(13, vo.getAuthorId());
        ts = new Timestamp(vo.getStartTime().getTime());
        pStmt.setTimestamp(14, ts);
        if (pStmt.execute())
            errorMsg.setCode(0);
        else {
            errorMsg.setCode(Config.FAIL_INSERT);
            errorMsg.setMsg(pStmt.toString());
        }
    }

    @Override
    public void doDelete(HourLiveRankDO vo) throws SQLException {
        String sql = String.format("DELETE FROM  hour_live_rank WHERE start_time=?", vo.getStartTime());
        pStmt = conn.prepareStatement(sql);
        if (pStmt.execute())
            errorMsg.setCode(0);
        else {
            errorMsg.setCode(Config.FAIL_INSERT);
            errorMsg.setMsg(pStmt.toString());
        }
    }

    @Override
    public void doBatch(List<HourLiveRankDO> t) throws SQLException {

    }

    //handle
    public List<java.util.Date> findDateHourLiveRank(Integer categoryId, String startDate, String endDate) throws SQLException {
        //获取日期没有被爬取的小时点
        Integer count = -1;
        String sql = "select DISTINCT start_time from hour_live_rank where category_id=? and start_time>=? ::timestamp with time zone and end_time<? ::timestamp with time zone ORDER BY start_time desc";
        List<java.util.Date> dates = new ArrayList<>();
        pStmt = conn.prepareStatement(sql);
        pStmt.setInt(1, categoryId);
        pStmt.setString(2, startDate);
        pStmt.setString(3, endDate);
        ResultSet set = pStmt.executeQuery();
        while (set.next()) {
            dates.add(new java.util.Date(set.getTimestamp("start_time").getTime()));
        }
        return dates;
    }

    /**
     * 根据类别ID、开始时间、结束时间和观看次数查询小时直播排名数据。
     *
     * @param categoryId 类别ID
     * @param startDate  开始时间（格式：yyyy-MM-dd HH:mm:ss）
     * @param endDate    结束时间（格式：yyyy-MM-dd HH:mm:ss）
     * @param watchCnt   观看次数阈值
     * @return 匹配条件的小时直播排名数据列表
     * @throws SQLException 如果数据库操作发生异常
     */
    public List<HourLiveRankDO> findHourLiveRankByCriteria(Integer categoryId, String startDate, String endDate, Integer watchCnt) throws SQLException {
        Integer count = -1;
        String sql = "";
        List<HourLiveRankDO> list = new ArrayList<>();
        sql = "SELECT * FROM hour_live_rank \n" +
                "WHERE start_time >= ?::timestamp \n" +
                "AND start_time < ?::timestamp \n" +
                "AND watch_cnt > ? \n" +
                "AND (? = -1 OR category_id = ?)";

        pStmt = conn.prepareStatement(sql);
        pStmt.setString(1, startDate);
        pStmt.setString(2, endDate);
        pStmt.setInt(3, watchCnt);
        pStmt.setInt(4, categoryId);
        pStmt.setInt(5, categoryId);
        ResultSet set = pStmt.executeQuery();
        while (set.next()) {
            HourLiveRankDO hourLiveRankDO = new HourLiveRankDO();
            hourLiveRankDO.setRoomId(set.getLong("room_id"));
            hourLiveRankDO.setAuthorId(set.getLong("author_id"));
            hourLiveRankDO.setWatchCnt(set.getInt("watch_cnt"));
            hourLiveRankDO.setPayAmt(set.getDouble("pay_amt"));
            list.add(hourLiveRankDO);
        }
        return list;
    }
}
