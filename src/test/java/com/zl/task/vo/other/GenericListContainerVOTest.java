package com.zl.task.vo.other;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GenericListContainerVOTest {


    @Test
    void containerVOTest() {
        GenericListContainerVO container = new GenericListContainerVO();
        List<String> strings=new ArrayList<>();
        strings.add("a");
        container.addList(String.class, strings);
        container.addList(Integer.class, Arrays.asList(1, 2));
        List<String> strList = container.getList(String.class);  // 安全获取 String 列表
        List<Integer> intList = container.getList(Integer.class);
        System.out.println(strList);
    }
}