package com.zl.dao;

import com.zl.dao.generate.FileRecordDO;

import java.sql.SQLException;
import java.util.List;

/**
 * @className: com.craw.nd.service-> DaoService
 * @description: Dao层操作接口
 * @author: zl
 * @createDate: 2023-01-04 10:39
 * @version: 1.0
 * @todo:
 */
public interface DaoService<T> {
    ErrorMsg getErrorMsg();

    void doInsert(T t) throws SQLException; //插入；

    void doUpdate(T t) throws SQLException; //更新；

    void doDelete(T t) throws SQLException; //删除；


    void doBatch(List<T> t) throws SQLException;

    //快速查询
}
