package com.zl.dao.generate;

import java.util.Date;

/**
 * @Description:
 * @Param:
 * @Auther: zl
 * @Date: 2025-02-20
 */
public class LiveRecordDO {
    private Integer userNum = 0;
    private String liveTrafficMap = "";
    private Integer exposedNum = 0;
    private String previewPic = "";
    private Double uvVal = 0.0;
    private String title = "";
    private Integer avgUserDuration = 0;
    private Long roomId = 0L;
    private Integer sales = 0;
    private String platform = "";
    private String coverUrl = "";
    private Double gmv = 0.0;
    private Integer transactionNum = 0;
    private Integer userCount = 0;
    private Integer totalUser = 0;
    private Integer liveTime = null;
    private Integer fansInc = 0;
    private String categories = "";
    private Date startLive = null;

    public Integer getUserNum() {
        return userNum;
    }

    public void setUserNum(Integer userNum) {
        this.userNum = userNum;
    }

    public String getLiveTrafficMap() {
        return liveTrafficMap;
    }

    public void setLiveTrafficMap(String liveTrafficMap) {
        this.liveTrafficMap = liveTrafficMap;
    }

    public Integer getExposedNum() {
        return exposedNum;
    }

    public void setExposedNum(Integer exposedNum) {
        this.exposedNum = exposedNum;
    }

    public String getPreviewPic() {
        return previewPic;
    }

    public void setPreviewPic(String previewPic) {
        this.previewPic = previewPic;
    }

    public Double getUvVal() {
        return uvVal;
    }

    public void setUvVal(Double uvVal) {
        this.uvVal = uvVal;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getAvgUserDuration() {
        return avgUserDuration;
    }

    public void setAvgUserDuration(Integer avgUserDuration) {
        this.avgUserDuration = avgUserDuration;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Integer getSales() {
        return sales;
    }

    public void setSales(Integer sales) {
        this.sales = sales;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public Double getGmv() {
        return gmv;
    }

    public void setGmv(Double gmv) {
        this.gmv = gmv;
    }

    public Integer getTransactionNum() {
        return transactionNum;
    }

    public void setTransactionNum(Integer transactionNum) {
        this.transactionNum = transactionNum;
    }

    public Integer getUserCount() {
        return userCount;
    }

    public void setUserCount(Integer userCount) {
        this.userCount = userCount;
    }

    public Integer getTotalUser() {
        return totalUser;
    }

    public void setTotalUser(Integer totalUser) {
        this.totalUser = totalUser;
    }

    public Integer getLiveTime() {
        return liveTime;
    }

    public void setLiveTime(Integer liveTime) {
        this.liveTime = liveTime;
    }

    public Integer getFansInc() {
        return fansInc;
    }

    public void setFansInc(Integer fansInc) {
        this.fansInc = fansInc;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public Date getStartLive() {
        return startLive;
    }

    public void setStartLive(Date startLive) {
        this.startLive = startLive;
    }

}
