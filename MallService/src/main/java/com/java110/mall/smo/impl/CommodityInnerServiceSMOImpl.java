package com.java110.mall.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.smo.commodity.ICommodityInnerServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.commodity.CommodityDetailDto;
import com.java110.dto.commodity.CommodityDto;
import com.java110.dto.commodity.converter.CommodityDetail;
import com.java110.dto.commodity.converter.ICommodityDto2DetailDto;
import com.java110.dto.user.UserDto;
import com.java110.mall.dao.ICommodityIntroServiceDao;
import com.java110.mall.dao.ICommodityPhotoServiceDao;
import com.java110.mall.dao.ICommodityServiceDao;
import com.java110.mall.dao.ICommodityStockpileServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 商品内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class CommodityInnerServiceSMOImpl extends BaseServiceSMO implements ICommodityInnerServiceSMO {

    @Autowired
    private ICommodityServiceDao commodityServiceDaoImpl;
    @Autowired
    private ICommodityIntroServiceDao commodityIntroServiceDaoImpl;
    @Autowired
    private ICommodityPhotoServiceDao commodityPhotoServiceDaoImpl;
    @Autowired
    private ICommodityStockpileServiceDao commodityStockpileServiceDaoImpl;
    @Autowired
    private ICommodityDto2DetailDto commodityDto2DetailDtoImpl;


    @Override
    public List<CommodityDetailDto> queryCommoditys(@RequestBody  CommodityDto commodityDto) {

        //校验是否传了 分页信息

        int page = commodityDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            commodityDto.setPage((page - 1) * commodityDto.getRow());
        }

        List<CommodityDto> commoditys = BeanConvertUtil.covertBeanList(commodityServiceDaoImpl.getCommodityInfo(BeanConvertUtil.beanCovertMap(commodityDto)), CommodityDto.class);

        if (commoditys == null || commoditys.size() == 0) {
            return Collections.emptyList();
        }
        return commoditys.stream().map(commodity -> {
            // 简介
            CommodityDetail detail = new CommodityDetail();
            detail.setIntro(commodityIntroServiceDaoImpl.getIntroByCommodityId(commodity.getCommodityId()));
            // 库存
            Map stockpile = commodityStockpileServiceDaoImpl.getCommodityStockpile(commodity.getCommodityId());
            detail.setStockpile(
                    Optional.ofNullable(stockpile)
                    .map(s -> s.getOrDefault("stockpile", 0))
                    .filter(s -> NumberUtils.isDigits(s.toString()))
                    .map(String::valueOf)
                    .map(Integer::valueOf)
                    .orElse(0)
            );
            // 配图
            Map photoQueryMap = new HashMap();
            photoQueryMap.put("commodityId", commodity.getCommodityId());
            String commodityPhotos = commodityPhotoServiceDaoImpl.getCommodityPhotoInfo(photoQueryMap).stream()
                    .map(m -> m.getOrDefault("photo", null))
                    .map(String::valueOf)
                    .filter(StringUtils::isNotEmpty)
                    .reduce((a, b) -> a + "," + b)
                    .orElse("");
            detail.setCommodityPhotos(commodityPhotos);

            return commodityDto2DetailDtoImpl.commodityDto2DetailDto(commodity, detail);
        }).collect(Collectors.toList());
    }

    /**
     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
     *
     * @param commodity 小区商品信息
     * @param users 用户列表
     */
    private void refreshCommodity(CommodityDto commodity, List<UserDto> users) {
        for (UserDto user : users) {
            if (commodity.getUserId().equals(user.getUserId())) {
                BeanConvertUtil.covertBean(user, commodity);
            }
        }
    }

    /**
     * 获取批量userId
     *
     * @param commoditys 小区楼信息
     * @return 批量userIds 信息
     */
    private String[] getUserIds(List<CommodityDto> commoditys) {
        List<String> userIds = new ArrayList<String>();
        for (CommodityDto commodity : commoditys) {
            userIds.add(commodity.getUserId());
        }

        return userIds.toArray(new String[userIds.size()]);
    }

    @Override
    public int queryCommoditysCount(@RequestBody CommodityDto commodityDto) {
        return commodityServiceDaoImpl.queryCommoditysCount(BeanConvertUtil.beanCovertMap(commodityDto));    }

    public ICommodityServiceDao getCommodityServiceDaoImpl() {
        return commodityServiceDaoImpl;
    }

    public void setCommodityServiceDaoImpl(ICommodityServiceDao commodityServiceDaoImpl) {
        this.commodityServiceDaoImpl = commodityServiceDaoImpl;
    }

}
