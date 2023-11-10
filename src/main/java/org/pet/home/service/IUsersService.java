package org.pet.home.service;

import org.apache.ibatis.annotations.Param;
import org.pet.home.entity.Users;

/**
 * @description:TODO
 * @author: 龚强
 * @data:
 **/
public interface IUsersService {
    int  register(Users user);
    Users getUser(@Param("phone") String phone, @Param("password") String password);
    Users getAdmin(String phone, String password);
    Users selectPhone(String phone);
}
