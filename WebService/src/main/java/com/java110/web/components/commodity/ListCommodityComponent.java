package com.java110.web.components.commodity;

import com.java110.core.context.IPageData;
import com.java110.web.smo.commodity.ICommodityServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * @author xuliangliang
 * @Classname ListCommodityComponent
 * @Description
 * @Date 2020/3/26 19:55
 * @blame Java Team
 */
@Component("listCommodity")
public class ListCommodityComponent {

    @Autowired
    private ICommodityServiceSMO commodityServiceSMOImpl;

    public ResponseEntity<String> list(IPageData pd) {
        return commodityServiceSMOImpl.listCommodities(pd);
    }
}
