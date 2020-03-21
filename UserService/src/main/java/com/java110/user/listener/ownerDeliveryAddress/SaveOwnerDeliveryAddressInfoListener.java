package com.java110.user.listener.ownerDeliveryAddress;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.user.dao.IOwnerDeliveryAddressServiceDao;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 保存 业主收货地址信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveOwnerDeliveryAddressInfoListener")
@Transactional
public class SaveOwnerDeliveryAddressInfoListener extends AbstractOwnerDeliveryAddressBusinessServiceDataFlowListener{

    private static Logger logger = LoggerFactory.getLogger(SaveOwnerDeliveryAddressInfoListener.class);

    @Autowired
    private IOwnerDeliveryAddressServiceDao ownerDeliveryAddressServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_OWNER_DELIVERY_ADDRESS_INFO;
    }

    /**
     * 保存业主收货地址信息 business 表中
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        //处理 businessOwnerDeliveryAddress 节点
        if(data.containsKey("businessOwnerDeliveryAddress")){
            Object bObj = data.get("businessOwnerDeliveryAddress");
            JSONArray businessOwnerDeliveryAddresss = null;
            if(bObj instanceof JSONObject){
                businessOwnerDeliveryAddresss = new JSONArray();
                businessOwnerDeliveryAddresss.add(bObj);
            }else {
                businessOwnerDeliveryAddresss = (JSONArray)bObj;
            }
            //JSONObject businessOwnerDeliveryAddress = data.getJSONObject("businessOwnerDeliveryAddress");
            for (int bOwnerDeliveryAddressIndex = 0; bOwnerDeliveryAddressIndex < businessOwnerDeliveryAddresss.size();bOwnerDeliveryAddressIndex++) {
                JSONObject businessOwnerDeliveryAddress = businessOwnerDeliveryAddresss.getJSONObject(bOwnerDeliveryAddressIndex);
                doBusinessOwnerDeliveryAddress(business, businessOwnerDeliveryAddress);
                if(bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("addressId", businessOwnerDeliveryAddress.getString("addressId"));
                }
            }
        }
    }

    /**
     * business 数据转移到 instance
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();

        Map info = new HashMap();
        info.put("bId",business.getbId());
        info.put("operate",StatusConstant.OPERATE_ADD);

        //业主收货地址信息
        List<Map> businessOwnerDeliveryAddressInfo = ownerDeliveryAddressServiceDaoImpl.getBusinessOwnerDeliveryAddressInfo(info);
        if( businessOwnerDeliveryAddressInfo != null && businessOwnerDeliveryAddressInfo.size() >0) {
            reFreshShareColumn(info, businessOwnerDeliveryAddressInfo.get(0));
            ownerDeliveryAddressServiceDaoImpl.saveOwnerDeliveryAddressInfoInstance(info);
            if(businessOwnerDeliveryAddressInfo.size() == 1) {
                dataFlowContext.addParamOut("addressId", businessOwnerDeliveryAddressInfo.get(0).get("address_id"));
            }
        }
    }


    /**
     * 刷 分片字段
     *
     * @param info         查询对象
     * @param businessInfo 小区ID
     */
    private void reFreshShareColumn(Map info, Map businessInfo) {

        if (info.containsKey("ownerId")) {
            return;
        }

        if (!businessInfo.containsKey("owner_id")) {
            return;
        }

        info.put("ownerId", businessInfo.get("owner_id"));
    }
    /**
     * 撤单
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doRecover(DataFlowContext dataFlowContext, Business business) {
        String bId = business.getbId();
        //Assert.hasLength(bId,"请求报文中没有包含 bId");
        Map info = new HashMap();
        info.put("bId",bId);
        info.put("statusCd",StatusConstant.STATUS_CD_VALID);
        Map paramIn = new HashMap();
        paramIn.put("bId",bId);
        paramIn.put("statusCd",StatusConstant.STATUS_CD_INVALID);
        //业主收货地址信息
        List<Map> ownerDeliveryAddressInfo = ownerDeliveryAddressServiceDaoImpl.getOwnerDeliveryAddressInfo(info);
        if(ownerDeliveryAddressInfo != null && ownerDeliveryAddressInfo.size() > 0){
            reFreshShareColumn(paramIn, ownerDeliveryAddressInfo.get(0));
            ownerDeliveryAddressServiceDaoImpl.updateOwnerDeliveryAddressInfoInstance(paramIn);
        }
    }



    /**
     * 处理 businessOwnerDeliveryAddress 节点
     * @param business 总的数据节点
     * @param businessOwnerDeliveryAddress 业主收货地址节点
     */
    private void doBusinessOwnerDeliveryAddress(Business business,JSONObject businessOwnerDeliveryAddress){

        Assert.jsonObjectHaveKey(businessOwnerDeliveryAddress,"addressId","businessOwnerDeliveryAddress 节点下没有包含 addressId 节点");

        if(businessOwnerDeliveryAddress.getString("addressId").startsWith("-")){
            //刷新缓存
            //flushOwnerDeliveryAddressId(business.getDatas());

            businessOwnerDeliveryAddress.put("addressId",GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_addressId));

        }

        businessOwnerDeliveryAddress.put("bId",business.getbId());
        businessOwnerDeliveryAddress.put("operate", StatusConstant.OPERATE_ADD);
        //保存业主收货地址信息
        ownerDeliveryAddressServiceDaoImpl.saveBusinessOwnerDeliveryAddressInfo(businessOwnerDeliveryAddress);

    }

    @Override
    public IOwnerDeliveryAddressServiceDao getOwnerDeliveryAddressServiceDaoImpl() {
        return ownerDeliveryAddressServiceDaoImpl;
    }

    public void setOwnerDeliveryAddressServiceDaoImpl(IOwnerDeliveryAddressServiceDao ownerDeliveryAddressServiceDaoImpl) {
        this.ownerDeliveryAddressServiceDaoImpl = ownerDeliveryAddressServiceDaoImpl;
    }
}
