package org.pet.home.dao;

import org.apache.ibatis.annotations.*;
import org.pet.home.entity.UserFindShop;
import org.pet.home.net.param.UserFindShopParam;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description:TODO
 * @author: 龚强
 * @data:
 **/
@Mapper
@Repository
public interface UserMsgMapper {
    /**
     * 添加寻主任务
     * @param userMsg
     * @return
     */
    @Insert("insert into user_msg(user_id,admin_id,shop_id,name,createtime,address,birth,price,isinoculation,sex,state) " +
            "values(#{userId},#{adminId},#{shopId},#{name},#{createtime},#{address},#{birth},#{price},#{isinoculation},#{sex},#{state})")
    int add(UserFindShop userMsg);
    @Update("update user_msg set shop_id=#{shopId},admin_id=#{employeeId},pet_id=#{petId},user_id=#{userId} " +
            "where id=#{userMsgId}")
    int addTask(@Param("shopId") long shopId, @Param("employeeId")long employeeId,
                @Param("petId")long petId, @Param("userId")long userId, @Param("userMsgId")long userMsgId);


    @Insert("insert into user_msg(user_id,admin_id,shop_id,name,createtime,address,birth,price,isinoculation,sex,state,pet_id) " +
            "values(#{userId},#{adminId},#{shopId},#{name},#{createtime},#{address},#{birth},#{price},#{isinoculation},#{sex},#{state},#{petId})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insert(UserFindShopParam userFindShopParam);

    /**
     * 通过状态查看寻主列表
     * 0是待处理 1是已处理
     * @param state
     * @return
     */
    @Select("select * from user_msg where state=#{state}")
    List<UserFindShop> getPetListByState(int state);

    /**
     * 用户查通过id看自己发表的寻主列表
     * @param userId
     * @return
     */
    @Select("select * from user_msg where user_id=#{userId}")
    List<UserFindShop> getUserList(long userId);
    /**
     *  根据店铺id查询寻主任务
     */
    @Select("select * from user_msg where shop_id=#{shopId}")
    List<UserFindShop> getShoplist(long shopId);
    @Select("select  shop_id from user_msg")
    List<Long> findAllShopId();

    @Select("select * from user_msg where shop_id=#{shopId}")
    List<UserFindShop> getShopList(long shopId);
    /**
     * 修改寻主任务状态
     * @param state
     * @return
     */
    @Update("update user_msg  set state=#{state} where id=#{id}")
    int updateState(int state,long id);
    @Select("select * from user_msg  where id=#{id} ")
    UserFindShop findById(long id);
}
