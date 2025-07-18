package com.zl.task.impl;

import com.zl.dao.DaoService;
import com.zl.utils.log.LoggerUtils;

import java.util.List;

/**
 * @className: com.craw.nd.service.other.person.Impl.save-> SaveServiceImpl
 * @description: //存储单元
 * @author: zl
 * @createDate: 2023-01-04 10:38
 * @version: 1.0
 * @todo:
 */
public class SaveServiceImpl implements SaveService {

    @Override
    public void savePgSql(DaoService daoService, List voList) {
        if (voList.size() >= 1) {
            for (Object vo : voList) {
                try {
                    daoService.doInsert(vo);
                } catch (Exception ex) {
                    String msg = String.format("保存失败，异常%s,执行错误的sql %s, ", ex, daoService.getErrorMsg().getMsg());
                    if (msg.indexOf("重复键违反唯一约束") < 0) {
                        LoggerUtils.logger.warn(msg);
                    }
                    ex.printStackTrace();
                }
            }
            voList.clear();
        }
    }

    public void saveUpdatePgSql(DaoService daoService, List voList) {
        if (voList.size() >= 1) {
            for (Object vo : voList) {
                try {
                    daoService.doUpdate(vo);
                } catch (Exception ex) {
                    String msg = String.format("保存失败，异常%s,执行错误的sql %s, ", ex, daoService.getErrorMsg().getMsg());
                    if (msg.indexOf("重复键违反唯一约束") < 0) {
                        LoggerUtils.logger.warn(msg);
                    }
                    ex.printStackTrace();

                }
            }
            voList.clear();
        }
    }

    @Override
    public void saveBatchPgSql(DaoService daoService, List voList) {
        if (voList.size() >= 1) {
            try {
                daoService.doBatch(voList);
            } catch (Exception ex) {
                String msg = String.format("保存失败，异常%s,执行错误的sql %s, ", ex, daoService.getErrorMsg().getMsg());
                if (msg.indexOf("重复键违反唯一约束") < 0) {
                    LoggerUtils.logger.warn(msg);
                }
                ex.printStackTrace();

            }

        }
    }

}
