package com.java110.mall.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.smo.commodity.ICommodityIntroInnerServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.commodity.CommodityIntroDto;
import com.java110.dto.user.UserDto;
import com.java110.mall.dao.ICommodityIntroServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 商品简介内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class CommodityIntroInnerServiceSMOImpl extends BaseServiceSMO implements ICommodityIntroInnerServiceSMO {

    @Autowired
    private ICommodityIntroServiceDao commodityIntroServiceDaoImpl;


    @Override
    public List<CommodityIntroDto> queryCommodityIntros(@RequestBody  CommodityIntroDto commodityIntroDto) {

        //校验是否传了 分页信息

        int page = commodityIntroDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            commodityIntroDto.setPage((page - 1) * commodityIntroDto.getRow());
        }

        List<CommodityIntroDto> commodityIntros = BeanConvertUtil.covertBeanList(commodityIntroServiceDaoImpl.getCommodityIntroInfo(BeanConvertUtil.beanCovertMap(commodityIntroDto)), CommodityIntroDto.class);

        if (commodityIntros == null || commodityIntros.size() == 0) {
            return commodityIntros;
        }

        return commodityIntros;
    }

    /**
     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
     *
     * @param commodityIntro 小区商品简介信息
     * @param users 用户列表
     */
    private void refreshCommodityIntro(CommodityIntroDto commodityIntro, List<UserDto> users) {
        for (UserDto user : users) {
            if (commodityIntro.getUserId().equals(user.getUserId())) {
                BeanConvertUtil.covertBean(user, commodityIntro);
            }
        }
    }

    /**
     * 获取批量userId
     *
     * @param commodityIntros 小区楼信息
     * @return 批量userIds 信息
     */
    private String[] getUserIds(List<CommodityIntroDto> commodityIntros) {
        List<String> userIds = new ArrayList<String>();
        for (CommodityIntroDto commodityIntro : commodityIntros) {
            userIds.add(commodityIntro.getUserId());
        }

        return userIds.toArray(new String[userIds.size()]);
    }

    @Override
    public int queryCommodityIntrosCount(@RequestBody CommodityIntroDto commodityIntroDto) {
        return commodityIntroServiceDaoImpl.queryCommodityIntrosCount(BeanConvertUtil.beanCovertMap(commodityIntroDto));    }

    public ICommodityIntroServiceDao getCommodityIntroServiceDaoImpl() {
        return commodityIntroServiceDaoImpl;
    }

    public void setCommodityIntroServiceDaoImpl(ICommodityIntroServiceDao commodityIntroServiceDaoImpl) {
        this.commodityIntroServiceDaoImpl = commodityIntroServiceDaoImpl;
    }

}
