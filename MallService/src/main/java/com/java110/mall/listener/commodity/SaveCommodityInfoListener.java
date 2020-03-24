package com.java110.mall.listener.commodity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.mall.dao.ICommodityServiceDao;
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
 * 保存 商品信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveCommodityInfoListener")
@Transactional
public class SaveCommodityInfoListener extends AbstractCommodityBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(SaveCommodityInfoListener.class);

    @Autowired
    private ICommodityServiceDao commodityServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_COMMODITY;
    }

    /**
     * 保存商品信息 business 表中
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        //处理 businessCommodity 节点
        if(data.containsKey("businessCommodity")){
            Object bObj = data.get("businessCommodity");
            JSONArray businessCommoditys = null;
            if(bObj instanceof JSONObject){
                businessCommoditys = new JSONArray();
                businessCommoditys.add(bObj);
            }else {
                businessCommoditys = (JSONArray)bObj;
            }
            //JSONObject businessCommodity = data.getJSONObject("businessCommodity");
            for (int bCommodityIndex = 0; bCommodityIndex < businessCommoditys.size();bCommodityIndex++) {
                JSONObject businessCommodity = businessCommoditys.getJSONObject(bCommodityIndex);
                doBusinessCommodity(business, businessCommodity);
                if(bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("commodityId", businessCommodity.getString("commodityId"));
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

        //商品信息
        List<Map> businessCommodityInfo = commodityServiceDaoImpl.getBusinessCommodityInfo(info);
        if( businessCommodityInfo != null && businessCommodityInfo.size() >0) {
            reFreshShareColumn(info, businessCommodityInfo.get(0));
            commodityServiceDaoImpl.saveCommodityInfoInstance(info);
            if(businessCommodityInfo.size() == 1) {
                dataFlowContext.addParamOut("commodityId", businessCommodityInfo.get(0).get("commodity_id"));
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

        if (info.containsKey("commodityId")) {
            return;
        }

        if (!businessInfo.containsKey("commodity_id")) {
            return;
        }

        info.put("commodityId", businessInfo.get("commodity_id"));
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
        //商品信息
        List<Map> commodityInfo = commodityServiceDaoImpl.getCommodityInfo(info);
        if(commodityInfo != null && commodityInfo.size() > 0){
            reFreshShareColumn(paramIn, commodityInfo.get(0));
            commodityServiceDaoImpl.updateCommodityInfoInstance(paramIn);
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
            //刷新缓存
            //flushCommodityId(business.getDatas());

            businessCommodity.put("commodityId",GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_commodityId));

        }

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
