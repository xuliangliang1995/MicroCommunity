package com.java110;

import com.java110.code.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xuliangliang
 * @Classname CommodityPhotoGeneratorApplication
 * @Description
 * @Date 2020/3/24 16:42
 * @blame Java Team
 */
public class CommodityPhotoGeneratorApplication {
    protected CommodityPhotoGeneratorApplication() {
        // prevents calls from subclass
        throw new UnsupportedOperationException();
    }

    /**
     * 代码生成器 入口方法
     *
     * @param args 参数
     */
    public static void main(String[] args) {
        Data data = new Data();
        data.setId("photoId");
        data.setName("commodityPhoto");
        data.setDesc("商品配图");
        data.setShareParam("commodityId");
        data.setShareColumn("commodity_id");
        data.setNewBusinessTypeCd("BUSINESS_TYPE_SAVE_COMMODITY_PHOTO");
        data.setUpdateBusinessTypeCd("BUSINESS_TYPE_UPDATE_COMMODITY_PHOTO");
        data.setDeleteBusinessTypeCd("BUSINESS_TYPE_DELETE_COMMODITY_PHOTO");
        data.setNewBusinessTypeCdValue("600000030001");
        data.setUpdateBusinessTypeCdValue("600100040001");
        data.setDeleteBusinessTypeCdValue("600100050001");
        data.setBusinessTableName("business_commodity_photo");
        data.setTableName("commodity_photo");
        Map<String, String> param = new HashMap<>();
        param.put("photoId", "photo_id");
        param.put("commodityId", "commodity_id");
        param.put("photo", "photo");
        param.put("userId", "user_id");
        param.put("bId", "b_id");
        param.put("remark", "remark");
        param.put("operate", "operate");
        data.setParams(param);
        GeneratorSaveInfoListener generatorSaveInfoListener = new GeneratorSaveInfoListener();
        generatorSaveInfoListener.generator(data);

        GeneratorAbstractBussiness generatorAbstractBussiness = new GeneratorAbstractBussiness();
        generatorAbstractBussiness.generator(data);

        GeneratorIServiceDaoListener generatorIServiceDaoListener = new GeneratorIServiceDaoListener();
        generatorIServiceDaoListener.generator(data);

        GeneratorServiceDaoImplListener generatorServiceDaoImplListener = new GeneratorServiceDaoImplListener();
        generatorServiceDaoImplListener.generator(data);

        GeneratorServiceDaoImplMapperListener generatorServiceDaoImplMapperListener = null;
        generatorServiceDaoImplMapperListener = new GeneratorServiceDaoImplMapperListener();
        generatorServiceDaoImplMapperListener.generator(data);

        GeneratorUpdateInfoListener generatorUpdateInfoListener = new GeneratorUpdateInfoListener();
        generatorUpdateInfoListener.generator(data);

        GeneratorDeleteInfoListener generatorDeleteInfoListener = new GeneratorDeleteInfoListener();
        generatorDeleteInfoListener.generator(data);

        GeneratorInnerServiceSMOImpl generatorInnerServiceSMOImpl = new GeneratorInnerServiceSMOImpl();
        generatorInnerServiceSMOImpl.generator(data);

        GeneratorDtoBean generatorDtoBean = new GeneratorDtoBean();
        generatorDtoBean.generator(data);

        GeneratorIInnerServiceSMO generatorIInnerServiceSMO = new GeneratorIInnerServiceSMO();
        generatorIInnerServiceSMO.generator(data);
    }
}
