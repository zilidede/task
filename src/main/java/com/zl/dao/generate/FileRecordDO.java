package com.zl.dao.generate;

import java.time.OffsetDateTime;
import java.util.*;

/**
 * @Description:
 * @Param:
 * @Auther: zl
 * @Date: 2025-06-11
 */
public class FileRecordDO {
    private String fileName = ""; //文件名
    private String fileLocalPath = ""; //文件本地路径
    private Integer fileStatus = 0; //文件状态 0-未处理 1-解析成功 2-保存成功 -1-解析失败 -2-保存失败
    private OffsetDateTime fileLastUpdate = null; //文件最后更新时间
    private String fileMd5 = ""; //文件md5

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileLocalPath() {
        return fileLocalPath;
    }

    public void setFileLocalPath(String fileLocalPath) {
        this.fileLocalPath = fileLocalPath;
    }

    public Integer getFileStatus() {
        return fileStatus;
    }

    public void setFileStatus(Integer fileStatus) {
        this.fileStatus = fileStatus;
    }

    public OffsetDateTime getFileLastUpdate() {
        return fileLastUpdate;
    }

    public void setFileLastUpdate(OffsetDateTime fileLastUpdate) {
        this.fileLastUpdate = fileLastUpdate;
    }

    public String getFileMd5() {
        return fileMd5;
    }

    public void setFileMd5(String fileMd5) {
        this.fileMd5 = fileMd5;
    }

}
