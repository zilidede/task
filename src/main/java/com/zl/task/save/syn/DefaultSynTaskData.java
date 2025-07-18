package com.zl.task.save.syn;


import com.zl.task.impl.ExecutorTaskService;

public class DefaultSynTaskData {
    private static ExecutorTaskService executor;

    static {
        try {
            executor = new SynTaskData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ExecutorTaskService getInstance() throws Exception {
        return executor;
    }
}
