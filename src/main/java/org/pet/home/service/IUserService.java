package org.pet.home.service;

import org.pet.home.entity.Employee;
import org.pet.home.net.Result;

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
    Result adminLogin(Employee employee);
}
