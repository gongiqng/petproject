package org.pet.home.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
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
    //处理流异常的状态
    public static  final String convertStreamToString(InputStream is) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (IOException e) {
            // 处理异常
            return null;
        }
    }
}
