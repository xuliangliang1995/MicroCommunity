package com.java110.vo.api;

import com.java110.dto.commodity.CommodityDetailDto;
import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

/**
 * @author xuliangliang
 * @Classname ApiCommodityVo
 * @Description
 * @Date 2020/3/26 20:41
 * @blame Java Team
 */
public class ApiCommodityVo extends MorePageVo implements Serializable {

    private List<CommodityDetailDto> commodities;

    public List<CommodityDetailDto> getCommodities() {
        return commodities;
    }

    public void setCommodities(List<CommodityDetailDto> commodities) {
        this.commodities = commodities;
    }
}
