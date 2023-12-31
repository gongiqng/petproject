package org.pet.home.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @description:TODO
 * @author: 龚强
 * @data:
 **/
@Data
public class User implements Serializable {
    //用户id
    private Long id;
    //用户名
    private String username;
    //手机号
    private String phone;
    //密码
    private String password;
    //状态
    private int state;
    //年龄
    private int age;
    //注册时间
    private Long registerTime;
    //角色 0是普通用户
    private int role;
    //token
    private String token;
    private double price;
    private Long product_id;
}
