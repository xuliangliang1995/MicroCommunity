package com.java110.core.client;

import com.aliyun.oss.OSSClient;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

/**
 * @author xuliangliang
 * @Classname AliOssUploadTemplate
 * @Description TODO
 * @Date 2020/3/18 11:23
 * @blame Java Team
 */
@Component
public class AliOssUploadTemplate {
    static final String END_POINT = "http://oss-cn-beijing.aliyuncs.com";
    static final String ACCESS_KEY_ID = "oCfGqNjz6HcOdbjr";
    static final String ACCESS_KEY_SECRET = "5XNJhIZe2zcko3V8yXH3ICQ3tQIAuB";
    static final String BUCKET_NAME = "jzb-ka-test-2015";
    static final OSSClient CLIENT = new OSSClient(END_POINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);

    /**
     * 上传文件
     * @param file
     * @param ossKey
     * @return
     */
    public String upload(MultipartFile file, String ossKey) {
        try {
            CLIENT.putObject(BUCKET_NAME, ossKey, new ByteArrayInputStream(file.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("上传文件失败");
        }
        return ossKey;
    }

    /**
     * @param file
     * @return
     */
    public String upload(MultipartFile file) {
        String ossKey = DateFormatUtils.format(new Date(), "yyyyMMdd")
                .concat("/")
                .concat(UUID.randomUUID().toString());
        String fileName = file.getOriginalFilename();
        if (fileName.contains(".")) {
            String suffix = fileName.substring(fileName.lastIndexOf("."));
            ossKey = ossKey.concat(suffix);
        }
        try {
            CLIENT.putObject(BUCKET_NAME, ossKey, new ByteArrayInputStream(file.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("上传文件失败");
        }
        return ossKey;
    }

}
