package com.java110.dto.commodity.converter;

/**
 * @author xuliangliang
 * @Classname CommodityDetail
 * @Description TODO
 * @Date 2020/3/26 20:24
 * @blame Java Team
 */
public class CommodityDetail {
    private String intro;

    private String commodityPhotos;

    private Integer stockpile;

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getCommodityPhotos() {
        return commodityPhotos;
    }

    public void setCommodityPhotos(String commodityPhotos) {
        this.commodityPhotos = commodityPhotos;
    }

    public Integer getStockpile() {
        return stockpile;
    }

    public void setStockpile(Integer stockpile) {
        this.stockpile = stockpile;
    }
}
