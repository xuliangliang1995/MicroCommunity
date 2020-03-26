package com.java110.mall.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.mall.dao.ICommodityStockpileServiceDao;
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
 * 商品库存服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("commodityStockpileServiceDaoImpl")
//@Transactional
public class CommodityStockpileServiceDaoImpl extends BaseServiceDao implements ICommodityStockpileServiceDao {

    private static Logger logger = LoggerFactory.getLogger(CommodityStockpileServiceDaoImpl.class);

    /**
     * 商品库存信息封装
     * @param businessCommodityStockpileInfo 商品库存信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessCommodityStockpileInfo(Map businessCommodityStockpileInfo) throws DAOException {
        businessCommodityStockpileInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存商品库存信息 入参 businessCommodityStockpileInfo : {}",businessCommodityStockpileInfo);
        int saveFlag = sqlSessionTemplate.insert("commodityStockpileServiceDaoImpl.saveBusinessCommodityStockpileInfo",businessCommodityStockpileInfo);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存商品库存数据失败："+ JSONObject.toJSONString(businessCommodityStockpileInfo));
        }
    }


    /**
     * 查询商品库存信息
     * @param info bId 信息
     * @return 商品库存信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessCommodityStockpileInfo(Map info) throws DAOException {

        logger.debug("查询商品库存信息 入参 info : {}",info);

        List<Map> businessCommodityStockpileInfos = sqlSessionTemplate.selectList("commodityStockpileServiceDaoImpl.getBusinessCommodityStockpileInfo",info);

        return businessCommodityStockpileInfos;
    }



    /**
     * 保存商品库存信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveCommodityStockpileInfoInstance(Map info) throws DAOException {
        logger.debug("保存商品库存信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("commodityStockpileServiceDaoImpl.saveCommodityStockpileInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存商品库存信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询商品库存信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getCommodityStockpileInfo(Map info) throws DAOException {
        logger.debug("查询商品库存信息 入参 info : {}",info);

        List<Map> businessCommodityStockpileInfos = sqlSessionTemplate.selectList("commodityStockpileServiceDaoImpl.getCommodityStockpileInfo",info);

        return businessCommodityStockpileInfos;
    }


    /**
     * 修改商品库存信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateCommodityStockpileInfoInstance(Map info) throws DAOException {
        logger.debug("修改商品库存信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("commodityStockpileServiceDaoImpl.updateCommodityStockpileInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改商品库存信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询商品库存数量
     * @param info 商品库存信息
     * @return 商品库存数量
     */
    @Override
    public int queryCommodityStockpilesCount(Map info) {
        logger.debug("查询商品库存数据 入参 info : {}",info);

        List<Map> businessCommodityStockpileInfos = sqlSessionTemplate.selectList("commodityStockpileServiceDaoImpl.queryCommodityStockpilesCount", info);
        if (businessCommodityStockpileInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessCommodityStockpileInfos.get(0).get("count").toString());
    }

    /**
     * 获取商品库存
     *
     * @param commodityId
     * @return
     */
    @Override
    public Map getCommodityStockpile(String commodityId) {
        logger.debug("获取商品库存 入参 info : {}", commodityId);
        return sqlSessionTemplate.selectOne("commodityStockpileServiceDaoImpl.getCommodityStockpile", commodityId);
    }


}
