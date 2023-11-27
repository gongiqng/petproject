package org.pet.home.service.Impl;

import org.apache.ibatis.annotations.Param;
import org.pet.home.dao.ProductMapper;
import org.pet.home.entity.Product;
import org.pet.home.service.IProductService;
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
public class ProductService implements IProductService {
    private ProductMapper productMapper;

    @Autowired
    public ProductService(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    @Override
    public void offProduct(Long id, Long offSaleTime) {
        productMapper.offProduct(id, offSaleTime);
    }


    @Override
    public void onProduct(Long id, Long onSaleTime) {
        productMapper.onProduct(id, onSaleTime);
    }


    @Override
    public List<Product> findProductByState(int offset, int pageSize) {
        return productMapper.findProductByState(offset, pageSize);
    }


    @Override
    public int count() {
        return productMapper.count();
    }


    @Override
    public Product findProductById(Long id) {
        return productMapper.findProductById(id);
    }

    @Override
    public  void updateCount(@Param("id")Long id, @Param("saleCount")int saleCount) {
        productMapper.updateCount(id,saleCount);
    }
}
