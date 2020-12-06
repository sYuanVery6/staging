package com.ayuan.staging.entity.po;

import com.ayuan.staging.common.annonation.mybatis.AutoDate;
import com.ayuan.staging.common.annonation.mybatis.AutoId;

import java.util.Date;

/**
 * User实体类
 * @author sYuan
 */
@AutoId(name = "id")
@AutoDate
public class User {

    private String id;
    private String userName;
    private String passWord;
    private Date createTime;
    private Date updateTime;

    public User() {
    }

    public User(String id, String userName, String passWord, Date createTime, Date updateTime) {
        this.id = id;
        this.userName = userName;
        this.passWord = passWord;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", userName='" + userName + '\'' +
                ", passWord='" + passWord + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
}
