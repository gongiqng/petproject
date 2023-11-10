package org.pet.home.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.pet.home.dao.EmployeeMapper;
import org.pet.home.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @description:TODO
 * @author: 龚强
 * @data: 2023/10/31
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class EmployeeTest {
    @Autowired
    private EmployeeMapper employeeMapper;
    @Test
    public  void  testEmployeeMapper(){
        Employee employee=employeeMapper.findById(326l);
//        employee.setUsername("李四");
//        employee.setEmail("22@qq.com");
//        employeeMapper.update(employee);
        System.out.println(employee);
    }
}
