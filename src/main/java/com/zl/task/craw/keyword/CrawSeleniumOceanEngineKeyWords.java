package com.zl.task.craw.keyword;


import com.ll.drissonPage.base.By;
import com.ll.drissonPage.element.ChromiumElement;
import com.ll.drissonPage.page.ChromiumTab;
import com.ll.drissonPage.units.listener.DataPacket;
import com.zl.dao.generate.OceanenSearchKeywordsDetailDao;
import com.zl.task.impl.ExecutorTaskService;
import com.zl.task.vo.task.taskResource.TaskResource;
import com.zl.task.vo.task.taskResource.TaskVO;
import com.zl.utils.io.DiskIoUtils;
import com.zl.utils.io.FileIoUtils;
import com.zl.utils.io.LatestCreatedFileFinderUtils;
import com.zl.utils.jdbc.generator.jdbc.DefaultDatabaseConnect;
import com.zl.utils.log.LoggerUtils;
import com.zl.utils.other.Ini4jUtils;
import com.zl.utils.time.TimeUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @className: com.craw.nd.service.other.person.Impl.craw.selenium-> CrawOceanEngineSearch
 * @description: 爬取巨量云图搜索词实现类
 * @author: zl
 * @createDate: 2023-04-07 17:14
 * @version: 1.0
 * @todo: 搜索策略
 * 观看观看视频列表；
 * 搜后观看商品；
 * 相关字图谱；
 * 热词下载；
 */
public class CrawSeleniumOceanEngineKeyWords implements ExecutorTaskService {


    private final Map<String, Integer> keywordDetailMaps;
    private final OceanenSearchKeywordsDetailDao dao;
    private List<Integer> categoryIds;
    private List<Integer> unCrawCategoryIds;
    private Boolean flag = false; // 开启搜索关键字详情爬取标识； true=开启 false
    private Map<String, Integer> keyWords;
    private ChromiumTab tab;
    private boolean secondFlag = false;//二级类目标识
    private String srcDir; //原始下载目录
    private final String xhrDir; // xhr保存目录
    private final Integer maxCrawCount = 1000; // 最大爬取次数
    private int crawCount = 0;//爬取次数 ;

    public CrawSeleniumOceanEngineKeyWords() throws Exception {
        keywordDetailMaps = new HashMap<>();
        dao = new OceanenSearchKeywordsDetailDao(DefaultDatabaseConnect.getConn());
        flag = true; //开启搜索关键字详情爬取标识； true=开启 false
        secondFlag = false;
        Ini4jUtils.loadIni("./data/config/config.ini");
        Ini4jUtils.setSectionValue("yunTu");
        srcDir = Ini4jUtils.readIni("srcDir");
        xhrDir = Ini4jUtils.readIni("xhrDir");
    }

    public void setSecondFlag(boolean secondFlag) {
        this.secondFlag = secondFlag;
    }

    public boolean isSecondFlag() {
        return secondFlag;
    }

    public void setSrcDir(String srcDir) {
        this.srcDir = srcDir;
    }

    public String getSrcDir() {
        return srcDir;
    }

    public static void main(String[] args) throws Exception {
        CrawSeleniumOceanEngineKeyWords craw = new CrawSeleniumOceanEngineKeyWords();

        craw.renameDownloadFile("s:\\", "D:\\data\\task\\爬虫\\yunTu\\巨量云图行业搜索词\\", "2024-12-01", "食品饮料", 1735651800);

    }

    public static String crawAll(CrawSeleniumOceanEngineKeyWords crawler, String name) throws Exception {
        TaskVO taskvo = new TaskVO(1, "云图搜索词");
        String s = FileIoUtils.readTxtFile("./data/task/云图行业.txt", "utf-8");
        String[] strings = s.split("\r\n");
        Ini4jUtils.loadIni("./data/config/config.ini");
        Ini4jUtils.setSectionValue("yunTu");
        String industryName = Ini4jUtils.readIni("industryName");
        Boolean flag = true; //载入标志
        for (int i = 0; i < strings.length; i++) {
            taskvo = new TaskVO(1, "云图搜索词");
            taskvo.setTaskDesc(strings[i]);
            //载入上次位进入
            if (!name.equals("") && !taskvo.getTaskDesc().equals(name)) {
                continue;
            }
            if (!industryName.equals("") && flag) {
                if (!taskvo.getTaskDesc().equals(industryName)) {
                    LoggerUtils.logger.info("爬取的巨量云图搜索词类目：" + taskvo.getTaskDesc() + "今日已爬取，爬取下一类目");
                    continue;
                } else
                    flag = false;
            } else {
                flag = false;
            }
            crawler.openEnterUrl("https://yuntu.oceanengine.com/yuntu_ng/search_strategy/search_words?aadvid=1760501554223111");
            try {
                LoggerUtils.logger.info("爬取巨量云图搜索词类目" + taskvo.getTaskDesc());
                crawler.run(taskvo);
                LoggerUtils.logger.info("爬取巨量云图搜索词类目" + taskvo.getTaskDesc() + "已完成");
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
        //重置为"
        Map<String, String> map = new HashMap<>();
        map.put("industryName", "");
        Ini4jUtils.writeIni(map);
        return "quit";
    }

    public ChromiumTab getTab() {
        return tab;
    }

    public void setTab(ChromiumTab tab) {
        this.tab = tab;
    }


    @Override
    public void ExecutorTaskService(TaskResource taskResource) {

    }

    @Override
    public void ExecutorTaskService(Object object) {

    }

    public void run(TaskVO vo) throws Exception {
        setKeywordDetailMaps();
        vo.setStatus(selectAll(vo.getTaskDesc()));
    }

    void openEnterUrl(String url) throws InterruptedException {
        tab.get(url);
        Thread.sleep(1000 * 10);
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        //开启关键字详情爬取标识； true=开启 false
        this.flag = flag;
    }

    public void setKeywordDetailMaps() throws SQLException, UnsupportedEncodingException {
        dao.findOcSearchKeywordsDetail(keywordDetailMaps);
    }


    public int selectAll(String name) throws IOException, InterruptedException {
        int i = 0;
        try {

            i = select(name);
            i = 3;// 已经完成任务;
        } catch (Exception ex) {
            ex.printStackTrace();
            Thread.sleep(1000 * 10);
            i = 0;
        }
        return i;
    }

    public int select(String name) throws InterruptedException, IOException {
        // 0-无异常 -1-中断（中断保存当前进度） -2-停止 -3-警告
        //name=*-全部 遍历行业类目
        ChromiumElement element = null;
        //选择类目
        String xpath = "//*[@data-log-value=\"行业搜索词\"]";
        element = tab.ele(By.xpath(xpath));
        Thread.sleep(1000);
        element.click().click();
        Thread.sleep(1000);
        String[] names = name.split("/");
        xpath = "";

        xpath = "//*[@class=\"search-strategy-icon search-strategy-icon-down search-strategy-cascader-arrow\"]";
        element = tab.eles(By.xpath(xpath)).get(0);
        Thread.sleep(1000);
        element.click().click();
        Thread.sleep(1000);
        //选择行业框
        xpath = "//*[@class=\"ReactVirtualized__Grid__innerScrollContainer\"]";
        List<ChromiumElement> elements = tab.eles(By.xpath(xpath));
        Thread.sleep(1000 * 2);
        element = elements.get(0);
        Thread.sleep(1000 * 2);
        elements = element.eles(By.xpath("./div"));
        //行业类目
        Boolean flag1 = true;
        for (int i = 0; i < elements.size(); i++) {
            String s = "";
            s = elements.get(i).text();
            if (s.equals("生鲜")) {
                System.out.println();
            }
            String industyName = s;
            tab.actions().moveTo(elements.get(i));
            Thread.sleep(1000);
            // tab.mouseWheel(elements.get(i), 0);
            //重新建立选择框
            if (i == 15 && flag1) {
                // getWebDriverUtils().getKeyBoardUtils().mouseMouseElement(elements.get(i),0,0);
                //  Thread.sleep(1000);
                xpath = "//*[@class=\"ReactVirtualized__Grid__innerScrollContainer\"]";
                elements = tab.eles(By.xpath(xpath));
                Thread.sleep(1000);
                element = elements.get(0);
                Thread.sleep(1000);
                elements = element.eles(By.xpath("./div"));
                i = 6;
                continue;
            }
            if (!names[0].equals("*")) {
                if (names.length < 3 || !names[0].equals(s))
                    continue;
            }
            Thread.sleep(1000);
            Thread.sleep(1000);
            //确认
            elements.get(i).click().click();
            Boolean flag2 = true;
            Thread.sleep(1000);
            //一级类目
            xpath = "//*[@class=\"ReactVirtualized__Grid__innerScrollContainer\"]";
            ChromiumElement element1 = tab.eles(By.xpath(xpath)).get(1);
            List<ChromiumElement> elements1 = element1.eles(By.xpath("./div"));
            for (int j = 0; j < elements1.size(); j++) {
                s = elements1.get(j).text();
                if (!names[1].equals("*")) {
                    if (names.length < 3 || !names[1].equals(s))
                        continue;
                }
                tab.actions().moveTo(elements1.get(j));
                Thread.sleep(1000);
                elements1.get(j).click().click();
                Thread.sleep(1000 * 3);
                String industryName1 = elements.get(i).text() + "-" + elements1.get(j).text();
                if (downloadIndustry(industryName1) == -1)
                    return -1;
                /*
                //获取搜索词详情
                if (flag) {
                    if (downloadKeyWordDetails() < 0) {
                        LoggerUtils.logger.warn(s + "类目获取搜索词详情失败，休眠3分钟");
                        return -1;
                        //退出程序重新爬取此类目；
                    }
                    else {
                        LoggerUtils.logger.debug(s + "获取搜索词详情爬取完成,休眠1分钟");
                        Thread.sleep(1000 * 60 * 1);
                    }
                }

                 */
                //超长子类
                if (j == 15 && flag2) {
                    //重新打开选择框
                    xpath = "//*[@class=\"search-strategy-icon search-strategy-icon-down search-strategy-cascader-arrow\"]";

                    element = tab.eles(By.xpath(xpath)).get(0);
                    element.click().click();
                    Thread.sleep(1000);

                    // getWebDriverUtils().getKeyBoardUtils().mouseMouseElement(elements.get(i),0,0);
                    //  Thread.sleep(1000);
                    xpath = "//*[@class=\"ReactVirtualized__Grid__innerScrollContainer\"]";
                    elements1 = tab.eles(By.xpath(xpath));
                    Thread.sleep(1000);
                    element = elements1.get(1);
                    Thread.sleep(1000);
                    elements1 = element.eles(By.xpath("./div"));
                    j = 4;
                    flag2 = false;
                    continue;

                }
                //二级类目
                //重新打开选择框
                xpath = "//*[@class=\"search-strategy-icon search-strategy-icon-down search-strategy-cascader-arrow\"]";
                element = tab.eles(By.xpath(xpath)).get(0);
                element.click().click();
                Thread.sleep(1000);
                //二级类目
                xpath = "//*[@class=\"ReactVirtualized__Grid__innerScrollContainer\"]";
                //二级类目 secondFlag爬取开关
                if (secondFlag) {
                    ChromiumElement element2 = tab.eles(By.xpath(xpath)).get(2);
                    List<ChromiumElement> elements2 = element2.eles(By.xpath("./div"));
                    for (int k = 0; k < elements2.size(); k++) {
                        // 非关注类目不爬取二级类目
                      //  if (!industyName.equals("服饰内衣") || !industyName.equals("鞋靴箱包") || !industyName.equals("运动户外") || !industyName.equals("母婴宠物")) {
                      //      break;
                     //   }
                        try {
                            s = elements2.get(k).text();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            System.out.println(s);
                        }
                        String className = s;
                        if (!names[2].equals("*")) {
                            if (names.length < 3 || !names[2].equals(s))
                                continue;
                        }
                        //选择类目;
                        Thread.sleep(1000);
                        //getWebDriverUtils().getKeyBoardUtils().mouseWheel(elements2.get(k), 0);
                        elements2.get(k).click().click();
                        // getJse().executeScript("arguments[0].click().click();", elements2.get(k));
                        Thread.sleep(1000);
                        xpath = "//*[@class=\"search-strategy-pager-text\"]";
                        try {
                        //    List<ChromiumElement> elements3 = tab.eles(By.xpath(xpath));
                        //    Thread.sleep(1000);
                           // elements3.get(1).click().click();
                        //    Thread.sleep(1000);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        industryName1 = elements.get(i).text() + "-" + elements1.get(j).text() + "-" + elements2.get(k).text();
                        if (downloadIndustry(industryName1) == -1)
                            return -1;

                        //获取搜索词详情
                        if (flag) {
                            if (downloadKeyWordDetails() < 0) {
                                LoggerUtils.logger.warn(s + "类目获取搜索词详情失败，休眠3分钟");
                                return -1;
                                //退出程序重新爬取此类目；
                            }
                            else {
                                LoggerUtils.logger.debug(s + "获取搜索词详情爬取完成,休眠1分钟");
                                Thread.sleep(1000 * 60 * 1);
                            }
                        }
                        //记录总数字
                        xpath = "//*[@class=\"search-strategy-pager-record\"]";
                        s = tab.ele(By.xpath(xpath)).text();
                        s = s.replaceAll("共", "");
                        s = s.replaceAll("条记录", "");
                        Integer count = Integer.parseInt(s);
                        String content = className + "-" + count + "\n";
                        LoggerUtils.logger.info(className + "类目搜索词数量：" + count);
                        //重新打开选择框
                        xpath = "//*[@class=\"search-strategy-icon search-strategy-icon-down search-strategy-cascader-arrow\"]";
                        tab.eles(By.xpath(xpath)).get(0).click().click();
                        Thread.sleep(1000);
                        if (k == 15) {
                            xpath = "//*[@class=\"ReactVirtualized__Grid__innerScrollContainer\"]";
                            elements2 = tab.eles(By.xpath(xpath));
                            Thread.sleep(1000);
                            element = elements2.get(2);
                            Thread.sleep(1000);
                            elements2 = element.eles(By.xpath("./div"));
                            k = 0;
                        }
                    }
                }

            }
            return 0;

        }
        return 0;

    }


    //下载当前页所有搜索词详细信息；
    public int downloadKeyWordDetails() throws InterruptedException {
        String xpath = "";
        List<ChromiumElement> elements;
        Thread.sleep(1000);
        Integer len = 0;
        //获取总条数
        try {
            xpath = "//*[@class=\"search-strategy-pager-record\"]";
            elements = tab.eles(By.xpath(xpath));
            if (elements.size() > 0) {
                String s = elements.get(0).text();
                s = s.replaceAll("条记录", "");
                s = s.replaceAll("共", "");
                len = Integer.parseInt(s) / 10;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return -1;
        }
        //排序 -搜索量
//*[@id="garfish_app_for_search_strategy_18nut35u"]/div/div[2]/div/div/div[3]/div/div[3]/div[2]/div/div[1]/div[1]/div/table/thead/tr/th[7]/span/span[2]/button
        xpath = "//*[@class=\"search-strategy-Table-HeadCellContentTrigger\"]";
        ChromiumElement element = tab.eles(By.xpath(xpath)).get(3);
        if (element != null) {
            element.click().click();
            Thread.sleep(1000);
        }
        //循环列表
        for (int j = 0; j < len; j++) {
            xpath = "//*[@class=\"search-strategy-Table-Body\"]";
            elements = tab.eles(By.xpath(xpath)).get(0).eles(By.xpath("./tr"));
            //当前页搜索词爬取
            for (int i = 0; i < 10; i++) {
                if (i > elements.size())
                    continue;
                element = elements.get(i).ele(By.xpath("td[3]/div/span/span/div"));
                // System.out.println(element.text());
                if (!keywordDetailMaps.containsKey(element.text())) {
                    tab.listen().start("lite_keywords_packet/get_search_word_detail"); //监听搜索词
                    if (downloadKeyWordDetail(element.text(), element) < 0)
                        return -1;
                    keywordDetailMaps.put(element.text(), 0);
                }
            }
            xpath = "//*[@class=\"search-strategy-pager-item-group\"]";
            elements = tab.eles(By.xpath(xpath));
            Thread.sleep(1000);
            if (elements.size() > 0) {
                try {
                    elements = elements.get(0).eles(By.xpath("./li"));
                    element = elements.get(elements.size() - 1);
                    element.click().click();
                    Thread.sleep(1000);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    LoggerUtils.logger.warn("获取页码失败");
                    return -1;
                }
            }


        }
        xpath = "//*[@class=\"search-strategy-pager-item-group\"]";
        elements = tab.eles(By.xpath(xpath));
        Thread.sleep(1000);
        elements = elements.get(0).eles(By.xpath("./li"));
        Thread.sleep(1000);
        elements.get(1).click().click();
        Thread.sleep(1000);
        return 0;

    }

    public int downloadKeyWordDetail(String keyword, ChromiumElement element) throws InterruptedException {
        if (crawCount++ > maxCrawCount) {
            LoggerUtils.logger.warn("已达到爬取次数上限，休眠15分钟");
            Thread.sleep(15 * 60 * 1000);
            crawCount = 0;
        }
        //获取当前页搜索词详细信息；
        try {
            try {
                if (openKeywordDetailPage(keyword, element) < 0)
                    return -1;
                Thread.sleep(3000);
                List<DataPacket> res = tab.listen().waits();
                if (res.size() >= 1) {
                    saveFile(res.get(0));
                } else {
                    System.out.println("error");
                }

            } catch (Exception e) {
                e.printStackTrace();

            }
            // 关闭详情页
            try {
                String xpath = "//*[@style=\"top: 0px; margin-top: 20px;\"]";

                List<ChromiumElement> element1 = tab.eles(By.xpath(xpath)); //关闭详情页按钮
                Thread.sleep(500);
                element1.get(1).click().click();
                Thread.sleep(1000);

            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;

    }

    // 打开单一搜索词详情页
    public int openKeywordDetailPage(String keyword, ChromiumElement element) throws InterruptedException {
        //0-sus -1=失败
        element.click().click(); //展开详情
        Thread.sleep(2000);
        try {
            String xpath = "//*[@style=\"position: absolute; inset: 0px; pointer-events: auto; z-index: 1; display: block;\"]";
            //  ChromiumElement element1 = getTab().ele(By.xpath(xpath));
            List<By> byList = new ArrayList<>();
            By by = By.xpath(xpath);
            byList.add(by);
            if (!tab.waits().elesLoaded(byList, 10.0, true, true)) {
                LoggerUtils.logger.warn(keyword + "搜索词详情页失败，休眠5分钟");
                Thread.sleep(5 * 60 * 1000);
            } else {
                LoggerUtils.logger.debug(keyword + "搜索词详情页成功");
            }
        } catch (Exception ex) {
            LoggerUtils.logger.warn("打开单一搜索词详情页失败，返回下级类目");
            Thread.sleep(60 * 1000);
            return -1;
        }
        return 0;


    }

    public int downloadIndustry(String industryName) throws InterruptedException {
        //
        long currentTimeMillis = System.currentTimeMillis() / 1000;
        //下载行业关键字
        String xpath = "";
        ChromiumElement element = null;
        int i = 0;
        xpath = "//*[@class=\"search-strategy-btn search-strategy-btn-size-md search-strategy-btn-type-primary search-strategy-btn-shape-angle search-strategy-can-input-grouped\"]";
        List<ChromiumElement> elements = tab.eles(By.xpath(xpath));
        elements.get(0).click().click(); //下载csv
        Thread.sleep(1000);
        elements = tab.eles(By.xpath(xpath));
        elements.get(2).click().click();
        Thread.sleep(1000);
        xpath = "//*[@data-log-value=\"行业搜索词\"]";
        tab.ele(By.xpath(xpath)).click().click();
        Thread.sleep(1000);
        //
        //*[@id="garfish_app_for_search_strategy_bddtkivs"]/div/div[2]/div/div/div[1]/div[2]/div[2]/div[2]/span/span/label
        String date = getDate();

        if (date.equals(""))
            LoggerUtils.logger.error("日期获取失败");
        else
            renameDownloadFile(srcDir, xhrDir + "\\巨量云图行业搜索词\\", date, industryName, currentTimeMillis);
        return 0;
    }

    public String getDate() throws InterruptedException {
        //获取搜索词行业大盘日期
        String xpath = "//*[@class=\"search-strategy-input-inner__wrapper search-strategy-input-inner__wrapper-border search-strategy-input-inner__wrapper-size-md search-strategy-input-inner__wrapper-add-suffix search-strategy-input-inner__wrapper-filled\"]";
        ChromiumElement element = tab.eles(By.xpath(xpath)).get(2);
        Thread.sleep(1000);
        String regex = "value=\"([^\"]*)\"";
        // 编译正则表达式
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(element.innerHtml());
        // 查找并提取 value 的值
        if (matcher.find()) {
            String value = matcher.group(1);  // 提取第一个捕获组的内容
            return value;
        } else {
            return "";
        }

    }

    public void saveFile(DataPacket data) throws Exception {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss-SSS");
        String timestamp = sdf.format(now);

        // 示例URL和响应体（这里需要替换为实际值）
        String url = data.url();
        String responseBody = null;
        try {
            responseBody = data.response().rawBody();
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
        String computerName = System.getenv("COMPUTERNAME");
        if (computerName == null) {
            // 如果上面的方法不奏效，尝试使用USERNAME环境变量
            computerName = System.getenv("USERNAME");
        }
        // 构造文件路径
        String filePath = xhrDir + "liteKeywordsPacketGetSearchWordDetail\\" + computerName + "&" + timestamp + ".txt";
        // 写入文件
        FileIoUtils.writeToFile(filePath, "url: " + url + "\nResponse body: " + responseBody);
    }

    public void renameDownloadFile(String srcDir, String desDir, String date, String industryName, long createTime) {
        //重命名下载的巨量云图行业搜索词文件
        //判断改文件是否存在；
        String industryName1 = industryName.replace("/", "-");
        List<String> csvFiles = DiskIoUtils.listFilesByExtension(srcDir, "csv");
        Optional<LatestCreatedFileFinderUtils.FileWithCreationTime> latestFile = LatestCreatedFileFinderUtils.findLatestCreatedFile(csvFiles);
        if (TimeUtils.isDifferenceLessThanSeconds(latestFile.get().getCreationTime(), createTime) > 10) {

            String desFilePath = desDir + String.format("%s-%s.csv", date, industryName1);
            FileIoUtils.aRenameFile(srcDir + latestFile.get().getFileName(), desFilePath);
        } else {
            //该行业文件无法找到，下载失败
            LoggerUtils.logger.error(String.format("%s-%s:在下载目录无法找到：下载失败", date, industryName));
        }

    }

    public void crawSingleKeywords(List<String> keywords) throws InterruptedException {
        openEnterUrl("https://yuntu.oceanengine.com/yuntu_lite/search_strategy/search_words");
        String xpath="//*[@class=\"search-strategy-cascader search-strategy-cascader-select search-strategy-can-input-grouped\"]" ;
        List<ChromiumElement> elements=getTab().eles(By.xpath(xpath)); //打开搜索词输入框
        elements.get(1).click().click();
        Thread.sleep(1000);
        for (String keyword : keywords) {
            xpath= "//*[@class=\"search-strategy-input search-strategy-input-size-md\"]";
            List<ChromiumElement> elements1=getTab().eles(By.xpath(xpath)); //输入搜索词
            Thread.sleep(1000);
            ChromiumElement element=elements1.get(5);
            element.input( keyword);
            Thread.sleep(2000);
            xpath= "//*[@class=\"search-strategy-list-item-inner-wrapper search-strategy-cascader-item-inner-wrapper\"]";;
            elements1=getTab().eles(By.xpath(xpath)); //选择搜索框
            elements1.get(0).click().click();
            xpath = "//*[@class=\"search-strategy-btn search-strategy-btn-size-md search-strategy-btn-type-primary search-strategy-btn-shape-angle search-strategy-can-input-grouped\"]";
            elements = tab.eles(By.xpath(xpath));
            elements.get(1).click().click(); //下载csv
            //下载相关性图谱
            downloadKeyWordDetails();
            for (ChromiumElement element1 : elements1){
                String s=element1.text();
                // System.out.println(s);
            }
            Thread.sleep(2000);
            //重新打开搜索框；
            xpath="//*[@class=\"search-strategy-cascader search-strategy-cascader-select search-strategy-can-input-grouped\"]" ;
            elements=getTab().eles(By.xpath(xpath)); //打开搜索词输入框
            elements.get(1).click().click();
        }
    }
}
