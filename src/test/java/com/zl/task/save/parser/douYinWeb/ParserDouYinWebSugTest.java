package com.zl.task.save.parser.douYinWeb;

import com.zl.task.save.parser.ParserJsonToHttpVO;
import com.zl.task.vo.http.HttpVO;
import org.junit.Test;


import java.util.List;



public class ParserDouYinWebSugTest {

    @Test
    public void parser() {
        try {
            ParserDouYinWebSug parser = new ParserDouYinWebSug();
            HttpVO httpVO= ParserJsonToHttpVO.parserXHRJson("D:\\work\\task\\data\\test\\sug.json");
            List<String> list = parser.parser(httpVO);
            for (String s : list) {
                System.out.println(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}