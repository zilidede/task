package com.util.jdbc.generator.bean;

/**
 * @className: com.craw.nd.common.generator.dao.bean-> ClassBean
 * @description: class 结构
 * @author: zl
 * @createDate: 2023-01-02 23:50
 * @version: 1.0
 * @todo:
 */
public class ClassBean {
    private String className;
    private String classType;
    private Object classValue;

    public Object getClassValue() {
        return classValue;
    }

    public void setClassValue(Object classValue) {
        this.classValue = classValue;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassType() {
        return classType;
    }


    public void setClassType(String classType) {
        this.classType = classType;
    }
}
