package com.zl.task.craw.main;

import com.zl.task.craw.list.CrawSeleniumDouYinList;
import com.zl.task.vo.task.taskResource.DefaultTaskResourceCrawTabList;
import com.zl.task.vo.task.taskResource.TaskVO;
import com.zl.utils.io.FileIoUtils;
import com.zl.utils.log.LoggerUtils;
// 爬取抖音直播数据
public class CrawLiveTestCase {
    public static void main(String[] args) throws Exception {
        crawLiveHourCategoryListFromCompass();
    }

    //爬取类目直播小时榜单 -抖店罗盘
    public static void crawLiveHourCategoryListFromCompass() throws Exception {
        String filePath = "./data/task/抖店罗盘小时榜.txt";
        //爬取24小时所有时间点的抖店罗盘小时榜
        LoggerUtils.logger.info("爬取抖店罗盘小时榜任务开始：" + filePath);
        CrawSeleniumDouYinList crawler = new CrawSeleniumDouYinList();
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

    //爬取类目直播数据-灰豚
    public static void crawLiveDayCategoryListFromHuiTun(String filePath) throws Exception {
        LoggerUtils.logger.info("爬取灰豚直播榜单数据：" );
    }
    //爬取类目直播数据-达人广场；
    public static void crawLiveDayCategoryListFromDarenSquare(String filePath) throws Exception {
        //
    }
}
