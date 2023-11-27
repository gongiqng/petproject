package org.pet.home.service;

import org.apache.ibatis.annotations.Param;
import org.pet.home.entity.PetShop;

import java.util.List;

/**
 * @description:TODO
 * @author: 龚强
 * @data:
 **/
public interface IPetShopService {
    PetShop findByUserFindShop_id(Long userFindShop_id);

    int add(PetShop petShop);

    void updateState(Long id,Long saleStartTime);

    PetShop findPetShopById(Long id);

    void adoptPet(Long id,Long endTime,Long user_id);

    void delistPet(Long id);

    PetShop findPetByUser(Long user_id);

    List<PetShop> findPetShopByState(int state, long user_id);
    PetShop getPetById(long id);
    int buy(@Param("id") long id, @Param("userId") long user_id);
}
