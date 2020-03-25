package com.java110.mall.listener.commodityphoto;

import com.alibaba.fastjson.JSONObject;
import com.java110.mall.dao.ICommodityPhotoServiceDao;
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
 * 商品配图 服务侦听 父类
 * Created by wuxw on 2018/7/4.
 */
public abstract class AbstractCommodityPhotoBusinessServiceDataFlowListener extends AbstractBusinessServiceDataFlowListener{
    private static Logger logger = LoggerFactory.getLogger(AbstractCommodityPhotoBusinessServiceDataFlowListener.class);


    /**
     * 获取 DAO工具类
     * @return
     */
    public abstract ICommodityPhotoServiceDao getCommodityPhotoServiceDaoImpl();

    /**
     * 刷新 businessCommodityPhotoInfo 数据
     * 主要将 数据库 中字段和 接口传递字段建立关系
     * @param businessCommodityPhotoInfo
     */
    protected void flushBusinessCommodityPhotoInfo(Map businessCommodityPhotoInfo,String statusCd){
        businessCommodityPhotoInfo.put("newBId", businessCommodityPhotoInfo.get("b_id"));
        businessCommodityPhotoInfo.put("operate",businessCommodityPhotoInfo.get("operate"));
businessCommodityPhotoInfo.put("photoId",businessCommodityPhotoInfo.get("photo_id"));
businessCommodityPhotoInfo.put("photo",businessCommodityPhotoInfo.get("photo"));
businessCommodityPhotoInfo.put("remark",businessCommodityPhotoInfo.get("remark"));
businessCommodityPhotoInfo.put("commodityId",businessCommodityPhotoInfo.get("commodity_id"));
businessCommodityPhotoInfo.put("userId",businessCommodityPhotoInfo.get("user_id"));
businessCommodityPhotoInfo.remove("bId");
        businessCommodityPhotoInfo.put("statusCd", statusCd);
    }


    /**
     * 当修改数据时，查询instance表中的数据 自动保存删除数据到business中
     * @param businessCommodityPhoto 商品配图信息
     */
    protected void autoSaveDelBusinessCommodityPhoto(Business business, JSONObject businessCommodityPhoto){
//自动插入DEL
        Map info = new HashMap();
        info.put("photoId",businessCommodityPhoto.getString("photoId"));
        info.put("statusCd",StatusConstant.STATUS_CD_VALID);
        List<Map> currentCommodityPhotoInfos = getCommodityPhotoServiceDaoImpl().getCommodityPhotoInfo(info);
        if(currentCommodityPhotoInfos == null || currentCommodityPhotoInfos.size() != 1){
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"未找到需要修改数据信息，入参错误或数据有问题，请检查"+info);
        }

        Map currentCommodityPhotoInfo = currentCommodityPhotoInfos.get(0);

        currentCommodityPhotoInfo.put("bId",business.getbId());

        currentCommodityPhotoInfo.put("operate",currentCommodityPhotoInfo.get("operate"));
        currentCommodityPhotoInfo.put("photoId",currentCommodityPhotoInfo.get("photo_id"));
        currentCommodityPhotoInfo.put("photo",currentCommodityPhotoInfo.get("photo"));
        currentCommodityPhotoInfo.put("remark",currentCommodityPhotoInfo.get("remark"));
        currentCommodityPhotoInfo.put("commodityId",currentCommodityPhotoInfo.get("commodity_id"));
        currentCommodityPhotoInfo.put("userId",currentCommodityPhotoInfo.get("user_id"));


        currentCommodityPhotoInfo.put("operate",StatusConstant.OPERATE_DEL);
        getCommodityPhotoServiceDaoImpl().saveBusinessCommodityPhotoInfo(currentCommodityPhotoInfo);
    }





}
