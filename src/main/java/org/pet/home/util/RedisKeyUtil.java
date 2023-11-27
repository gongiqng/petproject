package org.pet.home.util;

/**
 * @description:存redis里面的key
 * @author: 龚强
 * @data:
 **/
public class RedisKeyUtil {
    public static   String getSMSRedisKey(String phone){
        return  "sms_" + phone;
    }

    public static String getTokenRedisKey(String phone) {
        return "token_" + phone;
    }
}
