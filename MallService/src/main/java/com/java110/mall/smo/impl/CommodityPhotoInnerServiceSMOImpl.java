package com.java110.mall.smo.impl;


import com.java110.core.smo.commodity.ICommodityPhotoInnerServiceSMO;
import com.java110.dto.community.CommodityPhotoDto;
import com.java110.mall.dao.ICommodityPhotoServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.core.base.smo.BaseServiceSMO;
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
 * @Description 商品配图内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class CommodityPhotoInnerServiceSMOImpl extends BaseServiceSMO implements ICommodityPhotoInnerServiceSMO {

    @Autowired
    private ICommodityPhotoServiceDao commodityPhotoServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<CommodityPhotoDto> queryCommodityPhotos(@RequestBody  CommodityPhotoDto commodityPhotoDto) {

        //校验是否传了 分页信息

        int page = commodityPhotoDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            commodityPhotoDto.setPage((page - 1) * commodityPhotoDto.getRow());
        }

        List<CommodityPhotoDto> commodityPhotos = BeanConvertUtil.covertBeanList(commodityPhotoServiceDaoImpl.getCommodityPhotoInfo(BeanConvertUtil.beanCovertMap(commodityPhotoDto)), CommodityPhotoDto.class);

        if (commodityPhotos == null || commodityPhotos.size() == 0) {
            return commodityPhotos;
        }

        String[] userIds = getUserIds(commodityPhotos);
        //根据 userId 查询用户信息
        List<UserDto> users = userInnerServiceSMOImpl.getUserInfo(userIds);

        for (CommodityPhotoDto commodityPhoto : commodityPhotos) {
            refreshCommodityPhoto(commodityPhoto, users);
        }
        return commodityPhotos;
    }

    /**
     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
     *
     * @param commodityPhoto 小区商品配图信息
     * @param users 用户列表
     */
    private void refreshCommodityPhoto(CommodityPhotoDto commodityPhoto, List<UserDto> users) {
        for (UserDto user : users) {
            if (commodityPhoto.getUserId().equals(user.getUserId())) {
                BeanConvertUtil.covertBean(user, commodityPhoto);
            }
        }
    }

    /**
     * 获取批量userId
     *
     * @param commodityPhotos 小区楼信息
     * @return 批量userIds 信息
     */
    private String[] getUserIds(List<CommodityPhotoDto> commodityPhotos) {
        List<String> userIds = new ArrayList<String>();
        for (CommodityPhotoDto commodityPhoto : commodityPhotos) {
            userIds.add(commodityPhoto.getUserId());
        }

        return userIds.toArray(new String[userIds.size()]);
    }

    @Override
    public int queryCommodityPhotosCount(@RequestBody CommodityPhotoDto commodityPhotoDto) {
        return commodityPhotoServiceDaoImpl.queryCommodityPhotosCount(BeanConvertUtil.beanCovertMap(commodityPhotoDto));    }

    public ICommodityPhotoServiceDao getCommodityPhotoServiceDaoImpl() {
        return commodityPhotoServiceDaoImpl;
    }

    public void setCommodityPhotoServiceDaoImpl(ICommodityPhotoServiceDao commodityPhotoServiceDaoImpl) {
        this.commodityPhotoServiceDaoImpl = commodityPhotoServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
