package com.zl.dao.generate;

import com.zl.config.Config;
import com.zl.dao.DaoService;
import com.zl.dao.ErrorMsg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @Description:
 * @Param:
 * @Auther: zl
 * @Date: 2025-02-21
 */
public class CategoryTypeDao implements DaoService<CategoryTypeDO> {
    private static final int QueryTimeout = 30;
    private final ErrorMsg errorMsg;
    private final Connection conn;
    private Statement stmt = null;
    private PreparedStatement pStmt = null;
    private final String tableName = "category_type";

    public CategoryTypeDao(Connection connection) throws SQLException {
        errorMsg = new ErrorMsg();
        this.conn = connection;
        stmt = conn.createStatement();
        stmt.setQueryTimeout(QueryTimeout);  // set timeout to 30 sec;
    }

    public ErrorMsg getErrorMsg() {
        return errorMsg;
    }

    @Override
    public void doInsert(CategoryTypeDO vo) throws SQLException {
        String sql = "insert into category_type(industry_name,industry_second_category_name,industry_four_category_name,level,industry_first_category_name,id,industry_three_category_name)values (?,?,?,?,?,?,?)";
        pStmt = conn.prepareStatement(sql);
        pStmt.setString(1, vo.getIndustryName());
        pStmt.setString(2, vo.getIndustrySecondCategoryName());
        pStmt.setString(3, vo.getIndustryFourCategoryName());
        pStmt.setInt(4, vo.getLevel());
        pStmt.setString(5, vo.getIndustryFirstCategoryName());
        pStmt.setInt(6, vo.getId());
        pStmt.setString(7, vo.getIndustryThreeCategoryName());
        if (pStmt.execute())
            errorMsg.setCode(0);
        else {
            errorMsg.setCode(Config.FAIL_INSERT);
            errorMsg.setMsg(pStmt.toString());
        }
    }

    public void doUpdate(CategoryTypeDO vo) throws SQLException {
        String sql = "update  category_type SET  industry_name=? , industry_second_category_name=? , industry_four_category_name=? , level=? , industry_first_category_name=? , id=? , industry_three_category_name=?WHERE id=?";
        pStmt = conn.prepareStatement(sql);
        pStmt.setString(1, vo.getIndustryName());
        pStmt.setString(2, vo.getIndustrySecondCategoryName());
        pStmt.setString(3, vo.getIndustryFourCategoryName());
        pStmt.setInt(4, vo.getLevel());
        pStmt.setString(5, vo.getIndustryFirstCategoryName());
        pStmt.setInt(6, vo.getId());
        pStmt.setString(7, vo.getIndustryThreeCategoryName());
        pStmt.setInt(8, vo.getId());
        if (pStmt.execute())
            errorMsg.setCode(0);
        else {
            errorMsg.setCode(Config.FAIL_INSERT);
            errorMsg.setMsg(pStmt.toString());
        }
    }

    @Override
    public void doDelete(CategoryTypeDO vo) throws SQLException {
        String sql = String.format("DELETE FROM  category_type WHERE id=?", vo.getId());
        pStmt = conn.prepareStatement(sql);
        if (pStmt.execute())
            errorMsg.setCode(0);
        else {
            errorMsg.setCode(Config.FAIL_INSERT);
            errorMsg.setMsg(pStmt.toString());
        }
    }

    @Override
    public void doBatch(List<CategoryTypeDO> t) throws SQLException {

    }
}
