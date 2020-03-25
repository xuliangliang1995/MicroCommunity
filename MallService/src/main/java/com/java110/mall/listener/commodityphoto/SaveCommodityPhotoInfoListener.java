package com.java110.mall.listener.commodityphoto;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.mall.dao.ICommodityPhotoServiceDao;
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
 * 保存 商品配图信息 侦听
 * Created by wuxw on 2018/5/18.
 */
@Java110Listener("saveCommodityPhotoInfoListener")
@Transactional
public class SaveCommodityPhotoInfoListener extends AbstractCommodityPhotoBusinessServiceDataFlowListener{

    private static Logger logger = LoggerFactory.getLogger(SaveCommodityPhotoInfoListener.class);

    @Autowired
    private ICommodityPhotoServiceDao commodityPhotoServiceDaoImpl;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public String getBusinessTypeCd() {
        return BusinessTypeConstant.BUSINESS_TYPE_SAVE_COMMODITY_PHOTO;
    }

    /**
     * 保存商品配图信息 business 表中
     * @param dataFlowContext 数据对象
     * @param business 当前业务对象
     */
    @Override
    protected void doSaveBusiness(DataFlowContext dataFlowContext, Business business) {
        JSONObject data = business.getDatas();
        Assert.notEmpty(data,"没有datas 节点，或没有子节点需要处理");

        //处理 businessCommodityPhoto 节点
        if(data.containsKey("businessCommodityPhoto")){
            Object bObj = data.get("businessCommodityPhoto");
            JSONArray businessCommodityPhotos = null;
            if(bObj instanceof JSONObject){
                businessCommodityPhotos = new JSONArray();
                businessCommodityPhotos.add(bObj);
            }else {
                businessCommodityPhotos = (JSONArray)bObj;
            }
            //JSONObject businessCommodityPhoto = data.getJSONObject("businessCommodityPhoto");
            for (int bCommodityPhotoIndex = 0; bCommodityPhotoIndex < businessCommodityPhotos.size();bCommodityPhotoIndex++) {
                JSONObject businessCommodityPhoto = businessCommodityPhotos.getJSONObject(bCommodityPhotoIndex);
                doBusinessCommodityPhoto(business, businessCommodityPhoto);
                if(bObj instanceof JSONObject) {
                    dataFlowContext.addParamOut("photoId", businessCommodityPhoto.getString("photoId"));
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

        //商品配图信息
        List<Map> businessCommodityPhotoInfo = commodityPhotoServiceDaoImpl.getBusinessCommodityPhotoInfo(info);
        if( businessCommodityPhotoInfo != null && businessCommodityPhotoInfo.size() >0) {
            reFreshShareColumn(info, businessCommodityPhotoInfo.get(0));
            commodityPhotoServiceDaoImpl.saveCommodityPhotoInfoInstance(info);
            if(businessCommodityPhotoInfo.size() == 1) {
                dataFlowContext.addParamOut("photoId", businessCommodityPhotoInfo.get(0).get("photo_id"));
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
        //商品配图信息
        List<Map> commodityPhotoInfo = commodityPhotoServiceDaoImpl.getCommodityPhotoInfo(info);
        if(commodityPhotoInfo != null && commodityPhotoInfo.size() > 0){
            reFreshShareColumn(paramIn, commodityPhotoInfo.get(0));
            commodityPhotoServiceDaoImpl.updateCommodityPhotoInfoInstance(paramIn);
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
            //刷新缓存
            //flushCommodityPhotoId(business.getDatas());

            businessCommodityPhoto.put("photoId",GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_photoId));

        }

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
