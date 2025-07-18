package com.zl.dao.generate;

import java.util.Date;

/**
 * @Description:
 * @Param:
 * @Auther: zl
 * @Date: 2024-03-14
 */
public class GoodsProfitDO {
    private Double wholeSale = 0.00;
    private Double anchorTradePoints = 0.00;
    private Double afterSaleRate = 0.00;
    private Long goodsId;
    private Double acclaimCost = 0.00;
    private Double feeRoi = 0.00;
    private String goodsMainImg;
    private Double otherTradePoints = 0.00;
    private Double brandTradePoints = 0.00;
    private Double attritionCosts = 0.00;
    private Double giftsCost = 0.00;
    private Double expressCost = 0.00;
    private Double broadcasterTradePoints = 0.00;
    private Double transportInsuranceCost = 0.00;
    private Double platformTradePoints = 0.00;
    public Date startSaleTime;
    public Date endSaleTime;
    public Date recordTime;
    public Double otherCost = 0.00;
    public String goodsName;

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Date getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
    }

    public Double getOtherCost() {
        return otherCost;
    }

    public void setOtherCost(Double otherCost) {
        this.otherCost = otherCost;
    }

    public Date getStartSaleTime() {
        return startSaleTime;
    }

    public void setStartSaleTime(Date startSaleTime) {
        this.startSaleTime = startSaleTime;
    }

    public void setEndSaleTime(Date endSaleTime) {
        this.endSaleTime = endSaleTime;
    }

    public Date getEndSaleTime() {
        return endSaleTime;
    }

    public Double getWholeSale() {
        return wholeSale;
    }

    public void setWholeSale(Double wholeSale) {
        this.wholeSale = wholeSale;
    }

    public Double getAnchorTradePoints() {
        return anchorTradePoints;
    }

    public void setAnchorTradePoints(Double anchorTradePoints) {
        this.anchorTradePoints = anchorTradePoints;
    }

    public Double getAfterSaleRate() {
        return afterSaleRate;
    }

    public void setAfterSaleRate(Double afterSaleRate) {
        this.afterSaleRate = afterSaleRate;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Double getAcclaimCost() {
        return acclaimCost;
    }

    public void setAcclaimCost(Double acclaimCost) {
        this.acclaimCost = acclaimCost;
    }

    public Double getFeeRoi() {
        return feeRoi;
    }

    public void setFeeRoi(Double feeRoi) {
        this.feeRoi = feeRoi;
    }

    public String getGoodsMainImg() {
        return goodsMainImg;
    }

    public void setGoodsMainImg(String goodsMainImg) {
        this.goodsMainImg = goodsMainImg;
    }

    public Double getOtherTradePoints() {
        return otherTradePoints;
    }

    public void setOtherTradePoints(Double otherTradePoints) {
        this.otherTradePoints = otherTradePoints;
    }

    public Double getBrandTradePoints() {
        return brandTradePoints;
    }

    public void setBrandTradePoints(Double brandTradePoints) {
        this.brandTradePoints = brandTradePoints;
    }

    public Double getAttritionCosts() {
        return attritionCosts;
    }

    public void setAttritionCosts(Double attritionCosts) {
        this.attritionCosts = attritionCosts;
    }

    public Double getGiftsCost() {
        return giftsCost;
    }

    public void setGiftsCost(Double giftsCost) {
        this.giftsCost = giftsCost;
    }

    public Double getExpressCost() {
        return expressCost;
    }

    public void setExpressCost(Double expressCost) {
        this.expressCost = expressCost;
    }

    public Double getBroadcasterTradePoints() {
        return broadcasterTradePoints;
    }

    public void setBroadcasterTradePoints(Double broadcasterTradePoints) {
        this.broadcasterTradePoints = broadcasterTradePoints;
    }

    public Double getTransportInsuranceCost() {
        return transportInsuranceCost;
    }

    public void setTransportInsuranceCost(Double transportInsuranceCost) {
        this.transportInsuranceCost = transportInsuranceCost;
    }

    public Double getPlatformTradePoints() {
        return platformTradePoints;
    }

    public void setPlatformTradePoints(Double platformTradePoints) {
        this.platformTradePoints = platformTradePoints;
    }

}
