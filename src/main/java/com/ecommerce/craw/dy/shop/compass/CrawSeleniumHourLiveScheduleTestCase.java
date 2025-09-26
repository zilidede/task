package com.ecommerce.craw.dy.shop.compass;
/**
 * @program: ecommerce
 * @description: 爬取抖音直播榜单-抖店罗盘测试用例
 * @author: zl
 * @create: 2025-09-25 16:09
 **/
import com.zl.task.vo.task.taskResource.DefaultTaskResourceCrawTabList;
import com.zl.task.vo.task.taskResource.TaskVO;
import com.zl.utils.io.FileIoUtils;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CrawSeleniumHourLiveScheduleTestCase {

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(0);

    public static void main(String[] args) throws Exception {
        scheduleHourlyTask();
    }

    public static void scheduleHourlyTask() {
        scheduler.scheduleAtFixedRate(() -> {
            System.out.println("小时任务执行：" + new Date());
            String s = FileIoUtils.readTxtFile("./data/task/抖店罗盘小时榜.txt", "utf-8");
            String[] strings = s.split("\n");
            for (String string : strings) {
                TaskVO taskVO = new TaskVO(1, "抖店罗盘小时榜");
                ;
                taskVO.setTaskDesc(string);
                DefaultCrawSeleniumDouYinShopCompassHourLive.setTab(DefaultTaskResourceCrawTabList.getTabList().get(0));
                try {
                    DefaultCrawSeleniumDouYinShopCompassHourLive.getInstance().run(taskVO);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }, 0, 60 * 55, TimeUnit.SECONDS); //  立即执行小时任务 每个50分钟检查


    }
}
