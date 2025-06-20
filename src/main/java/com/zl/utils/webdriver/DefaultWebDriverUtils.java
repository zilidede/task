package com.zl.utils.webdriver;

import com.zl.config.ConfigIni;
import com.zl.utils.run.NonBlockingBatExecution;
import com.zl.utils.run.RuntimeUtils;

public class DefaultWebDriverUtils {
    private static Integer port=9222;
    DefaultWebDriverUtils(){

    }

    public static void setPort(Integer port) {
        DefaultWebDriverUtils.port = port;
    }

    public static Integer getPort() {
        return port;
    }

    private static WebDriverUtils singleton;

    static {
        try {
            singleton = new WebDriverUtils(ConfigIni.DRIVE_PATH, port);
            singleton.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //  System.out.println("");
    }

    public static WebDriverUtils getInstance() {
        return singleton;
    }

    public static void reSetWebDriverUtils(Integer  port) throws InterruptedException {
        //
        RuntimeUtils.killProcessByName("chrome");
        Thread.sleep(1000 * 5);
        RuntimeUtils.killProcessByName("chromedriver");
        Thread.sleep(1000 * 5);
        NonBlockingBatExecution.exe("C:\\Users\\zili\\Desktop\\启动浏览器\\运行chrome - 9223use.bat"); // 启动浏览器bat
        Thread.sleep(1000 * 10);
        try {
            singleton = new WebDriverUtils(ConfigIni.DRIVE_PATH, port);
            singleton.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //DefaultWebDriverUtils.getInstance().getWebDriver().get("https://item.jd.com/10093521210663.html#crumb-wrap");
    }
}
