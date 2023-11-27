package org.pet.home.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.pet.home.entity.PetShop;
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
public class PetShopMapperTest {
    @Autowired
    private PetShopMapper petShopMapper;

    @Test
    public void addTest() {
        PetShop petShop = new PetShop();
        petShop.setShop_id(38l);
        petShop.setName("旺财");
        petShop.setCostPrice(80.0);
        petShop.setSellPrice(200.0);
        petShop.setUser_id(1l);
        petShop.setEmployee_id(331l);
        petShop.setUserFindShop_id(6l);
        petShopMapper.add(petShop);
    }

}
