package com.java110;

import com.java110.code.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xuliangliang
 * @Classname CommodityGeneratorApplication
 * @Description TODO
 * @Date 2020/3/24 9:32
 * @blame Java Team
 */
public class CommodityGeneratorApplication {

    protected CommodityGeneratorApplication() {
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
        data.setId("commodityId");
        data.setName("commodity");
        data.setDesc("商品");
        data.setShareParam("commodityId");
        data.setShareColumn("commodity_id");
        data.setNewBusinessTypeCd("BUSINESS_TYPE_SAVE_COMMODITY");
        data.setUpdateBusinessTypeCd("BUSINESS_TYPE_UPDATE_COMMODITY");
        data.setDeleteBusinessTypeCd("BUSINESS_TYPE_DELETE_COMMODITY");
        data.setNewBusinessTypeCdValue("600000030001");
        data.setUpdateBusinessTypeCdValue("600000040001");
        data.setDeleteBusinessTypeCdValue("600000050001");
        data.setBusinessTableName("business_commodity");
        data.setTableName("m_commodity");
        Map<String, String> param = new HashMap<>();
        param.put("commodityId", "commodity_id");
        param.put("communityId", "community_id");
        param.put("userId", "user_id");
        param.put("bId", "b_id");
        param.put("title", "title");
        param.put("originalPrice", "original_price");
        param.put("currentPrice", "current_price");
        param.put("show", "show");
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
