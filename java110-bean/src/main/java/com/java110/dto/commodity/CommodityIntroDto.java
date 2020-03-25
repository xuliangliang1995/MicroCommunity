package com.java110.dto.commodity;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 商品简介数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class CommodityIntroDto extends PageDto implements Serializable {

    private String intro;
private String remark;
private String commodityId;
private String introId;
private String userId;


    private Date createTime;

    private String statusCd = "0";


    public String getIntro() {
        return intro;
    }
public void setIntro(String intro) {
        this.intro = intro;
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
public String getIntroId() {
        return introId;
    }
public void setIntroId(String introId) {
        this.introId = introId;
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