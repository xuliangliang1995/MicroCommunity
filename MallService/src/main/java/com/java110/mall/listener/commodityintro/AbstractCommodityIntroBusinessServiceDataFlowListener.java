package com.java110.mall.listener.commodityintro;

import com.alibaba.fastjson.JSONObject;
import com.java110.mall.dao.ICommodityIntroServiceDao;
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
 * 商品简介 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractCommodityIntroBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener{
    private static Logger logger = LoggerFactory.getLogger(AbstractCommodityIntroBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     * @return
     */
    public abstract ICommodityIntroServiceDao getCommodityIntroServiceDaoImpl();

    /**
     * 刷新 businessCommodityIntroInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     * @param businessCommodityIntroInfo
     */
    protected void flushBusinessCommodityIntroInfo(Map businessCommodityIntroInfo,String statusCd){
        businessCommodityIntroInfo.put("newBId", businessCommodityIntroInfo.get("b_id"));
        businessCommodityIntroInfo.put("operate",businessCommodityIntroInfo.get("operate"));
businessCommodityIntroInfo.put("intro",businessCommodityIntroInfo.get("intro"));
businessCommodityIntroInfo.put("remark",businessCommodityIntroInfo.get("remark"));
businessCommodityIntroInfo.put("commodityId",businessCommodityIntroInfo.get("commodity_id"));
businessCommodityIntroInfo.put("introId",businessCommodityIntroInfo.get("intro_id"));
businessCommodityIntroInfo.put("userId",businessCommodityIntroInfo.get("user_id"));
businessCommodityIntroInfo.remove("bId");
        businessCommodityIntroInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     * @param businessCommodityIntro 商品简介信息
     */
    protected void autoSaveDelBusinessCommodityIntro(Business business, JSONObject businessCommodityIntro){
//自动插入DEL
        Map info = new HashMap();
        info.put("introId",businessCommodityIntro.getString("introId"));
        info.put("statusCd",StatusConstant.STATUS_CD_VALID);
        List<Map> currentCommodityIntroInfos = getCommodityIntroServiceDaoImpl().getCommodityIntroInfo(info);
        if(currentCommodityIntroInfos == null || currentCommodityIntroInfos.size() != 1){
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"未找到需要修改数据信息，入参错误或数据有问题，请检查"+info);
        }

        Map currentCommodityIntroInfo = currentCommodityIntroInfos.get(0);

        currentCommodityIntroInfo.put("bId",business.getbId());

        currentCommodityIntroInfo.put("operate",currentCommodityIntroInfo.get("operate"));
currentCommodityIntroInfo.put("intro",currentCommodityIntroInfo.get("intro"));
currentCommodityIntroInfo.put("remark",currentCommodityIntroInfo.get("remark"));
currentCommodityIntroInfo.put("commodityId",currentCommodityIntroInfo.get("commodity_id"));
currentCommodityIntroInfo.put("introId",currentCommodityIntroInfo.get("intro_id"));
currentCommodityIntroInfo.put("userId",currentCommodityIntroInfo.get("user_id"));


        currentCommodityIntroInfo.put("operate",StatusConstant.OPERATE_DEL);
        getCommodityIntroServiceDaoImpl().saveBusinessCommodityIntroInfo(currentCommodityIntroInfo);
    }





}
