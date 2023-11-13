package org.pet.home.entity;

import lombok.Data;

/**
 * @description:TODO
 * @author: 龚强
 * @data:
 **/
@Data
public class Pet {
    //id
    private int id;
    //类别
    private String type;
    //描述
    private String description;
}
