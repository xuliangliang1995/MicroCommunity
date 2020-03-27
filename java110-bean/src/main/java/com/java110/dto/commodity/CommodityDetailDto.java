package com.java110.dto.commodity;

/**
 * @author xuliangliang
 * @Classname CommodityDetailDto
 * @Description TODO
 * @Date 2020/3/26 20:15
 * @blame Java Team
 */
public class CommodityDetailDto extends CommodityDto {
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
