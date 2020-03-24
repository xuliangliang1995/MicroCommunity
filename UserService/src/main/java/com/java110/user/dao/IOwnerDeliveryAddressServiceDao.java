package com.java110.user.dao;


import com.java110.utils.exception.DAOException;

import java.util.List;
import java.util.Map;

/**
 * 业主收货地址组件内部之间使用，没有给外围系统提供服务能力
 * 业主收货地址服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by wuxw on 2016/12/27.
 */
public interface IOwnerDeliveryAddressServiceDao {

    /**
     * 保存 业主收货地址信息
     * @param businessOwnerDeliveryAddressInfo 业主收货地址信息 封装
     * @throws DAOException 操作数据库异常
     */
    void saveBusinessOwnerDeliveryAddressInfo(Map businessOwnerDeliveryAddressInfo) throws DAOException;



    /**
     * 查询业主收货地址信息（business过程）
     * 根据bId 查询业主收货地址信息
     * @param info bId 信息
     * @return 业主收货地址信息
     * @throws DAOException DAO异常
     */
    List<Map> getBusinessOwnerDeliveryAddressInfo(Map info) throws DAOException;




    /**
     * 保存 业主收货地址信息 Business数据到 Instance中
     * @param info
     * @throws DAOException DAO异常
     */
    void saveOwnerDeliveryAddressInfoInstance(Map info) throws DAOException;

    /**
     * 删除 业主收货地址信息 Business数据到 Instance中
     * @param info
     * @throws DAOException DAO异常
     */
    void deleteBusinessOwnerDeliveryAddressInfo(Map info) throws DAOException;

    /**
     * 查询业主收货地址信息（instance过程）
     * 根据bId 查询业主收货地址信息
     * @param info bId 信息
     * @return 业主收货地址信息
     * @throws DAOException DAO异常
     */
    List<Map> getOwnerDeliveryAddressInfo(Map info) throws DAOException;



    /**
     * 修改业主收货地址信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateOwnerDeliveryAddressInfoInstance(Map info) throws DAOException;


    /**
     * 查询业主收货地址总数
     *
     * @param info 业主收货地址信息
     * @return 业主收货地址数量
     */
    int queryOwnerDeliveryAddresssCount(Map info);

    /**
     * 根据 memberId 获取地址
     * @param memberId
     * @return
     */
    Map getOwnerDeliveryAddress(String memberId);

}
