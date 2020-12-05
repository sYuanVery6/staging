package com.ayuan.staging.common.interceptor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.ayuan.staging.common.annonation.jwt.PassToken;
import com.ayuan.staging.common.annonation.jwt.UserLoginToken;
import com.ayuan.staging.entity.po.User;
import com.ayuan.staging.entity.vo.ResponseEntity;
import com.ayuan.staging.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;

/**
 * 拦截请求 验证token
 * @author sYuan
 */
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 现获取header中的token
        String token = request.getHeader("token");

        // 如果不是映射到的方法的直接通过
        if(!(handler instanceof HandlerMethod)){
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod)handler;

        Method method = handlerMethod.getMethod();
        // 如果被PassToken修饰 通过
        if(method.isAnnotationPresent(PassToken.class)){
            PassToken passToken = method.getAnnotation(PassToken.class);
            if(passToken.required()){
                return true;
            }
        }

        String userId = null;
        if(method.isAnnotationPresent(UserLoginToken.class)){
            UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
            if(userLoginToken.required()){
                if(token == null){
                    response.setCharacterEncoding("UTF-8");
                    response.setContentType("application/json; charset=utf-8");
                    PrintWriter out = null;
                    out = response.getWriter();
                    out.append(ResponseEntity.fail("no token ,please reLogin .","401").toString());
                    return false;
                }

                //获取token的userId
                userId = JWT.decode(token).getAudience().get(0);

                User user = userService.getUserById(userId);
                if(user == null){
                    response.setCharacterEncoding("UTF-8");
                    response.setContentType("application/json; charset=utf-8");
                    PrintWriter out = null;
                    out = response.getWriter();
                    out.append(ResponseEntity.fail("the user does not exist .","501").toString());
                    return false;
                }

                //验证token
                JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassWord())).build();
                try {
                    jwtVerifier.verify(token);
                }catch (JWTVerificationException e){
                    throw new RuntimeException("401 .");
                }
                return true;
            }
        }

        return false;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }



}
