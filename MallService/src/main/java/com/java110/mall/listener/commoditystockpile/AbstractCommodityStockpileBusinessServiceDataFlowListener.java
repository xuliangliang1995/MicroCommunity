package com.java110.mall.listener.commoditystockpile;

import com.alibaba.fastjson.JSONObject;
import com.java110.mall.dao.ICommodityStockpileServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import com.java110.entity.center.Business;
import com.java110.event.service.AbstractBusinessServiceDataFlowListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * 商品库存 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractCommodityStockpileBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener{
    private static Logger logger = LoggerFactory.getLogger(AbstractCommodityStockpileBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     * @return
     */
    public abstract ICommodityStockpileServiceDao getCommodityStockpileServiceDaoImpl();

    /**
     * 刷新 businessCommodityStockpileInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     * @param businessCommodityStockpileInfo
     */
    protected void flushBusinessCommodityStockpileInfo(Map businessCommodityStockpileInfo,String statusCd){
        businessCommodityStockpileInfo.put("newBId", businessCommodityStockpileInfo.get("b_id"));
        businessCommodityStockpileInfo.put("amount",businessCommodityStockpileInfo.get("amount"));
businessCommodityStockpileInfo.put("operate",businessCommodityStockpileInfo.get("operate"));
businessCommodityStockpileInfo.put("stockpileId",businessCommodityStockpileInfo.get("stockpile_id"));
businessCommodityStockpileInfo.put("remark",businessCommodityStockpileInfo.get("remark"));
businessCommodityStockpileInfo.put("commodityId",businessCommodityStockpileInfo.get("commodity_id"));
businessCommodityStockpileInfo.put("version",businessCommodityStockpileInfo.get("version"));
businessCommodityStockpileInfo.put("userId",businessCommodityStockpileInfo.get("user_id"));
businessCommodityStockpileInfo.remove("bId");
        businessCommodityStockpileInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     * @param businessCommodityStockpile 商品库存信息
     */
    protected void autoSaveDelBusinessCommodityStockpile(Business business, JSONObject businessCommodityStockpile){
//自动插入DEL
        Map info = new HashMap();
        info.put("stockpileId",businessCommodityStockpile.getString("stockpileId"));
        info.put("statusCd",StatusConstant.STATUS_CD_VALID);
        List<Map> currentCommodityStockpileInfos = getCommodityStockpileServiceDaoImpl().getCommodityStockpileInfo(info);
        if(currentCommodityStockpileInfos == null || currentCommodityStockpileInfos.size() != 1){
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"未找到需要修改数据信息，入参错误或数据有问题，请检查"+info);
        }

        Map currentCommodityStockpileInfo = currentCommodityStockpileInfos.get(0);

        currentCommodityStockpileInfo.put("bId",business.getbId());

        currentCommodityStockpileInfo.put("amount",currentCommodityStockpileInfo.get("amount"));
currentCommodityStockpileInfo.put("operate",currentCommodityStockpileInfo.get("operate"));
currentCommodityStockpileInfo.put("stockpileId",currentCommodityStockpileInfo.get("stockpile_id"));
currentCommodityStockpileInfo.put("remark",currentCommodityStockpileInfo.get("remark"));
currentCommodityStockpileInfo.put("commodityId",currentCommodityStockpileInfo.get("commodity_id"));
currentCommodityStockpileInfo.put("version",currentCommodityStockpileInfo.get("version"));
currentCommodityStockpileInfo.put("userId",currentCommodityStockpileInfo.get("user_id"));


        currentCommodityStockpileInfo.put("operate",StatusConstant.OPERATE_DEL);
        getCommodityStockpileServiceDaoImpl().saveBusinessCommodityStockpileInfo(currentCommodityStockpileInfo);
    }





}
