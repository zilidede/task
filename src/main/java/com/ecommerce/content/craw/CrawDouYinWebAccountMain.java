package com.ecommerce.content.craw;

import com.ecommerce.impl.ContentProcessDouYinWebAccountMainImpl;
import com.ll.drissonPage.base.By;
import com.ll.drissonPage.element.ChromiumElement;
import com.ll.drissonPage.page.ChromiumTab;
import com.zl.task.craw.SaveXHR;
import com.zl.task.craw.base.CrawBaseXHR;
import com.zl.task.impl.SaveService;
import com.zl.task.impl.SaveServiceImpl;
import com.zl.task.vo.task.taskResource.DefaultTaskResourceCrawTabList;
import com.zl.utils.io.FileIoUtils;
import com.zl.utils.log.LoggerUtils;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;

/**
 * @program: content-craw
 * @description:爬取抖音主页短视频达人所有视频 Page=https://www.douyin.com/user/MS4wLjABAAAA_y02UjgVpQGXY1qT8cpdJ28K6vhvYXfWPbRVMQzj2Rk?from_tab_name=main;
 * @author: zl
 * @create: 2025-09-15 15:05
 **/

public class CrawDouYinWebAccountMain extends CrawBaseXHR {
    private final SaveService saveService = new SaveServiceImpl();

    public CrawDouYinWebAccountMain(ChromiumTab tab) throws Exception {
        super(tab);
        super.init("mainVideo");
    }


    public static void main(String[] args) throws Exception {
        CrawDouYinWebAccountMain crawDouYinMainVideoService = new CrawDouYinWebAccountMain(DefaultTaskResourceCrawTabList.getTabList().get(3));
        crawDouYinMainVideoService.run();
    }

    public void run() throws IOException {
        // 获取达人主页信息 达人主页短视频记录； 保存为xhr;
        crawMainPage(cmdUserInput());
        //
        // 对比达人视频数据库中记录,更新达人短视频增量数据；
        // 下载达人短视频；
    }

    public List<String> cmdUserInput() throws IOException {
        //cmd用户输入函数   1. 用户主页面 2. 读取默认配置文件 - 达人号和达人ID；
        //返回主页url
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== 抖音爬虫工具 ===");
        System.out.println("请选择输入方式:");
        System.out.println("1. 手动输入URL");
        System.out.println("2. 使用默认配置文件");
        System.out.print("请输入选项 (1 或 2): ");
        int choice=2 ;
       // int choice = scanner.nextInt();
       // scanner.nextLine(); // 消费换行符
        String url = "";

        switch (choice) {
            case 1:
                System.out.print("请输入抖音用户主页URL: ");
                url = scanner.nextLine();
                if (url != null && !url.isEmpty()) {
                    return null;
                } else {
                    System.out.println("URL不能为空!");
                    return cmdUserInput();
                }
            case 2:
                // 读取默认配置文件逻辑
                System.out.println("正在读取默认配置文件...");
                return readFromConfigFile();
            default:
                System.out.println("无效选项，请重新输入!");
                return cmdUserInput();
        }
    }

    private List<String> readFromConfigFile() throws IOException {
        // 实现读取配置文件的逻辑
        // 返回达人号和达人ID
        String content= FileIoUtils.readFile("./data/task/mainAccount");
        String [] strs=content.split("\n");
        List<String> urls=new ArrayList<>();
        for (String str:strs){
            str=str.split("#")[1];
            urls.add(str);
        }
        return urls;
    }
    public void crawMainPage(List<String> urls) {
        for(String url:urls) {
            // 获取达人主页信息
            try {
                // 访问页面
                getTab().get(url);
                Thread.sleep(5000);
                // 等待页面加载完成
                //  wait = new WebDriverWait(driver, Duration.ofSeconds(15));
                //  wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.video-list")));

                // 滚动页面加载更多内容
                scrollAndLoadMore();

                // 提取页面数据
                //   String pageSource = driver.getPageSource();
                //  parseDouYinData(pageSource);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    public void scrollAndLoadMore() throws Exception {
        getTab().listen().start(getXhrList()); ;
        // 获取初始页面高度
        Integer lastHeight = (Integer) getTab().runJs("return document.body.scrollHeight");

        while (true) {
            //
            Robot robot = new Robot();
            // 获取当前鼠标位置
            int x = java.awt.MouseInfo.getPointerInfo().getLocation().x;
            int y = java.awt.MouseInfo.getPointerInfo().getLocation().y;
            robot.mouseMove(x, y);
           // Thread.sleep(300); // 短暂停顿
            // 模拟鼠标滚轮向下滚动（负值为向上滚动，正值为向下滚动）
            // 注意：每个"单位"通常对应3行文本，但网站可能自定义处理
           // robot.mouseWheel(7); // 向下滚动3个单位
            simulateNaturalScroll(robot, 9); // 更自然的滚动：随机滚动距离和速度
            // 等待页面加载新内容
            try {
                Thread.sleep(2000); // 可根据实际情况调整等待时间
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String xpath = "//*[@style=\"width:100%\"]";
            try {
                ChromiumElement element=getTab().ele(By.xpath(xpath));
                String text=element.innerHtml();
                if(text.contains("暂时没有更多了")){
                    LoggerUtils.logger.info("没有更多数据");
                    break;
                }
            }
            catch (Exception e){
                System.out.println("加载所有数据");
            }
            ContentProcessDouYinWebAccountMainImpl contentProcess=new ContentProcessDouYinWebAccountMainImpl();
            try {
                SaveXHR.saveXhr(getTab(), getXhrSaveDir(), getXhrList(),contentProcess);
            }
            catch (Exception e){
                e.printStackTrace();
                continue;
            }

        }
    }
    public static void simulateNaturalScroll(Robot robot, int scrollSteps) throws InterruptedException {
        for (int i = 0; i < scrollSteps; i++) {
            // 随机滚动距离（1-3个单位）
            int scrollAmount = 1 + (int)(Math.random() * 3);
            // 随机滚动方向（大多数向下，偶尔向上模拟真实用户）
            if (Math.random() > 0.85) {
                scrollAmount = -scrollAmount;
            }

            robot.mouseWheel(scrollAmount);

            // 随机等待时间（更自然的用户行为）
            int waitTime = 100 + (int)(Math.random() * 300);
            Thread.sleep(waitTime);

            // 每3-5步稍微长一点的停顿
            if (i % 4 == 0 && Math.random() > 0.3) {
                Thread.sleep(300 + (int)(Math.random() * 400));
            }
        }
    }
        public void save () {
            //
        }

        @Override
        public void craw () throws Exception {

        }


    }
