package org.pet.home;

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
        Employee employee=new Employee();
        employee.setUsername("张三");
        employee.setEmail("2574@qq.com");
        employee.setPhone("158980");
        employee.setPassword("111");
        employee.setState(0);
        employee.setAge(28);
        employee.setDid(11l);
        employeeMapper.add(employee);
    }
}
