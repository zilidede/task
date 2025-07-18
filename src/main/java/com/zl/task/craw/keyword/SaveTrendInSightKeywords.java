package com.zl.task.craw.keyword;

import com.zl.dao.generate.FileRecordDO;
import com.zl.task.save.parser.ParserJsonToHttpVO;
import com.zl.task.vo.http.HttpVO;
import com.zl.task.vo.other.GenericListContainerVO;

import java.sql.SQLException;

// 保存巨量算数趋势洞察关键词
public class SaveTrendInSightKeywords {
    ParserTrendinSightHotKeywords parserTrendinSightHotKeywords;
    public SaveTrendInSightKeywords() throws SQLException {
        parserTrendinSightHotKeywords=new ParserTrendinSightHotKeywords();
    }
    public  void save(GenericListContainerVO containerVO) throws Exception {

    }
    public  void saveHotKeywords(FileRecordDO fileRecordDO) throws Exception {
        HttpVO vo = ParserJsonToHttpVO.parserXHRJson(fileRecordDO.getFileLocalPath());
        parserTrendinSightHotKeywords.parserHotKeywords(vo);
        parserTrendinSightHotKeywords.save();
    }
    public void saveRelaKeywords(FileRecordDO fileRecordDO){

    }
}
