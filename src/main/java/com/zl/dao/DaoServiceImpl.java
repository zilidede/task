package com.zl.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @className: com.craw.nd.dao-> DaoServiceImpl
 * @description:
 * @author: zl
 * @createDate: 2023-01-04 11:05
 * @version: 1.0
 * @todo:
 */
public class DaoServiceImpl<T> implements DaoService<T> {
    private final ErrorMsg errorMsg;

    DaoServiceImpl(Connection connection) {
        errorMsg = new ErrorMsg();
    }

    @Override
    public ErrorMsg getErrorMsg() {
        return null;
    }

    @Override
    public void doInsert(T t) {

    }

    @Override
    public void doUpdate(T t) {

    }

    @Override
    public void doDelete(T t) {

    }

    @Override
    public void doBatch(List<T> t) throws SQLException {

    }
}
