package com.java110.core.client;

import com.aliyun.oss.OSSClient;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
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

    public String uploadBase64Img(String base64Str) {
        String suffix = base64Str.substring(11, base64Str.indexOf(";"));
        // 使用插件传输产生的前缀
        String prefix = base64Str.substring(0, base64Str.indexOf(",") + 1);
        // 替换前缀为空
        base64Str = base64Str.replace(prefix,"");
        // imageString = imageString.substring(imageString.indexOf(",") + 1);

        Base64 base64 = new Base64();
        byte[] imageByte =  base64.decode(base64Str);

        // 打包时将出现内部专用api异常
        // BASE64Decoder decoder = new BASE64Decoder();
        // byte[] imageByte = decoder.decodeBuffer(imageString);

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageByte);

        String ossKey = generateOssKey();
        if (StringUtils.isNotBlank(suffix)) {
            ossKey = ossKey.concat(".").concat(suffix);
        }
        CLIENT.putObject(BUCKET_NAME, ossKey, byteArrayInputStream);
        return ossKey;
    }

    /**
     * @param file
     * @return
     */
    public String upload(MultipartFile file) {
        String ossKey = generateOssKey();
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

    private String generateOssKey() {
        return DateFormatUtils.format(new Date(), "yyyyMMdd")
                .concat("/")
                .concat(UUID.randomUUID().toString());
    }

}
