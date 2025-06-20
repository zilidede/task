package com.zl.task.craw.keyword;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ll.drissonPage.base.By;
import com.ll.drissonPage.element.ChromiumElement;
import com.ll.drissonPage.units.listener.DataPacket;
import com.zl.task.craw.base.x.CrawServiceXImpl;
import com.zl.task.impl.taskResource.DefaultTaskResourceCrawTabList;
import com.zl.task.save.parser.ParserFiddlerJson;
import com.zl.task.vo.http.HttpVO;
import com.zl.task.vo.task.TaskVO;
import com.zl.utils.io.FileIoUtils;
import com.zl.utils.log.LoggerUtils;
import com.zl.utils.other.Ini4jUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @className: com.craw.nd.service.other.person.Impl.craw.selenium-> CrawOceanEngineSearch
 * @description: 爬取巨量算数搜索词
 * @author: zl
 * @createDate: 2025-01-06 12：07
 * @version: 1.0
 * @todo:
 */
public class CrawTrendinsightKeywordsX extends CrawServiceXImpl {
    private List<String> keywords;
    private final String srcDir;

    CrawTrendinsightKeywordsX() throws Exception {
        Ini4jUtils.loadIni("./data/config/config.ini");
        Ini4jUtils.setSectionValue("trendinsight");
        srcDir = Ini4jUtils.readIni("srcDir");
    }

    public static void main(String[] args) throws Exception {
        CrawTrendinsightKeywordsX craw = new CrawTrendinsightKeywordsX();
        craw.setTab(DefaultTaskResourceCrawTabList.getTabList().get(0));

        craw.run(new TaskVO(1, "爬取巨量算数关键字"));
    }

    public void run(TaskVO task) throws Exception {
        getXHRList().add("index/get_multi_keyword_interpretation");
        getXHRList().add("index/get_multi_keyword_hot_trend");
        getXHRList().add("index/get_relation_word");
        getXHRList().add("index/get_portrait");
        getTab().listen().start(getXHRList());
        crawFromFile();

    }

    public void crawRootKeyword(String rootKeyword, int deep, String startTime, String endTime) throws InterruptedException {
        //从词根爬取关键字列表-deep=深度，startTime=开始时间，endTime=结束时间;
        keywords = getKeywords("./data/task/手工词根列表.txt");
        for (String s : keywords) {
            craw(s, "2025-01-06", "2025-01-12");
        }
    }

    public void crawTopicYesterday(String keyword) {
        //爬取关键词话题题昨天数据

    }

    public void crawFromFile() throws InterruptedException {
        //从爬取文件获取关键字列表爬取
        keywords = getKeywords("./data/task/待爬取关键字列表.txt");
        for (String s : keywords)
            craw(s, "", "");
    }

    public List<String> getKeywords(String filePath) {
        List<String> result = new ArrayList<>();
        String[] content = FileIoUtils.readTxtFile(filePath, "utf-8").split("\r\n");
        Collections.addAll(result, content);
        return result;
    }

    public void craw(String keyword, String startTime, String endTime) throws InterruptedException {

        //爬取单一关键词
        String url = String.format("https://trendinsight.oceanengine.com/arithmetic-index/analysis?keyword=%s&appName=aweme", keyword);

        openUrl(url, 10.00);
        String xpath = "//*[@class=\"byted-tab-bar-item byted-tab-bar-item-type-card\"]";
        List<ChromiumElement> elements = getTab().eles(By.xpath(xpath));
        for (int i = 0; i < 2; i++) {
            elements.get(i).click().click();
            Thread.sleep(1000 * 4);
            xpath = "//*[@class=\"byted-tab-bar-item byted-tab-bar-item-type-card\"]";
            elements = getTab().eles(By.xpath(xpath));
        }
        List<DataPacket> res = getTab().listen().waits(100, 2.1, false, true);
        if (res.size() >= 1) {
            for (DataPacket data : res)
                if (data != null)
                    try {
                        String filePath = saveXhr(srcDir, data);
                        HttpVO httpVO = ParserFiddlerJson.parserXHRJson(filePath);
                        String json = httpVO.getResponse().getBody();
                        JsonParser parser = new JsonParser();
                        JsonObject object = parser.parse(json).getAsJsonObject();
                        CypTrendinsightData.decrypt(object.get("data").getAsString());
                    } catch (Exception e) {
                        LoggerUtils.logger.info("保存文件失败：" + data.url());
                    }

        } else {
            System.out.println("error");
        }


    }

    public void selectTime(String startTime, String endTime) {
        String xpath = "";
    }


}
