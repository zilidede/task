package com.ecommerce.content.craw;

import com.ll.drissonPage.base.By;
import com.ll.drissonPage.element.ChromiumElement;
import com.ll.drissonPage.page.ChromiumPage;
import com.zl.task.vo.task.taskResource.DefaultTaskResourceCrawTabList;
import com.zl.utils.webdriver.DefaultWebDriverUtils;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class CrawDouYinWebAccountMainTest {

    @Test
    void scrollAndLoadMore() throws InterruptedException {
        DefaultWebDriverUtils.setPort(9223);
        ChromiumPage chromiumPage = DefaultWebDriverUtils.getInstance().getDriver();
        chromiumPage.get("https://www.douyin.com/user/MS4wLjABAAAAHQmX_tX08R8GT3yjMLmjr0bDl5nn-ArOwS4HCai3wD0?from_tab_name=main");
// 3. 最后尝试滚动到底部确保
        // 模拟更自然的滚动行为
        String naturalScrollScript = "const container = document.scrollingElement || document.documentElement;" +
                "const targetPosition = container.scrollHeight - window.innerHeight * 0.8;" +
                "let currentPosition = container.scrollTop;" +
                "const startTime = Date.now();" +
                "const duration = 1200 + Math.random() * 800; // 1.2-2秒的随机滚动时间" +
                "" +
                "function easeInOutQuad(t, b, c, d) {" +
                "  t /= d/2;" +
                "  if (t < 1) return c/2*t*t + b;" +
                "  t--;" +
                "  return -c/2 * (t*(t-2) - 1) + b;" +
                "}" +
                "" +
                "return new Promise(resolve => {" +
                "  const animateScroll = () => {" +
                "    const elapsed = Date.now() - startTime;" +
                "    const progress = Math.min(elapsed / duration, 1);" +
                "    " +
                "    // 使用缓动函数模拟自然滚动" +
                "    const scrollY = easeInOutQuad(progress, currentPosition, targetPosition - currentPosition, 1);" +
                "    container.scrollTop = scrollY;" +
                "    " +
                "    if (progress < 1) {" +
                "      requestAnimationFrame(animateScroll);" +
                "    } else {" +
                "      // 滚动完成后随机停顿" +
                "      setTimeout(resolve, 500 + Math.random() * 1000);" +
                "    }" +
                "  };" +
                "  animateScroll();" +
                "});";

        try {
            // 创建Robot实例
            Robot robot = new Robot();

            // 获取当前鼠标位置
            int x = java.awt.MouseInfo.getPointerInfo().getLocation().x;
            int y = java.awt.MouseInfo.getPointerInfo().getLocation().y;

            // 移动鼠标到页面中间（更自然）
            robot.mouseMove(x, y);
            Thread.sleep(300); // 短暂停顿
            // 模拟鼠标滚轮向下滚动（负值为向上滚动，正值为向下滚动）
            // 注意：每个"单位"通常对应3行文本，但网站可能自定义处理
            robot.mouseWheel(3); // 向下滚动3个单位

            // 更自然的滚动：随机滚动距离和速度
          //  simulateNaturalScroll(robot, 5);

        } catch (Exception e) {
            e.printStackTrace();
        }
        Thread.sleep(2000);

    }
}