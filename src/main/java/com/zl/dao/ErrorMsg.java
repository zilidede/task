package com.zl.dao;

/**
 * @className: com.craw.nd.vo-> ErrorVO
 * @description:
 * @author: zl
 * @createDate: 2023-01-04 12:55
 * @version: 1.0
 * @todo:
 */
public class ErrorMsg {
    private String msg;
    private Integer code;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
