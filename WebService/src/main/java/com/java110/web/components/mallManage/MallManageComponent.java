package com.java110.web.components.mallManage;

import com.java110.core.context.IPageData;
import com.java110.web.smo.mallManage.impl.MallManageSMOImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 商城管理
 */
@Component("mallManage")
public class MallManageComponent {

    @Autowired
    private MallManageSMOImpl mallManageSMOImpl;

    /**
     * 查询应用列表
     *
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd) {
        return mallManageSMOImpl.saveMallManage(pd);
    }

    public MallManageSMOImpl getMallManageSMOImpl() {
        return mallManageSMOImpl;
    }

    public void setMallManageSMOImpl(MallManageSMOImpl mallManageSMOImpl) {
        this.mallManageSMOImpl = mallManageSMOImpl;
    }
}
