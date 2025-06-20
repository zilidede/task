package com.ll.chromeExtension;

import com.ll.drissonPage.config.ChromiumOptions;
import com.ll.drissonPage.page.ChromiumPage;

import java.io.IOException;

//执行浏览器扩展
public class ChromeExtension {
    public static void main(String[] args) throws IOException, InterruptedException {
        ChromiumOptions options = new ChromiumOptions();
        String driverPath = "C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe";
        options.setPaths(driverPath);
        options.setLocalPort(9222);
        String extensionPath = "S:\\work\\task\\data\\test\\extension\\gao-tu-bao";
        options.addExtension(extensionPath);
        ChromiumPage driver = ChromiumPage.getInstance(options);
        // 打开目标网页

        String url = "https://haohuo.jinritemai.com/ecommerce/trade/detail/index.html?id=3612249937892506246&origin_type=pc_compass_manage";
        driver.get(url);
        Thread.sleep(1000 * 5);
        // 获取插件 ID（可在 chrome://extensions/ 查看）
        String pluginId = "dloedampakbjhicnnoolammndjcjmiip"; // 替换为你的插件 ID

        // 构造 popup 页面 URL
        String popupUrl = "chrome-extension://" + pluginId + "/popup.html";
        driver.newTab(popupUrl);

    }
}
