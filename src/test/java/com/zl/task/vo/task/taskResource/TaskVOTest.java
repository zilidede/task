package com.zl.task.vo.task.taskResource;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskVOTest {

    @Test
    void getTaskResource() {
        // 2. 使用List<String>类型资源
        ListResource listResource= new ListResource();
        List<String> smallList = new ArrayList<>();
        smallList.add("Item 1");
        listResource.load(smallList);
        TaskVO<List<String>> task = new TaskVO<>(1,"测试",listResource);
        task.setTaskId(102);
        task.setTaskName("List Processing");
        List<String> strings=task.getTaskResource().getT();
        System.out.println(strings);
    }
}