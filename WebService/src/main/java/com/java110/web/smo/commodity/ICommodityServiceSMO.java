package com.java110.web.smo.commodity;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * @author xuliangliang
 * @Classname ICommodityServiceSMP
 * @Description TODO
 * @Date 2020/3/24 14:39
 * @blame Java Team
 */
public interface ICommodityServiceSMO {
    /**
     * 添加商品
     * @param pd
     * @return
     */
    ResponseEntity<String> addCommodity(IPageData pd);


    /**
     *
     * @param pd
     * @return
     */
    ResponseEntity<String> updateCommodity(IPageData pd);
}
