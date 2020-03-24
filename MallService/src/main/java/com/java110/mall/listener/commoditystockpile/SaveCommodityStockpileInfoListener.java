package com.java110.mall.listener.commoditystockpile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.mall.dao.ICommodityStockpileServiceDao;
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
 * 保存 商品库存信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveCommodityStockpileInfoListener")
@Transactional
public class SaveCommodityStockpileInfoListener extends AbstractCommodityStockpileBusinessServiceDataFlowListener{

    private static Logger logger = LoggerFactory.getLogger(SaveCommodityStockpileInfoListener.class);

    @Autowired
    private ICommodityStockpileServiceDao commodityStockpileServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_COMMODITY_STOCKPILE;
    }

    /**
     * 保存商品库存信息 business 表中
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        //处理 businessCommodityStockpile 节点
        if(data.containsKey("businessCommodityStockpile")){
            Object bObj = data.get("businessCommodityStockpile");
            JSONArray businessCommodityStockpiles = null;
            if(bObj instanceof JSONObject){
                businessCommodityStockpiles = new JSONArray();
                businessCommodityStockpiles.add(bObj);
            }else {
                businessCommodityStockpiles = (JSONArray)bObj;
            }
            //JSONObject businessCommodityStockpile = data.getJSONObject("businessCommodityStockpile");
            for (int bCommodityStockpileIndex = 0; bCommodityStockpileIndex < businessCommodityStockpiles.size();bCommodityStockpileIndex++) {
                JSONObject businessCommodityStockpile = businessCommodityStockpiles.getJSONObject(bCommodityStockpileIndex);
                doBusinessCommodityStockpile(business, businessCommodityStockpile);
                if(bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("stockpileId", businessCommodityStockpile.getString("stockpileId"));
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

        //商品库存信息
        List<Map> businessCommodityStockpileInfo = commodityStockpileServiceDaoImpl.getBusinessCommodityStockpileInfo(info);
        if( businessCommodityStockpileInfo != null && businessCommodityStockpileInfo.size() >0) {
            reFreshShareColumn(info, businessCommodityStockpileInfo.get(0));
            commodityStockpileServiceDaoImpl.saveCommodityStockpileInfoInstance(info);
            if(businessCommodityStockpileInfo.size() == 1) {
                dataFlowContext.addParamOut("stockpileId", businessCommodityStockpileInfo.get(0).get("stockpile_id"));
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
        //商品库存信息
        List<Map> commodityStockpileInfo = commodityStockpileServiceDaoImpl.getCommodityStockpileInfo(info);
        if(commodityStockpileInfo != null && commodityStockpileInfo.size() > 0){
            reFreshShareColumn(paramIn, commodityStockpileInfo.get(0));
            commodityStockpileServiceDaoImpl.updateCommodityStockpileInfoInstance(paramIn);
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
            //刷新缓存
            //flushCommodityStockpileId(business.getDatas());

            businessCommodityStockpile.put("stockpileId",GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_stockpileId));

        }

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
