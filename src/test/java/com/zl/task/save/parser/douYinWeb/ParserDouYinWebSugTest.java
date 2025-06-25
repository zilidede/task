package com.zl.task.save.parser.douYinWeb;

import com.zl.task.save.parser.ParserFiddlerJson;
import com.zl.task.vo.http.HttpVO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ParserDouYinWebSugTest {

    @Test
    void parser() {
        try {
            ParserDouYinWebSug parser = new ParserDouYinWebSug();
            HttpVO httpVO=ParserFiddlerJson.parserXHRJson("D:\\work\\task\\data\\test\\sug.json");
            List<String> list = parser.parser(httpVO);
            for (String s : list) {
                System.out.println(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}