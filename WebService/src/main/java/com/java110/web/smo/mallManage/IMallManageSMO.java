package com.java110.web.smo.mallManage;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

public interface IMallManageSMO {
    /**
     * 福利商城管理
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    ResponseEntity<String> saveMallManage(IPageData pd);
}
