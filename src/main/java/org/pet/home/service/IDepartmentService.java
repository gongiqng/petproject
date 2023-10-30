package org.pet.home.service;

import org.pet.home.common.DepartmentQuery;
import org.pet.home.entity.Department;

import java.util.List;

/**
 * @description:TODO
 * @author: 龚强
 * @data:
 **/
public interface IDepartmentService {







    void add(Department d);  //新增
    void remove(Long id);  //删除
    void update(Department d);  //修改
    Department find(Long id);  //通过id查询
    List<Department> findAll();//查询所有
    Long queryCount();//返回数据数目

    List<Department> findDepartmentsByPage(DepartmentQuery query);//返回分页数据据
    List<Department> getDepartmentTreeData();
}
