package com.zl.task.craw.keyword;

import com.zl.task.vo.task.taskResource.DefaultTaskResourceCrawTabList;
import com.zl.task.vo.task.taskResource.ListResource;
import com.zl.task.vo.task.taskResource.TaskResource;
import com.zl.task.vo.task.taskResource.TaskVO;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CrawSingleOceanEngineKeywordTest {

    @Test
    void run() throws Exception {
        List<String> keywords= new ArrayList<>();
        keywords.add("街舞");
        keywords.add("防晒");
        keywords.add("裤子");
        TaskResource<List<String>> listRes=new ListResource();
        listRes.load(keywords);

    }


}