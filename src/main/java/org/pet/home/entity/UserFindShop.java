package org.pet.home.entity;

import lombok.Data;

/**
 * @description:TODO
 * @author: 龚强
 * @data:
 **/
@Data
public class UserFindShop {
    //id
    private long id;
    //用户id
    private long userId;
    //管理员id
    private long adminId;
    //店铺id
    private long shopId;
    private long petId;
    //宠物name
    private String name;
    //地址
    private String address;
    //发布时间
    private long createtime;
    //生日
    private long birth;
    //价格
    private double price;
    //是否接种 0未接种 1已接种
    private int isinoculation;
    //性别
    private String sex;
    //状态 0未处理1已处理
    private int state;
    //宠物种类
    private Pet pet;
    private Shop shop;
    private Employee admin;
    private User user;

}
