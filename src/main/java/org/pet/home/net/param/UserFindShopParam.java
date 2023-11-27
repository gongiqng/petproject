package org.pet.home.net.param;

import lombok.Data;
import org.pet.home.entity.Pet;

/**
 * @description:TODO
 * @author: 龚强
 * @data:
 **/
@Data
public class UserFindShopParam {
    //id
    private long id;

    //用户id,发布的寻主任务的用户的id
    private long userId;

    //管理员id 该宠物分配给哪个商户的商户管理员的id
    private long adminId;

    //店铺id  该宠物分配给哪个商户的id
    private long shopId;

    // 对应的宠物的类别的id
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

}
