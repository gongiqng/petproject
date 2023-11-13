package org.pet.home.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.pet.home.entity.UserMsg;
import org.springframework.stereotype.Repository;

/**
 * @description:TODO
 * @author: 龚强
 * @data:
 **/
@Mapper
@Repository
public interface UserMsgMapper {
    @Insert("insert into user_msg(user_id,admin_id,shop_id,name,createtime,address,birth,price,isinoculation,sex,state) " +
            "values(#{userId},#{adminId},#{shopId},#{name},#{createtime},#{address},#{birth},#{price},#{isinoculation},#{sex},#{state})")
    int add(UserMsg userMsg);
    @Update("update user_msg set shop_id=#{shopId},admin_id=#{employeeId},pet_id=#{petId},user_id=#{userId} " +
            "where id=#{userMsgId}")
    int addTask(@Param("shopId") long shopId, @Param("employeeId")long employeeId,
                @Param("petId")long petId, @Param("userId")long userId, @Param("userMsgId")long userMsgId);
}
