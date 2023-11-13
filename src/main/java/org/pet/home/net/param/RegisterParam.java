package org.pet.home.net.param;

import lombok.Data;

/**
 * @description:TODO
 * @author: 龚强
 * @data:
 **/
@Data
public class RegisterParam {
    private String code;
    private String username;
    private String phone;
    private String password;
    private int state;
    private int age;
    private Long registerTime;
}
