package org.pet.home.controller;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.pet.home.common.Constants;
import org.pet.home.entity.Employee;
import org.pet.home.entity.Users;
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

import javax.naming.spi.DirStateFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
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
    public  LoginController(RedisService redisService,UserService userService,RedisTemplate redisTemplate){
        this.redisService = redisService;
        this.userService = userService;
        this.redisTemplate=redisTemplate;
    }

    //阿里云发验证码的
    @GetMapping("/getverifycode")
    public Result SendCode(@RequestParam String phone)  {
        /**
         * 排除手机号是空的状态
         */
        if (StringUtil.isEmpty(phone)){
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, Constants.PHONE_IS_NULL);
        }
        /**
         * 手机号不合法
         */
        if(!RegexUtil.isMobileExact(phone)){
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID,"手机号不合法");
        }
        String host = "https://dfsns.market.alicloudapi.com";
        String path = "/data/send_sms";
        String method = "GET";
        String appcode = "d99d2fa390cc464390cef535154ceffa";
        Map< String, String > headers = new HashMap< String, String >();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map< String, String > querys = new HashMap< String, String >();
        Map< String, String > bodys = new HashMap< String, String >();
        String code = RandomCode.getCode() ;
        bodys.put("content", "code:" + code);
        bodys.put("template_id", "CST_ptdie100");
        bodys.put("phone_number", phone);

        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            String result = EntityUtils.toString(response.getEntity());
            redisTemplate.opsForValue().set(phone, code, 300, TimeUnit.SECONDS);
            return ResultGenerator.genSuccessResult(result);
        }catch (Exception e){
            e.printStackTrace();
        }

        return ResultGenerator.genFailResult("发送验证码失败！");
    }

//    @GetMapping("/getverifycode")
//    public Result sendVerifyCode(@RequestParam String phone){
//        return userService.sendRegisterCode(phone);
//    }

    //验证
    @GetMapping("/verifycode")
    public Result verifyCode(@RequestParam String phone, @RequestParam String code){
        if (StringUtil.isEmpty(phone)){
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, Constants.PHONE_IS_NULL);
        }
        if(!RegexUtil.isMobileExact(phone)){
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID,"手机号不合法");
        }
//       Set<String> expiredV = redisService.getSet(phone+phone);
//        String e=new ArrayList(expiredV).get(0).toString();
//        System.out.println(e);
       // String e = redisService.getValue(phone);
        String e= (String) redisTemplate.opsForValue().get(phone);
        System.out.println(e);
        logger.info(e);
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
    @PostMapping(value = "/login" ,produces = {"application/json", "application/xml"})
    public Result adminLogin(@RequestBody LoginParam loginParam) {
        //获取验证码
        String cachedCode = (String) redisTemplate.opsForValue().get(loginParam.getPhone());
        String code = loginParam.getCode();
        if (!code.equals(cachedCode)){
            return ResultGenerator.genFailResult("验证码错误/过期");
        }
                if (loginParam.getType() == 0) {
                    try {
                        Result result = userService.userLogin(loginParam);
                        return result;
                    } catch (Exception e) {
                        return ResultGenerator.genFailResult("未知异常" + e.getMessage());
                    }
                } else {
                    try {
                        Result result= userService.adminlogin(loginParam);
                        return result;
                    } catch (Exception e) {
                        return ResultGenerator.genFailResult("未知异常" + e.getMessage());
                    }
                }
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
//    public Result userLogin(@RequestBody Users user) {
//        try {
//            Result result= userService.userLogin(user);
//            return result;
//        } catch (Exception e) {
//            return ResultGenerator.genFailResult("未知的异常"+e.getMessage());
//        }
//    }

    @PostMapping("/register")
    public Result register(@RequestBody RegisterParam registerParam) {
        try {
            Result result = userService.register(registerParam);
            return result;
        } catch (Exception e) {
            return ResultGenerator.genFailResult("未知的异常"+e.getMessage());
        }
    }

}
