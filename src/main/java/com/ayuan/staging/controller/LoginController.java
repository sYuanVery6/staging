package com.ayuan.staging.controller;

import com.alibaba.fastjson.JSONObject;
import com.ayuan.staging.common.annonation.jwt.PassToken;
import com.ayuan.staging.entity.vo.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sYuan
 * @Description 用户登录接口
 * @Version test
 */
@RestController
public class LoginController {

    @PassToken
    @PostMapping("publicKey")
    public JSONObject publicKey(){

        System.out.println("1");

        Map<String,Object> map = new HashMap<>(2);
        map.put("mockServer",false);
        map.put("publicKey","MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDBT2vr+dhZElF73FJ6xiP181txKWUSNLPQQlid6DUJhGAOZblluafIdLmnUyKE8mMHhT3R+Ib3ssZcJku6Hn72yHYj/qPkCGFv0eFo7G+GJfDIUeDyalBN0QsuiE/XzPHJBuJDfRArOiWvH0BXOv5kpeXSXM8yTt5Na1jAYSiQ/wIDAQAB");

        return ResponseEntity.ok(map);

    }


}
