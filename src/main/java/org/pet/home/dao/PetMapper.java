package org.pet.home.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.pet.home.entity.Pet;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description:TODO
 * @author: 龚强
 * @data:
 **/
@Mapper
@Repository
public interface PetMapper {
    @Insert("insert into t_pet(type,description)" +
            "values(#{type},#{description})")
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    int add(Pet pet);
    /**
     * 查询所有宠物类别
     * @return
     */
    @Select("SELECT * FROM t_pet")
    List<Pet> list();

    /**
     * 通过id查找宠物
     * @param id
     * @return
     */
    @Select("SELECT * FROM t_pet where id=#{id}")
    Pet findById(Long id);
}
