package com.edu.ccnu.app.demo.service;

import com.edu.ccnu.app.demo.pojo.User;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * created by IntelliJ Idea
 *
 * @Author: Anakin
 * @Date: 2020/09/23/9:55
 * @Description: 用户的服务层接口
 */
public interface UserService {
    
    /**
    * @Description: 获得所有的用户信息
    * @Param: []
    * @return: java.util.List<com.edu.ccnu.app.demo.pojo.User>
    * @Author: Anakin
    * @Date: 2020/9/23
    */
    public List<User> findAllUser();
    
    /**
    * @Description: 分页查看用户的信息（使用pageHelper插件）
    * @Param: [pageNum, pageSize]
    * @return: java.util.List<com.edu.ccnu.app.demo.pojo.User>
    * @Author: Anakin
    * @Date: 2020/9/23
    */
    public PageInfo<User> findUserByPage(Integer pageNum, Integer pageSize);
    
    /**
    * @Description: 根据用户的id来获取用户的信息
    * @Param: [userId]
    * @return: com.edu.ccnu.app.demo.pojo.User
    * @Author: Anakin
    * @Date: 2020/9/23
    */
    public User getUserById(String userId);
    
    /**
    * @Description: 插入一个新的用户
    * @Param: [user]
    * @return: java.lang.Integer
    * @Author: Anakin
    * @Date: 2020/9/23
    */
    public User insertUser(User user);
    
    /**
    * @Description: 更新用户的信息
    * @Param: [user]
    * @return: java.lang.Integer
    * @Author: Anakin
    * @Date: 2020/9/23
    */
    public User updateUser(User user);
    
    /**
    * @Description: 根据id删除一个用户
    * @Param: [userId]
    * @return: java.lang.Integer
    * @Author: Anakin
    * @Date: 2020/9/23
    */
    public Integer deleteUserById(String userId);
    
    /**
    * @Description: 根据多个id批量删除用户（对字符串进行一次解析）
    * @Param: [ids]
    * @return: java.lang.Integer
    * @Author: Anakin
    * @Date: 2020/9/23
    */
    public Integer deleteUserByIds(String ids);





}
