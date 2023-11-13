package org.pet.home.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.pet.home.entity.Pet;
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
public class PetMapperTest {
    @Autowired
    private PetMapper petMapper;

    @Test
    public void testAdd(){
        Pet pet = new Pet("狗","忠诚");
        petMapper.add(pet);
    }

    @Test
    public void testList(){
        System.out.println(petMapper.list());
    }

    @Test
    public void testFindById(){
        System.out.println(petMapper.findById(5l));
    }
}
