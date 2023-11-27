package org.pet.home.service;

import org.apache.ibatis.annotations.Param;
import org.pet.home.entity.Product;

import java.util.List;

/**
 * @description:TODO
 * @author: 龚强
 * @data:
 **/
public interface IProductService {
    void offProduct(Long id, Long offSaleTime);

    void onProduct(Long id, Long onSaleTime);

    List<Product> findProductByState(@Param("offset") int offset, @Param("pageSize") int pageSize);

    int count();

    Product findProductById(Long id);

    void updateCount(@Param("id")Long id, @Param("saleCount")int saleCount);
}
