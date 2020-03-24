package com.java110.core.smo.commodity;

import com.java110.core.feign.FeignConfiguration;
import com.java110.dto.community.CommodityPhotoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName ICommodityPhotoInnerServiceSMO
 * @Description 商品配图接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "mall-service", configuration = {FeignConfiguration.class})
@RequestMapping("/commodityPhotoApi")
public interface ICommodityPhotoInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param commodityPhotoDto 数据对象分享
     * @return CommodityPhotoDto 对象数据
     */
    @RequestMapping(value = "/queryCommodityPhotos", method = RequestMethod.POST)
    List<CommodityPhotoDto> queryCommodityPhotos(@RequestBody CommodityPhotoDto commodityPhotoDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param commodityPhotoDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryCommodityPhotosCount", method = RequestMethod.POST)
    int queryCommodityPhotosCount(@RequestBody CommodityPhotoDto commodityPhotoDto);
}
