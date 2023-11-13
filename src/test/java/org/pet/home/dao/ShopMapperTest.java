package org.pet.home.dao;

import org.junit.Test;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pet.home.dao.ShopMapper;
import org.pet.home.entity.Employee;
import org.pet.home.entity.Shop;
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
public class ShopMapperTest {
    @Autowired
    private ShopMapper shopMapper;

    @Test
    public void addTest() {
        Shop shop=new Shop();
        shop.setAddress("123");
        shop.setState(0);
        shop.setName("www");
        shop.setTel("15689076589");
        shop.setRegisterTime(System.currentTimeMillis());
        Employee employee=new Employee();
        employee.setId(0l);
        shop.setAdmin(employee);
        shopMapper.add(shop);
        System.out.println(shop);
    }
    @Test
    public void updateTest() {
        Shop shop=shopMapper.findById(30l);
//        shop.setAddress("456");
//        shop.setState(0);
//        shopMapper.update(shop);
        System.out.println(shop);
    }
}
