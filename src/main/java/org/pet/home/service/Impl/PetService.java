package org.pet.home.service.Impl;

import org.pet.home.dao.PetMapper;
import org.pet.home.entity.Pet;
import org.pet.home.service.IPetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:TODO
 * @author: 龚强
 * @data:
 **/
@Service
public class PetService implements IPetService {
    private PetMapper petMapper;

    @Autowired
    public PetService(PetMapper petMapper){
        this.petMapper = petMapper;
    }
    @Override
    public int add(Pet pet) {
        return petMapper.add(pet);
    }

    @Override
    public List<Pet> list() {
        return petMapper.list();
    }

    @Override
    public Pet findById(long id) {
        return petMapper.findById(id);
    }
}
