package com.java110.mall.dao;


import com.java110.utils.exception.DAOException;
import com.java110.entity.merchant.BoMerchant;
import com.java110.entity.merchant.BoMerchantAttr;
import com.java110.entity.merchant.Merchant;
import com.java110.entity.merchant.MerchantAttr;


import java.util.List;
import java.util.Map;

/**
 * 商品简介组件内部之间使用，没有给外围系统提供服务能力
 * 商品简介服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by wuxw on 2016/12/27.
 */
public interface ICommodityIntroServiceDao {

    /**
     * 保存 商品简介信息
     * @param businessCommodityIntroInfo 商品简介信息 封装
     * @throws DAOException 操作数据库异常
     */
    void saveBusinessCommodityIntroInfo(Map businessCommodityIntroInfo) throws DAOException;



    /**
     * 查询商品简介信息（business过程）
     * 根据bId 查询商品简介信息
     * @param info bId 信息
     * @return 商品简介信息
     * @throws DAOException DAO异常
     */
    List<Map> getBusinessCommodityIntroInfo(Map info) throws DAOException;




    /**
     * 保存 商品简介信息 Business数据到 Instance中
     * @param info
     * @throws DAOException DAO异常
     */
    void saveCommodityIntroInfoInstance(Map info) throws DAOException;




    /**
     * 查询商品简介信息（instance过程）
     * 根据bId 查询商品简介信息
     * @param info bId 信息
     * @return 商品简介信息
     * @throws DAOException DAO异常
     */
    List<Map> getCommodityIntroInfo(Map info) throws DAOException;



    /**
     * 修改商品简介信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateCommodityIntroInfoInstance(Map info) throws DAOException;


    /**
     * 查询商品简介总数
     *
     * @param info 商品简介信息
     * @return 商品简介数量
     */
    int queryCommodityIntrosCount(Map info);

}
