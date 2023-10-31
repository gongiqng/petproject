package org.pet.home.service;

import org.pet.home.entity.Employee;

/**
 * @description:TODO
 * @author: 龚强
 * @data: 2023/1031
 **/
public interface IEmployeeService {
    boolean add(Employee employee);
    void remove(Long id);
    void update(Employee employee);
}
