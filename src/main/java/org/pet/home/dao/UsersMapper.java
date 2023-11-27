package org.pet.home.dao;

import org.apache.ibatis.annotations.*;
import org.pet.home.entity.User;
import org.pet.home.net.param.RegisterParam;
import org.springframework.stereotype.Repository;

/**
 * @description:TODO
 * @author: 龚强
 * @data:
 **/
@Mapper
@Repository
public interface UsersMapper {
    /**
     * 新增用户
     * @param
     * @return
     */
    @Insert("insert into t_user(username,phone,password,state,age,registerTime)  " +
            "values(#{username},#{phone},#{password},#{state},#{age},#{registerTime})")
    int  register(User user);

    /**
     * 查询普通用户
     * @param phone
     * @param password
     * @return
     */
    @Select("select * from t_user where phone=#{phone} and password=#{password} and role=0")
    User getUser(@Param("phone") String phone, @Param("password") String password);

    /**
     * 查询普通用户
     * @param phone
     * @param password
     * @return
     */

    @Select("select * from t_user where phone=#{phone} and password=#{password} and role=1")
    User getAdmin(String phone, String password);

    /**
     * 检查号码是否存在
     * @param phone
     * @return
     */

    @Select("select * from t_user where phone=#{phone}")
    User CheckPhone(String phone);

    /**
     * 通过id查找用户
     * @param id
     * @return
     */
    @Select("select * from t_user where id=#{id}")
    User findById(long id);
    @Update("update t_user set product_id=#{product_id} , price=#{price} where id=#{id} ")
    void update(double price,Long product_id,Long id);
    @Update("update t_user set price=#{price} where id=#{id} ")
    int recharge(double price,Long id);
}
