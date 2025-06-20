package com.zl.task.craw.base;

import com.ll.drissonPage.page.ChromiumTab;
import com.ll.drissonPage.units.listener.DataPacket;
import com.zl.utils.io.FileIoUtils;
import com.zl.utils.jdbc.generator.convert.FieldConvert;
import com.zl.utils.log.LoggerUtils;
import com.zl.utils.other.Ini4jUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 实现了CrawServiceXHRTab接口的类，用于处理XHR（XMLHttpRequest）相关的爬虫任务
 */
public abstract class CrawServiceXHRTabImpl implements CrawServiceXHRTab {
    private ChromiumTab tab; // 使用的浏览器标签页
    private String xhrSaveDir; // xhr保存目录
    private String xhr; // xhr名称
    private final List<String> xhrList; // xhr列表

    /**
     * 构造函数
     *
     * @param tab        浏览器标签页对象
     * @param xhr        xhr名称
     * @param xhrSaveDir xhr保存目录
     * @throws IOException 当初始化XHR监听失败时抛出
     */
    public CrawServiceXHRTabImpl(ChromiumTab tab, String xhr, String xhrSaveDir) throws IOException {
        this.tab = tab;
        this.xhrSaveDir = xhrSaveDir;
        this.xhr = xhr;
        xhrList = listenXHR(tab);
    }

    /**
     * 监听XHR请求
     *
     * @return 返回监听的XHR请求列表
     * @throws IOException 当读取INI配置文件失败时抛出
     */
    @Override
    public List<String> listenXHR(ChromiumTab tab) throws IOException {
        List<String> list = new ArrayList<>();
        Ini4jUtils.loadIni("data/task/xhr.ini");
        Map<String, String> map = Ini4jUtils.traSpecificSection(xhr);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            list.add(entry.getValue());
        }
        tab.listen().start(list); // 监听xhr
        return list;
    }

    /**
     * 保存XHR请求和响应数据
     */
    @Override
    public void saveXHR(ChromiumTab tab) {
        List<DataPacket> res = tab.listen().waits(100, 2.1, false, true);
        if (res.size() >= 1) {
            for (DataPacket data : res) {
                if (data != null)
                    try {
                        saveFile(xhrSaveDir, data);
                    } catch (Exception e) {
                        LoggerUtils.logger.info("保存文件失败：" + data.url());
                    }
            }

        } else {

            System.out.println("error");
        }
        res.clear();
    }

    /**
     * 保存XHR数据到文件
     *
     * @param fileDir 保存文件的目录
     * @param data    XHR请求和响应数据包
     * @throws Exception 当保存文件失败时抛出
     */
    @Override
    public void saveFile(String fileDir, DataPacket data) throws Exception {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss-SSS");
        String timestamp = sdf.format(now);
        String target = data.getTarget();
        for (String s : xhrList) {
            if (target.equals(s)) {
                String value = s.replaceAll("/", "-").replaceAll("&", "-").replaceAll("\\?", "-").replaceAll("_", "-");
                fileDir = fileDir + "\\" + FieldConvert.toCameCase(value) + "\\";
                break;
            }
        }
        // 示例URL和响应体（这里需要替换为实际值）
        String url = data.url();
        String requestBody = data.request().postData();
        String responseBody = data.response().rawBody();
        // 构造文件路径
        String filePath = "";
        filePath = fileDir + timestamp + ".txt";
        // 写入文件
        FileIoUtils.writeToFile(filePath, "url: " + url + " Request body: " + requestBody + " Response body: " + responseBody);
    }

    /**
     * 打开指定URL
     *
     * @param url     要打开的URL
     * @param timeout 超时时间
     */
    @Override
    public void openUrl(String url, Double timeout) {
        tab.get(url, timeout);
    }

    /**
     * 获取XHR名称
     *
     * @return 返回xhr名称
     */
    @Override
    public String getXHR() {
        return xhr;
    }

    /**
     * 设置XHR名称
     *
     * @param xhr xhr名称
     */
    @Override
    public void setXHR(String xhr) {
        this.xhr = xhr;
    }

    /**
     * 获取浏览器标签页
     *
     * @return 返回浏览器标签页对象
     */
    @Override
    public ChromiumTab getTab() {
        return tab;
    }

    /**
     * 设置浏览器标签页
     *
     * @param tab 浏览器标签页对象
     */
    @Override
    public void setTab(ChromiumTab tab) {
        this.tab = tab;
    }

    /**
     * 获取XHR保存目录
     *
     * @return 返回xhr保存目录
     */
    @Override
    public String getXHRDir() {
        return xhrSaveDir;
    }

    /**
     * 设置XHR保存目录
     *
     * @param xhrDir xhr保存目录
     */
    @Override
    public void setXHRDir(String xhrDir) {
        this.xhrSaveDir = xhrDir;
    }

}
