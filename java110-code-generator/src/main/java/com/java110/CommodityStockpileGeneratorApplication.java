package com.java110;

import com.java110.code.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xuliangliang
 * @Classname CommodityStockpileGeneratorApplication
 * @Description TODO
 * @Date 2020/3/24 19:50
 * @blame Java Team
 */
public class CommodityStockpileGeneratorApplication {

    protected CommodityStockpileGeneratorApplication() {
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
        data.setId("stockpileId");
        data.setName("commodityStockpile");
        data.setDesc("商品库存");
        data.setShareParam("commodityId");
        data.setShareColumn("commodity_id");
        data.setNewBusinessTypeCd("BUSINESS_TYPE_SAVE_COMMODITY_STOCKPILE");
        data.setUpdateBusinessTypeCd("BUSINESS_TYPE_UPDATE_COMMODITY_STOCKPILE");
        data.setDeleteBusinessTypeCd("BUSINESS_TYPE_DELETE_COMMODITY_STOCKPILE");
        data.setNewBusinessTypeCdValue("600300030001");
        data.setUpdateBusinessTypeCdValue("600300040001");
        data.setDeleteBusinessTypeCdValue("600300050001");
        data.setBusinessTableName("business_commodity_stockpile");
        data.setTableName("commodity_stockpile");
        Map<String, String> param = new HashMap<>();
        param.put("stockpileId", "stockpile_id");
        param.put("commodityId", "commodity_id");
        param.put("amount", "amount");
        param.put("statusCd", "status_cd");
        param.put("version", "version");
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
