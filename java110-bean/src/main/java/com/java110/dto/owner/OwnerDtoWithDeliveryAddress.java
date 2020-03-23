package com.java110.dto.owner;

/**
 * @author xuliangliang
 * @Classname OwnerDtoWithDeliveryAddress
 * @Description 附带收货地址
 * @Date 2020/3/23 13:52
 * @blame Java Team
 */
public class OwnerDtoWithDeliveryAddress extends OwnerDto {

    private String companyName;

    private String companyFloor;

    public String getCompanyName() {
        return companyName;
    }

    public String getCompanyFloor() {
        return companyFloor;
    }
}
