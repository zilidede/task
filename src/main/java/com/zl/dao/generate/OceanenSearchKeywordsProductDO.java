package com.zl.dao.generate;

import java.util.Date;

/**
 * @Description:
 * @Param:
 * @Auther: zl
 * @Date: 2024-07-12
 */
public class OceanenSearchKeywordsProductDO {
    private Long prductId;
    private Integer productShowCnt;
    private Date recordTime;
    private Integer productAmountIndex;
    private Integer productClickCnt;
    private String roductOutLink;
    private Integer productOrderCnt;
    private String productUrl;
    private String keyword;
    private String prductName;

    public Long getPrductId() {
        return prductId;
    }

    public void setPrductId(Long prductId) {
        this.prductId = prductId;
    }

    public Integer getProductShowCnt() {
        return productShowCnt;
    }

    public void setProductShowCnt(Integer productShowCnt) {
        this.productShowCnt = productShowCnt;
    }

    public Date getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
    }

    public Integer getProductAmountIndex() {
        return productAmountIndex;
    }

    public void setProductAmountIndex(Integer productAmountIndex) {
        this.productAmountIndex = productAmountIndex;
    }

    public Integer getProductClickCnt() {
        return productClickCnt;
    }

    public void setProductClickCnt(Integer productClickCnt) {
        this.productClickCnt = productClickCnt;
    }

    public String getRoductOutLink() {
        return roductOutLink;
    }

    public void setRoductOutLink(String roductOutLink) {
        this.roductOutLink = roductOutLink;
    }

    public Integer getProductOrderCnt() {
        return productOrderCnt;
    }

    public void setProductOrderCnt(Integer productOrderCnt) {
        this.productOrderCnt = productOrderCnt;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getPrductName() {
        return prductName;
    }

    public void setPrductName(String prductName) {
        this.prductName = prductName;
    }

}
