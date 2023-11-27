package org.pet.home.entity;

import lombok.Data;

/**
 * @description:TODO
 * @author: 龚强
 * @data:
 **/
@Data
public class PetShop {
    //id
    private Long id;
    //宠物名
    private String name;
    //开始售卖时间
    private Long saleStartTime;
    //结束售卖时间
    private Long endTime;
    //成本价
    private double costPrice;
    //卖价
    private double sellPrice;
    //是否上架，0上架，1没上架
    private int state;
    //是否领养，0领养，1未领养
    private int adopt;
    //用户id
    private Long user_id;
    //商铺id
    private Long shop_id;
    //管理员id
    private Long employee_id;
    //寻主id
    private Long userFindShop_id;
}
