package com.ayuan.staging;

import com.ayuan.staging.dao.UserDao;
import com.ayuan.staging.entity.po.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class StagingApplicationTests {


    @Autowired
    private UserDao userDao;

    @Test
    public void contextLoads() {

        List<User> list = userDao.getAllUser();
        System.out.println(list);
    }

}
