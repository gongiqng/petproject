package org.pet.home.controller;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.pet.home.common.Constants;
import org.pet.home.entity.*;
import org.pet.home.net.Result;
import org.pet.home.net.NetCode;

import org.pet.home.net.param.AddPetParam;
import org.pet.home.net.param.UserFindShopParam;
import org.pet.home.service.*;
import org.pet.home.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @description:TODO
 * @author: 龚强
 * @data:
 **/
@RestController
@RequestMapping("/user")
public class UserController {
    private Logger logger = LoggerFactory.getLogger(UserController.class);
    private IUserFindShopService iUserFindShopService;

    private IUserService userService;
    private IShopService iShopService;


    private IPetService iPetService;
    private RedisTemplate redisTemplate;

    @Autowired
    public UserController(IUserService userService, IEmployeeService iEmployeeService
            , IUserFindShopService iUserFindShopService, IPetService iPetService, IShopService iShopService
            , RedisTemplate redisTemplate) {
        this.userService=userService;
        this.iShopService = iShopService;
        this.iUserFindShopService= iUserFindShopService;
        this.iPetService = iPetService;
        this.redisTemplate = redisTemplate;
    }


    /**
     * 用户A 把 它的宠物 以某种方式丢给 系统选的门店。
     * 1: 分析用户提交什么参数给你 ，也就是说你要接受哪些参数,一定要跟产品或者测试确定必须传的参数
     * 2: 业务逻辑
     * 3: 返回用户具体的结果，什么样的操作给什么样的结果
     *
     * @return
     */
    @PostMapping("/userfindshop")
    public Result userFindShop(@RequestBody UserFindShopParam userFindShopParam, HttpServletRequest request)  {
        //
        //拦截器里面其实已经判断了token的。
        //1:谁发布的任务
        String token = request.getHeader("token");
        //你通过token拿确定不是null?
        User user = null;
        Object userObject = redisTemplate.opsForValue().get(token);
        if (userObject == null) {
            return ResultGenerator.genErrorResult(NetCode.TOKEN_NOT_EXIST, Constants.TOKEN_IS_NULL);
        }
        user = (User) userObject;
        userFindShopParam.setUserId(user.getId());
        //检查参数，检查参数的目的是 按照需求把 必须要传的字段 进行校验 ， 可能只是检测 是否为空
        //或者是否为null，有的还需要进行其他的检查，比如手机号正则
        //名字不能空
        if (StringUtil.isEmpty(userFindShopParam.getName())) {
            return ResultGenerator.genErrorResult(NetCode.PET_NAME_INVALID, Constants.PETNAME_IS_NULL);
        }

        //地址不能空
        if (StringUtil.isEmpty(userFindShopParam.getAddress())) {
            return ResultGenerator.genErrorResult(NetCode.ADDRESS_INVALID, Constants.ADDRESS_IS_NULL);
        }

        //判断性别的
        if (StringUtil.isEmpty(userFindShopParam.getSex())) {
            return ResultGenerator.genErrorResult(NetCode.SEX_INVALID, Constants.SEX_ERROR);
        }
        //检查接种状态
        if (userFindShopParam.getIsinoculation() != 0 && userFindShopParam.getIsinoculation() != 1) {
            return ResultGenerator.genErrorResult(NetCode.ISINOCULATION_INVALID, Constants.INOCULATION_ERROR);
        }
        //检查生日
        if (userFindShopParam.getBirth() < 0) {
            return ResultGenerator.genErrorResult(NetCode.BIRTH_INVALID, Constants.BIRTH_ERROR);
        }
        List<Shop> shopList = iShopService.list();//假设你的数据非常多，比如美团。那你要对比这么多数据的经纬度，然后每条算距离麻?
        List<Location> locations = new ArrayList<>();
        for (Shop value : shopList) {
            try {
                //也就是说这个商户的地址 没办法解析到经纬度?
                //1:怎么避免商户的地址是非法的 , 也就是说在商户的提交注册时候，
                //  除了判断地址null这种,还要把地址转经纬度. 并且要把经纬度存下来
                //  所以，当商户管理员修改地址的时候，都要经过1:null判断2：转经纬度3:保存
                //2:万一出现怎么弥补
                locations.add(GaoDeMapUtil.getLngAndLag(value.getAddress()));
            } catch (UnsupportedEncodingException e) {
                //LoggerFactory.getLogger(UserController.class).error(e.getMessage());
                logger.error(e.getMessage());
            }
        }
        Location userLocation = null;
        try {
            userLocation = GaoDeMapUtil.getLngAndLag(userFindShopParam.getAddress());
        } catch (UnsupportedEncodingException e) {
            return ResultGenerator.genErrorResult(NetCode.SHOP_ADDRESS_INVALID, "非法的地址");
        }
        //获取用户发信息的地址
        //获取离用户最近的店铺的地址
        Location latest = AddressDistanceComparator.findNearestAddress(userLocation, locations);
        System.out.println(latest);
        Shop shop = iShopService.findByAddress(latest.getFormattedAddress());
        if(shop == null){
            //你能确定你能100%找到吗?
            return ResultGenerator.genErrorResult(NetCode.SHOP_ADDRESS_INVALID, "没有找到合适的商户");
        }
        userFindShopParam.setShopId(shop.getId());
        userFindShopParam.setAdminId(shop.getAdmin_id());
        //iUserMsgService.add(userFindShopParam);
        int result = iUserFindShopService.insert(userFindShopParam);//假设存进去了
        if (result != 1) {
            return ResultGenerator.genFailResult("添加失败");
        }
        return ResultGenerator.genSuccessResult(userFindShopParam);
    }
    //待处理和已处理的寻主列表（商铺后台）
    @GetMapping("/processingList")
    public Result petList(int state, HttpServletRequest request) {
        //从请求头拿到token
        String token = request.getHeader("token");
        System.out.println(token);
        //判断token是否存在
        if (StringUtil.isEmpty(token)) {
            return ResultGenerator.genErrorResult(NetCode.TOKEN_NOT_EXIST, "token不存在");
        }
        //登陆后的商铺，从redis里面读取token
        Employee employee = (Employee) redisTemplate.opsForValue().get(token);
        //判断token是否过期
        if (employee == null) {
            return ResultGenerator.genErrorResult(NetCode.TOKEN_INVALID, Constants.INVALID_TOKEN);
        }
        //待处理
        if (state == 0) {
            //根据state判断是否处理
            List<UserFindShop> list =iUserFindShopService.getPetListByState(state);
            return ResultGenerator.genSuccessResult(list);
        } else if (state == 1) {
            //已处理
            List<UserFindShop> list =iUserFindShopService.getPetListByState(state);
            return ResultGenerator.genSuccessResult(list);
        } else {
            return ResultGenerator.genErrorResult(NetCode.PET_PROCESS_ERROR, "列表错误");
        }
    }

    /**
     * 用户查看自己的寻主列表
     *
     * @param request
     * @return
     */
    @GetMapping("/userListById")
    public Result UserList(HttpServletRequest request) {
        String token = request.getHeader("token");
        Object object = redisTemplate.opsForValue().get(token);
        //判断token过期没
        if (object == null) {
            return ResultGenerator.genErrorResult(NetCode.TOKEN_INVALID, Constants.INVALID_TOKEN);
        }
        User user = (User) object;
        //通过用户id获取用户发布的寻主列表
        List<UserFindShop> list = iUserFindShopService.getUserList(user.getId());
        try {
            //判断用户是否有发布过寻主任务
            if (list.get(0) != null)
                return ResultGenerator.genSuccessResult(list);
        } catch (Exception e) {
            return ResultGenerator.genErrorResult(NetCode.USER_LIST_IS_NULL, Constants.USER_LIST_IS_NULL);
        }
        return ResultGenerator.genErrorResult(NetCode.USER_LIST_IS_NULL, Constants.USER_LIST_IS_NULL);
    }

    //商铺列表
    @GetMapping("/shoplist")
    public Result ShopList(HttpServletRequest request) {
        String token = request.getHeader("token");
        Object object = redisTemplate.opsForValue().get(token);
        //判断token过期没
        if (object == null) {
            return ResultGenerator.genErrorResult(NetCode.TOKEN_INVALID, Constants.INVALID_TOKEN);
        }
        //获取商铺列表
        List<Shop> list = iShopService.list();
        //没有的话 返回附近暂无店铺
        if (list.size()==0){
            return ResultGenerator.genErrorResult(NetCode.SHOP_LIST_IS_NULL, Constants.SHOP_ERROR);
        }

        return ResultGenerator.genSuccessResult(list);
    }
    /**
     * 点击商铺 获取该商品宝贝列表
     * @param
     * @return
     */ @GetMapping("/listbyshopid")
    public Result getShopBaby(@RequestParam int shop_id,HttpServletRequest request) {
        String token = request.getHeader("token");
        Object object = redisTemplate.opsForValue().get(token);
        //判断token过期没
        if (object == null) {
            return ResultGenerator.genErrorResult(NetCode.TOKEN_INVALID, Constants.INVALID_TOKEN);
        }
        //查询所有的商铺id
        List<Long> allShopId =iUserFindShopService.findAllShopId();
        // 使用distinct方法去重
        List<Long> distinctList = allShopId.stream().distinct().collect(Collectors.toList());
        //判断该店铺是否上架宠物
        if (!distinctList.contains(shop_id)) {
            return ResultGenerator.genErrorResult(NetCode.SHOP_IS_NULL, "该店铺未上架宠物");
        }
        //通过商铺id获取商品
        List<UserFindShop> list=iUserFindShopService.getShopList(shop_id);
        //没有的话 返回无商品
        if (list.size()==0){
            return ResultGenerator.genErrorResult(NetCode.SHOP_LIST_IS_NULL, Constants.SHOP_ERROR);
        }
        return ResultGenerator.genSuccessResult(list);
    }
    //寻主完毕接口
    @GetMapping("/finish")
    public Result shoppetfinish(Integer state, Long id,HttpServletRequest request) {
        String token = request.getHeader("token");
        Object object = redisTemplate.opsForValue().get(token);
        //判断token过期没
        if (object == null) {
            return ResultGenerator.genErrorResult(NetCode.TOKEN_INVALID, Constants.INVALID_TOKEN);
        }
         logger.error(state.toString());
         if(state==null||state<0){
             return  ResultGenerator.genFailResult("参数不正确");
         }
        int rows = iUserFindShopService.updateState(state, id);
        //修改失败
        if (rows != 1) {
            return ResultGenerator.genFailResult("确认失败");
        }
        //查找用户手机号，发送短信
        //在user_msg表中找到user_id
        long user_id = iUserFindShopService.findById(id).getUserId();
        logger.error("用户id",user_id);
        //根据user_id找到手机号
        String phone= userService.findById(user_id).getPhone();
        System.out.println(phone);
        //发送验证码
        sendShopCode(phone);
        //根据id查找返回数据
        UserFindShop userFindShop=iUserFindShopService.findById(id);
        return ResultGenerator.genSuccessResult(userFindShop);
    }

    public Result sendShopCode(@RequestParam String phone) {
        //检查手机号是否为空
        if (StringUtil.isEmpty(phone)) {
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, "手机号不能为空");
        }
        //检查手机号是否符合手机号的规范
        if (!RegexUtil.isMobileExact(phone)) {
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, "不是合法的手机号");
        }
        System.out.println(phone);
        String host = "https://dfsns.market.alicloudapi.com";
        String path = "/data/send_sms";
        String method = "GET";
        String appcode = "d99d2fa390cc464390cef535154ceffa";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "APPCODE " + appcode);
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        String code = RandomCode.getCode();
        Map<String, String> querys = new HashMap<String, String>();
        Map<String, String> bodys = new HashMap<String, String>();
        bodys.put("content", "code:" + code);
        bodys.put("template_id", "CST_ptdie100");
        bodys.put("phone_number", phone);
        System.out.println(code);
        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            //  System.out.println(response.toString());
            //把手机号和验证码存进redis,设置1个小时过期，用于测试，防止时间短了过期
            redisTemplate.opsForValue().set(phone, code, 60, TimeUnit.MINUTES);
            String result = EntityUtils.toString(response.getEntity());
            return ResultGenerator.genFailResult(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultGenerator.genFailResult("发送验证码失败！");
    }
    //余额充值
    @PostMapping("/recharge")
    public  Result  recharge(HttpServletRequest request,double price){
        //从请求头拿到token
        String token = request.getHeader("token");
        //判断token是否存在
        Object object = redisTemplate.opsForValue().get(token);
        //判断token过期没
        if (object == null) {
            return ResultGenerator.genErrorResult(NetCode.TOKEN_INVALID, Constants.INVALID_TOKEN);
        }
        if(price<=0){
            return ResultGenerator.genFailResult("充值异常");
        }
        User user = (User) object;
        Long id=user.getId();
        int count= userService.recharge(price,id);
        if(count==1){
            ResultGenerator.genSuccessResult("充值成功");
        }
        return  ResultGenerator.genFailResult("充值失败");
    }


}
