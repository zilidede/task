package com.zl.utils.jdbc.generator.bean;

/**
 * @className: com.craw.nd.common.generator.dao.bean-> ClassMemberBean
 * @description: class公有成员
 * @author: zl
 * @createDate: 2023-01-03 13:29
 * @version: 1.0
 * @todo:
 */
public class ClassMemberBean {

    private String getParameterType;
    private String returnParameterType;

    public String getGetParameterType() {
        return getParameterType;
    }

    public void setGetParameterType(String getParameterType) {
        this.getParameterType = getParameterType;
    }

    public String getReturnParameterType() {
        return returnParameterType;
    }

    public void setReturnParameterType(String returnParameterType) {
        this.returnParameterType = returnParameterType;
    }
}
