package com.util.jdbc.generator.bean;

import java.util.Map;

/**
 * @className: com.craw.nd.common.generator.dao.bean-> BenaType
 * @description: java Bean格式
 * @author: zl
 * @createDate: 2023-01-02 14:17
 * @version: 1.0
 * @todo:
 */
public class BeanType {
    private String beanFilePath; //Bean文件保存路径
    private String className;   //类名
    private String packageName; //包名
    private Map<String, ClassBean> memberMap; //类成员列表

    public Map<String, ClassBean> getMemberMap() {
        return memberMap;
    }

    public void setMemberMap(Map<String, ClassBean> memberMap) {
        this.memberMap = memberMap;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getBeanFilePath() {
        return beanFilePath;
    }

    public void setBeanFilePath(String beanFilePath) {
        this.beanFilePath = beanFilePath;
    }
}
