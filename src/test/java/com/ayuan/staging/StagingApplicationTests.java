package com.ayuan.staging;

import com.ayuan.staging.dao.UserDao;
import com.ayuan.staging.entity.po.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class StagingApplicationTests {


    @Autowired
    private UserDao userDao;

    @Test
    public void contextLoads() {

        List<User> list = userDao.getAllUser();
        System.out.println(list);
    }

    @Test
    public void getUser(){


        System.out.println(Integer.MAX_VALUE);
//        User user = userDao.getUserById("1");
//        System.out.println(user);

    }



}
