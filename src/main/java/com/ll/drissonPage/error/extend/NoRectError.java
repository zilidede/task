package com.ll.drissonPage.error.extend;

import com.ll.drissonPage.error.BaseError;

/**
 * @author 陆
 * @address <a href="https://t.me/blanksig"/>click
 * @original DrissionPage
 */
public class NoRectError extends BaseError {
    public NoRectError(String info) {
        super(info);
    }

    public NoRectError() {
        super("该元素没有位置及大小");
    }
}
