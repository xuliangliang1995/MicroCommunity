package com.java110.dto.owner.converter;

import com.java110.dto.owner.DeliveryAddressDto;
import com.java110.dto.owner.OwnerDeliveryAddressDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.owner.OwnerDtoWithDeliveryAddress;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * @author xuliangliang
 * @Classname OwnerConverter
 * @Description
 * @Date 2020/3/23 14:06
 * @blame Java Team
 */
@Mapper(componentModel = "spring")
public interface IOwnerConverter {

    /**
     * ownerDto 对象转换成 附带收货地址的 ownerDtoWithDeliveryAddress 对象
     * @param ownerDto
     * @param addressDto
     * @return
     */
    @Mappings({
            @Mapping(source = "addressDto.companyName", target = "companyName"),
            @Mapping(source = "addressDto.companyFloor", target = "companyFloor")
    })
    OwnerDtoWithDeliveryAddress ownerDtoAttachDeliveryAddress(OwnerDto ownerDto, DeliveryAddressDto addressDto);

    /**
     * ownerDeliveryAdress 2 deliveryAddressDto
     * @param address
     * @return
     */
    DeliveryAddressDto ownerDeliveryAddress2DeliveryAddress(OwnerDeliveryAddressDto address);

}
