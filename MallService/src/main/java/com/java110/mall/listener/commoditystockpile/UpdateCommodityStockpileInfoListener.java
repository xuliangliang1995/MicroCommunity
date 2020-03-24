package com.java110.mall.listener.commoditystockpile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.mall.dao.ICommodityStockpileServiceDao;
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
 * 修改商品库存信息 侦听
 *
 * 处理节点
 * 1、businessCommodityStockpile:{} 商品库存基本信息节点
 * 2、businessCommodityStockpileAttr:[{}] 商品库存属性信息节点
 * 3、businessCommodityStockpilePhoto:[{}] 商品库存照片信息节点
 * 4、businessCommodityStockpileCerdentials:[{}] 商品库存证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E4%BF%AE%E6%94%B9%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("updateCommodityStockpileInfoListener")
@Transactional
public class UpdateCommodityStockpileInfoListener extends AbstractCommodityStockpileBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(UpdateCommodityStockpileInfoListener.class);
    @Autowired
    private ICommodityStockpileServiceDao commodityStockpileServiceDaoImpl;

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_UPDATE_COMMODITY_STOCKPILE;
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

        //处理 businessCommodityStockpile 节点
        if(data.containsKey("businessCommodityStockpile")){
            //处理 businessCommodityStockpile 节点
            if(data.containsKey("businessCommodityStockpile")){
                Object _obj = data.get("businessCommodityStockpile");
                JSONArray businessCommodityStockpiles = null;
                if(_obj instanceof JSONObject){
                    businessCommodityStockpiles = new JSONArray();
                    businessCommodityStockpiles.add(_obj);
                }else {
                    businessCommodityStockpiles = (JSONArray)_obj;
                }
                //JSONObject businessCommodityStockpile = data.getJSONObject("businessCommodityStockpile");
                for (int _commodityStockpileIndex = 0; _commodityStockpileIndex < businessCommodityStockpiles.size();_commodityStockpileIndex++) {
                    JSONObject businessCommodityStockpile = businessCommodityStockpiles.getJSONObject(_commodityStockpileIndex);
                    doBusinessCommodityStockpile(business, businessCommodityStockpile);
                    if(_obj instanceof JSONObject) {
                        dataFlowContext.addParamOut("stockpileId", businessCommodityStockpile.getString("stockpileId"));
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

        //商品库存信息
        List<Map> businessCommodityStockpileInfos = commodityStockpileServiceDaoImpl.getBusinessCommodityStockpileInfo(info);
        if( businessCommodityStockpileInfos != null && businessCommodityStockpileInfos.size() >0) {
            for (int _commodityStockpileIndex = 0; _commodityStockpileIndex < businessCommodityStockpileInfos.size();_commodityStockpileIndex++) {
                Map businessCommodityStockpileInfo = businessCommodityStockpileInfos.get(_commodityStockpileIndex);
                flushBusinessCommodityStockpileInfo(businessCommodityStockpileInfo,StatusConstant.STATUS_CD_VALID);
                commodityStockpileServiceDaoImpl.updateCommodityStockpileInfoInstance(businessCommodityStockpileInfo);
                if(businessCommodityStockpileInfo.size() == 1) {
                    dataFlowContext.addParamOut("stockpileId", businessCommodityStockpileInfo.get("stockpile_id"));
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
        //商品库存信息
        List<Map> commodityStockpileInfo = commodityStockpileServiceDaoImpl.getCommodityStockpileInfo(info);
        if(commodityStockpileInfo != null && commodityStockpileInfo.size() > 0){

            //商品库存信息
            List<Map> businessCommodityStockpileInfos = commodityStockpileServiceDaoImpl.getBusinessCommodityStockpileInfo(delInfo);
            //除非程序出错了，这里不会为空
            if(businessCommodityStockpileInfos == null || businessCommodityStockpileInfos.size() == 0){
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR,"撤单失败（commodityStockpile），程序内部异常,请检查！ "+delInfo);
            }
            for (int _commodityStockpileIndex = 0; _commodityStockpileIndex < businessCommodityStockpileInfos.size();_commodityStockpileIndex++) {
                Map businessCommodityStockpileInfo = businessCommodityStockpileInfos.get(_commodityStockpileIndex);
                flushBusinessCommodityStockpileInfo(businessCommodityStockpileInfo,StatusConstant.STATUS_CD_VALID);
                commodityStockpileServiceDaoImpl.updateCommodityStockpileInfoInstance(businessCommodityStockpileInfo);
            }
        }

    }



    /**
     * 处理 businessCommodityStockpile 节点
     * @param business 总的数据节点
     * @param businessCommodityStockpile 商品库存节点
     */
    private void doBusinessCommodityStockpile(Business business,JSONObject businessCommodityStockpile){

        Assert.jsonObjectHaveKey(businessCommodityStockpile,"stockpileId","businessCommodityStockpile 节点下没有包含 stockpileId 节点");

        if(businessCommodityStockpile.getString("stockpileId").startsWith("-")){
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"stockpileId 错误，不能自动生成（必须已经存在的stockpileId）"+businessCommodityStockpile);
        }
        //自动保存DEL
        autoSaveDelBusinessCommodityStockpile(business,businessCommodityStockpile);

        businessCommodityStockpile.put("bId",business.getbId());
        businessCommodityStockpile.put("operate", StatusConstant.OPERATE_ADD);
        //保存商品库存信息
        commodityStockpileServiceDaoImpl.saveBusinessCommodityStockpileInfo(businessCommodityStockpile);

    }




    @Override
    public ICommodityStockpileServiceDao getCommodityStockpileServiceDaoImpl() {
        return commodityStockpileServiceDaoImpl;
    }

    public void setCommodityStockpileServiceDaoImpl(ICommodityStockpileServiceDao commodityStockpileServiceDaoImpl) {
        this.commodityStockpileServiceDaoImpl = commodityStockpileServiceDaoImpl;
    }



}
