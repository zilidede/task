package com.zl.dao.generate;
import com.zl.config.Config;
import com.zl.dao.DaoService;
import com.zl.dao.ErrorMsg;
import java.sql.*;
import java.util.List;

import com.util.jdbc.hikariCP.ConnectionPool;

/**
 * @Description:
 * @Param:
 * @Auther: zl
 * @Date: 2025-06-24
 */
public class ContentDao implements DaoService<ContentDO>{
    private static int QueryTimeout = 30;
    private ErrorMsg errorMsg;



    private String tableName = "content";
    public ContentDao() throws SQLException {
        errorMsg=new ErrorMsg();

    }    public ErrorMsg getErrorMsg() {
        return errorMsg;
    }
@Override
    public void doInsert(ContentDO vo) throws SQLException {
        String sql = "insert into content(item_id,local_path,title,data_source)values (?,?,?,?)";
    try (Connection conn = ConnectionPool.getConnection(); PreparedStatement pStmt = conn.prepareStatement(sql)) {
        pStmt.setLong(1, vo.getItemId());
        pStmt.setString(2, vo.getLocalPath());
        pStmt.setString(3, vo.getTitle());
        pStmt.setString(4, vo.getDataSource());
        if(pStmt.execute())
            errorMsg.setCode(0);
        else{
            errorMsg.setCode(Config.FAIL_INSERT);
            errorMsg.setMsg(pStmt.toString());
        }
    } catch (SQLException e) {
        // 异常处理
    }

    }
    public void doUpdate(ContentDO vo) throws SQLException {
        String sql = "update  content SET  item_id=? , local_path=? , title=? , data_source=?WHERE item_id=?";
        try (Connection conn = ConnectionPool.getConnection(); PreparedStatement pStmt = conn.prepareStatement(sql)) {
            pStmt.setLong(1, vo.getItemId());
            pStmt.setString(2, vo.getLocalPath());
            pStmt.setString(3, vo.getTitle());
            pStmt.setString(4, vo.getDataSource());
            pStmt.setLong(5, vo.getItemId());
            if(pStmt.execute())
                errorMsg.setCode(0);
            else{
                errorMsg.setCode(Config.FAIL_INSERT);
                errorMsg.setMsg(pStmt.toString());
            }
        } catch (SQLException e) {
            // 异常处理
        }

    }@Override
    public void doDelete(ContentDO vo) throws SQLException {
        String sql =String.format("DELETE FROM  content WHERE item_id=?",vo.getItemId());
        try (Connection conn = ConnectionPool.getConnection(); PreparedStatement pStmt = conn.prepareStatement(sql)) {
            if(pStmt.execute())
                errorMsg.setCode(0);
            else{
                errorMsg.setCode(Config.FAIL_INSERT);
                errorMsg.setMsg(pStmt.toString());
            }
        } catch (SQLException e) {
            // 异常处理
        }

    }

    @Override
    public void doBatch(List<ContentDO> t) throws SQLException {

    }
}
