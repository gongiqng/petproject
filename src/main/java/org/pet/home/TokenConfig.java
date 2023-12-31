package org.pet.home;

import org.pet.home.interceptor.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @description:TODO
 * @author: 龚强
 * @data:
 **/
@Configuration
public class TokenConfig implements WebMvcConfigurer {
    private RedisTemplate redisTemplate;

    @Autowired
    public TokenConfig(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册TestInterceptor拦截器
        InterceptorRegistration registration = registry.addInterceptor(new TokenInterceptor(redisTemplate));
        registration.addPathPatterns("/**"); //所有路径都被拦截
        registration.excludePathPatterns(    //添加不拦截路径
                "/login",                    //登录路径
                "/**/*.html",                //html静态资源
                "/**/*.js",                  //js静态资源
                "/**/*.css",                //css静态资源
                "/getverifycode",
                "/verifycode",
                "/register",
                "/adminlogin",
                "/userLogin",
                "/user/publish",
                "/user//processingList",
                "/user//userListById",
                "/user/shoplist",
                "/shop/register",
                "/shop//successfulAudit",
                "/user/listbyshopid",
                "/user/finish",
                "/buy",
                "/addPetShop",
                "/petshelving",
                "/onProduct",
                 "/offProduct",
                "/productList",
                 "/shoping"
        );
    }
}
