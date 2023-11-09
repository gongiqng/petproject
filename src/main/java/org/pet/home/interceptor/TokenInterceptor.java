package org.pet.home.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.pet.home.net.Result;
import org.pet.home.common.Constants;
import org.pet.home.net.NetCode;
import org.pet.home.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/10/18
 **/
@Component
public class TokenInterceptor implements HandlerInterceptor {
    private Logger logger = LoggerFactory.getLogger(TokenInterceptor.class);
    private RedisTemplate redisTemplate;
    public TokenInterceptor(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        logger.info("请求来了" + httpServletRequest.getRequestURI().toString());
        //要求接口里面的请求头必须有token这个字段
        String token = httpServletRequest.getHeader("token");
        if (!StringUtil.isEmpty(token)) {
            //如果token有,那么redis拿到token对应的用户信息
            Object obj = redisTemplate.opsForValue().get(token);
            if (obj == null) {
                writeRes(httpServletResponse, NetCode.TOKEN_INVALID, Constants.INVALID_TOKEN, null);
                return false;
            } else {
                //重新更新一下过期时间
                redisTemplate.opsForValue().set(token, obj, 30, TimeUnit.MINUTES);
                return true;
            }
        }
        writeRes(httpServletResponse, NetCode.TOKEN_NOT_EXIST, Constants.INVALID_REQUEST, null);
        //false就是此次接口后面不执行
        return false;
    }

    private void writeRes(HttpServletResponse httpServletResponse, int code, String message, Object data) throws IOException {
        httpServletResponse.setCharacterEncoding("utf-8");
        httpServletResponse.setContentType("application/json,charset=UTF-8");
        PrintWriter writer = httpServletResponse.getWriter();
        Result result = new Result();
        result.setResultCode(code);
        result.setMessage(message);
        if (data != null) {
            result.setData(data);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(result);
        writer.write(json);
        writer.close();
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
