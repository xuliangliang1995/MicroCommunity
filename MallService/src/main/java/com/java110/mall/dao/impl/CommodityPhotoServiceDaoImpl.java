package com.java110.mall.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.mall.dao.ICommodityPhotoServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 商品配图服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("commodityPhotoServiceDaoImpl")
//@Transactional
public class CommodityPhotoServiceDaoImpl extends BaseServiceDao implements ICommodityPhotoServiceDao {

    private static Logger logger = LoggerFactory.getLogger(CommodityPhotoServiceDaoImpl.class);

    /**
     * 商品配图信息封装
     * @param businessCommodityPhotoInfo 商品配图信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessCommodityPhotoInfo(Map businessCommodityPhotoInfo) throws DAOException {
        businessCommodityPhotoInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存商品配图信息 入参 businessCommodityPhotoInfo : {}",businessCommodityPhotoInfo);
        int saveFlag = sqlSessionTemplate.insert("commodityPhotoServiceDaoImpl.saveBusinessCommodityPhotoInfo",businessCommodityPhotoInfo);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存商品配图数据失败："+ JSONObject.toJSONString(businessCommodityPhotoInfo));
        }
    }


    /**
     * 查询商品配图信息
     * @param info bId 信息
     * @return 商品配图信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessCommodityPhotoInfo(Map info) throws DAOException {

        logger.debug("查询商品配图信息 入参 info : {}",info);

        List<Map> businessCommodityPhotoInfos = sqlSessionTemplate.selectList("commodityPhotoServiceDaoImpl.getBusinessCommodityPhotoInfo",info);

        return businessCommodityPhotoInfos;
    }



    /**
     * 保存商品配图信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveCommodityPhotoInfoInstance(Map info) throws DAOException {
        logger.debug("保存商品配图信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("commodityPhotoServiceDaoImpl.saveCommodityPhotoInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存商品配图信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询商品配图信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getCommodityPhotoInfo(Map info) throws DAOException {
        logger.debug("查询商品配图信息 入参 info : {}",info);

        List<Map> businessCommodityPhotoInfos = sqlSessionTemplate.selectList("commodityPhotoServiceDaoImpl.getCommodityPhotoInfo",info);

        return businessCommodityPhotoInfos;
    }


    /**
     * 修改商品配图信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateCommodityPhotoInfoInstance(Map info) throws DAOException {
        logger.debug("修改商品配图信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("commodityPhotoServiceDaoImpl.updateCommodityPhotoInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改商品配图信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询商品配图数量
     * @param info 商品配图信息
     * @return 商品配图数量
     */
    @Override
    public int queryCommodityPhotosCount(Map info) {
        logger.debug("查询商品配图数据 入参 info : {}",info);

        List<Map> businessCommodityPhotoInfos = sqlSessionTemplate.selectList("commodityPhotoServiceDaoImpl.queryCommodityPhotosCount", info);
        if (businessCommodityPhotoInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessCommodityPhotoInfos.get(0).get("count").toString());
    }


}
