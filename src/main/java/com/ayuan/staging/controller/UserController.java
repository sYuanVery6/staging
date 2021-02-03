package com.ayuan.staging.controller;

import com.alibaba.fastjson.JSONObject;
import com.ayuan.staging.common.annonation.jwt.PassToken;
import com.ayuan.staging.entity.vo.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sYuan
 * @Description 用户模块路由
 */
@RestController
public class UserController {

    /**
     * 登录
     */
    @PassToken
    @PostMapping("login")
    public JSONObject login(@RequestBody JSONObject user){

        System.out.println(user.toString());
        Map<String,Object> map = new HashMap<>();
        map.put("tokenName","111");
        return ResponseEntity.ok(map);
    }


    /**
     * 注册
     */
    @PostMapping("register")
    public JSONObject register(){

        return ResponseEntity.ok(new HashMap<>());
    }

    /**
     * 用户信息
     */
    @PassToken
    @PostMapping("userInfo")
    public JSONObject userInfo(){
        Map<String,Object> map = new HashMap<>();
        map.put("tokenName","111");
        return ResponseEntity.ok(map);
    }

    /**
     * 登出
     */
    @PostMapping("logout")
    public JSONObject logout(){

        return ResponseEntity.ok(new HashMap<>());
    }

}
