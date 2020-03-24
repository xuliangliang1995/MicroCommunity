package com.java110.user.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.owner.OwnerDeliveryAddressDto;
import com.java110.user.dao.IOwnerDeliveryAddressServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 业主收货地址服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("ownerDeliveryAddressServiceDaoImpl")
//@Transactional
public class OwnerDeliveryAddressServiceDaoImpl extends BaseServiceDao implements IOwnerDeliveryAddressServiceDao {

    private static Logger logger = LoggerFactory.getLogger(OwnerDeliveryAddressServiceDaoImpl.class);

    /**
     * 业主收货地址信息封装
     * @param businessOwnerDeliveryAddressInfo 业主收货地址信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessOwnerDeliveryAddressInfo(Map businessOwnerDeliveryAddressInfo) throws DAOException {
        businessOwnerDeliveryAddressInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存业主收货地址信息 入参 businessOwnerDeliveryAddressInfo : {}",businessOwnerDeliveryAddressInfo);
        int saveFlag = sqlSessionTemplate.insert("ownerDeliveryAddressServiceDaoImpl.saveBusinessOwnerDeliveryAddressInfo",businessOwnerDeliveryAddressInfo);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存业主收货地址数据失败："+ JSONObject.toJSONString(businessOwnerDeliveryAddressInfo));
        }
    }


    /**
     * 查询业主收货地址信息
     * @param info bId 信息
     * @return 业主收货地址信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessOwnerDeliveryAddressInfo(Map info) throws DAOException {

        logger.debug("查询业主收货地址信息 入参 info : {}",info);

        List<Map> businessOwnerDeliveryAddressInfos = sqlSessionTemplate.selectList("ownerDeliveryAddressServiceDaoImpl.getBusinessOwnerDeliveryAddressInfo",info);

        return businessOwnerDeliveryAddressInfos;
    }



    /**
     * 保存业主收货地址信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveOwnerDeliveryAddressInfoInstance(Map info) throws DAOException {
        logger.debug("保存业主收货地址信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("ownerDeliveryAddressServiceDaoImpl.saveOwnerDeliveryAddressInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存业主收货地址信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询业主收货地址信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getOwnerDeliveryAddressInfo(Map info) throws DAOException {
        logger.debug("查询业主收货地址信息 入参 info : {}",info);

        List<Map> businessOwnerDeliveryAddressInfos = sqlSessionTemplate.selectList("ownerDeliveryAddressServiceDaoImpl.getOwnerDeliveryAddressInfo",info);

        return businessOwnerDeliveryAddressInfos;
    }


    /**
     * 修改业主收货地址信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateOwnerDeliveryAddressInfoInstance(Map info) throws DAOException {
        logger.debug("修改业主收货地址信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("ownerDeliveryAddressServiceDaoImpl.updateOwnerDeliveryAddressInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改业主收货地址信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

    /*
    * 删除业主收获地址信息
    * */
    @Override
    public void deleteBusinessOwnerDeliveryAddressInfo(Map info) throws DAOException {
        logger.debug("删除业主收货地址信息Instance 入参 info : {}",info);
        int deleteFlag = sqlSessionTemplate.delete("ownerDeliveryAddressServiceDaoImpl.deleteOwnerDeliveryAddressInfoInstance",info);
        if(deleteFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"删除业主收货地址信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询业主收货地址数量
     * @param info 业主收货地址信息
     * @return 业主收货地址数量
     */
    @Override
    public int queryOwnerDeliveryAddresssCount(Map info) {
        logger.debug("查询业主收货地址数据 入参 info : {}",info);

        List<Map> businessOwnerDeliveryAddressInfos = sqlSessionTemplate.selectList("ownerDeliveryAddressServiceDaoImpl.queryOwnerDeliveryAddresssCount", info);
        if (businessOwnerDeliveryAddressInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessOwnerDeliveryAddressInfos.get(0).get("count").toString());
    }

    /**
     * 根据 memberId 获取地址
     *
     * @param memberId
     * @return
     */
    @Override
    public Map getOwnerDeliveryAddress(String memberId) {
        logger.debug("查询业主收货地址数据 入参 info : {}",memberId);
        Map map = sqlSessionTemplate.selectOne("ownerDeliveryAddressServiceDaoImpl.getOwnerDeliveryAddress", memberId);
        return map;
    }


}
