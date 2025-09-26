package com.ecommerce.impl;

import java.io.IOException;
// 内容处理接口
public interface ContentProcessor {
    void process(String content) throws IOException;
}
