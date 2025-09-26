package com.zl.task.craw.base.x;

import com.ll.drissonPage.base.By;
import com.ll.drissonPage.element.ChromiumElement;
import com.ll.drissonPage.page.ChromiumTab;
import com.ll.drissonPage.units.listener.DataPacket;
import com.zl.dao.generate.EcommerceOrderDao;
import com.zl.task.vo.task.taskResource.DefaultTaskResourceCrawTabList;
import com.zl.utils.io.FileIoUtils;
import com.util.jdbc.generator.jdbc.DefaultDatabaseConnect;
import com.zl.utils.log.LoggerUtils;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//爬取抖音店铺订单昵称
public class CrawDouYinShopOrderNickName {
    EcommerceOrderDao dao;
    private ChromiumTab tab;

    CrawDouYinShopOrderNickName(ChromiumTab tab) throws SQLException {
        this.tab = tab;
        dao = new EcommerceOrderDao(DefaultDatabaseConnect.getConn());
    }

    public static void main(String[] args) throws InterruptedException, SQLException {
        //TODO: 爬取抖音店铺订单昵称
        CrawDouYinShopOrderNickName crawler = new CrawDouYinShopOrderNickName(DefaultTaskResourceCrawTabList.getTabList().get(1));
        crawler.run();
    }

    public void run() throws InterruptedException, SQLException {
        tab.get("https://im.jinritemai.com/pc_seller_v2/main/workspace?selfId=7001789215643033120");
        Thread.sleep(5 * 1000);
        List<String> orders = getUnCrawOrderIds();
        String xpath = "";
        List<String> list = new ArrayList<>();
        list.add("/backstage/getuserbyorder");
        tab.listen().start(list);
        xpath = "//*[@id=\"rootContainer\"]/div/div[1]/div[1]/div[2]/div/input";

        for (String order : orders) {
            ChromiumElement ele = tab.ele(By.xpath(xpath));
            Thread.sleep(2 * 1000);
            ele.input(order);
            Thread.sleep(1000);
            List<DataPacket> res = tab.listen().waits(100, 2.1, false, true);
            if (res.size() >= 1) {
                for (DataPacket data : res)
                    if (data != null)
                        try {

                            saveFile(data);
                        } catch (Exception e) {
                            LoggerUtils.logger.info("保存文件失败：" + data.url());
                        }

            } else {
                System.out.println("error");
            }
        }
    }

    public List<String> getUnCrawOrderIds() throws SQLException {
        return dao.getUnUserOrderIds("西域骆驼运动旗舰店", "2024-03-03");
    }

    public ChromiumTab getTab() {
        return tab;
    }

    public void setTab(ChromiumTab tab) {
        this.tab = tab;
    }

    public void saveFile(DataPacket data) throws Exception {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss-SSS");
        String timestamp = sdf.format(now);

        // 示例URL和响应体（这里需要替换为实际值）
        String url = data.url();
        String requestBody = data.request().postData();
        String responseBody = data.response().rawBody();
        // 构造文件路径
        String filePath = "D:\\data\\爬虫\\电商\\抖音\\抖音小店\\order\\getuserbyorder\\" + timestamp + ".txt";
        // 写入文件
        FileIoUtils.writeToFile(filePath, "url: " + url + " Request body: " + requestBody + " Response body: " + responseBody);
    }

}
