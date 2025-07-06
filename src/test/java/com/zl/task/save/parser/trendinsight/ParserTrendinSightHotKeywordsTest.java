package com.zl.task.save.parser.trendinsight;

import com.zl.task.impl.SaveService;
import com.zl.task.save.parser.ParserJsonToHttpVO;
import com.zl.task.vo.http.HttpVO;
import org.junit.Before;

import java.io.IOException;
import java.sql.SQLException;

public class ParserTrendinSightHotKeywordsTest {
    ParserTrendinSightHotKeywords parser;
    @Before
    public void setUp() throws SQLException {
        parser=new ParserTrendinSightHotKeywords();

    }

    @org.junit.Test
    public void parserRelationWord() throws Exception {
        HttpVO vo = ParserJsonToHttpVO.parserXHRJson("./data/test/keywordHotTrend");
        parser.parserHotKeywords(vo);
        parser.save();


    }
}