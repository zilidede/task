package com.zl.task.craw;

import com.ll.drissonPage.page.ChromiumTab;
import com.ll.drissonPage.units.listener.DataPacket;
import com.zl.utils.io.FileIoUtils;
import com.zl.utils.jdbc.generator.convert.FieldConvert;
import com.zl.utils.log.LoggerUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SaveXHR {
    public static void saveXhr(ChromiumTab tab, String xhrSaveDir, List<String> xhrList) {
        List<DataPacket> res = tab.listen().waits(1000, 5.1, false, true);
        if (res.size() >= 1) {
            for (DataPacket data : res)
                if (data != null)
                    try {
                        saveFile(xhrSaveDir, data, xhrList);
                    } catch (Exception e) {
                        LoggerUtils.logger.info("保存文件失败：" + data.url());
                    }

        } else {
            System.out.println("error");
            LoggerUtils.logger.warn("无需要保存的记录");
        }
    }

    private static void saveFile(String xhrSaveDir, DataPacket data, List<String> xhrList) throws Exception {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss-SSS");
        String timestamp = sdf.format(now);
        String target = data.getTarget();
        for (String s : xhrList) {
            if (s.equals(target)) {
                target = target.replaceAll("/", "-").replaceAll("&", "-").replaceAll("\\?", "-").replaceAll("_", "-");
                target = FieldConvert.toCameCase(target) + "\\";
                xhrSaveDir = xhrSaveDir + target;
                break;
            }
        }
        // 示例URL和响应体（这里需要替换为实际值）
        String url = data.url();
        String requestBody = data.request().postData();
        String responseBody = data.response().rawBody();
        // 构造文件路径
        String filePath = "";
        filePath = xhrSaveDir + timestamp + ".txt";
        // 写入文件
        FileIoUtils.writeToFile(filePath, "url: " + url + " Request body: " + requestBody + " Response body: " + responseBody);
    }
}
