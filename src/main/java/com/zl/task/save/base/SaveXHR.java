package com.zl.task.save.base;

import com.zl.task.vo.http.HttpVO;

import java.io.UnsupportedEncodingException;
import java.util.List;


public interface SaveXHR<T> {
    void save(String sDir) throws Exception;

    List<T> parserJson(String json, HttpVO vo) throws UnsupportedEncodingException;

    HttpVO parserUrl(String json);

    boolean saveSql();
}
