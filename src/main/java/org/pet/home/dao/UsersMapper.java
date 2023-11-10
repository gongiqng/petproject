package org.pet.home.dao;

import org.apache.ibatis.annotations.*;
import org.pet.home.entity.Users;
import org.springframework.stereotype.Repository;

/**
 * @description:TODO
 * @author: 龚强
 * @data:
 **/
@Mapper
@Repository
public interface UsersMapper {
    /*增加*/
    @Insert("insert into t_user(username,phone,password,state,age,registertime,role)  " +
            "values(#{username},#{phone},#{password},#{state},#{age},#{registerTime},#{role})")
    int  register(Users user);

    @Select("select * from t_user where phone=#{phone} and password=#{password} and role=0")
    Users getUser(@Param("phone") String phone, @Param("password") String password);

    @Select("select * from t_user where phone=#{phone} and password=#{password} and role=1")
    Users getAdmin(String phone, String password);

    @Select("select * from t_user where phone=#{phone}")
    Users selectPhone(String phone);

}
