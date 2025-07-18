package com.zl.task.craw.list;

import com.zl.task.process.goods.CrawDouYinGoodsList;
import com.zl.task.vo.task.taskResource.DefaultTaskResourceCrawTabList;
import com.zl.task.vo.task.taskResource.TaskVO;
import com.zl.utils.io.FileIoUtils;
import com.zl.utils.log.LoggerUtils;
import org.junit.Before;
import org.junit.Test;

public class CrawDouYinGoodsListTest {
    private CrawDouYinGoodsList craw;

    @Before
    public void init() throws Exception {
        //测试webdriver；
        craw = new CrawDouYinGoodsList();
        craw.setTab(DefaultTaskResourceCrawTabList.getTabList().get(0));
        craw.setListonXhr();
        String url = "https://compass.jinritemai.com/shop/chance/product-rank?from_page=%2Fshop%2";
        craw.openEnterUrl(url);

    }

    public void run() throws Exception {
        LoggerUtils.logger.info("爬取抖店罗盘商品榜单开始：");
        String s = FileIoUtils.readTxtFile("./data/task/抖店罗盘商品日榜", "utf-8");
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
        TaskVO taskVO = new TaskVO(1, "抖店罗盘商品榜单");
        taskVO.setTaskDesc("近一天&短视频榜&服饰内衣-1");
        craw.select(taskVO);
    }

    @Test
    public void crawCompassList() throws Exception {
        craw.crawCompassList();
    }

    @Test
    public void craw() throws Exception {
        TaskVO taskVO = new TaskVO(1, "抖店罗盘商品榜单");
        taskVO.setTaskDesc("近一天&总榜&服饰内衣-2");
        craw.select(taskVO);
        craw.craw(taskVO);
    }

}