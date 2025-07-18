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
public class LiveMinRecordDao implements DaoService<LiveMinRecordDO> {
    private static final int QueryTimeout = 30;
    private final ErrorMsg errorMsg;
    private final Connection conn;
    private Statement stmt = null;
    private PreparedStatement pStmt = null;
    private final String tableName = "live_min_record";

    public LiveMinRecordDao(Connection connection) throws SQLException {
        errorMsg = new ErrorMsg();
        this.conn = connection;
        stmt = conn.createStatement();
        stmt.setQueryTimeout(QueryTimeout);  // set timeout to 30 sec;
    }

    public ErrorMsg getErrorMsg() {
        return errorMsg;
    }

    @Override
    public void doInsert(LiveMinRecordDO vo) throws SQLException {
        String sql = "insert into live_min_record(pay_ucnt,click_transaction_rate,pay_amt,gpm,plaform,pay_cnt,watch_ucnt,leave_ucnt,author_id,room_id,comment_cnt,fans_inc,entry_rate,online_user_cnt,viewing_click_rate,record_time)values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        pStmt = conn.prepareStatement(sql);
        pStmt.setInt(1, vo.getPayUcnt());
        pStmt.setDouble(2, vo.getClickTransactionRate());
        pStmt.setDouble(3, vo.getPayAmt());
        pStmt.setDouble(4, vo.getGpm());
        pStmt.setString(5, vo.getPlaform());
        pStmt.setInt(6, vo.getPayCnt());
        pStmt.setInt(7, vo.getWatchUcnt());
        pStmt.setInt(8, vo.getLeaveUcnt());
        pStmt.setLong(9, vo.getAuthorId());
        pStmt.setLong(10, vo.getRoomId());
        pStmt.setInt(11, vo.getCommentCnt());
        pStmt.setInt(12, vo.getFansInc());
        pStmt.setDouble(13, vo.getEntryRate());
        pStmt.setInt(14, vo.getOnlineUserCnt());
        pStmt.setString(15, vo.getViewingClickRate());
        Timestamp ts = new Timestamp(vo.getRecordTime().getTime());
        pStmt.setTimestamp(16, ts);
        if (pStmt.execute())
            errorMsg.setCode(0);
        else {
            errorMsg.setCode(Config.FAIL_INSERT);
            errorMsg.setMsg(pStmt.toString());
        }
    }

    public void doUpdate(LiveMinRecordDO vo) throws SQLException {
        String sql = "update  live_min_record SET  pay_ucnt=? , click_transaction_rate=? , pay_amt=? , gpm=? , plaform=? , pay_cnt=? , watch_ucnt=? , leave_ucnt=? , authorId=? , roomId=? , comment_cnt=? , fans_inc=? , entry_rate=? , online_user_cnt=? , viewing_click_rate=? , record_time=?WHERE roomId=?";
        pStmt = conn.prepareStatement(sql);
        pStmt.setInt(1, vo.getPayUcnt());
        pStmt.setDouble(2, vo.getClickTransactionRate());
        pStmt.setDouble(3, vo.getPayAmt());
        pStmt.setDouble(4, vo.getGpm());
        pStmt.setString(5, vo.getPlaform());
        pStmt.setInt(6, vo.getPayCnt());
        pStmt.setInt(7, vo.getWatchUcnt());
        pStmt.setInt(8, vo.getLeaveUcnt());
        pStmt.setLong(9, vo.getAuthorId());
        pStmt.setLong(10, vo.getRoomId());
        pStmt.setInt(11, vo.getCommentCnt());
        pStmt.setInt(12, vo.getFansInc());
        pStmt.setDouble(13, vo.getEntryRate());
        pStmt.setInt(14, vo.getOnlineUserCnt());
        pStmt.setString(15, vo.getViewingClickRate());
        Timestamp ts = new Timestamp(vo.getRecordTime().getTime());
        pStmt.setTimestamp(16, ts);
        pStmt.setLong(17, vo.getRoomId());
        if (pStmt.execute())
            errorMsg.setCode(0);
        else {
            errorMsg.setCode(Config.FAIL_INSERT);
            errorMsg.setMsg(pStmt.toString());
        }
    }

    @Override
    public void doDelete(LiveMinRecordDO vo) throws SQLException {
        String sql = String.format("DELETE FROM  live_min_record WHERE roomId=?", vo.getRoomId());
        pStmt = conn.prepareStatement(sql);
        if (pStmt.execute())
            errorMsg.setCode(0);
        else {
            errorMsg.setCode(Config.FAIL_INSERT);
            errorMsg.setMsg(pStmt.toString());
        }
    }

    @Override
    public void doBatch(List<LiveMinRecordDO> t) throws SQLException {

    }
}
