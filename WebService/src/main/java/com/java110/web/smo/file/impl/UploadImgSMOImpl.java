package com.java110.web.smo.file.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.client.RestTemplate;
import com.java110.core.component.BaseComponentSMO;
import com.java110.core.context.IPageData;
import com.java110.utils.constant.PrivilegeCodeConstant;
import com.java110.utils.constant.ServiceConstant;
import com.java110.utils.util.Assert;
import com.java110.web.smo.file.IUploadImgSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * @author xuliangliang
 * @Classname UploadImgSMOImpl
 * @Description TODO
 * @Date 2020/3/24 20:39
 * @blame Java Team
 */
@Service("uploadImgSMOImpl")
public class UploadImgSMOImpl extends BaseComponentSMO implements IUploadImgSMO {
    @Autowired
    private RestTemplate restTemplate;
    /**
     * 上传图片
     *
     * @param pd
     * @return
     */
    @Override
    public ResponseEntity<Object> uploadImg(IPageData pd) {
        validateUploadImg(pd);

        //校验员工是否有权限操作
        super.checkUserHasPrivilege(pd, restTemplate, PrivilegeCodeConstant.PRIVILEGE_OSS);

        JSONObject paramIn = JSONObject.parseObject(pd.getReqData());

        paramIn.put("userId", pd.getUserId());
        ResponseEntity responseEntity = this.callCenterService(restTemplate, pd, paramIn.toJSONString(),
                ServiceConstant.SERVICE_API_URL + "/api/oss.uploadImg",
                HttpMethod.POST);
        return responseEntity;
    }

    private void validateUploadImg(IPageData pd) {
        Assert.jsonObjectHaveKey(pd.getReqData(), "img", "未包含img");
    }
}
