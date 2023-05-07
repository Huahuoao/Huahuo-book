package com.huahuo.huahuobook.config;

import cn.hutool.jwt.JWTUtil;
import com.baomidou.mybatisplus.extension.exceptions.ApiException;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserLoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        //http的header中获得token
//        String token = request.getHeader("token");
//        //token不存在
//        if (token == null || token.equals("")) throw new ApiException("请先登录");
//        //验证token
//
//        boolean verify = JWTUtil.verify(token, "huahuo".getBytes());
//
//
//     if (verify)
//        return true;
//     else
//         throw new Exception("token验证失败");


        return  true;
    }
}
