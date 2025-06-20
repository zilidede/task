package com.zl.dao.generate;

import java.util.Date;

/**
 * @Description:
 * @Param:
 * @Auther: zl
 * @Date: 2024-07-12
 */
public class OceanenSearchKeywordsVideoDO {
    private Integer videoShowCnt;
    private Date recordTime;
    private Double videoInteractRate;
    private Double videoPlayOverRate;
    private String videoKeywordList;
    private String videoId;
    private String videoTitle;
    private String keyword;

    public Integer getVideoShowCnt() {
        return videoShowCnt;
    }

    public void setVideoShowCnt(Integer videoShowCnt) {
        this.videoShowCnt = videoShowCnt;
    }

    public Date getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
    }

    public Double getVideoInteractRate() {
        return videoInteractRate;
    }

    public void setVideoInteractRate(Double videoInteractRate) {
        this.videoInteractRate = videoInteractRate;
    }

    public Double getVideoPlayOverRate() {
        return videoPlayOverRate;
    }

    public void setVideoPlayOverRate(Double videoPlayOverRate) {
        this.videoPlayOverRate = videoPlayOverRate;
    }

    public String getVideoKeywordList() {
        return videoKeywordList;
    }

    public void setVideoKeywordList(String videoKeywordList) {
        this.videoKeywordList = videoKeywordList;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

}
