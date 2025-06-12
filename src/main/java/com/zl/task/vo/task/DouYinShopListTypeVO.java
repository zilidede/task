package com.zl.task.vo.task;

/**
 * @className: com.craw.nd.vo.goods-> GoodsListTypeVO
 * @description: 抖店罗盘榜单类型
 * @author: zl
 * @createDate: 2024-01-14 15:25
 * @version: 1.0
 * @todo:
 */
public class DouYinShopListTypeVO {
    private String listCarrier;//-榜单载体 -商品 -店铺 -内容
    private String listType; //榜单类型 // 直播 //商品卡 //达人；
    private String industryName; //行业 服饰/运动户外
    private String crawTime;// 爬取日期； 近一天/小时

    public String getCrawTime() {
        return crawTime;
    }


    public String getIndustryName() {
        return industryName;
    }


    public String getListCarrier() {
        return listCarrier;
    }

    public String getListType() {
        return listType;
    }

    public void setCrawTime(String crawTime) {
        this.crawTime = crawTime;
    }


    public void setIndustryName(String industryName) {
        this.industryName = industryName;
    }


    public void setListCarrier(String listCarrier) {
        this.listCarrier = listCarrier;
    }

    public void setListType(String listType) {
        this.listType = listType;
    }
}
