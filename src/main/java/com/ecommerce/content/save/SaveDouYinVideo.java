package com.ecommerce.content.save;

import com.zl.dao.generate.LiveRecordDO;
import com.zl.task.impl.SaveServiceImpl;
import com.zl.task.save.base.SaveXHRImpl;
import com.zl.task.save.parser.ParserJsonToHttpVO;
import com.zl.task.vo.http.HttpVO;
import com.zl.utils.io.DiskIoUtils;
import com.zl.utils.io.FileIoUtils;

import java.util.List;

// 解析抖音mainVideo xhr 请求
public class SaveDouYinVideo  extends SaveXHRImpl<LiveRecordDO> {
    public static void main(String[] args) throws Exception {
        SaveDouYinVideo saver=new SaveDouYinVideo();
        saver.save("D:\\data\\task\\爬虫\\mainVideo");
    }
    private final SaveServiceImpl saveService = new SaveServiceImpl();
    @Override
    public void save(String sDir) throws Exception {
        String dir = sDir + "\\awemeV1WebAwemePostDevicePlatform=webappAid=";
        List<String> files = DiskIoUtils.getFileListFromDir(dir);
        for (String file : files) {
            HttpVO httpVO = ParserJsonToHttpVO.parserXHRJson(file);
            String url = httpVO.getUrl();
            String json = httpVO.getResponse().getBody();
            FileIoUtils.writeToFile("d:/1.txt",json);
           // parserLiveRecordJson(json, httpVO);
        }

       // saveService.savePgSql(daoService, liveRecordDOS);
        //liveRecordDOS.clear();
    }
}
