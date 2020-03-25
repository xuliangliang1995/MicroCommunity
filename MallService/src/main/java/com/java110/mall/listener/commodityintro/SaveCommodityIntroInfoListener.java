package com.java110.mall.listener.commodityintro;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.mall.dao.ICommodityIntroServiceDao;
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
 * 保存 商品简介信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveCommodityIntroInfoListener")
@Transactional
public class SaveCommodityIntroInfoListener extends AbstractCommodityIntroBusinessServiceDataFlowListener{

    private static Logger logger = LoggerFactory.getLogger(SaveCommodityIntroInfoListener.class);

    @Autowired
    private ICommodityIntroServiceDao commodityIntroServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_COMMODITY_INTRO;
    }

    /**
     * 保存商品简介信息 business 表中
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        //处理 businessCommodityIntro 节点
        if(data.containsKey("businessCommodityIntro")){
            Object bObj = data.get("businessCommodityIntro");
            JSONArray businessCommodityIntros = null;
            if(bObj instanceof JSONObject){
                businessCommodityIntros = new JSONArray();
                businessCommodityIntros.add(bObj);
            }else {
                businessCommodityIntros = (JSONArray)bObj;
            }
            //JSONObject businessCommodityIntro = data.getJSONObject("businessCommodityIntro");
            for (int bCommodityIntroIndex = 0; bCommodityIntroIndex < businessCommodityIntros.size();bCommodityIntroIndex++) {
                JSONObject businessCommodityIntro = businessCommodityIntros.getJSONObject(bCommodityIntroIndex);
                doBusinessCommodityIntro(business, businessCommodityIntro);
                if(bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("introId", businessCommodityIntro.getString("introId"));
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

        //商品简介信息
        List<Map> businessCommodityIntroInfo = commodityIntroServiceDaoImpl.getBusinessCommodityIntroInfo(info);
        if( businessCommodityIntroInfo != null && businessCommodityIntroInfo.size() >0) {
            reFreshShareColumn(info, businessCommodityIntroInfo.get(0));
            commodityIntroServiceDaoImpl.saveCommodityIntroInfoInstance(info);
            if(businessCommodityIntroInfo.size() == 1) {
                dataFlowContext.addParamOut("introId", businessCommodityIntroInfo.get(0).get("intro_id"));
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
        //商品简介信息
        List<Map> commodityIntroInfo = commodityIntroServiceDaoImpl.getCommodityIntroInfo(info);
        if(commodityIntroInfo != null && commodityIntroInfo.size() > 0){
            reFreshShareColumn(paramIn, commodityIntroInfo.get(0));
            commodityIntroServiceDaoImpl.updateCommodityIntroInfoInstance(paramIn);
        }
    }



    /**
     * 处理 businessCommodityIntro 节点
     * @param business 总的数据节点
     * @param businessCommodityIntro 商品简介节点
     */
    private void doBusinessCommodityIntro(Business business,JSONObject businessCommodityIntro){

        Assert.jsonObjectHaveKey(businessCommodityIntro,"introId","businessCommodityIntro 节点下没有包含 introId 节点");

        if(businessCommodityIntro.getString("introId").startsWith("-")){
            //刷新缓存
            //flushCommodityIntroId(business.getDatas());

            businessCommodityIntro.put("introId",GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_introId));

        }

        businessCommodityIntro.put("bId",business.getbId());
        businessCommodityIntro.put("operate", StatusConstant.OPERATE_ADD);
        //保存商品简介信息
        commodityIntroServiceDaoImpl.saveBusinessCommodityIntroInfo(businessCommodityIntro);

    }

    @Override
    public ICommodityIntroServiceDao getCommodityIntroServiceDaoImpl() {
        return commodityIntroServiceDaoImpl;
    }

    public void setCommodityIntroServiceDaoImpl(ICommodityIntroServiceDao commodityIntroServiceDaoImpl) {
        this.commodityIntroServiceDaoImpl = commodityIntroServiceDaoImpl;
    }
}
