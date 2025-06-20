package com.zl.dao.generate;

import com.zl.config.Config;
import com.zl.dao.DaoService;
import com.zl.dao.ErrorMsg;

import java.sql.*;
import java.util.List;

/**
 * @Description:
 * @Param:
 * @Auther: zl
 * @Date: 2025-02-11
 */
public class LiveRecordDao implements DaoService<LiveRecordDO> {
    private static final int QueryTimeout = 30;
    private final ErrorMsg errorMsg;
    private final Connection conn;
    private Statement stmt = null;
    private PreparedStatement pStmt = null;
    private final String tableName = "live_record";

    public LiveRecordDao(Connection connection) throws SQLException {
        errorMsg = new ErrorMsg();
        this.conn = connection;
        stmt = conn.createStatement();
        stmt.setQueryTimeout(QueryTimeout);  // set timeout to 30 sec;
    }

    public ErrorMsg getErrorMsg() {
        return errorMsg;
    }

    @Override
    public void doInsert(LiveRecordDO vo) throws SQLException {
        String sql = "insert into live_record(cover_url,live_traffic_map,live_time,transaction_num,exposed_num,title,avg_user_duration,room_id,sales,platform,user_num,start_live,gmv,uv_val,total_user,user_count,fans_inc,categories,preview_pic)values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        pStmt = conn.prepareStatement(sql);
        pStmt.setString(1, vo.getCoverUrl());
        pStmt.setString(2, vo.getLiveTrafficMap());
        pStmt.setInt(3, vo.getLiveTime());
        pStmt.setInt(4, vo.getTransactionNum());
        pStmt.setInt(5, vo.getExposedNum());
        pStmt.setString(6, vo.getTitle());
        pStmt.setInt(7, vo.getAvgUserDuration());
        pStmt.setLong(8, vo.getRoomId());
        pStmt.setInt(9, vo.getSales());
        pStmt.setString(10, vo.getPlatform());
        pStmt.setInt(11, vo.getUserNum());
        Timestamp ts = new Timestamp(vo.getStartLive().getTime());
        ts = new Timestamp(vo.getStartLive().getTime());
        pStmt.setTimestamp(12, ts);
        pStmt.setDouble(13, vo.getGmv());
        pStmt.setDouble(14, vo.getUvVal());
        pStmt.setInt(15, vo.getTotalUser());
        pStmt.setInt(16, vo.getUserCount());
        pStmt.setInt(17, vo.getFansInc());
        pStmt.setString(18, vo.getCategories());
        pStmt.setString(19, vo.getPreviewPic());
        if (pStmt.execute())
            errorMsg.setCode(0);
        else {
            errorMsg.setCode(Config.FAIL_INSERT);
            errorMsg.setMsg(pStmt.toString());
        }
    }

    public void doUpdate(LiveRecordDO vo) throws SQLException {
        String sql = "update  live_record SET  cover_url=? , live_traffic_map=? , live_time=? , transaction_num=? , exposed_num=? , title=? , avg_user_duration=? , roomId=? , sales=? , platform=? , user_num=? , start_live=? , gmv=? , uv_val=? , total_user=? , user_count=? , fans_inc=? , categories=? , preview_pic=?WHERE roomId=?";
        pStmt = conn.prepareStatement(sql);
        pStmt.setString(1, vo.getCoverUrl());
        pStmt.setString(2, vo.getLiveTrafficMap());

        pStmt.setInt(3, vo.getLiveTime());
        pStmt.setInt(4, vo.getTransactionNum());
        pStmt.setInt(5, vo.getExposedNum());
        pStmt.setString(6, vo.getTitle());
        pStmt.setInt(7, vo.getAvgUserDuration());
        pStmt.setLong(8, vo.getRoomId());
        pStmt.setInt(9, vo.getSales());
        pStmt.setString(10, vo.getPlatform());
        pStmt.setInt(11, vo.getUserNum());

        Timestamp ts = new Timestamp(vo.getStartLive().getTime());
        pStmt.setTimestamp(12, ts);
        pStmt.setDouble(13, vo.getGmv());
        pStmt.setDouble(14, vo.getUvVal());
        pStmt.setInt(15, vo.getTotalUser());
        pStmt.setInt(16, vo.getUserCount());
        pStmt.setInt(17, vo.getFansInc());
        pStmt.setString(18, vo.getCategories());
        pStmt.setString(19, vo.getPreviewPic());
        pStmt.setLong(20, vo.getRoomId());
        if (pStmt.execute())
            errorMsg.setCode(0);
        else {
            errorMsg.setCode(Config.FAIL_INSERT);
            errorMsg.setMsg(pStmt.toString());
        }
    }

    @Override
    public void doDelete(LiveRecordDO vo) throws SQLException {
        String sql = String.format("DELETE FROM  live_record WHERE roomId=?", vo.getRoomId());
        pStmt = conn.prepareStatement(sql);
        if (pStmt.execute())
            errorMsg.setCode(0);
        else {
            errorMsg.setCode(Config.FAIL_INSERT);
            errorMsg.setMsg(pStmt.toString());
        }
    }

    @Override
    public void doBatch(List<LiveRecordDO> t) throws SQLException {

    }
}
