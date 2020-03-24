package com.java110.mall.listener.commodityphoto;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.mall.dao.ICommodityPhotoServiceDao;
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
 * 修改商品配图信息 侦听
 *
 * 处理节点
 * 1、businessCommodityPhoto:{} 商品配图基本信息节点
 * 2、businessCommodityPhotoAttr:[{}] 商品配图属性信息节点
 * 3、businessCommodityPhotoPhoto:[{}] 商品配图照片信息节点
 * 4、businessCommodityPhotoCerdentials:[{}] 商品配图证件信息节点
 * 协议地址 ：https://github.com/java110/MicroCommunity/wiki/%E4%BF%AE%E6%94%B9%E5%95%86%E6%88%B7%E4%BF%A1%E6%81%AF-%E5%8D%8F%E8%AE%AE
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("updateCommodityPhotoInfoListener")
@Transactional
public class UpdateCommodityPhotoInfoListener extends AbstractCommodityPhotoBusinessServiceDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(UpdateCommodityPhotoInfoListener.class);
    @Autowired
    private ICommodityPhotoServiceDao commodityPhotoServiceDaoImpl;

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_UPDATE_COMMODITY_PHOTO;
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

        //处理 businessCommodityPhoto 节点
        if(data.containsKey("businessCommodityPhoto")){
            //处理 businessCommodityPhoto 节点
            if(data.containsKey("businessCommodityPhoto")){
                Object _obj = data.get("businessCommodityPhoto");
                JSONArray businessCommodityPhotos = null;
                if(_obj instanceof JSONObject){
                    businessCommodityPhotos = new JSONArray();
                    businessCommodityPhotos.add(_obj);
                }else {
                    businessCommodityPhotos = (JSONArray)_obj;
                }
                //JSONObject businessCommodityPhoto = data.getJSONObject("businessCommodityPhoto");
                for (int _commodityPhotoIndex = 0; _commodityPhotoIndex < businessCommodityPhotos.size();_commodityPhotoIndex++) {
                    JSONObject businessCommodityPhoto = businessCommodityPhotos.getJSONObject(_commodityPhotoIndex);
                    doBusinessCommodityPhoto(business, businessCommodityPhoto);
                    if(_obj instanceof JSONObject) {
                        dataFlowContext.addParamOut("photoId", businessCommodityPhoto.getString("photoId"));
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

        //商品配图信息
        List<Map> businessCommodityPhotoInfos = commodityPhotoServiceDaoImpl.getBusinessCommodityPhotoInfo(info);
        if( businessCommodityPhotoInfos != null && businessCommodityPhotoInfos.size() >0) {
            for (int _commodityPhotoIndex = 0; _commodityPhotoIndex < businessCommodityPhotoInfos.size();_commodityPhotoIndex++) {
                Map businessCommodityPhotoInfo = businessCommodityPhotoInfos.get(_commodityPhotoIndex);
                flushBusinessCommodityPhotoInfo(businessCommodityPhotoInfo,StatusConstant.STATUS_CD_VALID);
                commodityPhotoServiceDaoImpl.updateCommodityPhotoInfoInstance(businessCommodityPhotoInfo);
                if(businessCommodityPhotoInfo.size() == 1) {
                    dataFlowContext.addParamOut("photoId", businessCommodityPhotoInfo.get("photo_id"));
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
        //商品配图信息
        List<Map> commodityPhotoInfo = commodityPhotoServiceDaoImpl.getCommodityPhotoInfo(info);
        if(commodityPhotoInfo != null && commodityPhotoInfo.size() > 0){

            //商品配图信息
            List<Map> businessCommodityPhotoInfos = commodityPhotoServiceDaoImpl.getBusinessCommodityPhotoInfo(delInfo);
            //除非程序出错了，这里不会为空
            if(businessCommodityPhotoInfos == null || businessCommodityPhotoInfos.size() == 0){
                throw new ListenerExecuteException(ResponseConstant.RESULT_CODE_INNER_ERROR,"撤单失败（commodityPhoto），程序内部异常,请检查！ "+delInfo);
            }
            for (int _commodityPhotoIndex = 0; _commodityPhotoIndex < businessCommodityPhotoInfos.size();_commodityPhotoIndex++) {
                Map businessCommodityPhotoInfo = businessCommodityPhotoInfos.get(_commodityPhotoIndex);
                flushBusinessCommodityPhotoInfo(businessCommodityPhotoInfo,StatusConstant.STATUS_CD_VALID);
                commodityPhotoServiceDaoImpl.updateCommodityPhotoInfoInstance(businessCommodityPhotoInfo);
            }
        }

    }



    /**
     * 处理 businessCommodityPhoto 节点
     * @param business 总的数据节点
     * @param businessCommodityPhoto 商品配图节点
     */
    private void doBusinessCommodityPhoto(Business business,JSONObject businessCommodityPhoto){

        Assert.jsonObjectHaveKey(businessCommodityPhoto,"photoId","businessCommodityPhoto 节点下没有包含 photoId 节点");

        if(businessCommodityPhoto.getString("photoId").startsWith("-")){
            throw new ListenerExecuteException(ResponseConstant.RESULT_PARAM_ERROR,"photoId 错误，不能自动生成（必须已经存在的photoId）"+businessCommodityPhoto);
        }
        //自动保存DEL
        autoSaveDelBusinessCommodityPhoto(business,businessCommodityPhoto);

        businessCommodityPhoto.put("bId",business.getbId());
        businessCommodityPhoto.put("operate", StatusConstant.OPERATE_ADD);
        //保存商品配图信息
        commodityPhotoServiceDaoImpl.saveBusinessCommodityPhotoInfo(businessCommodityPhoto);

    }




    @Override
    public ICommodityPhotoServiceDao getCommodityPhotoServiceDaoImpl() {
        return commodityPhotoServiceDaoImpl;
    }

    public void setCommodityPhotoServiceDaoImpl(ICommodityPhotoServiceDao commodityPhotoServiceDaoImpl) {
        this.commodityPhotoServiceDaoImpl = commodityPhotoServiceDaoImpl;
    }



}
