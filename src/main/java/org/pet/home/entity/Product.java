package org.pet.home.entity;

import lombok.Data;

/**
 * @description:TODO
 * @author: 龚强
 * @data:
 **/
@Data
public class Product {
    //服务id
    private Long id;
    //服务名
    private String name;
    //售价
    private double salePrice;
    //下架时间
    private Long offSaleTime;
    //上架时间
    private Long onSaleTime;
    //状态 0下架 1上架
    private int  state ;
    //成本价
    private double costPrice;
    //创建服务时间
    private Long createTime;
    //订单数量
    private int saleCount;
}
