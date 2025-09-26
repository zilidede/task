package com.zl.task.craw.base.x;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ll.drissonPage.page.ChromiumTab;
import com.ll.drissonPage.units.listener.DataPacket;
import com.zl.task.process.keyword.CypherData;
import com.zl.task.vo.task.taskResource.TaskVO;
import com.zl.utils.io.DiskIoUtils;
import com.zl.utils.io.FileIoUtils;
import com.util.jdbc.generator.convert.FieldConvert;
import com.zl.utils.log.LoggerUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class CrawServiceXImpl implements CrawServiceX {
    private ChromiumTab tab;
    private List<String> xhrList;

    public CrawServiceXImpl() {
        xhrList = new ArrayList<>();
    }


    @Override
    public void run(TaskVO task) throws Exception {

    }

    @Override
    public void openUrl(String url, Double timeout) {
        tab.get(url, timeout);
    }

    @Override
    public ChromiumTab getTab() {
        return this.tab;
    }

    @Override
    public void setTab(ChromiumTab tab) {
        this.tab = tab;
    }

    public String saveXhr(String fileDir, DataPacket data) {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss-SSS");
        String timestamp = sdf.format(now);
        String target = data.getTarget();
        for (String s : xhrList) {
            if (s.equals(target)) {
                target = target.replaceAll("/", "-").replaceAll("&", "-").replaceAll("\\?", "-").replaceAll("_", "-");
                fileDir = fileDir + "\\" + FieldConvert.toCameCase(target) + "\\";
                break;
            }
        }

        // 示例URL和响应体（这里需要替换为实际值）
        String url = data.url();
        String requestBody = data.request().postData();
        String responseBody = data.response().rawBody();
        // 构造文件路径
        String filePath = "";
        String computerName = System.getenv("COMPUTERNAME");
        if (computerName == null) {
            // 如果上面的方法不奏效，尝试使用USERNAME环境变量
            computerName = System.getenv("USERNAME");
        }
        if (!DiskIoUtils.isDir(fileDir)) {
            DiskIoUtils.createDir(fileDir);
        }
        filePath = fileDir + computerName + "&" + timestamp + ".txt";
        // 写入文件
        FileIoUtils.writeToFile(filePath, "url: " + url + " Request body: " + requestBody + " Response body: " + responseBody);
        return filePath;
    }



    public String saveXhr(String fileDir, DataPacket data, CypherData cy)  {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss-SSS");
        String timestamp = sdf.format(now);
        String target = data.getTarget();
        for (String s : xhrList) {
            if (s.equals(target)) {
                target = target.replaceAll("/", "-").replaceAll("&", "-").replaceAll("\\?", "-").replaceAll("_", "-");
                fileDir = fileDir + "\\" + FieldConvert.toCameCase(target) + "\\";
                break;
            }
        }

        // 示例URL和响应体（这里需要替换为实际值）
        String url = data.url();
        String requestBody = data.request().postData();
        String responseBody = data.response().rawBody();
        JsonParser parser = new JsonParser();
        try{
            String json = data.response().rawBody();
            JsonObject object = parser.parse(json).getAsJsonObject();
            responseBody = cy.decrypt((object.get("data").getAsString()));
        }
        catch (Exception e){
            LoggerUtils.logger.debug("解密失败");
        }

        // 构造文件路径
        String filePath = "";
        String computerName = System.getenv("COMPUTERNAME");
        if (computerName == null) {
            // 如果上面的方法不奏效，尝试使用USERNAME环境变量
            computerName = System.getenv("USERNAME");
        }
        if (!DiskIoUtils.isDir(fileDir)) {
            DiskIoUtils.createDir(fileDir);
        }
        filePath = fileDir + computerName + "&" + timestamp + ".txt";
        // 写入文件
        FileIoUtils.writeToFile(filePath, "url: " + url + " Request body: " + requestBody + " Response body: " + responseBody);
        return filePath;
    }

    public List<String> getXHRList() {
        return xhrList;
    }

    public void setXHRList(List<String> xhrList) {
        this.xhrList = xhrList;
    }
    public void setXHRList(Map<String, String> map) {
        xhrList.clear();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            xhrList.add(entry.getValue());
        }
    }

}
