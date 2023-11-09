package org.pet.home.service.Impl;

import org.pet.home.common.Constants;
import org.pet.home.dao.EmployeeMapper;
import org.pet.home.entity.CodeResBean;
import org.pet.home.entity.Employee;
import org.pet.home.net.NetCode;
import org.pet.home.net.Result;
import org.pet.home.service.IUserService;
import org.pet.home.util.MD5Util;
import org.pet.home.util.RegexUtil;
import org.pet.home.util.ResultGenerator;
import org.pet.home.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @description:TODO
 * @author: 龚强
 * @data:
 **/
@Service
public class UserService implements IUserService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private  RedisService redisService;

    private EmployeeMapper employeeMapper;
    private RedisTemplate redisTemplate;
    private VerificationCodeService verificationCodeService;
    @Autowired
    public UserService(RedisService redisService, EmployeeMapper employeeMapper, RedisTemplate redisTemplate,VerificationCodeService verificationCodeService) {
        this.redisService = redisService;
        this.employeeMapper=employeeMapper;
        this.redisTemplate=redisTemplate;
        this.verificationCodeService=verificationCodeService;
    }
    @Override
    public Result sendRegisterCode(String phone) {
        if (StringUtil.isEmpty(phone)){
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, Constants.PHONE_IS_NULL);
        }
        if(!RegexUtil.isMobileExact(phone)){
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID,"手机号不合法");
        }

        //已被注册
        if(!RegexUtil.isMobileExact(phone)){
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID,"手机号已注册");
        }
        Long lastSandTime = 0L;
        try {
            lastSandTime = Long.parseLong(this.redisService.getValue(phone));
        }catch (NumberFormatException e){
            logger.error(e.getMessage());
            lastSandTime = 0l;
            redisService.cacheValue(phone,System.currentTimeMillis()+"",60);
        }

        //在不在1分钟内
        if(System.currentTimeMillis() - lastSandTime < 1*60*1000){
            return  ResultGenerator.genErrorResult(NetCode.PHONE_INVALID,"调用频率过多");
        }
        //过来一分钟，发验证码
        String expiredV = redisService.getValue(phone+phone);

        if(StringUtil.isNullOrNullStr(expiredV)){
            String code = "123456_"+System.currentTimeMillis();
            redisService.cacheSet(phone+phone,code,60);
            //redisService.cacheValue(phone+phone,code,60);
            CodeResBean<String> resBean = new CodeResBean<>();
            resBean.code = code;
            return  ResultGenerator.genSuccessResult(resBean);
        }else {
            return  ResultGenerator.genSuccessResult(expiredV);
        }

    }

    @Override
    public Result adminLogin(Employee employee) {
        if (StringUtil.isEmpty(employee.getPhone())) {
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, "手机号名不能为空");
        }
        if (StringUtil.isEmpty(employee.getPassword())) {
            return ResultGenerator.genErrorResult(NetCode.PASSWORD_INVALID, "密码不能为空");
        }
        // employee.setPassword(MD5Util.MD5Encode(employee.getPassword(), "utf-8"));
        Employee e=employeeMapper.select(employee.getPhone(),employee.getPassword());
        if(e==null){
            return ResultGenerator.genFailResult("手机号号或密码错误");
        }
        else {
            //生成一个token
            String token= UUID.randomUUID().toString();
            logger.info("token__"+token);
            e.setToken(token);
            e.setPassword(null);
            //30分钟token过期
            redisTemplate.opsForValue().set(token,e,30, TimeUnit.MINUTES);
            return ResultGenerator.genSuccessResult(e);
        }
    }
    //验证码校对
    public boolean verifyCode(String username, String code) {
        String savedCode = verificationCodeService.getCodeByUsername(username);
        return code.equals(savedCode);
    }

}
