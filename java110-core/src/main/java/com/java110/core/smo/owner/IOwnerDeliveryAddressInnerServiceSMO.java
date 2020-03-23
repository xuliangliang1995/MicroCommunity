package com.java110.core.smo.owner;

import com.java110.core.feign.FeignConfiguration;
import com.java110.dto.owner.OwnerDeliveryAddressDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IOwnerDeliveryAddressInnerServiceSMO
 * @Description 业主收货地址接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "user-service", configuration = {FeignConfiguration.class})
@RequestMapping("/ownerDeliveryAddressApi")
public interface IOwnerDeliveryAddressInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param ownerDeliveryAddressDto 数据对象分享
     * @return OwnerDeliveryAddressDto 对象数据
     */
    @RequestMapping(value = "/queryOwnerDeliveryAddresss", method = RequestMethod.POST)
    List<OwnerDeliveryAddressDto> queryOwnerDeliveryAddresss(@RequestBody OwnerDeliveryAddressDto ownerDeliveryAddressDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param ownerDeliveryAddressDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryOwnerDeliveryAddresssCount", method = RequestMethod.POST)
    int queryOwnerDeliveryAddresssCount(@RequestBody OwnerDeliveryAddressDto ownerDeliveryAddressDto);

    /**
     * 获取业主/家庭成员收货地址
     * @param memberId
     * @return
     */
    @RequestMapping(value = "/{memberId}/ownerDeliveryAddress")
    OwnerDeliveryAddressDto getOwnerDeliveryAddress(@PathVariable("memberId") String memberId);
}
