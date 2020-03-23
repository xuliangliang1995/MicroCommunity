package com.java110.api.listener.owner;


import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiDataFlowListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.owner.IOwnerDeliveryAddressInnerServiceSMO;
import com.java110.core.smo.owner.IOwnerInnerServiceSMO;
import com.java110.dto.owner.DeliveryAddressDto;
import com.java110.dto.owner.OwnerDeliveryAddressDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.owner.OwnerDtoWithDeliveryAddress;
import com.java110.dto.owner.converter.IOwnerConverter;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.ApiOwnerDataVo;
import com.java110.vo.api.ApiOwnerVo;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName OwnerDto
 * @Description 小区楼数据层侦听类
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@Java110Listener("queryOwnerMembersListener")
public class QueryOwnerMembersListener extends AbstractServiceApiDataFlowListener {

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IOwnerDeliveryAddressInnerServiceSMO ownerDeliveryAddressInnerServiceSMOImpl;

    @Autowired
    private IOwnerConverter ownerConverter;

    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_QUERY_OWNER_MEMBER;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    /**
     * 业务层数据处理
     *
     * @param event 时间对象
     */
    @Override
    public void soService(ServiceDataFlowEvent event) {
        DataFlowContext dataFlowContext = event.getDataFlowContext();
        //获取请求数据
        JSONObject reqJson = dataFlowContext.getReqJson();
        validateOwnerData(reqJson);

        OwnerDto ownerDto = BeanConvertUtil.covertBean(reqJson, OwnerDto.class);
        ownerDto.setOwnerTypeCds(new String[]{"1002", "1003", "1004", "1005"});
        List<OwnerDto> ownerDtoList = ownerInnerServiceSMOImpl.queryOwnerMembers(ownerDto);

        // 再依次查出各个业主/家庭成员的收货地址
        List<OwnerDtoWithDeliveryAddress> ownerDtoWithDeliveryAddressList = ownerDtoList.parallelStream()
                .map(owner -> {
                    OwnerDeliveryAddressDto example = new OwnerDeliveryAddressDto();
                    example.setMemberId(owner.getMemberId());
                    List<OwnerDeliveryAddressDto> addressList = ownerDeliveryAddressInnerServiceSMOImpl.queryOwnerDeliveryAddresss(example);

                    DeliveryAddressDto addressDto = DeliveryAddressDto.EMPTY_ADDRESS;
                    if (CollectionUtils.isNotEmpty(addressList)) {
                        addressDto = ownerConverter.ownerDeliveryAddress2DeliveryAddress(addressList.get(0));
                    }

                    return ownerConverter.ownerDtoAttachDeliveryAddress(owner, addressDto);
                }).collect(Collectors.toList());


        ApiOwnerVo apiOwnerVo = new ApiOwnerVo();
        apiOwnerVo.setOwners(BeanConvertUtil.covertBeanList(ownerDtoWithDeliveryAddressList, ApiOwnerDataVo.class));
        apiOwnerVo.setTotal(ownerDtoList.size());
        apiOwnerVo.setRecords(1);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiOwnerVo), HttpStatus.OK);
        dataFlowContext.setResponseEntity(responseEntity);

    }


    /**
     * 校验查询条件是否满足条件
     *
     * @param reqJson 包含查询条件
     */
    private void validateOwnerData(JSONObject reqJson) {
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求中未包含communityId信息");
        Assert.jsonObjectHaveKey(reqJson, "ownerId", "请求中未包含ownerId信息");
        // Assert.jsonObjectHaveKey(reqJson, "ownerTypeCd", "请求中未包含ownerTypeCd信息");

    }

    @Override
    public int getOrder() {
        return super.DEFAULT_ORDER;
    }


    public IOwnerInnerServiceSMO getOwnerInnerServiceSMOImpl() {
        return ownerInnerServiceSMOImpl;
    }

    public void setOwnerInnerServiceSMOImpl(IOwnerInnerServiceSMO ownerInnerServiceSMOImpl) {
        this.ownerInnerServiceSMOImpl = ownerInnerServiceSMOImpl;
    }

}
