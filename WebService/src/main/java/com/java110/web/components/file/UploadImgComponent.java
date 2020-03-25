package com.java110.web.components.file;

import com.java110.core.context.IPageData;
import com.java110.web.smo.file.IUploadImgSMO;
import com.java110.web.smo.file.IUploadVedioSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author xuliangliang
 * @Classname UploadImgComponent
 * @Description TODO
 * @Date 2020/3/24 20:37
 * @blame Java Team
 */
@Component("uploadImg")
public class UploadImgComponent {

    @Autowired
    private IUploadImgSMO uploadImgSMOImpl;


    /**
     * 查询应用列表
     *
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<Object> upload(IPageData pd) throws IOException {
        return uploadImgSMOImpl.uploadImg(pd);
    }

    public IUploadImgSMO getUploadImgSMOImpl() {
        return uploadImgSMOImpl;
    }

    public void setUploadImgSMOImpl(IUploadImgSMO uploadImgSMOImpl) {
        this.uploadImgSMOImpl = uploadImgSMOImpl;
    }
}
