package com.zl.dao.generate;

import java.util.Date;

/**
 * @Description:
 * @Param:
 * @Auther: zl
 * @Date: 2025-02-21
 */
public class HourLiveRankDO {
    private Integer watchCnt = 0;
    private Integer fansUcntP1d = 0;
    private Date liveStartTime = null;
    private Long authorId = 0L;
    private Double payAmt = 0.0;
    private Long roomId = 0L;
    private Integer productClickCnt = 0;
    private Integer liveDuration = 0;
    private String nickname = "";
    private Date startTime = null;
    private Date endTime = null;
    private String roomTitle = "";
    private Integer categoryId = 0;

    public Integer getWatchCnt() {
        return watchCnt;
    }

    public void setWatchCnt(Integer watchCnt) {
        this.watchCnt = watchCnt;
    }

    public Integer getFansUcntP1d() {
        return fansUcntP1d;
    }

    public void setFansUcntP1d(Integer fansUcntP1d) {
        this.fansUcntP1d = fansUcntP1d;
    }

    public Date getLiveStartTime() {
        return liveStartTime;
    }

    public void setLiveStartTime(Date liveStartTime) {
        this.liveStartTime = liveStartTime;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public Double getPayAmt() {
        return payAmt;
    }

    public void setPayAmt(Double payAmt) {
        this.payAmt = payAmt;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Integer getProductClickCnt() {
        return productClickCnt;
    }

    public void setProductClickCnt(Integer productClickCnt) {
        this.productClickCnt = productClickCnt;
    }

    public Integer getLiveDuration() {
        return liveDuration;
    }

    public void setLiveDuration(Integer liveDuration) {
        this.liveDuration = liveDuration;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getRoomTitle() {
        return roomTitle;
    }

    public void setRoomTitle(String roomTitle) {
        this.roomTitle = roomTitle;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

}
