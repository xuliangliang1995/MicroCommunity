package com.java110;

import com.java110.code.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xuliangliang
 * @Classname OwnerDeliveryAddressGeneratorApplication
 * @Description TODO
 * @Date 2020/3/21 10:42
 * @blame Java Team
 */
public class OwnerDeliveryAddressGeneratorApplication {
    protected OwnerDeliveryAddressGeneratorApplication() {
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
        data.setId("addressId");
        data.setName("ownerDeliveryAddress");
        data.setDesc("业主收货地址");
        data.setShareParam("ownerId");
        data.setShareColumn("owner_id");
        data.setNewBusinessTypeCd("BUSINESS_TYPE_SAVE_OWNER_DELIVERY_ADDRESS_INFO");
        data.setUpdateBusinessTypeCd("BUSINESS_TYPE_UPDATE_OWNER_DELIVERY_ADDRESS_INFO");
        data.setDeleteBusinessTypeCd("BUSINESS_TYPE_DELETE_OWNER_DELIVERY_ADDRESS_INFO");
        data.setNewBusinessTypeCdValue("111300030001");
        data.setUpdateBusinessTypeCdValue("111300040001");
        data.setDeleteBusinessTypeCdValue("111300050001");
        data.setBusinessTableName("business_building_owner_delivery_address");
        data.setTableName("building_owner_delivery_address");
        Map<String, String> param = new HashMap<String, String>();
        param.put("addressId", "address_id");
        param.put("memberId", "member_id");
        param.put("ownerId", "owner_id");
        param.put("userId", "user_id");
        param.put("bId", "b_id");
        param.put("companyName", "company_name");
        param.put("companyFloor", "company_floor");
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
