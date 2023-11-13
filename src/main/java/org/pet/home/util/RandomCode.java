package org.pet.home.util;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @description:TODO
 * @author: 龚强
 * @data:
 **/
public class RandomCode {
    public static String getCode(){
        return String.format("%06d", ThreadLocalRandom.current().nextInt(1000000));
    }
}
