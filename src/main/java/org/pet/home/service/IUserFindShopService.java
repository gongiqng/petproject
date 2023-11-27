package org.pet.home.service;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.pet.home.entity.UserFindShop;
import org.apache.ibatis.annotations.Param;
import org.pet.home.net.param.UserFindShopParam;

import java.util.List;

/**
 * @description:TODO
 * @author: 龚强
 * @data:
 **/
public interface IUserFindShopService {
    int add(UserFindShop userMsg);

    int addTask(@Param("shopId") long shopId, @Param("employeeId")long employeeId,
                @Param("petId")long petId, @Param("userId")long userId, @Param("userMsgId")long userMsgId);

    List<UserFindShop> getPetListByState(int state);

    List<UserFindShop> getUserList(long userId);
    @Select("select * from user_msg where shop_id=#{shopId}")
    List<UserFindShop> getShoplist(long shopId);
    List<Long> findAllShopId();
    List<UserFindShop> getShopList(long shopId);
    @Update("update user_msg  set state=#{state} where id=#{id}")
    int updateState(int state,long id);
    int insert(UserFindShopParam userFindShopParam);
    UserFindShop findById(long id);
}
