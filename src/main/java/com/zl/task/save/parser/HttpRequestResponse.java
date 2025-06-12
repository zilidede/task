package com.zl.task.save.parser;

import com.zl.utils.io.DiskIoUtils;
import com.zl.utils.io.FileIoUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpRequestResponse {

    private String url;
    private String requestBody;
    private String responseBody;

    public HttpRequestResponse(String input) {
        try {
            parseInput(input);
        } catch (Exception e) {
            // Handle exceptions gracefully by setting default values or logging errors.
            this.url = "";
            this.requestBody = "";
            this.responseBody = "";
            System.err.println("Error parsing input: " + e.getMessage());
        }
    }

    private void parseInput(String input) {
        // Define the regular expressions to match the parts
        // Define the regular expressions to match the parts
        Pattern urlPattern = Pattern.compile("^url:\\s+(https?://[^\\n]*)", Pattern.MULTILINE);
        Pattern requestBodyPattern = Pattern.compile("(?<=Request body:\\s+).*?(?=Response body:|$)", Pattern.DOTALL);
        Pattern responseBodyPattern = Pattern.compile("(?<=Response body:\\s+).*", Pattern.DOTALL);

        Matcher urlMatcher = urlPattern.matcher(input);
        Matcher requestBodyMatcher = requestBodyPattern.matcher(input);
        Matcher responseBodyMatcher = responseBodyPattern.matcher(input);

        if (urlMatcher.find()) {
            this.url = urlMatcher.group(1); // Extract the URL
        }

        if (requestBodyMatcher.find()) {
            this.requestBody = requestBodyMatcher.group().trim(); // Extract the Request body
        } else {
            this.requestBody = ""; // Set default value if not found
        }

        if (responseBodyMatcher.find()) {
            this.responseBody = responseBodyMatcher.group().trim(); // Extract the Response body
        } else {
            // If response body is not found, use the substring from the end of the request body until the end of the input
            int endIndex = input.indexOf("Request body:");
            if (endIndex != -1) {
                this.responseBody = input.substring(endIndex).trim();
            } else {
                this.responseBody = input.trim();
            }
        }
    }

    public String getUrl() {
        return url;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public static void main(String[] args) {
        String dir = "D:\\data\\百度云\\BaiduSyncdisk\\2024-08-25\\market-zl\\compassApiShopProductProductChanceMarketCategoryOverviewPriceAnalysisProduct";
        List<String> files = DiskIoUtils.getFileListFromDir(dir);
        for (String file : files) {
            HttpRequestResponse requestResponse = new HttpRequestResponse(FileIoUtils.readTxtFile(file, "utf-8"));
            System.out.println("URL: " + requestResponse.getUrl());
            System.out.println("Request Body: " + requestResponse.getRequestBody());
            System.out.println("Response Body: " + requestResponse.getResponseBody());

        }
    }
}