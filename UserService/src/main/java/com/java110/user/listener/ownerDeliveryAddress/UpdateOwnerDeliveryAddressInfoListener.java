package com.java110.user.listener.ownerDeliveryAddress;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.user.dao.IOwnerDeliveryAddressServiceDao;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.utils.util.Assert;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.entity.center.Business;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 修改业主收货地址信息 侦听
 *
 * 处理节点
 * 1、businessOwnerDeliveryAddress:{} 业主收货地址基本信息节点
 * 2、businessOwnerDeliveryAddressAttr:[{}] 业主收货地址属性信息节点
 * 3、businessOwnerDeliveryAddressPhoto:[{}] 业主收货地址照片信息节点
 * 4、businessOwnerDeliveryAddressCerdentials:[{}] 业主收货地址证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E4%BF%AE%E6%94%B9%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("updateOwnerDeliveryAddressInfoListener")
@Transactional
public class UpdateOwnerDeliveryAddressInfoListener extends AbstractOwnerDeliveryAddressBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(UpdateOwnerDeliveryAddressInfoListener.class);
    @Autowired
    private IOwnerDeliveryAddressServiceDao ownerDeliveryAddressServiceDaoImpl;

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_UPDATE_OWNER_DELIVERY_ADDRESS_INFO;
    }

    /**
     * business过程
     * @param dataFlowContext 上下文对象
     * @param business 业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {

        JSONObject data = business.getDatas();

        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        //处理 businessOwnerDeliveryAddress 节点
        if(data.containsKey("businessOwnerDeliveryAddress")){
            //处理 businessOwnerDeliveryAddress 节点
            if(data.containsKey("businessOwnerDeliveryAddress")){
                Object _obj = data.get("businessOwnerDeliveryAddress");
                JSONArray businessOwnerDeliveryAddresss = null;
                if(_obj instanceof JSONObject){
                    businessOwnerDeliveryAddresss = new JSONArray();
                    businessOwnerDeliveryAddresss.add(_obj);
                }else {
                    businessOwnerDeliveryAddresss = (JSONArray)_obj;
                }
                //JSONObject businessOwnerDeliveryAddress = data.getJSONObject("businessOwnerDeliveryAddress");
                for (int _ownerDeliveryAddressIndex = 0; _ownerDeliveryAddressIndex < businessOwnerDeliveryAddresss.size();_ownerDeliveryAddressIndex++) {
                    JSONObject businessOwnerDeliveryAddress = businessOwnerDeliveryAddresss.getJSONObject(_ownerDeliveryAddressIndex);
                    doBusinessOwnerDeliveryAddress(business, businessOwnerDeliveryAddress);
                    if(_obj instanceof JSONObject) {
                        dataFlowContext.addParamOut("addressId", businessOwnerDeliveryAddress.getString("addressId"));
                    }
                }
            }
        }
    }


    /**
     * business to instance 过程
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
        List<Map> businessOwnerDeliveryAddressInfos = ownerDeliveryAddressServiceDaoImpl.getBusinessOwnerDeliveryAddressInfo(info);
        if( businessOwnerDeliveryAddressInfos != null && businessOwnerDeliveryAddressInfos.size() >0) {
            for (int _ownerDeliveryAddressIndex = 0; _ownerDeliveryAddressIndex < businessOwnerDeliveryAddressInfos.size();_ownerDeliveryAddressIndex++) {
                Map businessOwnerDeliveryAddressInfo = businessOwnerDeliveryAddressInfos.get(_ownerDeliveryAddressIndex);
                flushBusinessOwnerDeliveryAddressInfo(businessOwnerDeliveryAddressInfo,StatusConstant.STATUS_CD_VALID);
                ownerDeliveryAddressServiceDaoImpl.updateOwnerDeliveryAddressInfoInstance(businessOwnerDeliveryAddressInfo);
                if(businessOwnerDeliveryAddressInfo.size() == 1) {
                    dataFlowContext.addParamOut("addressId", businessOwnerDeliveryAddressInfo.get("address_id"));
                }
            }
        }

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
        Map delInfo = new HashMap();
        delInfo.put("bId",business.getbId());
        delInfo.put("operate",StatusConstant.OPERATE_DEL);
        //业主收货地址信息
        List<Map> ownerDeliveryAddressInfo = ownerDeliveryAddressServiceDaoImpl.getOwnerDeliveryAddressInfo(info);
        if(ownerDeliveryAddressInfo != null && ownerDeliveryAddressInfo.size() > 0){

            //业主收货地址信息
            List<Map> businessOwnerDeliveryAddressInfos = ownerDeliveryAddressServiceDaoImpl.getBusinessOwnerDeliveryAddressInfo(delInfo);
            //除非程序出错了，这里不会为空
            if(businessOwnerDeliveryAddressInfos == null || businessOwnerDeliveryAddressInfos.size() == 0){
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR,"撤单失败（ownerDeliveryAddress），程序内部异常,请检查！ "+delInfo);
            }
            for (int _ownerDeliveryAddressIndex = 0; _ownerDeliveryAddressIndex < businessOwnerDeliveryAddressInfos.size();_ownerDeliveryAddressIndex++) {
                Map businessOwnerDeliveryAddressInfo = businessOwnerDeliveryAddressInfos.get(_ownerDeliveryAddressIndex);
                flushBusinessOwnerDeliveryAddressInfo(businessOwnerDeliveryAddressInfo,StatusConstant.STATUS_CD_VALID);
                ownerDeliveryAddressServiceDaoImpl.updateOwnerDeliveryAddressInfoInstance(businessOwnerDeliveryAddressInfo);
            }
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
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"addressId 错误，不能自动生成（必须已经存在的addressId）"+businessOwnerDeliveryAddress);
        }
        //自动保存DEL
        autoSaveDelBusinessOwnerDeliveryAddress(business,businessOwnerDeliveryAddress);

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
