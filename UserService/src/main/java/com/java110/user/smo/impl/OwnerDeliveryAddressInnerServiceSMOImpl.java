package com.java110.user.smo.impl;


import com.java110.core.smo.owner.IOwnerDeliveryAddressInnerServiceSMO;
import com.java110.dto.owner.OwnerDeliveryAddressDto;
import com.java110.user.dao.IOwnerDeliveryAddressServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.smo.user.IUserInnerServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.user.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 业主收货地址内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class OwnerDeliveryAddressInnerServiceSMOImpl extends BaseServiceSMO implements IOwnerDeliveryAddressInnerServiceSMO {

    @Autowired
    private IOwnerDeliveryAddressServiceDao ownerDeliveryAddressServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<OwnerDeliveryAddressDto> queryOwnerDeliveryAddresss(@RequestBody  OwnerDeliveryAddressDto ownerDeliveryAddressDto) {

        //校验是否传了 分页信息

        int page = ownerDeliveryAddressDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            ownerDeliveryAddressDto.setPage((page - 1) * ownerDeliveryAddressDto.getRow());
        }

        List<OwnerDeliveryAddressDto> ownerDeliveryAddresss = BeanConvertUtil.covertBeanList(ownerDeliveryAddressServiceDaoImpl.getOwnerDeliveryAddressInfo(BeanConvertUtil.beanCovertMap(ownerDeliveryAddressDto)), OwnerDeliveryAddressDto.class);

        if (ownerDeliveryAddresss == null || ownerDeliveryAddresss.size() == 0) {
            return ownerDeliveryAddresss;
        }

        String[] userIds = getUserIds(ownerDeliveryAddresss);
        //根据 userId 查询用户信息
        List<UserDto> users = userInnerServiceSMOImpl.getUserInfo(userIds);

        for (OwnerDeliveryAddressDto ownerDeliveryAddress : ownerDeliveryAddresss) {
            refreshOwnerDeliveryAddress(ownerDeliveryAddress, users);
        }
        return ownerDeliveryAddresss;
    }

    /**
     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
     *
     * @param ownerDeliveryAddress 小区业主收货地址信息
     * @param users 用户列表
     */
    private void refreshOwnerDeliveryAddress(OwnerDeliveryAddressDto ownerDeliveryAddress, List<UserDto> users) {
        for (UserDto user : users) {
            if (ownerDeliveryAddress.getUserId().equals(user.getUserId())) {
                BeanConvertUtil.covertBean(user, ownerDeliveryAddress);
            }
        }
    }

    /**
     * 获取批量userId
     *
     * @param ownerDeliveryAddresss 小区楼信息
     * @return 批量userIds 信息
     */
    private String[] getUserIds(List<OwnerDeliveryAddressDto> ownerDeliveryAddresss) {
        List<String> userIds = new ArrayList<String>();
        for (OwnerDeliveryAddressDto ownerDeliveryAddress : ownerDeliveryAddresss) {
            userIds.add(ownerDeliveryAddress.getUserId());
        }

        return userIds.toArray(new String[userIds.size()]);
    }

    @Override
    public int queryOwnerDeliveryAddresssCount(@RequestBody OwnerDeliveryAddressDto ownerDeliveryAddressDto) {
        return ownerDeliveryAddressServiceDaoImpl.queryOwnerDeliveryAddresssCount(BeanConvertUtil.beanCovertMap(ownerDeliveryAddressDto));    }

    /**
     * 获取业主/家庭成员收货地址
     *
     * @param memberId
     * @return
     */
    @Override
    public OwnerDeliveryAddressDto getOwnerDeliveryAddress(@PathVariable("memberId") String memberId) {
        return Optional.ofNullable(ownerDeliveryAddressServiceDaoImpl.getOwnerDeliveryAddress(memberId))
                .map(map -> BeanConvertUtil.covertBean(map, OwnerDeliveryAddressDto.class))
                .orElse(null);
    }

    public IOwnerDeliveryAddressServiceDao getOwnerDeliveryAddressServiceDaoImpl() {
        return ownerDeliveryAddressServiceDaoImpl;
    }

    public void setOwnerDeliveryAddressServiceDaoImpl(IOwnerDeliveryAddressServiceDao ownerDeliveryAddressServiceDaoImpl) {
        this.ownerDeliveryAddressServiceDaoImpl = ownerDeliveryAddressServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
