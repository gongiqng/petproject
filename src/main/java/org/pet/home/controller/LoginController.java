package org.pet.home.controller;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.pet.home.common.Constants;
import org.pet.home.net.NetCode;
import org.pet.home.net.Result;
import org.pet.home.net.param.LoginParam;
import org.pet.home.net.param.RegisterParam;
import org.pet.home.service.Impl.RedisService;
import org.pet.home.service.Impl.UserService;
import org.pet.home.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @description:TODO
 * @author: 龚强
 * @data:
 **/
@RestController
public class LoginController {
    private Logger logger = LoggerFactory.getLogger(LoginController.class);
    private RedisService redisService;
    private UserService userService;
    private RedisTemplate redisTemplate;

    @Autowired
    public LoginController(RedisService redisService, UserService userService, RedisTemplate redisTemplate) {
        this.redisService = redisService;
        this.userService = userService;
        this.redisTemplate = redisTemplate;
    }

    //阿里云发验证码的接口
    @GetMapping("/getverifycode")
    public Result SendCode(@RequestParam String phone) throws Exception {
        /**
         * 排除手机号是空的状态
         */
        if (StringUtil.isEmpty(phone)) {
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, Constants.PHONE_IS_NULL);
        }
        /**
         * 手机号不合法
         */
        if (!RegexUtil.isMobileExact(phone)) {
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, "手机号不合法");
        }
        String code = RandomCode.getCode();
        String ssmResult = AliSendSMSUtil.sendSms(code, phone);
        if (ssmResult == null) {
            return ResultGenerator.genFailResult("发送验证码失败");
        }
        //把手机号和验证码存进redis,设置1个小时过期，用于测试，防止时间短了过期
        redisTemplate.opsForValue().set(phone, code, 60, TimeUnit.MINUTES);
        return ResultGenerator.genSuccessResult(Result.StringToJson(ssmResult));
    }


//    @GetMapping("/getverifycode")
//    public Result sendVerifyCode(@RequestParam String phone){
//        return userService.sendRegisterCode(phone);
//    }

    //验证输入的验证码是否正确
    @GetMapping("/verifycode")
    public Result verifyCode(@RequestParam String phone, @RequestParam String code) {
        //检查手机号码
        if (StringUtil.isEmpty(phone)) {
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, Constants.PHONE_IS_NULL);
        }
        if (!RegexUtil.isMobileExact(phone)) {
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, "手机号不合法");
        }
//       Set<String> expiredV = redisService.getSet(phone+phone);
//        String e=new ArrayList(expiredV).get(0).toString();
//        System.out.println(e);
        // String e = redisService.getValue(phone);
        //从redis里面取出验证码
        String e = (String) redisTemplate.opsForValue().get(phone);
        //打印一下验证码
        System.out.println(e);
        logger.info(e);
        //检查验证码
        if (StringUtil.isNullOrNullStr(e)) {
            return ResultGenerator.genFailResult(Constants.CODE_LAPSE);
        } else {
            //比较验证码
            if (e.equals(code)) {
                return ResultGenerator.genSuccessResult("验证码正常");
            } else {
                return ResultGenerator.genFailResult(Constants.CODE_ERROR);
            }
        }
    }

    //登录接口，管理员和用户一起
    @PostMapping(value = "/login")
    public Result login(@RequestBody LoginParam loginParam) {
        //检查手机号码
        if (StringUtil.isEmpty(loginParam.getPhone())) {
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, Constants.PHONE_IS_NULL);
        }
        if (!RegexUtil.isMobileExact(loginParam.getPhone())) {
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, "手机号不合法");
        }
        //从redis获取验证码
        String cachedCode = (String) redisTemplate.opsForValue().get(loginParam.getPhone());
        String code = loginParam.getCode();
        //比较验证码
        if (!code.equals(cachedCode)) {
            return ResultGenerator.genFailResult("验证码错误/过期");
        }
        Result result = userService.login(loginParam);
        return ResultGenerator.genSuccessResult(result);
    }


//    /**
//     * 管理员登录
//     * @param employee
//     * @return
//     */
//    @PostMapping("/adminlogin")
//    public Result login(@RequestBody Employee employee) {
//            try {
//                Result result = userService.adminLogin(employee);
//                return result;
//            } catch (Exception e) {
//                return ResultGenerator.genFailResult("未知的异常" + e.getMessage());
//            }
//    }
//
//    /**
//     * 普通用户登录
//     * @param user
//     * @return
//     */
//    @PostMapping("/userLogin")
//    public Result userLogin(@RequestBody User user) {
//        try {
//            Result result= userService.userLogin(user);
//            return result;
//        } catch (Exception e) {
//            return ResultGenerator.genFailResult("未知的异常"+e.getMessage());
//        }
//    }

    /**
     * 用户注册
     *
     * @param registerParam
     * @return
     */
    @PostMapping("/register")
    public Result register(@RequestBody RegisterParam registerParam) {
        try {
            Result result = userService.register(registerParam);
            return result;
        } catch (Exception e) {
            return ResultGenerator.genFailResult("未知的异常" + e.getMessage());
        }
    }

}
