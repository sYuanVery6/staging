package com.ayuan.staging.service.impl;

import com.ayuan.staging.dao.UserDao;
import com.ayuan.staging.entity.po.User;
import com.ayuan.staging.entity.vo.RequestEntity;
import com.ayuan.staging.service.UserService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author sYuan
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public List<User> getUser(RequestEntity requestEntity) {

        Integer pageNum = requestEntity.getPageNum();
        Integer pageSize = requestEntity.getPageSize();

        Page<User> pageInfo = PageHelper.startPage(pageNum,pageSize);

        return userDao.getAllUserPage();
    }

    @Override
    public User getUserById(String id) {
        return userDao.getUserById(id);
    }

    @Override
    public void addUser(User user) {
        userDao.addUser(user);
    }

    @Override
    public void updateUser(User user) {
        userDao.updateUser(user);
    }
}
