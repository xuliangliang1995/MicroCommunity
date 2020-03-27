package com.java110.dto.commodity.converter;

import com.java110.dto.commodity.CommodityDetailDto;
import com.java110.dto.commodity.CommodityDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * @author xuliangliang
 * @Classname CommodityDto2DetailDto
 * @Description
 * @Date 2020/3/26 20:23
 * @blame Java Team
 */
@Mapper(componentModel = "spring")
public interface ICommodityDto2DetailDto {

    @Mappings({
            @Mapping(source = "detail.intro", target = "intro"),
            @Mapping(source = "detail.commodityPhotos", target = "commodityPhotos"),
            @Mapping(source = "detail.stockpile", target = "stockpile")
    })
    CommodityDetailDto commodityDto2DetailDto(CommodityDto commodityDto, CommodityDetail detail);

}
