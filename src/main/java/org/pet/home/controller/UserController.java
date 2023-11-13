package org.pet.home.controller;

import org.pet.home.entity.*;
import org.pet.home.net.Result;
import org.pet.home.net.NetCode;

import org.pet.home.net.param.AddPetParam;
import org.pet.home.service.*;
import org.pet.home.util.AddressDistanceComparator;
import org.pet.home.util.GaoDeMapUtil;
import org.pet.home.util.ResultGenerator;
import org.pet.home.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:TODO
 * @author: 龚强
 * @data:
 **/
@RestController
@RequestMapping("/user")
public class UserController {
    private IUserMsgService iUserMsgService;

    private IEmployeeService iEmployeeService;

    private IShopService iShopService;

    private IUsersService iUsersService;

    private IPetService iPetService;

    @Autowired
    public UserController (IUserMsgService iUserMsgService, IEmployeeService iEmployeeService
            , IUsersService iUsersService, IPetService iPetService,IShopService iShopService){
        this.iUserMsgService = iUserMsgService;
        this.iEmployeeService = iEmployeeService;
        this.iShopService = iShopService;
        this.iUsersService = iUsersService;
        this.iPetService = iPetService;
    }

    @PostMapping("/publish")
    public Result Publish(@RequestBody AddPetParam addPetParam){
        UserMsg userMsg = addPetParam.userMsg;
        int user_id = addPetParam.user_id;
        if (StringUtil.isEmpty(userMsg.getName())){
            return ResultGenerator.genErrorResult(NetCode.PET_NAME_INVALID, "宠物名不能空");
        }

        if (StringUtil.isEmpty(userMsg.getAddress())){
            return ResultGenerator.genErrorResult(NetCode.ADDRESS_INVALID, "地址不能空");
        }

        if (StringUtil.isEmpty(userMsg.getSex())){
            return ResultGenerator.genErrorResult(NetCode.SEX_INVALID, "性别不能空");
        }

        if (userMsg.getIsinoculation()<0){
            return ResultGenerator.genErrorResult(NetCode.ISINOCULATION_INVALID, "接种信息错误");
        }

        if (userMsg.getBirth()<0){
            return  ResultGenerator.genErrorResult(NetCode.BIRTH_INVALID, "生日错误");
        }

        if (iUsersService.findById(userMsg.getUserId())==null){
            return ResultGenerator.genErrorResult(NetCode.ID_INVALID, "没有这个用户id");
        }

        List<Shop> shopList = iShopService.list();
        List<Location> locations = new ArrayList<>();

        try {
            //把店铺地址加到list
            for (Shop value : shopList) {
                locations.add(GaoDeMapUtil.getLngAndLag(value.getAddress()));
            }
            //获取用户发信息的地址
            Location userLocation = GaoDeMapUtil.getLngAndLag(userMsg.getAddress());
            //获取离用户最近的店铺的地址
            Location latest = AddressDistanceComparator.findNearestAddress(userLocation,locations);
            //找到这个店铺并绑定
            Shop shop = iShopService.findByAddress(latest.getFormattedAddress());
            userMsg.setShop(shop);
            //添加时间
            userMsg.setCreatetime(System.currentTimeMillis());
            //绑定店铺 admin 账号
            System.out.println(shop.getAdminId());
            Employee admin = iEmployeeService.findById(shop.getAdminId());
            userMsg.setAdmin(admin);
            //绑定宠物类型
            Pet pet = iPetService.findById(userMsg.getPetId());
            userMsg.setPet(pet);
            //绑定user
            Users users = iUsersService.findById(user_id);
            userMsg.setUsers(users);

            int result = iUserMsgService.add(userMsg);
            if (result!=1){
                return ResultGenerator.genFailResult("添加失败");
            }
            System.out.println(shop.getId());
            System.out.println(admin.getId());
            System.out.println(pet.getId());
            System.out.println(users.getId());
            System.out.println(userMsg.getId());
            int result1 = iUserMsgService.addTask(shop.getId(),admin.getId(),pet.getId(),users.getId(),userMsg.getId());
            if (result1!=1){
                return ResultGenerator.genFailResult("添加失败");
            }
            return ResultGenerator.genSuccessResult(pet);
        }catch (Exception e){
            e.printStackTrace();
        }

        return ResultGenerator.genFailResult("添加失败");
    }

}
