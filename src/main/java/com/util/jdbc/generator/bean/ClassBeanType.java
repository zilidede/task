package com.util.jdbc.generator.bean;

/**
 * @className: com.craw.nd.common.generator.bean-> GenerateType
 * @description: 自动生成bean
 * @author: zl
 * @createDate: 2023-02-11 15:40
 * @version: 1.0
 * @todo:
 */
public class ClassBeanType {
    private String className;
    private String programDir;
    private String packageName;

    private String filePath;
    private String fileContent;

    public String getFileContent() {
        return fileContent;
    }

    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
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

    public String getProgramDir() {
        return programDir;
    }


    public void setProgramDir(String programDir) {
        this.programDir = programDir;
    }
}
