package org.pet.home.controller;

import org.pet.home.common.Constants;
import org.pet.home.entity.Employee;
import org.pet.home.entity.PetShop;
import org.pet.home.entity.User;
import org.pet.home.entity.UserFindShop;
import org.pet.home.net.NetCode;
import org.pet.home.net.Result;
import org.pet.home.service.Impl.PetShopService;
import org.pet.home.service.Impl.UserFindShopService;
import org.pet.home.util.RedisKeyUtil;
import org.pet.home.util.ResultGenerator;
import org.pet.home.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @description:TODO
 * @author: 龚强
 * @data:
 **/
@RestController
public class PetShopController {
    private Logger logger = LoggerFactory.getLogger(PetShopController.class);
    private PetShopService petShopService;
    private UserFindShopService userFindShopService;
    private RedisTemplate redisTemplate;

    public PetShopController(PetShopService petShopService, UserFindShopService userFindShopService, RedisTemplate redisTemplate) {
        this.petShopService = petShopService;
        this.userFindShopService = userFindShopService;
        this.redisTemplate = redisTemplate;
    }


    //添加宠物商品
    @PostMapping("/addPetShop")
    private Result addPetShop(@RequestBody PetShop petShop, HttpServletRequest request) {
        //从请求头拿到token
        String token = request.getHeader("token");
        //判断token是否存在
        if (StringUtil.isEmpty(token)) {
            return ResultGenerator.genErrorResult(NetCode.TOKEN_NOT_EXIST, "token不存在");
        }
        Employee employee = (Employee) redisTemplate.opsForValue().get(token);
        //判断user的token是否过期
        if (employee == null) {
            return ResultGenerator.genErrorResult(NetCode.TOKEN_INVALID, Constants.INVALID_TOKEN);
        }
        //寻主id
        long userFindShop_id = petShop.getUserFindShop_id();
        //判断是否寻主
        if (userFindShopService.findById(userFindShop_id) == null) {
            return ResultGenerator.genFailResult("没有寻主");
        }
        //判断是否已经上架过
        if (petShopService.findByUserFindShop_id(userFindShop_id) != null) {
            return ResultGenerator.genFailResult("已有该商品");
        }
        //找到寻主历史
        UserFindShop userFindShop = userFindShopService.findById(userFindShop_id);
        //从寻主历史中找到成本价
        double costPrice = userFindShop.getPrice();
        //售价
        double sellPrice = petShop.getSellPrice();
        //售价异常或售价低于成本
        if (sellPrice < 0 || sellPrice < costPrice) {
            return ResultGenerator.genErrorResult(NetCode.SELL_PRICE_INVALID, "售价小于0或者售价小于成本价");
        }
        //设置宠物名
        petShop.setName(userFindShop.getName());
        //设置商铺id
        petShop.setShop_id(userFindShop.getShopId());
        //设置用户id
        petShop.setUser_id(userFindShop.getUserId());
        //设置管理员id
        petShop.setEmployee_id(userFindShop.getAdminId());
        //设置类别id
        petShop.setUserFindShop_id(userFindShop.getPetId());
        //设置成本价格
        petShop.setCostPrice(costPrice);
        //设置售价
        petShop.setSellPrice(sellPrice);
        //添加商品
        int rows = petShopService.add(petShop);
        if (rows == 1) {
            return ResultGenerator.genSuccessResult(petShop);
        }
        return ResultGenerator.genFailResult("添加失败");
    }


    //上架宠物商品
    @GetMapping("/petshelving")
    private Result petshelving(long id, HttpServletRequest request) {
        //从请求头拿到token
        String token = request.getHeader("token");
        //判断token是否存在
        if (StringUtil.isEmpty(token)) {
            return ResultGenerator.genErrorResult(NetCode.TOKEN_NOT_EXIST, "token不存在");
        }
        Employee employee = (Employee) redisTemplate.opsForValue().get(token);
        //判断user的token是否过期
        if (employee == null) {
            return ResultGenerator.genErrorResult(NetCode.TOKEN_INVALID, Constants.INVALID_TOKEN);
        }
        System.out.println(id);
        try {
            //上架商品，当前时间就是售卖时间，同时把state改为1,同时设置未被领养
            Long saleStartTime = System.currentTimeMillis();
            petShopService.updateState(id, saleStartTime);
            return ResultGenerator.genSuccessResult("上架成功");
        } catch (Exception e) {
            return ResultGenerator.genFailResult("未知的异常" + e.getMessage());
        }
    }
    //买宠物
    @PostMapping("/buy")
    public Result buyPet(long id, HttpServletRequest request) {
        String token = request.getHeader("token");

        Object object = redisTemplate.opsForValue().get(token);
        //判断token过期没
        if (object == null) {
            return ResultGenerator.genErrorResult(NetCode.TOKEN_INVALID, Constants.INVALID_TOKEN);
        }
        User user = (User) object;

        //在数据库找这个宠物信息
        PetShop petShop =petShopService.getPetById(id);
        //判断有无这个宠物
        if (petShop == null) {
            return ResultGenerator.genErrorResult(NetCode.PETSHOP_ERROR,"没有这个宠物信息");
        }
        //判断宠物是否被买走
        if (petShop.getState() == 1) {
            return ResultGenerator.genErrorResult(NetCode.PETSHOP_IS_BUY,"x宠物已经·出售");
        }
        //把状态改为1 1是已出售 再把user_id改成被买家的id
        int result =petShopService.buy(id, user.getId());
        //判断数据库插入成功没
        if (result == 0) {
            return ResultGenerator.genErrorResult(NetCode.BUY_ERROR,"购买失败");
        }
        return ResultGenerator.genSuccessResult("购买成功!");
    }
}

