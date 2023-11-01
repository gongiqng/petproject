package org.pet.home.service.Impl;

import org.pet.home.dao.DepartmentMapper;
import org.pet.home.dao.EmployeeMapper;
import org.pet.home.entity.Department;
import org.pet.home.entity.Employee;
import org.pet.home.service.IEmployeeService;
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
public class EmployeeServiceImpl implements IEmployeeService {

   private   EmployeeMapper employeeMapper;
    private DepartmentMapper departmentMapper;
    public EmployeeServiceImpl(EmployeeMapper employeeMapper,DepartmentMapper departmentMapper){
        this.employeeMapper=employeeMapper;
        this.departmentMapper=departmentMapper;
    }
    @Override
    public boolean add(Employee employee) {
    int rows=employeeMapper.add(employee);
    if(rows==0){
        return  false;
    }else {
        Department department=this.departmentMapper.find(employee.getDid());
        employee.setDepartment(department);
        return  true;
    }
    }

    @Override
    public void remove(Long id) {
        employeeMapper.remove(id);
    }

    @Override
    public void update(Employee employee) {
  employeeMapper.update(employee);
    }

    @Override
    public Employee findById(Long id) {
        return employeeMapper.findById(id);
    }

    @Override
    public Employee findIncumbency(Long id) {
        return employeeMapper.findIncumbency(id);
    }

    @Override
    public List<Employee> findAll() {
        return employeeMapper.findAll();
    }
}
