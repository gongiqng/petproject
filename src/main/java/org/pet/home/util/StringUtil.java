package org.pet.home.util;

/**
 * @description:TODO
 * @author: 龚强
 * @data:
 **/
public class StringUtil {
    /*
    *判断s是否为空或null
     */
    public  static boolean isEmpty(String s){
        return  s==null||s.isEmpty();

    }
    public static boolean isNullOrNullStr(String s){
        return s==null||s.equals("null");
    }
}
