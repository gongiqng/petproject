package org.pet.home.net.param;

import lombok.Data;

/**
 * @description:TODO
 * @author: 龚强
 * @data:
 **/
@Data
public class LoginParam {
    private String phone;
    private String password;
    private String username;
    //type 1管理员 type 0用户
    private int type;
}
