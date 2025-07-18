package com.zl.dao.generate;

import java.util.Date;

/**
 * @Description:
 * @Param:
 * @Auther: zl
 * @Date: 2025-02-20
 */
public class LiveMinRecordDO {
    private Double gpm = 0.0;
    private String plaform = "";
    private Integer watchUcnt = 0;
    private String viewingClickRate = "";
    private Integer payCnt = 0;
    private Long authorId = 0L;
    private Integer payUcnt = 0;
    private Double payAmt = 0.0;
    private Integer leaveUcnt = 0;
    private Long roomId = 0L;
    private Date recordTime = null;
    private Double clickTransactionRate = 0.0;
    private Integer onlineUserCnt = 0;
    private Integer fansInc = 0;
    private Integer commentCnt = 0;
    private Double entryRate = 0.0;

    public Double getGpm() {
        return gpm;
    }

    public void setGpm(Double gpm) {
        this.gpm = gpm;
    }

    public String getPlaform() {
        return plaform;
    }

    public void setPlaform(String plaform) {
        this.plaform = plaform;
    }

    public Integer getWatchUcnt() {
        return watchUcnt;
    }

    public void setWatchUcnt(Integer watchUcnt) {
        this.watchUcnt = watchUcnt;
    }

    public String getViewingClickRate() {
        return viewingClickRate;
    }

    public void setViewingClickRate(String viewingClickRate) {
        this.viewingClickRate = viewingClickRate;
    }

    public Integer getPayCnt() {
        return payCnt;
    }

    public void setPayCnt(Integer payCnt) {
        this.payCnt = payCnt;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public Integer getPayUcnt() {
        return payUcnt;
    }

    public void setPayUcnt(Integer payUcnt) {
        this.payUcnt = payUcnt;
    }

    public Double getPayAmt() {
        return payAmt;
    }

    public void setPayAmt(Double payAmt) {
        this.payAmt = payAmt;
    }

    public Integer getLeaveUcnt() {
        return leaveUcnt;
    }

    public void setLeaveUcnt(Integer leaveUcnt) {
        this.leaveUcnt = leaveUcnt;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Date getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
    }

    public Double getClickTransactionRate() {
        return clickTransactionRate;
    }

    public void setClickTransactionRate(Double clickTransactionRate) {
        this.clickTransactionRate = clickTransactionRate;
    }

    public Integer getOnlineUserCnt() {
        return onlineUserCnt;
    }

    public void setOnlineUserCnt(Integer onlineUserCnt) {
        this.onlineUserCnt = onlineUserCnt;
    }

    public Integer getFansInc() {
        return fansInc;
    }

    public void setFansInc(Integer fansInc) {
        this.fansInc = fansInc;
    }

    public Integer getCommentCnt() {
        return commentCnt;
    }

    public void setCommentCnt(Integer commentCnt) {
        this.commentCnt = commentCnt;
    }

    public Double getEntryRate() {
        return entryRate;
    }

    public void setEntryRate(Double entryRate) {
        this.entryRate = entryRate;
    }

}
