package com.java110.web.components.commodity;

import com.java110.core.context.IPageData;
import com.java110.web.smo.commodity.ICommodityServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * @author xuliangliang
 * @Classname AddCommodityComponent
 * @Description 添加商品组件
 * @Date 2020/3/24 14:36
 * @blame Java Team
 */
@Component("addCommodity")
public class AddCommodityComponent {

    @Autowired
    private ICommodityServiceSMO commodityServiceSMOImpl;

    /**
     * 添加商品
     * @param pd
     * @return
     */
    public ResponseEntity<String> save(IPageData pd) {
        return commodityServiceSMOImpl.addCommodity(pd);
    }

}
