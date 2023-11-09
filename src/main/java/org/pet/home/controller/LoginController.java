package org.pet.home.controller;

import org.pet.home.common.Constants;
import org.pet.home.entity.Employee;
import org.pet.home.net.NetCode;
import org.pet.home.net.Result;
import org.pet.home.service.Impl.RedisService;
import org.pet.home.service.Impl.UserService;
import org.pet.home.util.RegexUtil;
import org.pet.home.util.ResultGenerator;
import org.pet.home.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Set;

/**
 * @description:TODO
 * @author: 龚强
 * @data:
 **/
@RestController
public class LoginController {
    private RedisService redisService;
    private UserService userService;

    @Autowired
    public  LoginController(RedisService redisService,UserService userService){
        this.redisService = redisService;
        this.userService = userService;
    }

    //登录
    @GetMapping("/getverifycode")
    public Result sendVerifyCode(@RequestParam String phone){
        return userService.sendRegisterCode(phone);
    }

    //验证
    @GetMapping("/verifycode")
    public Result verifyCode(@RequestParam String phone, @RequestParam String code){
        if (StringUtil.isEmpty(phone)){
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, Constants.PHONE_IS_NULL);
        }
        if(!RegexUtil.isMobileExact(phone)){
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID,"手机号不合法");
        }
        Set<String> expiredV = redisService.getSet(phone+phone);
        String e=new ArrayList(expiredV).get(0).toString();
        System.out.println(e);
        if(StringUtil.isNullOrNullStr(e)){
            return  ResultGenerator.genFailResult("验证码过期");
        }else {
            if (e.equals(code)){
                return  ResultGenerator.genSuccessResult("验证码正常");
            }else {
                return ResultGenerator.genFailResult("验证码不存在");
            }
        }
    }
    @PostMapping("/login")
    public Result login(@RequestBody Employee employee) {
        try {
            Result result = userService.adminLogin(employee);
            return result;
        } catch (Exception e) {
            return ResultGenerator.genFailResult("未知的异常"+e.getMessage());
        }

    }
    //注册
    public void register(){

    }
}
