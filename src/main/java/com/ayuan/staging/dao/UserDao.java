package com.ayuan.staging.dao;

import com.ayuan.staging.entity.po.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author sYuan
 */
@Component
public interface UserDao {

    /**
     * 返回所有用户
     * @return alluser
     */
    List<User> getAllUser();


    /**
     * 分页返回用户
     * @return getPage
     */
    List<User> getAllUserPage();

    /**
     * 根据id获取User
     * @return user
     */
    User getUserById(@Param("id") String userId);

}
