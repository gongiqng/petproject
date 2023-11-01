package org.pet.home.dao;

import org.apache.ibatis.annotations.*;
import org.pet.home.entity.Department;
import org.pet.home.entity.Employee;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:TODO
 * @author: 龚强
 * @data:
 **/
@Mapper
@Repository
@Service
public interface EmployeeMapper {
    @Insert("insert into t_employee(username,email,phone,password,age,state,did)" +
            "values(#{username},#{email},#{phone},#{password},#{age},#{state},#{did})")
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    int add(Employee employee);
    @Delete("delete from t_employee where id=#{id}")
    void remove(Long id);

    @Update("update t_employee set " +
            "username=#{username},email=#{email},phone=#{phone}," +
            "age=#{age},state=#{state} ,did=#{did}" +
            "where id=#{id}")
    void update(Employee employee);
    @Select("select * from t_employee where id=#{id}")
    Employee findById(Long id);
    /*
    *查找在职员工
     */
    @Select("select * from t_employee where id=#{id} and state=0")
    Employee findIncumbency(Long id);
    @Select("select * from t_employee")
    List<Employee> findAll();
}
