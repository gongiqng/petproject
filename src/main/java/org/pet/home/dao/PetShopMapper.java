package org.pet.home.dao;

import org.apache.ibatis.annotations.*;
import org.pet.home.entity.PetShop;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description:TODO
 * @author: 龚强
 * @data:
 **/
@Mapper
@Repository
public interface PetShopMapper {
    @Select("select * from petShop where userFindShop_id=#{userFindShop_id} ")
    PetShop findByUserFindShop_id(Long userFindShop_id);

    @Insert("insert into petShop(name,costPrice,sellPrice,user_id,shop_id,employee_id,userFindShop_id) values" +
            "(#{name},#{costPrice},#{sellPrice},#{user_id},#{shop_id},#{employee_id},#{userFindShop_id}) ")
    int add(PetShop petShop);

    @Update("update petShop set state=0,adopt=1,saleStartTime=#{saleStartTime} where id=#{id}")
    void updateState(Long id, Long saleStartTime);

    @Select("select * from petShop where id=#{id} ")
    PetShop findPetShopById(Long id);

    @Update("update petShop set adopt=0, endTime=#{endTime}, user_id=#{user_id} where id=#{id}")
    void adoptPet(Long id, Long endTime, Long user_id);

    @Update("update petShop set state=1 where id=#{id}")
    void delistPet(Long id);

    @Select("select * from petShop where user_id=#{user_id}")
    PetShop findPetByUser(Long user_id);

    @Select("select * from petShop where state=#{state} and user_id=#{user_id}")
    List<PetShop> findPetShopByState(int state, long user_id);
    @Select("select * from petShop where id=#{id}")
    PetShop getPetById(long id);

    @Update("update petShop set adopt=1 , user_id=#{user_id} where id=#{id} ")
    int buy(@Param("id") long id, @Param("userId") long user_id);
}
