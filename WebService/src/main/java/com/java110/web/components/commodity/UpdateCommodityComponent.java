package com.java110.web.components.commodity;

import com.java110.core.context.IPageData;
import com.java110.web.smo.commodity.ICommodityServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * @author xuliangliang
 * @Classname UpdateCommodityComponent
 * @Description TODO
 * @Date 2020/3/26 14:31
 * @blame Java Team
 */
@Component("updateCommodity")
public class UpdateCommodityComponent {

    @Autowired
    private ICommodityServiceSMO commodityServiceSMOImpl;

    /**
     * 添加商品
     * @param pd
     * @return
     */
    public ResponseEntity<String> update(IPageData pd) {
        return commodityServiceSMOImpl.updateCommodity(pd);
    }

}
