package com.java110.mall.smo.impl;


import com.java110.dto.commodity.CommodityDto;
import com.java110.mall.dao.ICommodityServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.smo.commodity.ICommodityInnerServiceSMO;
import com.java110.core.smo.user.IUserInnerServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.user.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

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
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<CommodityDto> queryCommoditys(@RequestBody  CommodityDto commodityDto) {

        //校验是否传了 分页信息

        int page = commodityDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            commodityDto.setPage((page - 1) * commodityDto.getRow());
        }

        List<CommodityDto> commoditys = BeanConvertUtil.covertBeanList(commodityServiceDaoImpl.getCommodityInfo(BeanConvertUtil.beanCovertMap(commodityDto)), CommodityDto.class);

        if (commoditys == null || commoditys.size() == 0) {
            return commoditys;
        }

        String[] userIds = getUserIds(commoditys);
        //根据 userId 查询用户信息
        List<UserDto> users = userInnerServiceSMOImpl.getUserInfo(userIds);

        for (CommodityDto commodity : commoditys) {
            refreshCommodity(commodity, users);
        }
        return commoditys;
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

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
