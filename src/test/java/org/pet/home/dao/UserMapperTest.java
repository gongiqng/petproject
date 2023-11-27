package org.pet.home.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.pet.home.entity.User;
import org.pet.home.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @description:TODO
 * @author: 龚强
 * @data:
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserMapperTest {
    @Autowired
    private  UsersMapper usersMapper;
    @Test
    public  void  testAdd(){
        User user =new User();
        user.setUsername("sa");
        user.setPassword("112233");
        user.setRegisterTime(System.currentTimeMillis());
        user.setAge(18);
        user.setPhone("13387138909");
       // user.setRole(1);
        user.setState(0);
      //  usersMapper.register(user);
    }
    @Test
    public  void  testSelect(){
        User user =new User();
        user.setPhone("10086110000");
        System.out.println(usersMapper.getUser("10086110000",MD5Util.MD5Encode("123456","utf-8")));
    }
    @Test
    public  void  testPhone(){
        System.out.println(usersMapper.CheckPhone("13387138909"));
    }

}
