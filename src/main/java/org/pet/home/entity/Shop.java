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
public class Shop {
    private Long id;
    private String name;
    private String tel;
    private Long registerTime;
    private int state=0;//state为0代表未审核
    private String address;
    private String logo;
    private Employee admin;
    private Long admin_id;
    private Location location;
}
