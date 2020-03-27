package com.java110.api.listener.commodity;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiDataFlowListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.commodity.ICommodityInnerServiceSMO;
import com.java110.dto.commodity.CommodityDetailDto;
import com.java110.dto.commodity.CommodityDto;
import com.java110.dto.owner.DeliveryAddressDto;
import com.java110.dto.owner.OwnerDeliveryAddressDto;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.owner.OwnerDtoWithDeliveryAddress;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.api.ApiCommodityVo;
import com.java110.vo.api.ApiOwnerDataVo;
import com.java110.vo.api.ApiOwnerVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xuliangliang
 * @Classname ListCommodityListener
 * @Description 商品列表
 * @Date 2020/3/26 20:05
 * @blame Java Team
 */
@Java110Listener("listCommodityListener")
public class ListCommodityListener extends AbstractServiceApiDataFlowListener {
    @Autowired
    private ICommodityInnerServiceSMO commodityInnerServiceSMOImpl;
    /**
     * 业务 编码
     *
     * @return
     */
    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_LIST_COMMODITY;
    }

    /**
     * 获取调用时的方法
     *
     * @return 接口对外提供方式 如HttpMethod.GET
     */
    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public void soService(ServiceDataFlowEvent event) {
        DataFlowContext dataFlowContext = event.getDataFlowContext();
        //获取请求数据
        JSONObject reqJson = dataFlowContext.getReqJson();
        validateCommodityData(reqJson);

        CommodityDto commodityDto = new CommodityDto();
        commodityDto.setCommodityId(reqJson.getString("commodityId"));
        commodityDto.setTitle(reqJson.getString("title"));
        commodityDto.setShow(reqJson.getString("show"));
        List<CommodityDetailDto> commodityDtos = commodityInnerServiceSMOImpl.queryCommoditys(commodityDto);

        ApiCommodityVo apiCommodityVo = new ApiCommodityVo();
        apiCommodityVo.setCommodities(commodityDtos);
        apiCommodityVo.setTotal(commodityDtos.size());
        apiCommodityVo.setRecords(1);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiCommodityVo), HttpStatus.OK);
        dataFlowContext.setResponseEntity(responseEntity);
    }

    private void validateCommodityData(JSONObject reqJson) {
    }

    /**
     * 获取顺序,为了同一个事件需要多个侦听处理时，需要有前后顺序
     *
     * @return
     */
    @Override
    public int getOrder() {
        return 0;
    }
}
