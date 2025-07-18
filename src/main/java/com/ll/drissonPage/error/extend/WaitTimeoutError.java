package com.ll.drissonPage.error.extend;

import com.ll.drissonPage.error.BaseError;

/**
 * @author 陆
 * @address <a href="https://t.me/blanksig"/>click
 * @original DrissionPage
 */
public class WaitTimeoutError extends BaseError {
    public WaitTimeoutError(String info) {
        super(info);
    }

    public WaitTimeoutError() {
        super("等待失败。");
    }
}
