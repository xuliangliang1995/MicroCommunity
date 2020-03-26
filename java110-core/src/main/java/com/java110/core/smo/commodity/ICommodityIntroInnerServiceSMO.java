package com.java110.core.smo.commodity;

import com.java110.core.feign.FeignConfiguration;
import com.java110.dto.commodity.CommodityIntroDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName ICommodityIntroInnerServiceSMO
 * @Description 商品简介接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "mall-service", configuration = {FeignConfiguration.class})
@RequestMapping("/commodityIntroApi")
public interface ICommodityIntroInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param commodityIntroDto 数据对象分享
     * @return CommodityIntroDto 对象数据
     */
    @RequestMapping(value = "/queryCommodityIntros", method = RequestMethod.POST)
    List<CommodityIntroDto> queryCommodityIntros(@RequestBody CommodityIntroDto commodityIntroDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param commodityIntroDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryCommodityIntrosCount", method = RequestMethod.POST)
    int queryCommodityIntrosCount(@RequestBody CommodityIntroDto commodityIntroDto);

    @GetMapping("/{commodityId}/introId")
    String getIntroIdByCommodityId(@PathVariable("commodityId") String commodityId);
}
