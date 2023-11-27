package org.pet.home.service.Impl;

import org.pet.home.dao.PetShopMapper;
import org.pet.home.entity.PetShop;
import org.pet.home.service.IPetShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @description:TODO
 * @author: 龚强
 * @data:
 **/
@Transactional(readOnly = true,propagation = Propagation.SUPPORTS)
@Service
public class PetShopService implements IPetShopService {
    private PetShopMapper petShopMapper;

    @Autowired
    public PetShopService(PetShopMapper petShopMapper){
        this.petShopMapper = petShopMapper;
    }

    @Override
    public PetShop findByUserFindShop_id(Long userFindShop_id) {
        return petShopMapper.findByUserFindShop_id(userFindShop_id);
    }

    @Override
    public int add(PetShop petShop) {
        return petShopMapper.add(petShop);
    }

    @Override
    public void updateState(Long id,Long saleStartTime) {
        petShopMapper.updateState(id,saleStartTime);
    }

    @Override
    public PetShop findPetShopById(Long id) {
        return petShopMapper.findPetShopById(id);
    }

    @Override
    public void adoptPet(Long id, Long endTime,Long user_id) {
        petShopMapper.adoptPet(id,endTime,user_id);
    }

    @Override
    public void delistPet(Long id) {
        petShopMapper.delistPet(id);
    }

    @Override
    public PetShop findPetByUser(Long user_id) {
        return petShopMapper.findPetByUser(user_id);
    }

    @Override
    public List<PetShop> findPetShopByState(int state, long user_id) {
        return petShopMapper.findPetShopByState(state,user_id);
    }

    @Override
    public PetShop getPetById(long id) {
        return petShopMapper.getPetById(id);
    }

    @Override
    public int buy(long id, long user_id) {
        return petShopMapper.buy(id,user_id);
    }

}
