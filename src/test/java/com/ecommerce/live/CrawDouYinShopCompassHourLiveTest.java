package com.ecommerce.live;

import com.zl.task.craw.list.CrawDouYinShopCompassHourLive1;
import com.zl.task.vo.task.taskResource.DefaultTaskResourceCrawTabList;
import com.zl.task.vo.task.taskResource.TaskVO;
import com.zl.utils.io.FileIoUtils;
import com.zl.utils.log.LoggerUtils;
import org.junit.jupiter.api.Test;

class CrawDouYinShopCompassHourLiveTest {

    @Test
    void run() throws Exception {
        String filePath = "./data/task/抖店罗盘小时榜.txt";
        //爬取24小时所有时间点的抖店罗盘小时榜
        LoggerUtils.logger.info("爬取抖店罗盘小时榜任务开始：" + filePath);
        CrawDouYinShopCompassHourLive1 crawler = new CrawDouYinShopCompassHourLive1();
        for (int i = 0; i < 24; i++) {
            String s = FileIoUtils.readTxtFile(filePath, "utf-8");
            String[] strings = s.split("\r\n");
            for (String string : strings) {
                TaskVO taskVO = new TaskVO(1, "抖店罗盘小时榜");
                taskVO.setTaskDesc(string);
                try {
                    crawler.setTab(DefaultTaskResourceCrawTabList.getTabList().get(0));
                    crawler.setHour(i);
                    crawler.run(taskVO);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}