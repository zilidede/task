package com.zl.dao.generate;

import java.util.Date;

/**
 * @Description:
 * @Param:
 * @Auther: zl
 * @Date: 2025-03-01
 */
public class AccountGoodsDO {
    private Integer accountId = 0;
    private Date recordTime = null;
    private Integer goodsId = 0;
    private String accountType = "";
    private Integer douYinId = 0;
    private String liveFeatures = "";
    private String liveRecordPath = "";
    private String priceDomain = "";
    private Integer categoryId = 0;
    private String goodsFeatures = "";
    private Integer fans = 0;

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Date getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public Integer getDouYinId() {
        return douYinId;
    }

    public void setDouYinId(Integer douYinId) {
        this.douYinId = douYinId;
    }

    public String getLiveFeatures() {
        return liveFeatures;
    }

    public void setLiveFeatures(String liveFeatures) {
        this.liveFeatures = liveFeatures;
    }

    public String getLiveRecordPath() {
        return liveRecordPath;
    }

    public void setLiveRecordPath(String liveRecordPath) {
        this.liveRecordPath = liveRecordPath;
    }

    public String getPriceDomain() {
        return priceDomain;
    }

    public void setPriceDomain(String priceDomain) {
        this.priceDomain = priceDomain;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getGoodsFeatures() {
        return goodsFeatures;
    }

    public void setGoodsFeatures(String goodsFeatures) {
        this.goodsFeatures = goodsFeatures;
    }

    public Integer getFans() {
        return fans;
    }

    public void setFans(Integer fans) {
        this.fans = fans;
    }

}
