package com.java110.core.smo.commodity;

import com.java110.core.feign.FeignConfiguration;
import com.java110.dto.commodity.CommodityStockpileDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName ICommodityStockpileInnerServiceSMO
 * @Description 商品库存接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "mall-service", configuration = {FeignConfiguration.class})
@RequestMapping("/commodityStockpileApi")
public interface ICommodityStockpileInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param commodityStockpileDto 数据对象分享
     * @return CommodityStockpileDto 对象数据
     */
    @RequestMapping(value = "/queryCommodityStockpiles", method = RequestMethod.POST)
    List<CommodityStockpileDto> queryCommodityStockpiles(@RequestBody CommodityStockpileDto commodityStockpileDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param commodityStockpileDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryCommodityStockpilesCount", method = RequestMethod.POST)
    int queryCommodityStockpilesCount(@RequestBody CommodityStockpileDto commodityStockpileDto);
}
