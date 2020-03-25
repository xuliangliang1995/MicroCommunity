package com.java110.mall.listener.commodity;

import com.alibaba.fastjson.JSONObject;
import com.java110.entity.center.Business;
import com.java110.event.service.AbstractBusinessServiceDataFlowListener;
import com.java110.mall.dao.ICommodityServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.ListenerExecuteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * 商品 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractCommodityBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener{
    private static Logger logger = LoggerFactory.getLogger(AbstractCommodityBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     * @return
     */
    public abstract ICommodityServiceDao getCommodityServiceDaoImpl();

    /**
     * 刷新 businessCommodityInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     * @param businessCommodityInfo
     */
    protected void flushBusinessCommodityInfo(Map businessCommodityInfo,String statusCd){
        businessCommodityInfo.put("newBId", businessCommodityInfo.get("b_id"));
        businessCommodityInfo.put("operate",businessCommodityInfo.get("operate"));
        businessCommodityInfo.put("originalPrice",businessCommodityInfo.get("original_price"));
        businessCommodityInfo.put("show",businessCommodityInfo.get("show"));
        businessCommodityInfo.put("currentPrice",businessCommodityInfo.get("current_price"));
        businessCommodityInfo.put("remark",businessCommodityInfo.get("remark"));
        businessCommodityInfo.put("commodityId",businessCommodityInfo.get("commodity_id"));
        businessCommodityInfo.put("communityId",businessCommodityInfo.get("community_id"));
        businessCommodityInfo.put("title",businessCommodityInfo.get("title"));
        businessCommodityInfo.put("userId",businessCommodityInfo.get("user_id"));
        businessCommodityInfo.remove("bId");
        businessCommodityInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     * @param businessCommodity 商品信息
     */
    protected void autoSaveDelBusinessCommodity(Business business, JSONObject businessCommodity){
//自动插入DEL
        Map info = new HashMap();
        info.put("commodityId",businessCommodity.getString("commodityId"));
        info.put("statusCd",StatusConstant.STATUS_CD_VALID);
        List<Map> currentCommodityInfos = getCommodityServiceDaoImpl().getCommodityInfo(info);
        if(currentCommodityInfos == null || currentCommodityInfos.size() != 1){
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"未找到需要修改数据信息，入参错误或数据有问题，请检查"+info);
        }

        Map currentCommodityInfo = currentCommodityInfos.get(0);

        currentCommodityInfo.put("bId",business.getbId());

        currentCommodityInfo.put("operate",currentCommodityInfo.get("operate"));
        currentCommodityInfo.put("originalPrice",currentCommodityInfo.get("original_price"));
        currentCommodityInfo.put("show",currentCommodityInfo.get("show"));
        currentCommodityInfo.put("currentPrice",currentCommodityInfo.get("current_price"));
        currentCommodityInfo.put("remark",currentCommodityInfo.get("remark"));
        currentCommodityInfo.put("commodityId",currentCommodityInfo.get("commodity_id"));
        currentCommodityInfo.put("communityId",currentCommodityInfo.get("community_id"));
        currentCommodityInfo.put("title",currentCommodityInfo.get("title"));
        currentCommodityInfo.put("userId",currentCommodityInfo.get("user_id"));


        currentCommodityInfo.put("operate",StatusConstant.OPERATE_DEL);
        getCommodityServiceDaoImpl().saveBusinessCommodityInfo(currentCommodityInfo);
    }





}
