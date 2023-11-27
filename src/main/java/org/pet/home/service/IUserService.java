package org.pet.home.service;

import org.apache.ibatis.annotations.Param;
import org.pet.home.entity.User;
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
    //注册用户
    Result register(RegisterParam registerParam);
    //管理员登录
    Result login(LoginParam loginParam);
    //检查号码
    User CheckPhone(String phone);

    //查询用户
    User findById(long id);
    void update(double price,Long product_id,Long id);
    int recharge(double price,Long id);
}
