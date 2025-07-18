package com.zl.task.save.base;

import com.zl.task.vo.http.HttpVO;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class SaveXHRImpl<M> implements SaveXHR<M> {

    @Override
    public void save(String sDir) throws Exception {

    }

    @Override
    public List<M> parserJson(String json, HttpVO vo) throws UnsupportedEncodingException {
        // TODO 解析json
        return null;
    }

    @Override
    public HttpVO parserUrl(String json) {
        return null;
    }

    @Override
    public boolean saveSql() {
        return false;
    }
}
