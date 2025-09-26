package com.zl.task.generator;


import com.zl.dao.generate.LocalTaskDO;
import com.zl.dao.generate.LocalTaskDao;
import com.zl.task.impl.SaveServiceImpl;
import com.zl.task.vo.task.taskResource.TaskVO;
import com.util.jdbc.generator.jdbc.DefaultDatabaseConnect;
import com.zl.utils.log.LoggerUtils;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

//本地
public class LocalTaskCommon {
    public static final LocalTaskDao dao;

    static {
        try {
            dao = new LocalTaskDao(DefaultDatabaseConnect.getConn());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static final SaveServiceImpl saveService = new SaveServiceImpl();

    public static void save(List list) throws SQLException {

        saveService.savePgSql(dao, list);
    }

    public static List<LocalTaskDO> getUnExeTaskList() throws SQLException, InterruptedException {
        List<LocalTaskDO> list = dao.findUnExeTask();
        if (list.size() > 0) {
            return list;
        } else {
            LoggerUtils.logger.info("没有可执行的任务，休眠1分钟");
            Thread.sleep(1000 * 60 * 2);
            return null;
        }
    }

    public static boolean updateTaskStatus(TaskVO task) throws SQLException {
        LocalTaskDO vo = new LocalTaskDO();
        vo.setId(task.getTaskUuid());
        vo.setName(task.getTaskName());
        vo.setContent(task.getTaskDesc());
        vo.setStatus(task.getStatus());
        vo.setEndTime(new Date());
        vo.setExecuteTime(task.getExecuteTime());
        dao.doUpdate(vo);
        return true;
    }

}
