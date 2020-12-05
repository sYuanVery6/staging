package com.ayuan.staging.entity.vo;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 统一封装响应结果并返回
 *
 * @author sYuan
 */
public class ResponseEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    public String code;

    public Map<String, Object> data;

    public String message;

    public static JSONObject ok(Map<String,Object> data) {
        ResponseEntity resp =  new ResponseEntity(data);
        resp.setCode("200");
        JSONObject json = (JSONObject) JSONObject.toJSON(resp);

        return json;
    }

    public static JSONObject fail(Map<String,Object> data, String message, String code){
        ResponseEntity resp =  new ResponseEntity(data);
        resp.setCode(code);
        resp.setMessage(message);
        JSONObject json = (JSONObject) JSONObject.toJSON(resp);

        return json;
    }

    public static JSONObject fail(String message, String code){
        ResponseEntity resp =  new ResponseEntity();
        resp.setCode(code);
        resp.setMessage(message);
        JSONObject json = (JSONObject) JSONObject.toJSON(resp);

        return json;
    }

    public ResponseEntity(Map<String, Object> data) {
        this.data = data;
    }

    public ResponseEntity() {
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
