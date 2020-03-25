package com.java110.mall.listener.commodityintro;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.mall.dao.ICommodityIntroServiceDao;
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
 * 修改商品简介信息 侦听
 *
 * 处理节点
 * 1、businessCommodityIntro:{} 商品简介基本信息节点
 * 2、businessCommodityIntroAttr:[{}] 商品简介属性信息节点
 * 3、businessCommodityIntroPhoto:[{}] 商品简介照片信息节点
 * 4、businessCommodityIntroCerdentials:[{}] 商品简介证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E4%BF%AE%E6%94%B9%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("updateCommodityIntroInfoListener")
@Transactional
public class UpdateCommodityIntroInfoListener extends AbstractCommodityIntroBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(UpdateCommodityIntroInfoListener.class);
    @Autowired
    private ICommodityIntroServiceDao commodityIntroServiceDaoImpl;

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_UPDATE_COMMODITY_INTRO;
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

        //处理 businessCommodityIntro 节点
        if(data.containsKey("businessCommodityIntro")){
            //处理 businessCommodityIntro 节点
            if(data.containsKey("businessCommodityIntro")){
                Object _obj = data.get("businessCommodityIntro");
                JSONArray businessCommodityIntros = null;
                if(_obj instanceof JSONObject){
                    businessCommodityIntros = new JSONArray();
                    businessCommodityIntros.add(_obj);
                }else {
                    businessCommodityIntros = (JSONArray)_obj;
                }
                //JSONObject businessCommodityIntro = data.getJSONObject("businessCommodityIntro");
                for (int _commodityIntroIndex = 0; _commodityIntroIndex < businessCommodityIntros.size();_commodityIntroIndex++) {
                    JSONObject businessCommodityIntro = businessCommodityIntros.getJSONObject(_commodityIntroIndex);
                    doBusinessCommodityIntro(business, businessCommodityIntro);
                    if(_obj instanceof JSONObject) {
                        dataFlowContext.addParamOut("introId", businessCommodityIntro.getString("introId"));
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

        //商品简介信息
        List<Map> businessCommodityIntroInfos = commodityIntroServiceDaoImpl.getBusinessCommodityIntroInfo(info);
        if( businessCommodityIntroInfos != null && businessCommodityIntroInfos.size() >0) {
            for (int _commodityIntroIndex = 0; _commodityIntroIndex < businessCommodityIntroInfos.size();_commodityIntroIndex++) {
                Map businessCommodityIntroInfo = businessCommodityIntroInfos.get(_commodityIntroIndex);
                flushBusinessCommodityIntroInfo(businessCommodityIntroInfo,StatusConstant.STATUS_CD_VALID);
                commodityIntroServiceDaoImpl.updateCommodityIntroInfoInstance(businessCommodityIntroInfo);
                if(businessCommodityIntroInfo.size() == 1) {
                    dataFlowContext.addParamOut("introId", businessCommodityIntroInfo.get("intro_id"));
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
        //商品简介信息
        List<Map> commodityIntroInfo = commodityIntroServiceDaoImpl.getCommodityIntroInfo(info);
        if(commodityIntroInfo != null && commodityIntroInfo.size() > 0){

            //商品简介信息
            List<Map> businessCommodityIntroInfos = commodityIntroServiceDaoImpl.getBusinessCommodityIntroInfo(delInfo);
            //除非程序出错了，这里不会为空
            if(businessCommodityIntroInfos == null || businessCommodityIntroInfos.size() == 0){
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR,"撤单失败（commodityIntro），程序内部异常,请检查！ "+delInfo);
            }
            for (int _commodityIntroIndex = 0; _commodityIntroIndex < businessCommodityIntroInfos.size();_commodityIntroIndex++) {
                Map businessCommodityIntroInfo = businessCommodityIntroInfos.get(_commodityIntroIndex);
                flushBusinessCommodityIntroInfo(businessCommodityIntroInfo,StatusConstant.STATUS_CD_VALID);
                commodityIntroServiceDaoImpl.updateCommodityIntroInfoInstance(businessCommodityIntroInfo);
            }
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
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"introId 错误，不能自动生成（必须已经存在的introId）"+businessCommodityIntro);
        }
        //自动保存DEL
        autoSaveDelBusinessCommodityIntro(business,businessCommodityIntro);

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
