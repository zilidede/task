package com.ecommerce.content.craw;
// 爬取巨量算数视频热度以及人群相关信息；
public class CrawTrendinsightVideo {
    public static void main(String[] args) {
        CrawTrendinsightVideo crawTrendinsightVideoService = new CrawTrendinsightVideo();
        crawTrendinsightVideoService.run(1l);
    }
    public void run(Long videoId) {
        // 获取用户输入
        craw(videoId);
        save();
    }
    public void craw(Long videoId) {


        // 保存数据

    }
    public void save() {
        // 保存数据
    }
}
