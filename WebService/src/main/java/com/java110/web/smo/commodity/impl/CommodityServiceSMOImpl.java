package com.java110.web.smo.commodity.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.component.BaseComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.utils.constant.PrivilegeCodeConstant;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.util.Assert;
import com.java110.web.smo.commodity.ICommodityServiceSMO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author xuliangliang
 * @Classname CommodityServiceSMOImpl
 * @Description TODO
 * @Date 2020/3/24 14:42
 * @blame Java Team
 */
@Service("commodityServiceSMOImpl")
public class CommodityServiceSMOImpl extends BaseComponentSMO implements ICommodityServiceSMO {

    private static Logger logger = LoggerFactory.getLogger(CommodityServiceSMOImpl.class);


    @Autowired
    private RestTemplate restTemplate;
    /**
     * 添加商品
     *
     * @param pd
     * @return
     */
    @Override
    public ResponseEntity<String> addCommodity(IPageData pd) {

        validateSaveOwner(pd);

        //校验员工是否有权限操作
        super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.PRIVILEGE_MALL);

        JSONObject paramIn = JSONObject.parseObject(pd.getReqData());

        paramIn.put("userId", pd.getUserId());
        ResponseEntity responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                ServiceConstant.SERVICE_API_URL + "/api/commodity.saveCommodity",
                HttpMethod.POST);

        return responseEntity;
    }

    private void validateSaveOwner(IPageData pd) {
        Assert.jsonObjectHaveKey(pd.getReqData(), "communityId", "未包含小区ID");
        Assert.jsonObjectHaveKey(pd.getReqData(), "title", "请求报文中未包含age");
        Assert.jsonObjectHaveKey(pd.getReqData(), "currentPrice", "请求报文中未包含currentPrice");
        Assert.jsonObjectHaveKey(pd.getReqData(), "intro", "未包含商品介绍");
    }
}
