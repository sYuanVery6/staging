package com.ayuan.staging.common.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ayuan.staging.entity.po.User;

import java.util.Date;
import java.util.HashMap;

/**
 * JWT 工具类
 * @author sYuan
 */
public class JwtUtil {

    /**
     * 设置过期时间（1天）
     */
    private static final long EXPIRE_TIME = 24*60*60*1000;

    /**
     * token 私钥
     */
    private static final String TOKEN_SECRET = "ghjytfchfgfghcvb7575754323";

    /**
     * 生成token签名
     * @param id 用户id
     * @param userName 用户名
     * @return token
     */
    public static String sign(String userName,String id){

        //过期时间
        Date date = new Date(System.currentTimeMillis()+EXPIRE_TIME);
        //设置加密算法
        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);

        HashMap<String,Object> header = new HashMap<>(2);
        header.put("typ","JWT");
        header.put("alg","HS256");

        return JWT.create()
                .withHeader(header)
                .withClaim("userId",id)
                .withClaim("userName",userName)
                .withExpiresAt(date)
                .sign(algorithm);
    }

    public static String getToken(User user){
        String token = "";
        //过期时间
        Date date = new Date(System.currentTimeMillis()+EXPIRE_TIME);
        token = JWT.create()
                .withAudience(user.getId())
                .withExpiresAt(date)
                .sign(Algorithm.HMAC256(user.getPassWord()));

        return token;
    }


}
