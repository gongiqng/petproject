package org.pet.home.service.Impl;

import org.pet.home.dao.ShopMapper;
import org.pet.home.entity.Shop;
import org.pet.home.service.IShopService;
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

    @Autowired
    public ShopServiceImpl(ShopMapper shopMapper) {
        this.shopMapper = shopMapper;

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
}
