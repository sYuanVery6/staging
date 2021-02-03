package com.ayuan.staging.controller;

import com.alibaba.fastjson.JSONObject;
import com.ayuan.staging.common.annonation.jwt.PassToken;
import com.ayuan.staging.common.annonation.jwt.UserLoginToken;
import com.ayuan.staging.common.util.JwtUtil;
import com.ayuan.staging.dao.UserDao;
import com.ayuan.staging.entity.po.User;
import com.ayuan.staging.entity.vo.ResponseEntity;
import com.ayuan.staging.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.ayuan.staging.entity.vo.RequestEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author sYuan
 */
@RestController
@RequestMapping("helloStaging")
public class HelloController {

    private static final Logger log =  LoggerFactory.getLogger(HelloController.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserService userService;

    @UserLoginToken
    @GetMapping("")
    public void getHello() {
        System.out.println(userDao.getAllUser());
    }

    @PassToken
    @GetMapping("user")
    public JSONObject getUser(RequestEntity requestParam) {
        Map<String,Object> data = new HashMap<>(2);
        List<User> userList = userService.getUser(requestParam);
        data.put("userList",userList);
        data.put("total",1);

        return ResponseEntity.ok(data);
    }

    @PassToken
    @PostMapping("login")
    public JSONObject login(@RequestBody User user , BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            throw new RuntimeException(Objects.requireNonNull(bindingResult.getFieldError()).toString());
        }

        User userForBase = userService.getUserById(user.getId());

        if(userForBase == null){
            return ResponseEntity.fail("The user does not exist .",400);
        }
        if(!userForBase.getPassWord().equals(user.getPassWord())){
            return ResponseEntity.fail("Incorrect password .",401);
        }

        String token = JwtUtil.getToken(userForBase);

        HashMap<String,Object> data = new HashMap<>(2);
        data.put("token",token);

        return ResponseEntity.ok(data);
    }

    @PassToken
    @GetMapping("log")
    public void printLog(){

        log.info("info");
        log.warn("warn");
        log.error("error");
        log.debug("debug");
        log.trace("trace");

    }

    @PassToken
    @PutMapping("user")
    public void addUser(){
        User user = new User();
        user.setUserName("aaa");
        user.setPassWord("bbsss");
        userService.addUser(user);
    }

    @PassToken
    @PostMapping("user")
    public void updateUser(){
        User user = new User();
        user.setId("1669468160");
        user.setUserName("sssyyy");
        userService.updateUser(user);
    }

}
