package com.ecommerce.content.craw;
// 爬取抖音热点宝web 数据检测小时
public class CrawDouhotWebVideo {
    public static void main(String[] args) {
        CrawDouhotWebVideo crawler = new CrawDouhotWebVideo();
        crawler.run(1l);
    }
    public void run(Long videoId) {
        // 获取用户输入
        craw(videoId);
        save();
    }
    public void craw(Long videoId) {
        //

    }
    public void save() {
        // 保存数据
    }
}
