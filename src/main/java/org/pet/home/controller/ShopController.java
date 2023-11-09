package org.pet.home.controller;

import io.swagger.annotations.Api;
import org.pet.home.net.Result;
import org.pet.home.entity.Employee;
import org.pet.home.entity.Shop;
import org.pet.home.net.NetCode;
import org.pet.home.service.IShopService;
import org.pet.home.util.ResultGenerator;
import org.pet.home.util.ShopUtil;
import org.pet.home.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description:TODO
 * @author: 龚强
 * @data:
 **/
@Api(tags = "shop接口文档")
@RestController
@RequestMapping("/shop")
public class ShopController {
    private IShopService iShopService;

    @Autowired
    public ShopController(IShopService iShopService) {
        this.iShopService = iShopService;
    }
    @PostMapping("/register")
    public Result shopRegister(@RequestBody Shop shop) {
        if (StringUtil.isEmpty(shop.getName())) {
            return  ResultGenerator.genErrorResult(NetCode.SHOP_NAME_INVALID, "店铺名不能为空");
        }
        if (StringUtil.isEmpty(shop.getTel())) {
            return  ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, "手机号不能为空");
        }
        if (StringUtil.isEmpty(shop.getAddress())) {
            return ResultGenerator.genErrorResult(NetCode.SHOP_ADDRESS_INVALID, "地址名不能为空");
        }
        if (StringUtil.isEmpty(shop.getLogo())) {
            return ResultGenerator.genErrorResult(NetCode.SHOP_LOGO_INVALID, "logo不能为空");
        }
        if (shop.getAdmin() == null) {
            Employee employee = new Employee();
            employee.setId(0l);
            shop.setAdmin(employee);
        }
        shop.setRegisterTime(System.currentTimeMillis());
        int count = iShopService.add(shop);
        if (count != 1) {
            return ResultGenerator.genFailResult("添加shop失败");
        }
        return ResultGenerator.genSuccessResult();
    }

    @RequestMapping("/list")
    public Result list() {
        return ResultGenerator.genSuccessResult(iShopService.list());
    }

    @PostMapping("/delete")
    public Result delete(Long id) {
        try {
            iShopService.remove(id);
            return ResultGenerator.genSuccessResult("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultGenerator.genErrorResult(NetCode.REMOVE_DEPARTMENT_ERROR, "删除失败！" + e.getMessage());
        }
    }

    @PostMapping("/auditFailure")
    public Result auditFailure(Long id) {
        try {
            iShopService.auditFailure(id);
            return ResultGenerator.genSuccessResult("审核失败");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultGenerator.genErrorResult(NetCode.REMOVE_DEPARTMENT_ERROR, "error" + e.getMessage());
        }
    }

    @PostMapping("/successfulAudit")
    public Result successfulAudit(Long id) {
        try {
            iShopService.successfulAudit(id);
            return ResultGenerator.genSuccessResult("审核成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultGenerator.genErrorResult(NetCode.REMOVE_DEPARTMENT_ERROR, "error！" + e.getMessage());
        }
    }


    @PostMapping("/update")
    public Result shopUpdate(@RequestBody Shop shop) {
        try {
            if (StringUtil.isEmpty(shop.getName())) {
                return ResultGenerator.genErrorResult(NetCode.SHOP_NAME_INVALID, "店铺名不能为空");
            }
            if (StringUtil.isEmpty(shop.getTel())) {
                return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, "手机号不能为空");
            }
            if (StringUtil.isEmpty(shop.getAddress())) {
                return ResultGenerator.genErrorResult(NetCode.SHOP_ADDRESS_INVALID, "地址名不能为空");
            }
            iShopService.update(shop);
            return ResultGenerator.genSuccessResult(shop);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultGenerator.genErrorResult(NetCode.UPDATE_DEPARTMENT_ERROR, "修改失败！" + e.getMessage());
        }
    }
    @PostMapping("/paginationList")
    public Result list(@RequestParam("page") int page, @RequestParam("pageSize") int pageSize){
        // 获取总记录数
        int count = iShopService.count();
        int offset = (page-1) * pageSize;
        List<Shop> shops = iShopService.paginationList(offset,pageSize);
        ShopUtil shopUtil = new ShopUtil();
        shopUtil.shops=shops;
        shopUtil.total = count;
        return ResultGenerator.genSuccessResult(shopUtil);
    }
}
