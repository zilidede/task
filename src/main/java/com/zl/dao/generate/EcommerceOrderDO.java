package com.zl.dao.generate;

import java.util.Date;

/**
 * @Description:
 * @Param:
 * @Auther: zl
 * @Date: 2023-10-21
 */
public class EcommerceOrderDO {
    private String orderSource = "";
    private String userProfileTag = "";
    private String orderId;
    private String buyersInfo = "";
    private String userNickname = "";
    private String goodsSku = "";
    private Long userId = 0L;
    private Double orderPayAmount = 0.0;
    private String goodsIds = "";
    private Date orderSubmitTime;
    private Date orderFinishTime;
    private String childOrderId = "";
    private String shopId = "";
    private String buyeerAddr = "";
    private Date orderDeliverTime;
    private String goodsCount = "";
    private String afterSale = "";
    private Double profit = 0.0;
    private String platform;

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public Double getProfit() {
        return profit;
    }

    public void setProfit(Double profit) {
        this.profit = profit;
    }

    public String getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(String goodsCount) {
        this.goodsCount = goodsCount;
    }

    public Date getOrderDeliverTime() {
        return orderDeliverTime;
    }

    public void setOrderDeliverTime(Date orderDeliverTime) {
        this.orderDeliverTime = orderDeliverTime;
    }

    public void setAfterSale(String afterSale) {
        this.afterSale = afterSale;
    }

    public String getAfterSale() {
        return afterSale;
    }

    public String getOrderSource() {
        return orderSource;
    }

    public void setOrderSource(String orderSource) {
        this.orderSource = orderSource;
    }

    public String getUserProfileTag() {
        return userProfileTag;
    }

    public void setUserProfileTag(String userProfileTag) {
        this.userProfileTag = userProfileTag;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getBuyersInfo() {
        return buyersInfo;
    }

    public void setBuyersInfo(String buyersInfo) {
        this.buyersInfo = buyersInfo;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getGoodsSku() {
        return goodsSku;
    }

    public void setGoodsSku(String goodsSku) {
        this.goodsSku = goodsSku;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getOrderPayAmount() {
        return orderPayAmount;
    }

    public void setOrderPayAmount(Double orderPayAmount) {
        this.orderPayAmount = orderPayAmount;
    }

    public String getGoodsIds() {
        return goodsIds;
    }

    public void setGoodsIds(String goodsIds) {
        this.goodsIds = goodsIds;
    }

    public Date getOrderSubmitTime() {
        return orderSubmitTime;
    }

    public void setOrderSubmitTime(Date orderSubmitTime) {
        this.orderSubmitTime = orderSubmitTime;
    }

    public Date getOrderFinishTime() {
        return orderFinishTime;
    }

    public void setOrderFinishTime(Date orderFinishTime) {
        this.orderFinishTime = orderFinishTime;
    }

    public String getChildOrderId() {
        return childOrderId;
    }

    public void setChildOrderId(String childOrderId) {
        this.childOrderId = childOrderId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getBuyeerAddr() {
        return buyeerAddr;
    }

    public void setBuyeerAddr(String buyeerAddr) {
        this.buyeerAddr = buyeerAddr;
    }

}
