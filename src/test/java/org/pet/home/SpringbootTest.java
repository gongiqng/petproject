package org.pet.home;

import org.pet.home.dao.CityMapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.pet.home.dao.DepartmentMapper;
import org.pet.home.entity.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @description:TODO类描述
 * @author: hzh
 * @data: 2023/10/24
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootTest {

@Autowired
private CityMapper cityMapper;

    @Test
    public  void  testMapper(){
        System.out.println(cityMapper.findById(2));
    }
    @Autowired
    public DepartmentMapper departmentMapper;
    @Test
    public  void  test(){
        System.out.println(departmentMapper.find(2l));
    }
    @Test
    public  void  testInsert(){
        Department department=new Department();
        department.setSn("005");
        department.setName("测试部三");
        department.setState(0);
        //department.setManager_id(0l);
        //department.setParent_id(11l);
        departmentMapper.add(department);
    }
    @Test
    public  void  testUpdate(){
        Department department=new Department();
        department.setId(10l);
        department.setSn("007");
        department.setName("测试部七");
        //department.setParent_id(11l);
        departmentMapper.update(department);
    }

}

