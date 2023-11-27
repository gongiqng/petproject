package org.pet.home.net.param;

import lombok.Data;

/**
 * @description:TODO
 * @author: 龚强
 * @data:
 **/
@Data
public class RegisterParam {
    public String username;
    public String email;
    public String phone;
    public String password;
    public int age;
    public String code;
}
