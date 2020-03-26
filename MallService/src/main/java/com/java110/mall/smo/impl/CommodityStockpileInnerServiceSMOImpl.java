package com.java110.mall.smo.impl;


import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.smo.commodity.ICommodityStockpileInnerServiceSMO;
import com.java110.dto.PageDto;
import com.java110.dto.commodity.CommodityStockpileDto;
import com.java110.dto.user.UserDto;
import com.java110.mall.dao.ICommodityStockpileServiceDao;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 商品库存内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class CommodityStockpileInnerServiceSMOImpl extends BaseServiceSMO implements ICommodityStockpileInnerServiceSMO {

    @Autowired
    private ICommodityStockpileServiceDao commodityStockpileServiceDaoImpl;

    @Override
    public List<CommodityStockpileDto> queryCommodityStockpiles(@RequestBody  CommodityStockpileDto commodityStockpileDto) {

        //校验是否传了 分页信息

        int page = commodityStockpileDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            commodityStockpileDto.setPage((page - 1) * commodityStockpileDto.getRow());
        }

        List<CommodityStockpileDto> commodityStockpiles = BeanConvertUtil.covertBeanList(commodityStockpileServiceDaoImpl.getCommodityStockpileInfo(BeanConvertUtil.beanCovertMap(commodityStockpileDto)), CommodityStockpileDto.class);

        if (commodityStockpiles == null || commodityStockpiles.size() == 0) {
            return commodityStockpiles;
        }

        return commodityStockpiles;
    }

    /**
     * 从用户列表中查询用户，将用户中的信息 刷新到 floor对象中
     *
     * @param commodityStockpile 小区商品库存信息
     * @param users 用户列表
     */
    private void refreshCommodityStockpile(CommodityStockpileDto commodityStockpile, List<UserDto> users) {
        for (UserDto user : users) {
            if (commodityStockpile.getUserId().equals(user.getUserId())) {
                BeanConvertUtil.covertBean(user, commodityStockpile);
            }
        }
    }

    /**
     * 获取批量userId
     *
     * @param commodityStockpiles 小区楼信息
     * @return 批量userIds 信息
     */
    private String[] getUserIds(List<CommodityStockpileDto> commodityStockpiles) {
        List<String> userIds = new ArrayList<String>();
        for (CommodityStockpileDto commodityStockpile : commodityStockpiles) {
            userIds.add(commodityStockpile.getUserId());
        }

        return userIds.toArray(new String[userIds.size()]);
    }

    @Override
    public int queryCommodityStockpilesCount(@RequestBody CommodityStockpileDto commodityStockpileDto) {
        return commodityStockpileServiceDaoImpl.queryCommodityStockpilesCount(BeanConvertUtil.beanCovertMap(commodityStockpileDto));    }

    /**
     * 商品库存
     *
     * @param commodityId
     * @return
     */
    @Override
    public CommodityStockpileDto getStockpile(@PathVariable("commodityId") String commodityId) {
        Map map = commodityStockpileServiceDaoImpl.getCommodityStockpile(commodityId);
        if (null != map) {
            return BeanConvertUtil.covertBean(map, CommodityStockpileDto.class);
        }
        return null;
    }

    public ICommodityStockpileServiceDao getCommodityStockpileServiceDaoImpl() {
        return commodityStockpileServiceDaoImpl;
    }

    public void setCommodityStockpileServiceDaoImpl(ICommodityStockpileServiceDao commodityStockpileServiceDaoImpl) {
        this.commodityStockpileServiceDaoImpl = commodityStockpileServiceDaoImpl;
    }

}
