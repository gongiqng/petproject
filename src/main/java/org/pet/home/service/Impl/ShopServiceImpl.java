package org.pet.home.service.Impl;

import org.pet.home.dao.EmployeeMapper;
import org.pet.home.dao.ShopMapper;
import org.pet.home.entity.Employee;
import org.pet.home.entity.Shop;
import org.pet.home.service.IShopService;
import org.pet.home.util.MD5Util;
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
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
@Service
public class ShopServiceImpl implements IShopService {

   private ShopMapper shopMapper;
   private EmployeeMapper employeeMapper;

    @Autowired
    public ShopServiceImpl(ShopMapper shopMapper, EmployeeMapper employeeMapper) {
        this.shopMapper = shopMapper;
        this.employeeMapper=employeeMapper;
    }

    @Override
    public int add(Shop shop) {
        return shopMapper.add(shop);
    }

    @Override
    public List<Shop> list() {
        return shopMapper.list();
    }

    @Override
    public void remove(Long id) {
        shopMapper.remove(id);
    }

    @Override
    public void successfulAudit(Long id) {
        //公司管理员 审核 商铺成功
        // 1: 改状态
        // 2: 生产账号，账号:手机号，密码:123456 然后存到Employee表
        // 3: 发短信告知成功
        Employee employee = new Employee();
        Shop shop=shopMapper.findById(id);
        employee.setUsername(shop.getName());
        employee.setPhone(shop.getTel());
        employee.setPassword(MD5Util.MD5Encode("123456", "utf-8"));
        employeeMapper.insert(employee);
        Employee e=employeeMapper.getAdmin(employee.getPhone(),employee.getPassword());
        shopMapper.addAdmin(e,id);
//        employee.setPhone();
//        employee.setPassword();
        // employee的service去存数据,
        // 存数据后，会返回对应的id
        // 在把这个id绑定到 shop里面，更新下shop表

        shopMapper.successfulAudit(id);
    }

    @Override
    public void auditFailure(Long id) {
        shopMapper.auditFailure(id);
    }

    @Override
    public void update(Shop shop) {
        shopMapper.update(shop);
    }

    @Override
    public List<Shop> paginationList(int offset, int pageSize) {
        return shopMapper.paginationList(offset,pageSize);
    }

    @Override
    public int count() {
        return shopMapper.count();
    }

    @Override
    public Shop findById(long id) {
        return shopMapper.findById(id);
    }

    @Override
    public Shop findByAddress(String address) {
        return shopMapper.findByAddress(address);
    }
}
