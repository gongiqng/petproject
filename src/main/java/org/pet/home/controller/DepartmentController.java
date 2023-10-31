package org.pet.home.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.pet.home.entity.Department;
import org.pet.home.net.param.Departmentparam;
import org.pet.home.net.NetCode;
import org.pet.home.net.Result;
import org.pet.home.service.IDepartmentService;
import org.pet.home.util.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description:TODO
 * @author: 龚强
 * @data:
 **/
@Api(tags = "部门接口文档")
@RestController
@RequestMapping("/department")
public class DepartmentController {
    private IDepartmentService departmentService;

    @Autowired
    public DepartmentController(IDepartmentService departmentService) {
        this.departmentService = departmentService;
    }
    @ApiOperation("添加部门")
    @PostMapping("/create")
    public Result add(@RequestBody Department  department){
        System.out.println("添加"+department);
        try {
            departmentService.add(department);
            return ResultGenerator.genSuccessResult(department);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultGenerator.genErrorResult(NetCode.CREATE_DEPARTMENT_ERROR, "保存对象失败" + e.getMessage());
        }
    }
    /**
     * 添加对象
     */
    @ApiOperation("添加部门")
    @PostMapping("/add")
    public Result add(@org.springframework.web.bind.annotation.RequestBody Departmentparam departmentparam) {
        System.out.println("添加"+departmentparam);
        try {
            Department department1 = new Department();
            department1.setSn(departmentparam.getSn());
            department1.setName(departmentparam.getName());
            long parent_id = departmentparam.getParentId();
            Department parentDepartment = new Department();
            parentDepartment.setId(parent_id);
            department1.setParent(parentDepartment);

            departmentService.add(department1);
            return ResultGenerator.genSuccessResult(department1);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultGenerator.genErrorResult(NetCode.CREAT_DEPARTMENT_ERROR, "保存对象失败" + e.getMessage());
        }
    }

    @PostMapping("/delete")
    public Result delete(Long id) {
        try {
            departmentService.remove(id);
            return ResultGenerator.genSuccessResult(id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultGenerator.genErrorResult(NetCode.REMOVE_DEPARTMENT_ERROR, "删除部门失败" + e.getMessage());
        }
    }
    @PostMapping("/update")
    public Result update(Department department) {
        try {
            departmentService.update(department);
            return ResultGenerator.genSuccessResult();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultGenerator.genErrorResult(NetCode.UPDATE_DEPARTMENT_ERROR, "更新部门失败" + e.getMessage());
        }
    }
    @GetMapping("/get")
    public Result get(Long id) {
      Department department=  departmentService.find(id);
        return ResultGenerator.genSuccessResult(department);
    }
    @GetMapping("/list")
    public Result list() {
       List<Department>departments=  departmentService.findAll();
        return ResultGenerator.genSuccessResult(departments);
    }
    @GetMapping("/tree")
    public  Result tree(){
        List<Department>departments=departmentService.getDepartmentTreeData();
        return ResultGenerator.genSuccessResult(departments);
    }

}
