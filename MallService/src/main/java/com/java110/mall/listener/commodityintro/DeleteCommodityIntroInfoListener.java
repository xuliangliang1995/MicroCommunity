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
 * 删除商品简介信息 侦听
 *
 * 处理节点
 * 1、businessCommodityIntro:{} 商品简介基本信息节点
 * 2、businessCommodityIntroAttr:[{}] 商品简介属性信息节点
 * 3、businessCommodityIntroPhoto:[{}] 商品简介照片信息节点
 * 4、businessCommodityIntroCerdentials:[{}] 商品简介证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E5%88%A0%E9%99%A4%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("deleteCommodityIntroInfoListener")
@Transactional
public class DeleteCommodityIntroInfoListener extends AbstractCommodityIntroBusinessServiceDataFlowListener {

    private final static Logger logger = LoggerFactory.getLogger(DeleteCommodityIntroInfoListener.class);
    @Autowired
    ICommodityIntroServiceDao commodityIntroServiceDaoImpl;

    @Override
    public int getOrder() {
        return 3;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_DELETE_COMMODITY_INTRO;
    }

    /**
     * 根据删除信息 查出Instance表中数据 保存至business表 （状态写DEL） 方便撤单时直接更新回去
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
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
     * 删除 instance数据
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doBusinessToInstance(DataFlowContext dataFlowContext, Business business) {
        String bId = business.getbId();
        //Assert.hasLength(bId,"请求报文中没有包含 bId");

        //商品简介信息
        Map info = new HashMap();
        info.put("bId",business.getbId());
        info.put("operate",StatusConstant.OPERATE_DEL);

        //商品简介信息
        List<Map> businessCommodityIntroInfos = commodityIntroServiceDaoImpl.getBusinessCommodityIntroInfo(info);
        if( businessCommodityIntroInfos != null && businessCommodityIntroInfos.size() >0) {
            for (int _commodityIntroIndex = 0; _commodityIntroIndex < businessCommodityIntroInfos.size();_commodityIntroIndex++) {
                Map businessCommodityIntroInfo = businessCommodityIntroInfos.get(_commodityIntroIndex);
                flushBusinessCommodityIntroInfo(businessCommodityIntroInfo,StatusConstant.STATUS_CD_INVALID);
                commodityIntroServiceDaoImpl.updateCommodityIntroInfoInstance(businessCommodityIntroInfo);
                dataFlowContext.addParamOut("introId",businessCommodityIntroInfo.get("intro_id"));
            }
        }

    }

    /**
     * 撤单
     * 从business表中查询到DEL的数据 将instance中的数据更新回来
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doRecover(DataFlowContext dataFlowContext, Business business) {
        String bId = business.getbId();
        //Assert.hasLength(bId,"请求报文中没有包含 bId");
        Map info = new HashMap();
        info.put("bId",bId);
        info.put("statusCd",StatusConstant.STATUS_CD_INVALID);

        Map delInfo = new HashMap();
        delInfo.put("bId",business.getbId());
        delInfo.put("operate",StatusConstant.OPERATE_DEL);
        //商品简介信息
        List<Map> commodityIntroInfo = commodityIntroServiceDaoImpl.getCommodityIntroInfo(info);
        if(commodityIntroInfo != null && commodityIntroInfo.size() > 0){

            //商品简介信息
            List<Map> businessCommodityIntroInfos = commodityIntroServiceDaoImpl.getBusinessCommodityIntroInfo(delInfo);
            //除非程序出错了，这里不会为空
            if(businessCommodityIntroInfos == null ||  businessCommodityIntroInfos.size() == 0){
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
        //自动插入DEL
        autoSaveDelBusinessCommodityIntro(business,businessCommodityIntro);
    }

    @Override
    public ICommodityIntroServiceDao getCommodityIntroServiceDaoImpl() {
        return commodityIntroServiceDaoImpl;
    }

    public void setCommodityIntroServiceDaoImpl(ICommodityIntroServiceDao commodityIntroServiceDaoImpl) {
        this.commodityIntroServiceDaoImpl = commodityIntroServiceDaoImpl;
    }
}
