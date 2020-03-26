package com.java110.api.listener.commodity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.api.listener.AbstractServiceApiDataFlowListener;
import com.java110.core.annotation.Java110Listener;
import com.java110.core.context.DataFlowContext;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.entity.center.AppService;
import com.java110.event.service.api.ServiceDataFlowEvent;
import com.java110.utils.constant.BusinessTypeConstant;
import com.java110.utils.constant.CommonConstant;
import com.java110.utils.constant.ServiceCodeConstant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

/**
 * @author xuliangliang
 * @Classname SaveCommodityListener
 * @Description 保存商品
 * @Date 2020/3/24 15:03
 * @blame Java Team
 */
@Java110Listener("saveCommodityListener")
public class SaveCommodityListener extends AbstractServiceApiDataFlowListener {

    private static Logger logger = LoggerFactory.getLogger(SaveCommodityListener.class);

    /**
     * 业务 编码
     *
     * @return
     */
    @Override
    public String getServiceCode() {
        return ServiceCodeConstant.SERVICE_CODE_SAVE_COMMODITY;
    }

    /**
     * 获取调用时的方法
     *
     * @return 接口对外提供方式 如HttpMethod.POST
     */
    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public void soService(ServiceDataFlowEvent event) {
        logger.debug("ServiceDataFlowEvent : {}", event);

        DataFlowContext dataFlowContext = event.getDataFlowContext();
        AppService service = event.getAppService();

        logger.info("服务地址：" + service.getUrl());

        String paramIn = dataFlowContext.getReqData();

        //校验数据
        validate(paramIn);
        JSONObject paramObj = JSONObject.parseObject(paramIn);

        HttpHeaders header = new HttpHeaders();
        dataFlowContext.getRequestCurrentHeaders().put(CommonConstant.HTTP_ORDER_TYPE_CD, "D");
        JSONArray businesses = new JSONArray();

        //生成 commodityID
        String commodityId = GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_commodityId);
        paramObj.put("commodityId", commodityId);

        //添加商品
        businesses.add(addCommodity(paramObj, dataFlowContext));

        // 库存
        final String PARAM_STOCK = "stockpile";
        if (paramObj.containsKey(PARAM_STOCK)) {
            businesses.add(addCommodityStockpile(paramObj, dataFlowContext));
        }

        // 介绍
        final String PARAM_INTRO = "intro";
        if (paramObj.containsKey(PARAM_INTRO)) {
            businesses.add(addCommodityIntro(paramObj, dataFlowContext));
        }

        // 配图
        final String PARAM_COMMODITY_PHOTOS = "commodityPhotos";
        if (paramObj.containsKey(PARAM_COMMODITY_PHOTOS) && !StringUtils.isEmpty(paramObj.getString(PARAM_COMMODITY_PHOTOS))) {
            // 多张图片
            JSONObject[] photoBusinesses = addCommodityPhoto(paramObj, dataFlowContext);
            for (int i = 0; i < photoBusinesses.length; i++) {
                businesses.add(photoBusinesses[i]);
            }
        }

        JSONObject paramInObj = super.restToCenterProtocol(businesses, dataFlowContext.getRequestCurrentHeaders());

        //将 rest header 信息传递到下层服务中去
        super.freshHttpHeader(header, dataFlowContext.getRequestCurrentHeaders());


        ResponseEntity<String> responseEntity = this.callService(dataFlowContext, service.getServiceCode(), paramInObj);

        dataFlowContext.setResponseEntity(responseEntity);
    }

    /**
     * 添加商品库存
     * @param paramObj
     * @param dataFlowContext
     * @return
     */
    private JSONObject addCommodityStockpile(JSONObject paramObj, DataFlowContext dataFlowContext) {
        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_COMMODITY_STOCKPILE);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessCommodity = new JSONObject();
        businessCommodity.put("stockpileId", "-1");
        businessCommodity.put("amount", paramObj.getIntValue("amount"));
        businessCommodity.put("remark", "");
        businessCommodity.put("commodityId", paramObj.getString("commodityId"));
        businessCommodity.put("version", 1);
        businessCommodity.put("userId", dataFlowContext.getRequestCurrentHeaders().get(CommonConstant.HTTP_USER_ID));
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessCommodityStockpileInfo", businessCommodity);
        return business;
    }


    /**
     * 添加商品
     * @param paramInJson
     * @return
     */
    private JSONObject addCommodity(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_COMMODITY);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessCommodity = new JSONObject();
        businessCommodity.putAll(paramInJson);
        businessCommodity.put("show", 1);
        businessCommodity.put("remark", "");
        businessCommodity.put("userId", dataFlowContext.getRequestCurrentHeaders().get(CommonConstant.HTTP_USER_ID));
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessCommodityInfo", businessCommodity);
        return business;
    }

    /**
     * 添加商品配图
     * @param paramObj
     * @param dataFlowContext
     * @return
     */
    private JSONObject[] addCommodityPhoto(JSONObject paramObj, DataFlowContext dataFlowContext) {
        String photos = paramObj.getString("commodityPhotos");
        String[] photoArray = photos.split(",");
        JSONObject[] objs = new JSONObject[photoArray.length];

        for (int i = 0; i < photoArray.length; i++) {
            JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
            business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_COMMODITY_PHOTO);
            business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
            business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
            JSONObject businessCommodityPhoto = new JSONObject();
            businessCommodityPhoto.put("photoId", -1);
            businessCommodityPhoto.put("photo", photoArray[i]);
            businessCommodityPhoto.put("remark", "");
            businessCommodityPhoto.put("commodityId", paramObj.getString("commodityId"));
            businessCommodityPhoto.put("userId", dataFlowContext.getRequestCurrentHeaders().get(CommonConstant.HTTP_USER_ID));
            business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessCommodityPhotoInfo", businessCommodityPhoto);
            objs[i] = business;
        }
        return objs;
    }

    /**
     * 添加商品接收
     * @param paramObj
     * @param dataFlowContext
     * @return
     */
    private JSONObject addCommodityIntro(JSONObject paramObj, DataFlowContext dataFlowContext) {
        JSONObject business = JSONObject.parseObject("{\"datas\":{}}");
        business.put(CommonConstant.HTTP_BUSINESS_TYPE_CD, BusinessTypeConstant.BUSINESS_TYPE_SAVE_COMMODITY_INTRO);
        business.put(CommonConstant.HTTP_SEQ, DEFAULT_SEQ);
        business.put(CommonConstant.HTTP_INVOKE_MODEL, CommonConstant.HTTP_INVOKE_MODEL_S);
        JSONObject businessCommodity = new JSONObject();
        businessCommodity.put("commodityId", paramObj.getString("commodityId"));
        businessCommodity.put("intro", paramObj.getString("intro"));
        businessCommodity.put("remark", "");
        businessCommodity.put("userId", dataFlowContext.getRequestCurrentHeaders().get(CommonConstant.HTTP_USER_ID));
        business.getJSONObject(CommonConstant.HTTP_BUSINESS_DATAS).put("businessCommodityIntroInfo", businessCommodity);
        return business;
    }
    /**
     * 参数校验
     * @param paramIn
     */
    private void validate(String paramIn) {
    }

    /**
     * 获取顺序,为了同一个事件需要多个侦听处理时，需要有前后顺序
     *
     * @return
     */
    @Override
    public int getOrder() {
        return 0;
    }
}
