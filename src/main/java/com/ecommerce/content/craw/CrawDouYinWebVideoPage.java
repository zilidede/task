package com.ecommerce.content.craw;

import java.util.List;
import java.util.Map;

/**
 * 抓取抖音web端视频-https://www.douyin.com/video/7539745217725877562;
 */
public class CrawDouYinWebVideoPage {
    public static void main(String[] args) {

    }
    public void run() {
        // 抖音web原始视频下载;
        Map<Long,String> urls=getOriginVideoUrls();

        for (Map.Entry<Long,String>url: urls.entrySet()){
            download(url.getValue(), "", ""); //下载原始视频
            crawDouYinWebVideoInfo(url.getKey());//抓取视频页面信息
        }
        // 获取视频页面信息


    }
    public   Map<Long,String> getOriginVideoUrls(){
        return null;
    }
    public  void crawDouYinWebVideoInfo(Long videoId){

        return;
    }
    public int download(String originUrl, String savePath, String saveName){
        return 0;
    }
}
