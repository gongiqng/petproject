package org.pet.home.service.Impl;

import org.pet.home.common.Constants;
import org.pet.home.dao.EmployeeMapper;
import org.pet.home.dao.UsersMapper;
import org.pet.home.entity.CodeResBean;
import org.pet.home.entity.Employee;
import org.pet.home.entity.User;
import org.pet.home.net.NetCode;
import org.pet.home.net.Result;
import org.pet.home.net.param.LoginParam;
import org.pet.home.net.param.RegisterParam;
import org.pet.home.service.IUserService;
import org.pet.home.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
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
    private RedisService redisService;
    private EmployeeMapper employeeMapper;
    private RedisTemplate redisTemplate;
    private UsersMapper usersMapper;


    @Autowired
    public UserService(RedisService redisService, EmployeeMapper employeeMapper, RedisTemplate redisTemplate,UsersMapper usersMapper) {
        this.redisService = redisService;
        this.employeeMapper = employeeMapper;
        this.redisTemplate = redisTemplate;
        this.usersMapper=usersMapper;
    }

    //发送注册的验证码
    @Override
    public Result sendRegisterCode(String phone) {
        //检查手机号是否为空或者为null
        if (StringUtil.isEmpty(phone)) {
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, Constants.PHONE_IS_NULL);
        }
        //检查手机号是否合法
        if (!RegexUtil.isMobileExact(phone)) {
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, "手机号不合法");
        }
        // 检查手机号是否注册过，已被注册
        if (!RegexUtil.isMobileExact(phone)) {
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, "手机号已注册");
        }
        //通过手机号查找用户，检查用户是否注册
        User u = usersMapper.CheckPhone(phone);
        //如果用户不为nll就说明已经注册了
        if (u != null) {
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, "用户已经被注册");
        }
        //定义一个变量，用来确定发送验证码的时间
        Long lastSandTime = 0L;
        try {
            lastSandTime = Long.parseLong(this.redisService.getValue(phone));
        } catch (NumberFormatException e) {
            logger.error(e.getMessage());
            lastSandTime = 0l;
            redisService.cacheValue(phone, System.currentTimeMillis() + "", 60);
        }

        //在不在1分钟内
        if (System.currentTimeMillis() - lastSandTime < 1 * 60 * 1000) {
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, "调用频率过多");
        }
        //过来一分钟，发验证码
        String expiredV = redisService.getValue(phone + phone);

        if (StringUtil.isNullOrNullStr(expiredV)) {
            String code = "123456";//+System.currentTimeMillis();
            redisService.cacheSet(phone + phone, code, 60);
            //redisService.cacheValue(phone+phone,code,60);
            CodeResBean<String> resBean = new CodeResBean<>();
            resBean.code = code;
            return ResultGenerator.genSuccessResult(resBean);
        } else {
            return ResultGenerator.genSuccessResult(expiredV);
        }

    }

    /**
     * 用户注册
     *
     * @param registerParam
     * @return
     */
    @Override
    public Result register(RegisterParam registerParam) {
        //先对获取的注册参数进行一个判断
        //检查手机号码
        if (StringUtil.isEmpty(registerParam.getPhone())) {
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, "手机号不能为空");
        }
        //检查用户名
        if (StringUtil.isEmpty(registerParam.getUsername())) {
            return ResultGenerator.genErrorResult(NetCode.USERNAME_INVALID, Constants.NAME_IS_NULL);
        }
        //如果没有输入密码。则自动设置为123456
        if (StringUtil.isEmpty(registerParam.getPassword())) {
            registerParam.setPassword(MD5Util.MD5Encode("123456", "utf-8"));
        }
        //检查用户年龄
        if (registerParam.getAge() < 0) {
            return ResultGenerator.genErrorResult(NetCode.AGE_INVALID, Constants.AGE_ERROR);
        }
        //获取注册的验证码
        String code = registerParam.getCode();
        // 尝试从缓存中获取短信验证码
        String cachedCode = (String) redisTemplate.opsForValue().get(registerParam.getPhone());
        //对验证码进行比较
        if (!code.equals(cachedCode)){
            return ResultGenerator.genFailResult(Constants.CODE_ERROR);
        }else {
            //通过手机号对用户检查，判断是否已经注册过
            //User u = iUsersService.CheckPhone(registerParam.getPhone());
            User u=usersMapper.CheckPhone(registerParam.getPhone());
            System.out.println(u);
            if (u != null) {
                return ResultGenerator.genFailResult(Constants.PHONE_OCCUPATION);
            }
            try {
                u = new User();
                //copy属性
                BeanUtils.copyProperties(registerParam,u);
                //密码进行MD5加密
                String phone=registerParam.getPhone();
                u.setPassword(MD5Util.MD5Encode(phone, "utf-8"));
                //设置当前时间为注册时间
                u.setRegisterTime(System.currentTimeMillis());
                //将用户添加进用户数据库
                usersMapper.register(u);
                return ResultGenerator.genSuccessResult("注册成功");
            }catch (Exception e){
                return ResultGenerator.genFailResult("注册失败"+e.getMessage());
            }
        }
    }
     //用户和管理员登录，用手机号和密码登录，类型代表不同的身份
    @Override
    public Result login(LoginParam loginParam){
        loginParam.setPassword(MD5Util.MD5Encode(loginParam.getPassword(), "utf-8"));
        //随机生成一个token
        String token = UUID.randomUUID().toString();
        //如果type=0则为普通用户，type=1则为管理员
        if (loginParam.getType() == 0){
            //找这个用户
            User user = usersMapper.getUser(loginParam.getPhone(),loginParam.getPassword());
            //如果用户存在
            if(user != null){
                //设置token
                user.setToken(token);
                logger.info("token__" + token);
                //把密码设置成null 保密
                user.setPassword(null);
                //把用户数据存入redis中 要用token取
                redisTemplate.opsForValue().set(token, user, 30, TimeUnit.MINUTES);
                return ResultGenerator.genSuccessResult(user);
            }
            //不存在返回用户账号密码错误
            return ResultGenerator.genErrorResult(NetCode.LOGIN_ERROR,Constants.LOGIN_ERROR);
        }else if(loginParam.getType()==1){
            //找到这个管理员
            Employee employee = employeeMapper.getAdmin(loginParam.getPhone(),loginParam.getPassword());
            //如果存在
            if (employee != null) {
                //设置token
                employee.setToken(token);
                logger.info("token__" + token);
                //把密码设置成null 保密
                employee.setPassword(null);
                //把管理员数据存入redis中 要用token取
                redisTemplate.opsForValue().set(token, employee, 30, TimeUnit.MINUTES);
                return ResultGenerator.genSuccessResult(employee);
            }
            //不存在返回管理员账号密码错误
            return ResultGenerator.genErrorResult(NetCode.LOGIN_ERROR,Constants.ADMIN_IS_NULL);
        }else{
            //不是1或0 返回非法请求
            return ResultGenerator.genErrorResult(NetCode.TYPE_ERROR, Constants.INVALID_REQUEST);
        }
    }


//    /**
//     * 管理员登录
//     * @param loginParam
//     * @return
//     */
//    @Override
//    public Result adminlogin(LoginParam loginParam) {
//        //获取登录信息
//        System.out.println(loginParam);
//        //判断账号密码是不是空
//        if (StringUtil.isEmpty(loginParam.getPhone())) {
//            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, Constants.PHONE_IS_NULL);
//        }
//        if (StringUtil.isEmpty(loginParam.getPassword())) {
//            return ResultGenerator.genErrorResult(NetCode.USERNAME_INVALID, Constants.PASSWORD_IS_NULL);
//        }
//
//        //对密码md5加密
//        loginParam.setPassword(MD5Util.MD5Encode(loginParam.getPassword(), "utf-8"));
//       // Employee employee=employeeMapper.getAdmin(loginParam.getPhone(), loginParam.getPassword());
//        //在数据库中查找管理员
//        User user = usersMapper.getAdmin(loginParam.getPhone(), loginParam.getPassword());
//        if (user != null) {
//            //如果有 加个token
//            String token = UUID.randomUUID().toString();
//            //给管理员设置一个token
//            user.setToken(token);
//            //为了安全，将密码设置为null
//            user.setPassword(null);
//            //把token存入redis并设置一个有效时长
//            redisTemplate.opsForValue().set(token, user, 30, TimeUnit.MINUTES);
//           logger.info("token__"+token);
//            //返回登录成功的信息
//            return ResultGenerator.genSuccessResult(user);
//        }
//        return ResultGenerator.genFailResult(Constants.ADMIN_IS_NULL);
//    }
//
//
//    /**
//     * 普通用户登录
//     *
//     * @param loginParam
//     * @return
//     */
//    @Override
//    public Result userLogin(LoginParam loginParam) {
//        //获取登录参数
//        System.out.println(loginParam);
//        //检查手机号码
//        if (StringUtil.isEmpty(loginParam.getPhone())) {
//            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, Constants.PHONE_IS_NULL);
//        }
//        //检查密码
//        if (StringUtil.isEmpty(loginParam.getPassword())) {
//            return ResultGenerator.genErrorResult(NetCode.PASSWORD_INVALID, Constants.PASSWORD_IS_NULL);
//        }
//        //对密码加密
//        loginParam.setPassword(MD5Util.MD5Encode(loginParam.getPassword(), "utf-8"));
//        //在数据库中查找这个用户
//        User u = usersMapper.getUser(loginParam.getPhone(), loginParam.getPassword());
//        System.out.println(u);
//        //如果没有这个用户，则登录失败
//        if (u == null) {
//            return ResultGenerator.genFailResult(Constants.LOGIN_ERROR);
//        } else {
//            //生成一个token
//            String token = UUID.randomUUID().toString();
//            //打印一下这个token
//            logger.info("token__" + token);
//            //给用户设置一个token
//            u.setToken(token);
//            //保证安全，密码为null
//            u.setPassword(null);
//            //30分钟token过期
//            redisTemplate.opsForValue().set(token, u, 30, TimeUnit.MINUTES);
//            return ResultGenerator.genSuccessResult(u);
//        }
//    }

    @Override
    public User CheckPhone(String phone) {
        return usersMapper.CheckPhone(phone);
    }


    @Override
    public User findById(long id) {
        return usersMapper.findById(id);
    }

    @Override
    public void update(double price, Long product_id, Long id) {
        usersMapper.update(price,product_id,id);
    }

    @Override
    public int recharge(double price, Long id) {
        return usersMapper.recharge(price,id);
    }
}
