package com.ayuan.staging.service;

import com.ayuan.staging.entity.po.User;
import com.ayuan.staging.entity.vo.RequestEntity;

import java.util.List;

public interface UserService {

    List<User> getUser(RequestEntity requestParam);

    User getUserById(String id);

    void addUser(User user);

}
