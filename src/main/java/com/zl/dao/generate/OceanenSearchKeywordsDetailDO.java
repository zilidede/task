package com.zl.dao.generate;

import java.util.Date;

/**
 * @Description:
 * @Param:
 * @Auther: zl
 * @Date: 2024-07-12
 */
public class OceanenSearchKeywordsDetailDO {
    private Date recordTime;
    private Integer keywordCount;
    private String keyword;
    private String platformSource;

    public String getPlatformSource() {
        return platformSource;
    }

    public void setPlatformSource(String platformSource) {
        this.platformSource = platformSource;
    }

    public Date getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
    }

    public Integer getKeywordCount() {
        return keywordCount;
    }

    public void setKeywordCount(Integer keywordPath) {
        this.keywordCount = keywordPath;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

}
