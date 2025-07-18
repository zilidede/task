package com.zl.task.category;

import com.ll.drissonPage.page.ChromiumTab;
import com.zl.task.craw.keyword.CrawSeleniumOceanEngineKeyWords;
import com.zl.task.save.Saver;
import com.zl.task.vo.task.taskResource.TaskVO;
import com.zl.utils.io.FileIoUtils;
import com.zl.utils.log.LoggerUtils;
import com.zl.utils.other.Ini4jUtils;

import java.util.HashMap;
import java.util.Map;

// 添加搜索词
public class CrawSearchKeyword {
    public static final String YUN_TU_URL = "https://yuntu.oceanengine.com/yuntu_ng/search_strategy/search_words?aadvid=1760501554223111";
    public static final String INDUSTRY_FILE_PATH = "./data/task/云图行业.txt";
    public static final String CONFIG_FILE_PATH = "./data/config/config.ini";
    public static final String SECTION_NAME = "yunTu";

    /**
     * 爬取云图搜索关键词
     * 该方法负责启动爬虫程序，按照预定逻辑循环爬取云图搜索关键词，直到满足退出条件
     *

     * @throws Exception 如果爬取过程中发生错误，则抛出异常
     */
    public static void crawYunTuSearchKeyword(CrawSeleniumOceanEngineKeyWords crawler, String categoryFilePath ) throws Exception {

        String rName = "";
        while (!"quit".equals(rName)) {
            try {
                Saver.save();
                rName = crawAll(crawler, rName,categoryFilePath);

            } catch (Exception e) {
                LoggerUtils.logger.error("爬取过程中发生异常: " + e.getMessage());
                e.printStackTrace();
                sleepWithInterruptCheck(60_000); // 休眠60秒
            }
        }

        // 可选：添加资源释放逻辑
        if (crawler != null) {
            // crawler.close(); // 若有close方法
        }
    }

    /**
     * 安全地进行线程休眠，保留中断状态
     *
     * @param millis 休眠毫秒数
     */
    private static void sleepWithInterruptCheck(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // 恢复中断状态
            LoggerUtils.logger.error("线程被中断");
            throw new RuntimeException("线程被中断", e);
        }
    }

    /**
     * 爬取所有云图搜索关键词
     * 该方法负责具体执行云图搜索关键词的爬取逻辑，包括读取任务、设置任务描述、打开URL、运行爬虫等步骤
     *
     * @param crawler 爬虫对象，用于执行爬取操作
     * @param name    上一次爬取的名称，用于确定从哪个任务开始爬取
     * @return String 返回下一次爬取的名称，如果为"quit"则表示爬取完成
     * @throws Exception 如果爬取过程中发生错误，则抛出异常
     */
    public static String crawAll(CrawSeleniumOceanEngineKeyWords crawler, String name,String categoryFilePath) throws Exception {
        // 初始化任务对象
        TaskVO taskvo = new TaskVO(1, "云图搜索词");
        // 读取任务文件内容

        String s = FileIoUtils.readTxtFile(categoryFilePath, "utf-8");
        String[] strings = s.split("\r\n");

        // 加载配置文件
        Ini4jUtils.loadIni("./data/config/config.ini");
        Ini4jUtils.setSectionValue("yunTu");
        String industryName = Ini4jUtils.readIni("industryName");
        Boolean flag = true; // 载入标志
        // 遍历任务列表
        for (int i = 0; i < strings.length; i++) {
            taskvo = new TaskVO(1, "云图搜索词");
            taskvo.setTaskDesc(strings[i]);

            // 载入上次位进入
            if (!name.equals("") && !taskvo.getTaskDesc().equals(name)) {
                continue;
            }
            // 检查是否需要跳过已爬取的任务
            if (!industryName.equals("") && flag) {
                if (!taskvo.getTaskDesc().equals(industryName)) {
                    LoggerUtils.logger.info("爬取的巨量云图搜索词类目：" + taskvo.getTaskDesc() + "今日已爬取，爬取下一类目");
                    continue;
                } else
                    flag = false;
            } else {
                flag = false;
            }

            // 打开爬取URL
            crawler.openEnterUrl("https://yuntu.oceanengine.com/yuntu_ng/search_strategy/search_words?aadvid=1760501554223111");

            try {
                // 记录日志并执行爬取操作
                LoggerUtils.logger.info("爬取巨量云图搜索词类目" + taskvo.getTaskDesc());
                crawler.run(taskvo);
                LoggerUtils.logger.info("爬取巨量云图搜索词类目" + taskvo.getTaskDesc() + "已完成");
                // 更新已爬取的行业名称
                industryName = taskvo.getTaskDesc();
                Map<String, String> map = new HashMap<>();
                map.put("industryName", industryName);
                Ini4jUtils.writeIni(map);
            } catch (Exception e) {
                System.out.println(taskvo.getTaskDesc() + "爬取失败");
                LoggerUtils.logger.error("爬取巨量云图搜索词类目" + taskvo.getTaskName() + "失败，重新载入tab 爬取下一类目");
                return taskvo.getTaskDesc();
            }
        }

        // 重置行业名称为""
        Map<String, String> map = new HashMap<>();
        map.put("industryName", "");
        Ini4jUtils.writeIni(map);

        return "quit";
    }


}
