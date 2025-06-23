package com.zl.task.craw.list;

import com.zl.task.vo.task.taskResource.DefaultTaskResourceCrawTabList;
import com.zl.task.vo.task.taskResource.TaskVO;
import com.zl.utils.io.FileIoUtils;
import com.zl.utils.log.LoggerUtils;
import org.junit.Before;
import org.junit.Test;

public class CrawDouYinShopListTest {
    private CrawDouYinShopList craw;

    @Before
    public void init() throws Exception {
        //测试webdriver；
        craw = new CrawDouYinShopList();
        craw.setTab(DefaultTaskResourceCrawTabList.getTabList().get(0));
        craw.setListonXhr();
        String url = "https://compass.jinritemai.com/shop/chance/rank-shop?from_page=%2Fshop";
        craw.openEnterUrl(url);

    }

    public void run() throws Exception {
        LoggerUtils.logger.info("爬取抖店罗盘店铺榜单开始：");
        String s = FileIoUtils.readTxtFile("./data/task/抖店罗盘店铺日榜", "utf-8");
        String[] strings = s.split("\r\n");
        for (String string : strings) {
            TaskVO taskVO = new TaskVO(1, "抖店罗盘小时榜");
            taskVO.setTaskDesc(string);
            try {
                craw.run(taskVO);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Test
    public void select() throws InterruptedException {
        TaskVO taskVO = new TaskVO(1, "抖店罗盘店铺榜单");
        taskVO.setTaskDesc("近一天&总榜&商品卡&服饰内衣-2");
        craw.select(taskVO);
    }

    @Test
    public void craw() throws Exception {
        TaskVO taskVO = new TaskVO(1, "抖店罗盘商品榜单");
        taskVO.setTaskDesc("近一天&总榜&商品卡&服饰内衣-2");
        craw.select(taskVO);
        craw.craw(taskVO);
    }
}