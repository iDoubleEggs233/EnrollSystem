package com.edu.ccnu.app.demo.service.impl;

import com.edu.ccnu.app.demo.mapper.UserMapper;
import com.edu.ccnu.app.demo.pojo.User;
import com.edu.ccnu.app.demo.service.UserService;
import com.edu.ccnu.app.demo.util.RedisUtils;
import com.edu.ccnu.app.demo.util.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * created by IntelliJ Idea
 *
 * @Author: Anakin
 * @Date: 2020/09/23/10:15
 * @Description:
 */
@Service
@CacheConfig(cacheNames = "userCache")
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    @Cacheable(value = "users",key = "allUser")
    public List<User> findAllUser() {
        return userMapper.getUserByPage();
    }

    @Override
    @Cacheable(value = "users",key = "#pageNum+'-'+#pageSize")
    public PageInfo<User> findUserByPage(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<User> users = userMapper.getUserByPage();
        PageInfo<User> pageInfo = new PageInfo<User>(users);
        return pageInfo;
    }

    @Override
    @Cacheable(value = "users",key = "#userId")
    public User getUserById(String userId) {
        return userMapper.getUserById(userId);
    }

    @Override
    @CachePut(value = "users",key = "#user.userId")
    public User insertUser(User user) {
        String userId = userMapper.insertUser(user);
        log.info("当前插入的用户id为: %s" , userId);
        return user;
    }

    @Override
    @CachePut(value = "users",key = "#user.userId")
    public User updateUser(User user) {
        userMapper.updateUser(user);
        log.info("修改后的用户信息为: %s",user.toString());
        return user;
    }

    @Override
    @CacheEvict(value = "users",key = "userId")
    public Integer deleteUserById(String userId) {
        log.info("当前删除的用户的id为: %s", userId);
        return userMapper.deleteUserById(userId);
    }

    @Override
    public Integer deleteUserByIds(String ids) {
        String[] idArray = StringUtils.toStrArray(ids);
        log.info("当前批量删除的用户id有: %s" ,ids);
        RedisUtils.del(idArray);
        return userMapper.deleteUserByIds(StringUtils.toStrArray(ids));
    }


}
