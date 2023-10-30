package org.pet.home.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:TODO
 * @author: 龚强
 * @data: 2023/10/26
 **/
@Getter
@Setter
@Data //相当于配置get set hashcode equals tostring
public class Department {
    private Long id;//主键
    private String sn;//部门编号
    private String name;//部门名称
    private String dirPath;
    private int state; //部门状态 0 正常 ，-1 停用
    /**
     * 配置部门经理
     */
    private Employee manager;
    //用不到
//    private Long manager_id;
//    //用不到
//    private Long parent_id;
    /**
     * 配置父部门
     */
    private Department parent;
    private List<Department>children=new ArrayList<>();
}