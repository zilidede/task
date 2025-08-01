package com.zl.dao.generate;

import com.zl.dao.DaoService;
import com.zl.dao.ErrorMsg;
import com.zl.utils.jdbc.hikariCP.ConnectionPool;

import java.sql.*;
import java.util.List;

/**
 * @Description:
 * @Param:
 * @Auther: zl
 * @Date: 2024-07-12
 */
public class OceanenSearchKeywordsVideoDao implements DaoService<OceanenSearchKeywordsVideoDO> {
    private static final int QueryTimeout = 30;
    private final ErrorMsg errorMsg;


    private final String tableName = "oceanen_search_keywords_video";

    public OceanenSearchKeywordsVideoDao() throws SQLException {
        errorMsg = new ErrorMsg();

    }

    public ErrorMsg getErrorMsg() {
        return errorMsg;
    }

    @Override
    public void doInsert(OceanenSearchKeywordsVideoDO vo) throws SQLException {
        String sql = "insert into oceanen_search_keywords_video(video_play_over_rate,video_keyword_list,video_title,keyword,video_show_cnt,record_time,video_id,video_interact_rate)values (?,?,?,?,?,?,?,?)";
        try (Connection conn = ConnectionPool.getConnection(); PreparedStatement pStmt = conn.prepareStatement(sql)) {
            pStmt.setDouble(1, vo.getVideoPlayOverRate());
            pStmt.setString(2, vo.getVideoKeywordList());
            pStmt.setString(3, vo.getVideoTitle());
            pStmt.setString(4, vo.getKeyword());
            pStmt.setInt(5, vo.getVideoShowCnt());
            Timestamp ts = new Timestamp(vo.getRecordTime().getTime());
            pStmt.setTimestamp(6, ts);
            pStmt.setString(7, vo.getVideoId());
            pStmt.setDouble(8, vo.getVideoInteractRate());
            if (pStmt.execute())
                errorMsg.setCode(0);
            else {
                errorMsg.setMsg(pStmt.toString());
            }
        } catch (SQLException e) {
            // 异常处理
        }

    }

    public void doUpdate(OceanenSearchKeywordsVideoDO vo) throws SQLException {
        String sql = "update  oceanen_search_keywords_video SET  video_play_over_rate=? , video_keyword_list=? , video_title=? , keyword=? , video_show_cnt=? , record_time=? , video_id=? , video_interact_rate=?WHERE video_play_over_rate=?";
        try (Connection conn = ConnectionPool.getConnection(); PreparedStatement pStmt = conn.prepareStatement(sql)) {
            pStmt.setDouble(1, vo.getVideoPlayOverRate());
            pStmt.setString(2, vo.getVideoKeywordList());
            pStmt.setString(3, vo.getVideoTitle());
            pStmt.setString(4, vo.getKeyword());
            pStmt.setInt(5, vo.getVideoShowCnt());
            Timestamp ts = new Timestamp(vo.getRecordTime().getTime());
            pStmt.setTimestamp(6, ts);
            pStmt.setString(7, vo.getVideoId());
            pStmt.setDouble(8, vo.getVideoInteractRate());
            pStmt.setDouble(9, vo.getVideoPlayOverRate());
            if (pStmt.execute())
                errorMsg.setCode(0);
            else {

                errorMsg.setMsg(pStmt.toString());
            }
        } catch (SQLException e) {
            // 异常处理
        }

    }

    @Override
    public void doDelete(OceanenSearchKeywordsVideoDO vo) throws SQLException {
        String sql = String.format("DELETE FROM  oceanen_search_keywords_video WHERE video_play_over_rate=?", vo.getVideoPlayOverRate());
        try (Connection conn = ConnectionPool.getConnection(); PreparedStatement pStmt = conn.prepareStatement(sql)) {
            if (pStmt.execute())
                errorMsg.setCode(0);
            else {

                errorMsg.setMsg(pStmt.toString());
            }
        } catch (SQLException e) {
            // 异常处理
        }

    }

    @Override
    public void doBatch(List<OceanenSearchKeywordsVideoDO> t) throws SQLException {

    }
}
