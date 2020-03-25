package com.java110.core.smo.commodity;

import com.java110.core.feign.FeignConfiguration;

import com.java110.dto.commodity.CommodityDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName ICommodityInnerServiceSMO
 * @Description 商品接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "mall-service", configuration = {FeignConfiguration.class})
@RequestMapping("/commodityApi")
public interface ICommodityInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param commodityDto 数据对象分享
     * @return CommodityDto 对象数据
     */
    @RequestMapping(value = "/queryCommoditys", method = RequestMethod.POST)
    List<CommodityDto> queryCommoditys(@RequestBody CommodityDto commodityDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param commodityDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryCommoditysCount", method = RequestMethod.POST)
    int queryCommoditysCount(@RequestBody CommodityDto commodityDto);
}
