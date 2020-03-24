package com.java110.api.listener.oss;

import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiDataFlowListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.smo.file.IFileInnerServiceSMO;
import com.java110.dto.file.FileDto;
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.ServiceCodeConstant;
import com.java110.utils.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

/**
 * @author xuliangliang
 * @Classname UploadFile2OssListener
 * @Description 上传文件到 OSS
 * @Date 2020/3/24 20:27
 * @blame Java Team
 */
@Java110Listener("uploadImg2OssListener")
public class UploadImg2OssListener extends AbstractServiceApiDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(UploadImg2OssListener.class);

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    /**
     * 业务 编码
     *
     * @return
     */
    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_UPLOAD_IMAGE_TO_OSS;
    }

    /**
     * 获取调用时的方法
     *
     * @return 接口对外提供方式 如HttpMethod.POST
     */
    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public void soService(ServiceDataFlowEvent event) {

        logger.debug("ServiceDataFlowEvent : {}", event);

        DataFlowContext dataFlowContext = event.getDataFlowContext();
        AppService service = event.getAppService();

        String paramIn = dataFlowContext.getReqData();

        //校验数据
        validate(paramIn);
        JSONObject paramObj = JSONObject.parseObject(paramIn);
        String base64 = paramObj.getString("img");

        FileDto fileDto = FileDto.AliOssBuilder.aFileDto()
                .withContext(base64)
                .build();
        String fileName = fileInnerServiceSMOImpl.saveFile(fileDto);
        paramObj.put("ownerPhotoId", fileDto.getFileId());
        paramObj.put("fileSaveName", fileName);

        ResponseEntity<String> responseEntity = ResponseEntity.ok(paramObj.toJSONString());
        dataFlowContext.setResponseEntity(responseEntity);
    }

    private void validate(String paramIn) {
        Assert.jsonObjectHaveKey(paramIn, "img", "请求报文中未包含img");
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
