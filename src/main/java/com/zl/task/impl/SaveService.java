package com.zl.task.impl;

import com.zl.dao.DaoService;

import java.util.List;

/**
 * @className: com.craw.nd.service-> SaveService
 * @description: //存储单元
 * @author: zl
 * @createDate: 2023-01-04 9:51
 * @version: 1.0
 * @todo:
 */
public interface SaveService<T extends DaoService> {
    ////

    void savePgSql(T t, List voList); //保存到pgsql；

    void saveBatchPgSql(T t, List voList);


}
