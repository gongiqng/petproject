package org.pet.home.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.pet.home.entity.City;
import org.springframework.stereotype.Repository;

/**
 * @description:TODO类描述
 * @author: hzh
 * @data: 2023/10/24
 **/
@Mapper
@Repository
public interface CityMapper {
    @Select("select * from city where id=#{id}")
    City findById(@Param("id") int id);
}
