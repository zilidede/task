package com.util.drissionPage;

import com.ll.drissonPage.page.ChromiumPage;
import com.zl.task.vo.task.taskResource.DefaultTaskResourceCrawTabList;
import com.zl.utils.webdriver.DefaultWebDriverUtils;

public class DownloadFileUtils {
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        String url = "https://www.douyin.com/aweme/v1/play/?video_id=v0300fg10000d33d54vog65mh5526mt0";
        String save_path = "d:/data";
        DefaultWebDriverUtils.setPort(9223);
        ChromiumPage chromiumPage = DefaultWebDriverUtils.getInstance().getDriver();
        chromiumPage.getTab().download().download(url, save_path, "logo.png");

    }
}
