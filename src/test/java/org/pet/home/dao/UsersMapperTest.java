package org.pet.home.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.pet.home.entity.Users;
import org.pet.home.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.ServletOutputStream;

/**
 * @description:TODO
 * @author: 龚强
 * @data:
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class UsersMapperTest {
    @Autowired
    private  UsersMapper usersMapper;
    @Test
    public  void  testAdd(){
        Users users=new Users();
        users.setUsername("sa");
        users.setPassword("112233");
        users.setRegisterTime(System.currentTimeMillis());
        users.setAge(18);
        users.setPhone("13387138909");
       // users.setRole(1);
        users.setState(0);
      //  usersMapper.register(users);
    }
    @Test
    public  void  testSelect(){
        Users users=new Users();
        users.setPhone("10086110000");
        System.out.println(usersMapper.getUser("10086110000",MD5Util.MD5Encode("123456","utf-8")));
    }
    @Test
    public  void  testPhone(){
        System.out.println(usersMapper.selectPhone("13387138909"));
    }

}
