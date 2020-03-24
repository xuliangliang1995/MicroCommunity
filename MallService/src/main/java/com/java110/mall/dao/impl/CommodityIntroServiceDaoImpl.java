package com.java110.mall.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.mall.dao.ICommodityIntroServiceDao;
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
 * 商品简介服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("commodityIntroServiceDaoImpl")
//@Transactional
public class CommodityIntroServiceDaoImpl extends BaseServiceDao implements ICommodityIntroServiceDao {

    private static Logger logger = LoggerFactory.getLogger(CommodityIntroServiceDaoImpl.class);

    /**
     * 商品简介信息封装
     * @param businessCommodityIntroInfo 商品简介信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessCommodityIntroInfo(Map businessCommodityIntroInfo) throws DAOException {
        businessCommodityIntroInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存商品简介信息 入参 businessCommodityIntroInfo : {}",businessCommodityIntroInfo);
        int saveFlag = sqlSessionTemplate.insert("commodityIntroServiceDaoImpl.saveBusinessCommodityIntroInfo",businessCommodityIntroInfo);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存商品简介数据失败："+ JSONObject.toJSONString(businessCommodityIntroInfo));
        }
    }


    /**
     * 查询商品简介信息
     * @param info bId 信息
     * @return 商品简介信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessCommodityIntroInfo(Map info) throws DAOException {

        logger.debug("查询商品简介信息 入参 info : {}",info);

        List<Map> businessCommodityIntroInfos = sqlSessionTemplate.selectList("commodityIntroServiceDaoImpl.getBusinessCommodityIntroInfo",info);

        return businessCommodityIntroInfos;
    }



    /**
     * 保存商品简介信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveCommodityIntroInfoInstance(Map info) throws DAOException {
        logger.debug("保存商品简介信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("commodityIntroServiceDaoImpl.saveCommodityIntroInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存商品简介信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询商品简介信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getCommodityIntroInfo(Map info) throws DAOException {
        logger.debug("查询商品简介信息 入参 info : {}",info);

        List<Map> businessCommodityIntroInfos = sqlSessionTemplate.selectList("commodityIntroServiceDaoImpl.getCommodityIntroInfo",info);

        return businessCommodityIntroInfos;
    }


    /**
     * 修改商品简介信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateCommodityIntroInfoInstance(Map info) throws DAOException {
        logger.debug("修改商品简介信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("commodityIntroServiceDaoImpl.updateCommodityIntroInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改商品简介信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询商品简介数量
     * @param info 商品简介信息
     * @return 商品简介数量
     */
    @Override
    public int queryCommodityIntrosCount(Map info) {
        logger.debug("查询商品简介数据 入参 info : {}",info);

        List<Map> businessCommodityIntroInfos = sqlSessionTemplate.selectList("commodityIntroServiceDaoImpl.queryCommodityIntrosCount", info);
        if (businessCommodityIntroInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessCommodityIntroInfos.get(0).get("count").toString());
    }


}
