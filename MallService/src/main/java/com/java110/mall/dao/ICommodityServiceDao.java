package com.java110.mall.dao;


import com.java110.utils.exception.DAOException;
import com.java110.entity.merchant.BoMerchant;
import com.java110.entity.merchant.BoMerchantAttr;
import com.java110.entity.merchant.Merchant;
import com.java110.entity.merchant.MerchantAttr;


import java.util.List;
import java.util.Map;

/**
 * 商品组件内部之间使用，没有给外围系统提供服务能力
 * 商品服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by wuxw on 2016/12/27.
 */
public interface ICommodityServiceDao {

    /**
     * 保存 商品信息
     * @param businessCommodityInfo 商品信息 封装
     * @throws DAOException 操作数据库异常
     */
    void saveBusinessCommodityInfo(Map businessCommodityInfo) throws DAOException;



    /**
     * 查询商品信息（business过程）
     * 根据bId 查询商品信息
     * @param info bId 信息
     * @return 商品信息
     * @throws DAOException DAO异常
     */
    List<Map> getBusinessCommodityInfo(Map info) throws DAOException;




    /**
     * 保存 商品信息 Business数据到 Instance中
     * @param info
     * @throws DAOException DAO异常
     */
    void saveCommodityInfoInstance(Map info) throws DAOException;




    /**
     * 查询商品信息（instance过程）
     * 根据bId 查询商品信息
     * @param info bId 信息
     * @return 商品信息
     * @throws DAOException DAO异常
     */
    List<Map> getCommodityInfo(Map info) throws DAOException;



    /**
     * 修改商品信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateCommodityInfoInstance(Map info) throws DAOException;


    /**
     * 查询商品总数
     *
     * @param info 商品信息
     * @return 商品数量
     */
    int queryCommoditysCount(Map info);

}
