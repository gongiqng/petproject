package org.pet.home.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @description:TODO
 * @author: 龚强
 * @data:
 **/
@Getter
@Setter
@Data
public class Employee {
    public Long id;
    private String username;
    private String email;
    private String phone;
    private String password;
    private int age;
    private int state;
}
