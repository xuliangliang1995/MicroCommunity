package com.java110.web.smo.file;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

/**
 * @author xuliangliang
 * @Classname IUploadImgSMO
 * @Description TODO
 * @Date 2020/3/24 20:38
 * @blame Java Team
 */
public interface IUploadImgSMO {
    /**
     * 上传图片
     * @param pd
     * @return
     */
    ResponseEntity<Object> uploadImg(IPageData pd);
}
