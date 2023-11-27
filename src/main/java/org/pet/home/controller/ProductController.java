package org.pet.home.controller;

import org.apache.ibatis.annotations.Param;
import org.pet.home.common.Constants;
import org.pet.home.common.ProductQuery;
import org.pet.home.entity.Employee;
import org.pet.home.entity.Product;
import org.pet.home.entity.User;
import org.pet.home.net.NetCode;
import org.pet.home.net.Result;
import org.pet.home.service.Impl.ProductService;
import org.pet.home.service.Impl.UserService;
import org.pet.home.util.ResultGenerator;
import org.pet.home.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @description:TODO
 * @author: 龚强
 * @data:
 **/
@RestController
public class ProductController {
    private Logger logger = LoggerFactory.getLogger(ProductController.class);
    private ProductService productService;
    private RedisTemplate redisTemplate;
    private UserService userService;

    public ProductController(ProductService productService, RedisTemplate redisTemplate, UserService userService) {
        this.productService = productService;
        this.redisTemplate = redisTemplate;
        this.userService = userService;
    }
    //上架
    @GetMapping("/onProduct")
    private Result onProduct(Long id, HttpServletRequest request) {
        //从请求头拿到token
        String token = request.getHeader("token");
        //判断token是否存在
        if (StringUtil.isEmpty(token)) {
            return ResultGenerator.genErrorResult(NetCode.TOKEN_NOT_EXIST, Constants.INVALID_TOKEN);
        }
        Employee employee = (Employee) redisTemplate.opsForValue().get(token);
        //判断employee的token是否过期
        if (employee == null) {
            return ResultGenerator.genErrorResult(NetCode.TOKEN_INVALID, Constants.INVALID_TOKEN);
        }
        //判断商品是否存在
        Product product =productService.findProductById(id);
        if (product == null){
            //不存在返回没有此商品
            return ResultGenerator.genErrorResult(NetCode.PRODUCT_IS_NULL,Constants.PRODUCT_IS_NULL);
        }

        try {
            //上架，修改状态，修改上架时间
            Long onSaleTime = System.currentTimeMillis();
            productService.onProduct(id, onSaleTime);
            return ResultGenerator.genSuccessResult("上架成功");
        } catch (Exception e) {
            return ResultGenerator.genFailResult("未知的异常" + e.getMessage());
        }
    }

    //下架
    @GetMapping("/offProduct")
    private Result offProduct(Long id, HttpServletRequest request) {
        //从请求头拿到token
        String token = request.getHeader("token");
        //判断token是否存在
        if (StringUtil.isEmpty(token)) {
            return ResultGenerator.genErrorResult(NetCode.TOKEN_NOT_EXIST, Constants.INVALID_TOKEN);
        }
        Employee employee = (Employee) redisTemplate.opsForValue().get(token);
        //判断employee的token是否过期
        if (employee == null) {
            return ResultGenerator.genErrorResult(NetCode.TOKEN_INVALID, Constants.INVALID_TOKEN);
        }
        try {
            //下架，修改状态，修改下架时间
            Long offSaleTime = System.currentTimeMillis();
            productService.offProduct(id, offSaleTime);
            return ResultGenerator.genSuccessResult("下架成功");
        } catch (Exception e) {
            return ResultGenerator.genFailResult("未知的异常" + e.getMessage());
        }
    }
    //上下架商品列表
    @GetMapping("/productList")
    public Result paging(HttpServletRequest request, @RequestParam("page") int page, @RequestParam("pageSize") int pageSize) {
        //从请求头拿到token
        String token = request.getHeader("token");
        //判断token是否存在
        if (StringUtil.isEmpty(token)) {
            return ResultGenerator.genErrorResult(NetCode.TOKEN_NOT_EXIST,Constants.INVALID_TOKEN);
        }
        Employee employee = (Employee) redisTemplate.opsForValue().get(token);
        //判断employee的token是否过期
        if (employee == null) {
            return ResultGenerator.genErrorResult(NetCode.TOKEN_INVALID, Constants.INVALID_TOKEN);
        }
        int count = productService.count();
        int offset = (page - 1) * pageSize;
        List<Product> products = productService.findProductByState(offset, pageSize);
        ProductQuery productQuery = new ProductQuery();
        productQuery.total = count;
        productQuery.products = products;
        return ResultGenerator.genSuccessResult(productQuery);
    }
    //用户购买
    @GetMapping("/shoping")
    public Result shoping(HttpServletRequest request, @Param("id") Long id, @Param("count") int count) {
        //从请求头拿到token
        String token = request.getHeader("token");
        //判断token是否存在
        Object object = redisTemplate.opsForValue().get(token);
        //判断token过期没
        if (object == null) {
            return ResultGenerator.genErrorResult(NetCode.TOKEN_INVALID, Constants.INVALID_TOKEN);
        }
        User user = (User) object;
        //检查购买数量
        User u=userService.findById(user.getId());
        if (count<=0){
            return ResultGenerator.genErrorResult(NetCode.COUNT_ERROR,Constants.COUNT_ERROR);
        }
        //判断商品是否存在
        Product product =productService.findProductById(id);
        if (product == null){
            //不存在返回没有此商品
            return ResultGenerator.genErrorResult(NetCode.PRODUCT_IS_NULL,Constants.PRODUCT_IS_NULL);
        }
        //判断商品是否上架
        int state = product.getState();
        if (state == 0) {
            return ResultGenerator.genErrorResult(NetCode.STATE_INVALID, Constants.STATE_INVALID);
        }
        //获取商品的库存
        int saleCount = product.getSaleCount();
        //判断库存是否充足
        if (count > saleCount) {
            return ResultGenerator.genErrorResult(NetCode.Count_INVALID, Constants.Count_INVALID);
        }
        //获取商品的售价
        double salePrice = product.getSalePrice();
        //获取用户余额
        double price = u.getPrice();
        //余额不足
        if (price < salePrice * count) {
            return ResultGenerator.genErrorResult(NetCode.Price_INVALID, Constants.Price_INVALID);
        }
        //修改库存
        logger.info("库存",saleCount);
        logger.info("销售", count);
        productService.updateCount(id, saleCount - count);
        //修改用户余额
        logger.info("用户余额",price);
        logger.info("消费",salePrice * count);
        double price1=price - salePrice * count;
        userService.update(price1, id, u.getId());
        return ResultGenerator.genSuccessResult("购买成功");
    }
}
