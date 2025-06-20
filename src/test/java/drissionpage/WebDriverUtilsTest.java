package drissionpage;

import com.ll.drissonPage.units.listener.DataPacket;
import com.zl.utils.webdriver.WebDriverUtils;

import java.util.List;


//web 自动化测试用例
public class WebDriverUtilsTest {
    public static void main(String[] args) throws InterruptedException {
        WebDriverUtils webDriverUtils = new WebDriverUtils("", 9223);
        String xpath = "//*[@id=\"fxg-pc-header\"]/div/div[1]/div[2]/div[3]/div[1]";
        /*
        webDriverUtils.getUrl("https://fxg.jinritemai.com/ffa/mshop/homepage/index");
        ChromiumElement element=webDriverUtils.findElement(xpath);
        System.out.println(element.text());
        element.click().click();

         */
        webDriverUtils.listen("shop/product_lander/lander/interface");
        webDriverUtils.getUrl("https://compass.jinritemai.com/shop/chance/category-overview");
        Integer I = 10;
        Double j = 12.1;
        List<DataPacket> packets = webDriverUtils.getDriver().listen().waits(10);
        for (DataPacket packet : packets) {
            System.out.println(packet.response().body());
        }

        // webDriverUtils.close();
    }
}
