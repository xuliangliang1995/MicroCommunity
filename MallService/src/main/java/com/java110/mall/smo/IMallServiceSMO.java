package com.java110.mall.smo;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.context.BusinessServiceDataFlow;
import com.java110.utils.exception.SMOException;

/**
 * @author xuliangliang
 * @Classname IMallServiceSMO
 * @Description
 * @Date 2020/3/23 21:12
 * @blame Java Team
 */
public interface IMallServiceSMO {

    JSONObject service(BusinessServiceDataFlow businessServiceDataFlow) throws SMOException;
}
