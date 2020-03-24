package com.java110.dto.community;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 商品配图数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class CommodityPhotoDto extends PageDto implements Serializable {

    private String photoId;
private String photo;
private String remark;
private String commodityId;
private String userId;


    private Date createTime;

    private String statusCd = "0";


    public String getPhotoId() {
        return photoId;
    }
public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }
public String getPhoto() {
        return photo;
    }
public void setPhoto(String photo) {
        this.photo = photo;
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
