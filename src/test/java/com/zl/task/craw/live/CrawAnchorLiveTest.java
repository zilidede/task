package com.zl.task.craw.live;

import com.zl.task.vo.task.taskResource.DefaultTaskResourceCrawTabList;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class CrawAnchorLiveTest {


    @Test
    void craw() throws Exception {
        CrawAnchorLive crawAnchorLive = new CrawAnchorLive(DefaultTaskResourceCrawTabList.getTabList().get(0));
        crawAnchorLive.craw();

    }
}