package com.java110.dto.owner;

/**
 * @author xuliangliang
 * @Classname DeliveryAddressDto
 * @Description
 * @Date 2020/3/23 14:48
 * @blame Java Team
 */
public class DeliveryAddressDto {

    public final static DeliveryAddressDto EMPTY_ADDRESS = new DeliveryAddressDto();
    static {
        EMPTY_ADDRESS.setCompanyName("");
        EMPTY_ADDRESS.setCompanyFloor("");
    }

    private String companyName;

    private String companyFloor;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyFloor() {
        return companyFloor;
    }

    public void setCompanyFloor(String companyFloor) {
        this.companyFloor = companyFloor;
    }
}
