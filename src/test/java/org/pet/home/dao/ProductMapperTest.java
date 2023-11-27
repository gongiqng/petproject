package org.pet.home.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.pet.home.entity.Product;
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
public class ProductMapperTest {

    @Autowired
    private ProductMapper productMapper;

    @Test
    public void addTest() {
        Product product = new Product();
        product.setName("高级洗澡1");
        product.setSalePrice(288);
        product.setOffSaleTime(System.currentTimeMillis());
        product.setOnSaleTime(System.currentTimeMillis());
       // product.setState(0);
        product.setCostPrice(98);
        product.setCreateTime(System.currentTimeMillis());
        product.setSaleCount(20);
        productMapper.add(product);
        System.out.println(product);
    }
}
