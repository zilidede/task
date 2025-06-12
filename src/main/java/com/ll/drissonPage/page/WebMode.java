package com.ll.drissonPage.page;

/**
 * 'd' 或 's'，即driver模式和session模式
 *
 * @author 陆
 * @address <a href="https://t.me/blanksig"/>click
 */
public enum WebMode {
    s("s"), d("d"), NULL("d");
    final String mode;

    WebMode(String mode) {
        this.mode = mode;
    }

}