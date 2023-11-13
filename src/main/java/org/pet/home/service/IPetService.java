package org.pet.home.service;

import org.pet.home.entity.Pet;

import java.util.List;

/**
 * @description:TODO
 * @author: 龚强
 * @data:
 **/
public interface IPetService {
    int add(Pet pet);
    List<Pet> list();
    Pet findById(long id);
}
