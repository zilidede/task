package com.zl.task.save.parser;

import com.zl.task.vo.http.HttpRequest;
import com.zl.task.vo.http.HttpResponse;
import com.zl.task.vo.http.HttpVO;
import com.zl.utils.io.FileIoUtils;

import java.io.IOException;


// 解析fiddler Json
public class ParserFiddlerJson {
    public static void main(String[] args) throws Exception {

        HttpVO httpVO = ParserFiddlerJson.parserXHRJson("D:\\data\\爬虫\\电商\\抖音\\抖店罗盘\\market\\compassApiShopProductProductChanceMarketCategoryOverviewPriceAnalysisProduct\\2024-8-25-0-11-34-13-871.txt");
    }

    public static HttpVO parserXHRJson(String fileName) throws IOException {
        // 解析fiddler 生成的文件
        HttpVO httpVO = new HttpVO();
        String content = FileIoUtils.readFile(fileName);
        String url = "";
        String response = "";
        String request = "";

        if (content.indexOf("Request body: ") > 0) {
            request = content.substring(content.indexOf("Request body: ") + 14, content.indexOf("Response body: "));
            response = content.substring(content.indexOf("Response body: ") + 15);
            url = content.substring(content.indexOf("url: ") + 5, content.indexOf("Request body: "));
        } else {
            url = content.substring(content.indexOf("url: ") + 5, content.indexOf("Response body: "));
            response = content.substring(content.indexOf("Response body: ") + 15);
        }
        httpVO.setUrl(url);
        HttpResponse responseVO = new HttpResponse(); // 返回数据
        HttpRequest requestVO = new HttpRequest(); // 请求数据
        requestVO.setBody(request);
        responseVO.setBody(response);
        httpVO.setRequest(requestVO);
        httpVO.setResponse(responseVO);
        return httpVO;
    }
}
