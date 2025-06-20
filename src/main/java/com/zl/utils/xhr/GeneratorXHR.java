package com.zl.utils.xhr;


import com.zl.utils.io.DiskIoUtils;
import com.zl.utils.io.FileIoUtils;
import com.zl.utils.jdbc.generator.convert.FieldConvert;
import com.zl.utils.other.Ini4jUtils;

import java.util.Map;

/**
 * @className: com.craw.nd.service.task.craw.xhr.ini-> GeneratorXHR
 * @description: 生成接口文件保存目录以及fiddler接口保存文件
 * @author: zl
 * @createDate: 2024-01-25 16:20
 * @version: 1.0
 * @todo:
 */
public class GeneratorXHR {
    public static void main(String[] args) throws Exception {

        Ini4jUtils.loadIni("./data/task/xhr.ini");
        Ini4jUtils.setSectionValue("weather");
        Map<String, String> map = Ini4jUtils.getWini().get("weather");
        String dir = "S:\\data\\task\\爬虫\\weather\\";
        FileIoUtils.clearTxtFile("./data/task/fiddler.java");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String s1 = map.get(entry.getKey());
            String value = map.get(entry.getKey()).replaceAll("/", "-").replaceAll("&", "-").replaceAll("\\?", "-").replaceAll("_", "-");
            String filePath = dir + FieldConvert.toCameCase(value) + "\\";
            DiskIoUtils.createDir(filePath);
            String s = generateFiddler(map.get(entry.getKey()), filePath);
            FileIoUtils.writeTxtFile("./data/task/fiddler.java", s + "\t\n", "utf-8");
        }
    }

    public static String generateFiddler(String interfaceName, String filePath) {
        filePath = filePath.replaceAll("\\\\", "\\\\\\\\");
        String s = String.format("if (oSession.fullUrl.Contains(\"%s\")) {\n" +
                "            var fso;\n" +
                "            var file;\n" +
                "            var now = new Date();\n" +
                "            var month=now.getMonth()+1;\n" +
                "            var ts = now.getFullYear()+\"-\"+month+\"-\"+now.getDate()+\"-\"+now.getDay()+\"-\"+now.getHours()+\"-\"+now.getMinutes()+\"-\"+now.getSeconds()+\"-\"+now.getMilliseconds();\n" +
                "            fso = new ActiveXObject(\"Adodb.Stream\");\n" +
                "            fso.Charset = \"utf-8\";\n" +
                "            fso.Open();\n" +
                "            fso.WriteText(\"url: \" + oSession.fullUrl + \" Request body: \" + oSession.GetRequestBodyAsString + \" Response body: \" + oSession.GetResponseBodyAsString());\n" +
                "            fso.SaveToFile( \"%s\"+ts+\".txt\",  2 );\n" +
                "        } ", interfaceName, filePath);
        return s;

    }
}
