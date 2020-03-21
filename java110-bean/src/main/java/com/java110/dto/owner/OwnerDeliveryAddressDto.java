package com.java110.dto.owner;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 业主收货地址数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class OwnerDeliveryAddressDto extends PageDto implements Serializable {

    private String companyFloor;
    private String companyName;
    private String ownerId;
    private String userId;
    private String addressId;
    private String memberId;


    private Date createTime;

    private String statusCd = "0";


    public String getCompanyFloor() {
        return companyFloor;
    }
    public void setCompanyFloor(String companyFloor) {
            this.companyFloor = companyFloor;
        }
    public String getCompanyName() {
            return companyName;
        }
    public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }
    public String getOwnerId() {
            return ownerId;
        }
    public void setOwnerId(String ownerId) {
            this.ownerId = ownerId;
        }
    public String getUserId() {
            return userId;
        }
    public void setUserId(String userId) {
            this.userId = userId;
        }
    public String getAddressId() {
            return addressId;
        }
    public void setAddressId(String addressId) {
            this.addressId = addressId;
        }
    public String getMemberId() {
            return memberId;
        }
    public void setMemberId(String memberId) {
            this.memberId = memberId;
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
