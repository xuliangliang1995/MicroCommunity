package com.java110.dto.commodity;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 商品数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class CommodityDto extends PageDto implements Serializable {

    private String originalPrice;
    private String show;
    private String currentPrice;
    private String remark;
    private String commodityId;
    private String communityId;
    private String title;
    private String userId;


    private Date createTime;

    private String statusCd = "0";


    public String getOriginalPrice() {
        return originalPrice;
    }
    public void setOriginalPrice(String originalPrice) {
            this.originalPrice = originalPrice;
        }
    public String getShow() {
            return show;
        }
    public void setShow(String show) {
            this.show = show;
        }
    public String getCurrentPrice() {
            return currentPrice;
        }
    public void setCurrentPrice(String currentPrice) {
            this.currentPrice = currentPrice;
        }
    public String getRemark() {
            return remark;
        }
    public void setRemark(String remark) {
            this.remark = remark;
        }
    public String getCommodityId() {
            return commodityId;
        }
    public void setCommodityId(String commodityId) {
            this.commodityId = commodityId;
        }
    public String getCommunityId() {
            return communityId;
        }
    public void setCommunityId(String communityId) {
            this.communityId = communityId;
        }
    public String getTitle() {
            return title;
        }
    public void setTitle(String title) {
            this.title = title;
        }
    public String getUserId() {
            return userId;
        }
    public void setUserId(String userId) {
            this.userId = userId;
        }


    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }
}
