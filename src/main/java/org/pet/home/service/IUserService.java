package org.pet.home.service;

import org.pet.home.entity.Employee;
import org.pet.home.entity.Users;
import org.pet.home.net.Result;
import org.pet.home.net.param.LoginParam;
import org.pet.home.net.param.RegisterParam;

/**
 * @description:TODO
 * @author: 龚强
 * @data:
 **/
public interface IUserService {
    /**
     * 发送二维码
     */
    Result sendRegisterCode(String phone);
   // Result adminLogin(Employee employee);
    Result register(RegisterParam registerParam);
    Result adminlogin(LoginParam loginParam);

    Result userLogin(LoginParam loginParam);
}
