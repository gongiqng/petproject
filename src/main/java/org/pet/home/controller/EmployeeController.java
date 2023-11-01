package org.pet.home.controller;

import io.swagger.annotations.Api;
import org.apache.ibatis.annotations.Param;
import org.pet.home.entity.Department;
import org.pet.home.entity.Employee;
import org.pet.home.net.NetCode;
import org.pet.home.net.Result;
import org.pet.home.service.IDepartmentService;
import org.pet.home.service.IEmployeeService;
import org.pet.home.util.MD5Util;
import org.pet.home.util.ResultGenerator;
import org.pet.home.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description:TODO
 * @author: 龚强
 * @data:
 **/
@Api(tags = "员工接口文档")
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    private IDepartmentService departmentService;
    private IEmployeeService employeeService;

    @Autowired
    public EmployeeController(IDepartmentService departmentService, IEmployeeService employeeService) {
        this.departmentService = departmentService;
        this.employeeService = employeeService;
    }

    @PostMapping("/add")
    public Result add(@RequestBody Employee employee) {
        if (StringUtil.isEmpty(employee.getPhone())) {
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, "手机号不能为空");
        }
        if (StringUtil.isEmpty(employee.getUsername())) {
            return ResultGenerator.genErrorResult(NetCode.USERNAME_INVALID, "用户名不能为空");
        }
        if (StringUtil.isEmpty(employee.getEmail())) {
            return ResultGenerator.genErrorResult(NetCode.EMAIL_INVALID, "邮箱不能为空");
        }
        if (StringUtil.isEmpty(employee.getPassword())) {
            employee.setPassword(MD5Util.MD5Encode("123456", "utf-8"));
        }
          Department department = departmentService.find(employee.getDid());
        if (department == null) {
            return ResultGenerator.genErrorResult(NetCode.DEPARTMENT_ID_INVALID, "非法的部门id");
        }
        boolean result = employeeService.add(employee);
        if (!result) {
            return ResultGenerator.genFailResult("添加员工失败");
        }
        return ResultGenerator.genSuccessResult(employee);
    }
    @PostMapping("/delete")
    public Result delete(Long id) {
        try {
           employeeService.remove(id);
            return ResultGenerator.genSuccessResult(id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultGenerator.genErrorResult(NetCode.REMOVE_EMPLOYEE_ERROR, "删除员工失败" + e.getMessage());
        }
    }
    @PostMapping("/update")
    public Result update(Employee employee) {
        try {
            employeeService.update(employee);
            return ResultGenerator.genSuccessResult("更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultGenerator.genErrorResult(NetCode.UPDATE_EMPLOYEE_ERROR, "更新员工失败" + e.getMessage());
        }
    }
    @GetMapping("/find")
    public Result find(Long id){
        Employee employee = employeeService.findById(id);
        return  ResultGenerator.genSuccessResult(employee);
    }

    @GetMapping("/findall")
    public Result findAll(){
        List<Employee> employees = employeeService.findAll();
        return ResultGenerator.genSuccessResult(employees);
    }
}

