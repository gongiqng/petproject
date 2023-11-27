package org.pet.home.dao;

import org.apache.ibatis.annotations.*;
import org.pet.home.entity.Product;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description:TODO
 * @author: 龚强
 * @data:
 **/
@Mapper
@Repository
public interface ProductMapper {
    @Insert("insert into product(name,salePrice,offSaleTime,onSaleTime,state,costPrice,createTime,saleCount) values" +
            "(#{name},#{salePrice},#{offSaleTime},#{onSaleTime},#{state},#{costPrice},#{createTime},#{saleCount})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int add(Product product);

    @Update("update product set state=0,offSaleTime=#{offSaleTime} where id=#{id}")
    void offProduct(Long id, Long offSaleTime);

    @Update("update product set state=1,onSaleTime=#{onSaleTime} where id=#{id}")
    void onProduct(Long id, Long onSaleTime);

    @Select("select * from product limit #{offset}, #{pageSize} ")
    List<Product> findProductByState(@Param("offset") int offset, @Param("pageSize") int pageSize);

    @Select("SELECT COUNT(*) FROM product ")
    int count();

    @Select("select * from product where id=#{id} ")
    Product findProductById(Long id);

    @Update("update product set saleCount=#{saleCount} where id=#{id}")
    void updateCount(@Param("id")Long id,@Param("saleCount")int saleCount);
}
