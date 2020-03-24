package com.java110.mall.listener.commodity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.mall.dao.ICommodityServiceDao;
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
 * 修改商品信息 侦听
 *
 * 处理节点
 * 1、businessCommodity:{} 商品基本信息节点
 * 2、businessCommodityAttr:[{}] 商品属性信息节点
 * 3、businessCommodityPhoto:[{}] 商品照片信息节点
 * 4、businessCommodityCerdentials:[{}] 商品证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E4%BF%AE%E6%94%B9%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("updateCommodityInfoListener")
@Transactional
public class UpdateCommodityInfoListener extends AbstractCommodityBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(UpdateCommodityInfoListener.class);
    @Autowired
    private ICommodityServiceDao commodityServiceDaoImpl;

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_UPDATE_COMMODITY;
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

        //处理 businessCommodity 节点
        if(data.containsKey("businessCommodity")){
            //处理 businessCommodity 节点
            if(data.containsKey("businessCommodity")){
                Object _obj = data.get("businessCommodity");
                JSONArray businessCommoditys = null;
                if(_obj instanceof JSONObject){
                    businessCommoditys = new JSONArray();
                    businessCommoditys.add(_obj);
                }else {
                    businessCommoditys = (JSONArray)_obj;
                }
                //JSONObject businessCommodity = data.getJSONObject("businessCommodity");
                for (int _commodityIndex = 0; _commodityIndex < businessCommoditys.size();_commodityIndex++) {
                    JSONObject businessCommodity = businessCommoditys.getJSONObject(_commodityIndex);
                    doBusinessCommodity(business, businessCommodity);
                    if(_obj instanceof JSONObject) {
                        dataFlowContext.addParamOut("commodityId", businessCommodity.getString("commodityId"));
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

        //商品信息
        List<Map> businessCommodityInfos = commodityServiceDaoImpl.getBusinessCommodityInfo(info);
        if( businessCommodityInfos != null && businessCommodityInfos.size() >0) {
            for (int _commodityIndex = 0; _commodityIndex < businessCommodityInfos.size();_commodityIndex++) {
                Map businessCommodityInfo = businessCommodityInfos.get(_commodityIndex);
                flushBusinessCommodityInfo(businessCommodityInfo,StatusConstant.STATUS_CD_VALID);
                commodityServiceDaoImpl.updateCommodityInfoInstance(businessCommodityInfo);
                if(businessCommodityInfo.size() == 1) {
                    dataFlowContext.addParamOut("commodityId", businessCommodityInfo.get("commodity_id"));
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
        //商品信息
        List<Map> commodityInfo = commodityServiceDaoImpl.getCommodityInfo(info);
        if(commodityInfo != null && commodityInfo.size() > 0){

            //商品信息
            List<Map> businessCommodityInfos = commodityServiceDaoImpl.getBusinessCommodityInfo(delInfo);
            //除非程序出错了，这里不会为空
            if(businessCommodityInfos == null || businessCommodityInfos.size() == 0){
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR,"撤单失败（commodity），程序内部异常,请检查！ "+delInfo);
            }
            for (int _commodityIndex = 0; _commodityIndex < businessCommodityInfos.size();_commodityIndex++) {
                Map businessCommodityInfo = businessCommodityInfos.get(_commodityIndex);
                flushBusinessCommodityInfo(businessCommodityInfo,StatusConstant.STATUS_CD_VALID);
                commodityServiceDaoImpl.updateCommodityInfoInstance(businessCommodityInfo);
            }
        }

    }



    /**
     * 处理 businessCommodity 节点
     * @param business 总的数据节点
     * @param businessCommodity 商品节点
     */
    private void doBusinessCommodity(Business business,JSONObject businessCommodity){

        Assert.jsonObjectHaveKey(businessCommodity,"commodityId","businessCommodity 节点下没有包含 commodityId 节点");

        if(businessCommodity.getString("commodityId").startsWith("-")){
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"commodityId 错误，不能自动生成（必须已经存在的commodityId）"+businessCommodity);
        }
        //自动保存DEL
        autoSaveDelBusinessCommodity(business,businessCommodity);

        businessCommodity.put("bId",business.getbId());
        businessCommodity.put("operate", StatusConstant.OPERATE_ADD);
        //保存商品信息
        commodityServiceDaoImpl.saveBusinessCommodityInfo(businessCommodity);

    }




    @Override
    public ICommodityServiceDao getCommodityServiceDaoImpl() {
        return commodityServiceDaoImpl;
    }

    public void setCommodityServiceDaoImpl(ICommodityServiceDao commodityServiceDaoImpl) {
        this.commodityServiceDaoImpl = commodityServiceDaoImpl;
    }



}
