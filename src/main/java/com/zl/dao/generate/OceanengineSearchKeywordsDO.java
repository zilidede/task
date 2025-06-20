package com.zl.dao.generate;

import java.util.Date;

/**
 * @Description:
 * @Param:
 * @Auther: zl
 * @Date: 2023-12-14
 */
public class OceanengineSearchKeywordsDO {
    private String keyWordDetailPath;
    private Date recordEndTime;
    private Double clickRate;
    private Double traderRate;
    private Integer orderCount;
    private Integer runningCount;
    private Integer searchCount;
    private Date recordStartTime;
    private String keyword;
    private Integer orderSaleCount;

    public String getKeyWordDetailPath() {
        return keyWordDetailPath;
    }

    public void setKeyWordDetailPath(String keyWordDetailPath) {
        this.keyWordDetailPath = keyWordDetailPath;
    }

    public Date getRecordEndTime() {
        return recordEndTime;
    }

    public void setRecordEndTime(Date recordEndTime) {
        this.recordEndTime = recordEndTime;
    }

    public Double getClickRate() {
        return clickRate;
    }

    public void setClickRate(Double clickRate) {
        this.clickRate = clickRate;
    }

    public Double getTraderRate() {
        return traderRate;
    }

    public void setTraderRate(Double traderRate) {
        this.traderRate = traderRate;
    }

    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }

    public Integer getRunningCount() {
        return runningCount;
    }

    public void setRunningCount(Integer runningCount) {
        this.runningCount = runningCount;
    }

    public Integer getSearchCount() {
        return searchCount;
    }

    public void setSearchCount(Integer searchCount) {
        this.searchCount = searchCount;
    }

    public Date getRecordStartTime() {
        return recordStartTime;
    }

    public void setRecordStartTime(Date recordStartTime) {
        this.recordStartTime = recordStartTime;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getOrderSaleCount() {
        return orderSaleCount;
    }

    public void setOrderSaleCount(Integer orderSaleCount) {
        this.orderSaleCount = orderSaleCount;
    }

}
