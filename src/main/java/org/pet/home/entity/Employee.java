package org.pet.home.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @description:TODO
 * @author: 龚强
 * @data:
 **/
@Getter
@Setter
@Data
public class Employee  implements Serializable {
    public Long id;
    private String username;
    private String email;
    private String phone;
    private String password;
    private int age;
    private int state;
    private  Long did;
    private  Department department;
    private String token;
}
