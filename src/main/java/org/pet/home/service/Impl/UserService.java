package org.pet.home.service.Impl;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.pet.home.common.Constants;
import org.pet.home.dao.EmployeeMapper;
import org.pet.home.entity.CodeResBean;
import org.pet.home.entity.Employee;
import org.pet.home.entity.Users;
import org.pet.home.net.NetCode;
import org.pet.home.net.Result;
import org.pet.home.net.param.LoginParam;
import org.pet.home.service.IUserService;
import org.pet.home.service.IUsersService;
import org.pet.home.util.*;
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
    private IUsersService iUsersService;
    @Autowired
    public UserService(RedisService redisService, EmployeeMapper employeeMapper, RedisTemplate redisTemplate, IUsersService iUsersService) {
        this.redisService = redisService;
        this.employeeMapper=employeeMapper;
        this.redisTemplate=redisTemplate;
        this.iUsersService=iUsersService;
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
        Users u =iUsersService.selectPhone(phone);
        if (u!=null) {
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, "用户已经被注册");
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
            String code = "123456";//+System.currentTimeMillis();
            redisService.cacheSet(phone+phone,code,60);
            //redisService.cacheValue(phone+phone,code,60);
            CodeResBean<String> resBean = new CodeResBean<>();
            resBean.code = code;
            return  ResultGenerator.genSuccessResult(resBean);
        }else {
            return  ResultGenerator.genSuccessResult(expiredV);
        }

    }

    /**
     * 管理员登录
     * @param employee
     * @return
     */
//    @Override
//    public Result adminLogin(Employee employee) {
//        if (StringUtil.isEmpty(employee.getUsername())) {
//            return ResultGenerator.genErrorResult(NetCode.USERNAME_INVALID, "用户名号不能为空");
//        }
//        if (StringUtil.isEmpty(employee.getPassword())) {
//            return ResultGenerator.genErrorResult(NetCode.PASSWORD_INVALID, "密码不能为空");
//        }
//        // employee.setPassword(MD5Util.MD5Encode(employee.getPassword(), "utf-8"));
//        Employee e=employeeMapper.login(employee);
//        if(e==null){
//            return ResultGenerator.genFailResult("用户名号或密码错误");
//        }
//        else {
//            //生成一个token
//            String token= UUID.randomUUID().toString();
//            logger.info("token__"+token);
//            e.setToken(token);
//            e.setPassword(null);
//            //30分钟token过期
//            redisTemplate.opsForValue().set(token,e,30, TimeUnit.MINUTES);
//            return ResultGenerator.genSuccessResult(e);
//        }
//    }

    /**
     * 注册
     * @param user
     * @return
     */
    @Override
    public Result register(Users user) {
        if (StringUtil.isEmpty(user.getPhone())) {
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, "手机号不能为空");
        }
        if (StringUtil.isEmpty(user.getUsername())) {
            return ResultGenerator.genErrorResult(NetCode.USERNAME_INVALID, "用户名不能为空");
        }
        if (StringUtil.isEmpty(user.getPassword())) {
            user.setPassword(MD5Util.MD5Encode("123456", "utf-8"));
        }
        if (user.getAge() < 0) {
            return ResultGenerator.genErrorResult(NetCode.AGE_INVALID, "年龄不能小于0");
        }
        Users u = iUsersService.selectPhone(user.getPhone());
        if (u != null) {
            return ResultGenerator.genFailResult("该用户已注册");
        }
        user.setRegisterTime(System.currentTimeMillis());
        int rows = iUsersService.register(user);
        if (rows == 1) {
            return ResultGenerator.genSuccessResult("注册成功");
        } else {
            return ResultGenerator.genFailResult("注册失败");
        }
    }

    @Override
    public Result adminlogin(LoginParam loginParam) {
        System.out.println(loginParam);
        //判断账号密码是不是空
        if (StringUtil.isEmpty(loginParam.getPhone())) {
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, "手机号不能为空");
        }
        if (StringUtil.isEmpty(loginParam.getPassword())) {
            return ResultGenerator.genErrorResult(NetCode.USERNAME_INVALID, "密码不能为空");
        }
        //密码md5加密
        loginParam.setPassword(MD5Util.MD5Encode(loginParam.getPassword(), "utf-8"));
        //从数据库找这个人
        Users users = iUsersService.getAdmin(loginParam.getPhone(),loginParam.getPassword());
        if (users != null) {
            //如果有 加个token
            String token = UUID.randomUUID().toString();
            users.setToken(token);
            users.setPassword(null);
            redisTemplate.opsForValue().set(token, users, 30, TimeUnit.MINUTES);
//            logger.info("token__"+token);
            return ResultGenerator.genSuccessResult(users);
        }
        return ResultGenerator.genFailResult("你不是管理员");
    }

    /**
     * 普通用户登录
     * @param loginParam
     * @return
     */
    @Override
    public Result userLogin(LoginParam loginParam) {
        if (StringUtil.isEmpty(loginParam.getPhone())) {
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, "手机号不能为空");
        }
        if (StringUtil.isEmpty(loginParam.getPassword())) {
            return ResultGenerator.genErrorResult(NetCode.PASSWORD_INVALID, "密码不能为空");
        }
        loginParam.setPassword(MD5Util.MD5Encode(loginParam.getPassword(), "utf-8"));
        Users u=iUsersService.getUser(loginParam.getPhone(), loginParam.getPassword());
        if (u == null) {
            return ResultGenerator.genFailResult("账号或密码错误");
        } else {
            //生成一个token
            String token = UUID.randomUUID().toString();
            logger.info("token__" + token);
            u.setToken(token);
            u.setPassword(null);
            //30分钟token过期
            redisTemplate.opsForValue().set(token, u, 30, TimeUnit.MINUTES);
            return ResultGenerator.genSuccessResult(u);
        }
    }


}
