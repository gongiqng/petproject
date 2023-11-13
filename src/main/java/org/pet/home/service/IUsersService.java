package org.pet.home.service;

import org.apache.ibatis.annotations.Param;
import org.pet.home.entity.Users;
import org.pet.home.net.param.RegisterParam;

/**
 * @description:TODO
 * @author: 龚强
 * @data:
 **/
public interface IUsersService {
    int  register(RegisterParam registerParam);
    Users getUser(@Param("phone") String phone, @Param("password") String password);
    Users getAdmin(String phone, String password);
    Users selectPhone(String phone);
    Users findById(long id);
}
