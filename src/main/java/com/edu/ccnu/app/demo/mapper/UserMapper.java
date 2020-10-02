package com.edu.ccnu.app.demo.mapper;


import com.edu.ccnu.app.demo.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * created by IntelliJ Idea
 *
 * @Author: Anakin
 * @Date: 2020/09/22/18:50
 * @Description: 用户的数据接口
 */

@Repository
public interface UserMapper {
    
    /**
    * @Description: 分页获得所有用户的信息
    * @Param: []
    * @return: java.util.List<com.edu.ccnu.app.demo.pojo.User>
    * @Author: Anakin
    * @Date: 2020/9/22
    */
    List<User> getUserByPage();

    // 根据用户id查询用户信息
    User getUserById(String userId);

    // 插入一个新的用户
    String insertUser(User user);

    // 根据id删除一个用户
    Integer deleteUserById(String userId);

    // 批量删除
    Integer deleteUserByIds(String[] ids);

    // 更新用户信息
    Integer updateUser(User user);


}
