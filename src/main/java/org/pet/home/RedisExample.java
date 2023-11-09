package org.pet.home;

import org.pet.home.util.JedisUtil;

/**
 * @description:TODO
 * @author: 龚强
 * @data:
 **/
public class RedisExample {
    public static  void main(String[] ages){
        JedisUtil.INSTANCE.set("test","aaa");
        System.out.println(JedisUtil.INSTANCE.get("test"));
    }
}
