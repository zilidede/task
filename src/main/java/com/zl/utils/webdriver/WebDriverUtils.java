package com.zl.utils.webdriver;

import com.ll.drissonPage.base.By;
import com.ll.drissonPage.config.ChromiumOptions;
import com.ll.drissonPage.element.ChromiumElement;
import com.ll.drissonPage.page.ChromiumPage;
import com.ll.drissonPage.units.listener.DataPacket;

import java.util.List;

public class WebDriverUtils {
    private final ChromiumPage driver;

    public WebDriverUtils(String drivePath, Integer port) {
        ChromiumOptions options = new ChromiumOptions();
        options.setPaths(drivePath);
        options.setLocalPort(port);
        driver = ChromiumPage.getInstance(options);

    }

    public ChromiumPage getDriver() {
        return driver;
    }

    public void init() {
    }

    public void close() {
        driver.close();
    }

    public boolean listen(String xhr) {
        driver.listen().start(xhr);
        return true;
    }

    public List<DataPacket> listenOffData() {
        return driver.listen().steps();
    }

    public void getUrl(String url) {
        driver.get(url);
    }

    public ChromiumElement findElement(String xpath) {
        return driver.ele(By.xpath(xpath));
    }

    public List<ChromiumElement> findElements(String xpath) {
        return driver.eles(By.xpath(xpath));
    }
}
