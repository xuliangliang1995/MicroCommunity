package com.java110.user.listener.ownerDeliveryAddress;

import com.alibaba.fastjson.JSONObject;
import com.java110.user.dao.IOwnerDeliveryAddressServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.Business;
import com.java110.event.service.AbstractBusinessServiceDataFlowListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * 业主收货地址 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractOwnerDeliveryAddressBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener{
    private static Logger logger = LoggerFactory.getLogger(AbstractOwnerDeliveryAddressBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     * @return
     */
    public abstract IOwnerDeliveryAddressServiceDao getOwnerDeliveryAddressServiceDaoImpl();

    /**
     * 刷新 businessOwnerDeliveryAddressInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     * @param businessOwnerDeliveryAddressInfo
     */
    protected void flushBusinessOwnerDeliveryAddressInfo(Map businessOwnerDeliveryAddressInfo,String statusCd){
        businessOwnerDeliveryAddressInfo.put("newBId", businessOwnerDeliveryAddressInfo.get("b_id"));
        businessOwnerDeliveryAddressInfo.put("operate",businessOwnerDeliveryAddressInfo.get("operate"));
businessOwnerDeliveryAddressInfo.put("companyFloor",businessOwnerDeliveryAddressInfo.get("company_floor"));
businessOwnerDeliveryAddressInfo.put("companyName",businessOwnerDeliveryAddressInfo.get("company_name"));
businessOwnerDeliveryAddressInfo.put("ownerId",businessOwnerDeliveryAddressInfo.get("owner_id"));
businessOwnerDeliveryAddressInfo.put("userId",businessOwnerDeliveryAddressInfo.get("user_id"));
businessOwnerDeliveryAddressInfo.put("addressId",businessOwnerDeliveryAddressInfo.get("address_id"));
businessOwnerDeliveryAddressInfo.put("memberId",businessOwnerDeliveryAddressInfo.get("member_id"));
businessOwnerDeliveryAddressInfo.remove("bId");
        businessOwnerDeliveryAddressInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     * @param businessOwnerDeliveryAddress 业主收货地址信息
     */
    protected void autoSaveDelBusinessOwnerDeliveryAddress(Business business, JSONObject businessOwnerDeliveryAddress){
//自动插入DEL
        Map info = new HashMap();
        info.put("addressId",businessOwnerDeliveryAddress.getString("addressId"));
        info.put("statusCd",StatusConstant.STATUS_CD_VALID);
        List<Map> currentOwnerDeliveryAddressInfos = getOwnerDeliveryAddressServiceDaoImpl().getOwnerDeliveryAddressInfo(info);
        if(currentOwnerDeliveryAddressInfos == null || currentOwnerDeliveryAddressInfos.size() != 1){
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"未找到需要修改数据信息，入参错误或数据有问题，请检查"+info);
        }

        Map currentOwnerDeliveryAddressInfo = currentOwnerDeliveryAddressInfos.get(0);

        currentOwnerDeliveryAddressInfo.put("bId",business.getbId());

        currentOwnerDeliveryAddressInfo.put("operate",currentOwnerDeliveryAddressInfo.get("operate"));
currentOwnerDeliveryAddressInfo.put("companyFloor",currentOwnerDeliveryAddressInfo.get("company_floor"));
currentOwnerDeliveryAddressInfo.put("companyName",currentOwnerDeliveryAddressInfo.get("company_name"));
currentOwnerDeliveryAddressInfo.put("ownerId",currentOwnerDeliveryAddressInfo.get("owner_id"));
currentOwnerDeliveryAddressInfo.put("userId",currentOwnerDeliveryAddressInfo.get("user_id"));
currentOwnerDeliveryAddressInfo.put("addressId",currentOwnerDeliveryAddressInfo.get("address_id"));
currentOwnerDeliveryAddressInfo.put("memberId",currentOwnerDeliveryAddressInfo.get("member_id"));


        currentOwnerDeliveryAddressInfo.put("operate",StatusConstant.OPERATE_DEL);
        getOwnerDeliveryAddressServiceDaoImpl().saveBusinessOwnerDeliveryAddressInfo(currentOwnerDeliveryAddressInfo);
    }





}
