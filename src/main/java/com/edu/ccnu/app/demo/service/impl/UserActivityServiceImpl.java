package com.edu.ccnu.app.demo.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.edu.ccnu.app.demo.mapper.UserActivityMapper;
import com.edu.ccnu.app.demo.pojo.Activity;
import com.edu.ccnu.app.demo.pojo.UserActivity;
import com.edu.ccnu.app.demo.service.UserActivityService;
import com.edu.ccnu.app.demo.util.RedisUtils;
import com.edu.ccnu.app.demo.util.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * created by IntelliJ Idea
 *
 * @Author: Anakin
 * @Date: 2020/09/23/11:14
 * @Description:
 */
@Service
@Slf4j
@CacheConfig(cacheNames = "userActivityCache")
public class UserActivityServiceImpl implements UserActivityService {

    @Autowired
    private UserActivityMapper userActivityMapper;

    @Autowired
    @Qualifier("myRedisTemplate")
    private RedisTemplate redisTemplate;

    @Override
    public List<UserActivity> getUserActivityByActivityId(String activityId) {
        // 创建列表，用于返回数据
        List<UserActivity> userActivities = new ArrayList<>();
        String result = RedisUtils.get(activityId).toString();
        String idsInfo = "";
        if(result.equals(null)){
            log.info("走的是数据库，读取数据之后存入缓存");
            userActivities = userActivityMapper.getUserActivityByActivityId(activityId);
            for (UserActivity userActivity: userActivities) {
                idsInfo.concat(userActivity.getActivityId() + ",");
            }
            // 将查询得到的列表信息转化为JSON字符串
            result = JSON.toJSONString(userActivities);
            // 将该JSON字符串放入redis的缓存数据中
            RedisUtils.set(activityId,result);
            idsInfo.substring(0,result.length()-1);
            log.info("该活动下报名的所有用户的id信息为: %s",idsInfo);
            return userActivities;
        }else{
            // 此时代表只需要查询缓存即可
            log.info("走的是缓存，无需访问数据库，如果需要返回值，从缓存当中获取,得到的缓存数据为:%s",result);
            userActivities = JSONObject.parseArray(result,UserActivity.class);
            return userActivities;
        }

    }

    @Override
    public List<UserActivity> getActivityByUserId(String userId) {
        // 创建用户与活动的关联表，用于返回
        List<UserActivity> userActivities = new ArrayList<>();
        String idsInfo = "";
        String result = RedisUtils.get(userId).toString();
        // 若缓存中无数据，则将该缓存的所有数据写入
        if(result.equals(null)){
            log.info("走的是数据库，读取数据之后存入缓存");
            userActivities = userActivityMapper.getUserActivityByUserId(userId);
            for (UserActivity userActivity: userActivities) {
                idsInfo.concat(userActivity.getActivityId() + ",");
            }
            // 将查询得到的列表信息转化为JSON字符串
            result = JSON.toJSON(userActivities).toString();
            // 将该json字符串放入redis的缓存中
            RedisUtils.set(userId,result);
            idsInfo.substring(0,idsInfo.length()-1);
            log.info("该用户报名的所有活动的id信息: %s",idsInfo);
            return userActivities;
        }else{
            // 此时查询数据只需要走缓存即可
            log.info("走的是缓存，无需访问数据库，如果需要返回值，从缓存当中获取,得到的缓存数据为:%s",result);
            userActivities = JSONObject.parseArray(result,UserActivity.class);
            return userActivities;
        }
    }

    @Override
    @Cacheable(value = "userActivities",key = "#pageNum+'-'+#pageSize+'-'+#userId")
    public PageInfo<UserActivity> getActivityByUserIdByPage(Integer pageNum, Integer pageSize,String userId) {
        PageHelper.startPage(pageNum,pageSize);
        List<UserActivity> userActivities = userActivityMapper.getUserActivityByUserId(userId);
        PageInfo<UserActivity> pageInfo = new PageInfo<UserActivity>(userActivities);
        return pageInfo;
    }

    @Override
    public String insertUserActivity(UserActivity userActivity) {

        // 更新用户报名的所有活动的缓存
        String userCache = RedisUtils.get(userActivity.getUserId()).toString();
        List<UserActivity> listOfUserCache = JSON.parseArray(userCache,UserActivity.class);
        listOfUserCache.add(userActivity);
        RedisUtils.set(userActivity.getUserId(),JSON.toJSONString(listOfUserCache));

        // 更新活动下所有用户报名信息的缓存
        String activityCache = RedisUtils.get(userActivity.getActivityId()).toString();
        List<UserActivity> listOfActivityCache = JSON.parseArray(activityCache,UserActivity.class);
        listOfActivityCache.add(userActivity);
        RedisUtils.set(userActivity.getActivityId(),JSON.toJSONString(listOfActivityCache));

        return userActivityMapper.insertUserActivity(userActivity);
    }

    @Override
    public Integer deleteUserActivityByUserId(String userId) {

        // 将该用户id下属的所有缓存删除
        RedisUtils.del(userId);
        return userActivityMapper.deleteUserActivityByUserId(userId);
    }

    @Override
    public Integer deleteUserActivityByActivityId(String activityId) {

        // 将该活动id下属的所有缓存删除
        RedisUtils.del(activityId);
        return userActivityMapper.deleteUserActivityByActivityId(activityId);
    }

    @Override
    public boolean isRegistered(String userId, String activityId) {

        // 返回值
        boolean isRegistered = false;
        // 得到该用户id下所有活动的缓存信息
        String userCache = RedisUtils.get(userId).toString();
        List<UserActivity> listOfUserCache = JSON.parseArray(userCache,UserActivity.class);
        for (UserActivity userActivity: listOfUserCache) {
            if (userActivity.getActivityId().equals(activityId)){
                return userActivity.getUserActivityIsRegistered() == 1 ? true : false;
            }
        }
        return userActivityMapper.getUserActivityByUserIdAndActivityId(userId,activityId).getUserActivityIsRegistered() == 1 ? true : false;
    }

    @Override
    public Integer register(String userId, String activityId) {

        int index = userActivityMapper.updateUserActivityRegister(userId,activityId);
        // 先对id为用户id的缓存进行修改
        // 获取该用户id对应的缓存
        String userCache = RedisUtils.get(userId).toString();
        // 先将需要修改的缓存删除
        RedisUtils.del(userId);
        // 从缓存中解析得到报名信息列表
        List<UserActivity> listOfUserCache = JSON.parseArray(userCache,UserActivity.class);
        for (UserActivity userActivity:listOfUserCache) {
            if(userActivity.getActivityId().equals(activityId)){
                // 将指定用户及活动的注册状态进行修改
                userActivity.setUserActivityIsRegistered(1);
                break;
            }
        }
        // 将修改后的列表转换为可以存储的JSON字符串
        String newUserCache = JSON.toJSONString(listOfUserCache);
        // 将进行过修改的JSON字符串放入缓存，id依然是原来的userId
        RedisUtils.set(userId,newUserCache);

        // 接下来对id为活动id的缓存进行修改
        String activityCache = RedisUtils.get(activityId).toString();
        RedisUtils.del(activityId);
        List<UserActivity> listOfActivityCache = JSON.parseArray(activityCache,UserActivity.class);
        for (UserActivity userActivity:listOfActivityCache) {
            if(userActivity.getUserId().equals(userId)){
                // 将指定用户及活动的注册状态进行修改
                userActivity.setUserActivityIsRegistered(1);
                break;
            }
        }
        String newActivityCache = JSON.toJSONString(listOfActivityCache);
        RedisUtils.set(activityId,newActivityCache);

        return index;
    }

    @Override
    public UserActivity getUserActivityByUserIdAndActivityId(String userId, String activityId) {

        // 从key为userId的缓存中找数据，不必从key为activityId的缓存中找，因为插入的时候两份缓存都有写入，查询其中一种即可
        String userCache = RedisUtils.get(userId).toString();
        if(userCache.equals(null)){
            log.info("走的是数据库，从数据库查询到数据之后再将数据写入缓存");
            UserActivity userActivity = userActivityMapper.getUserActivityByUserIdAndActivityId(userId,activityId);
            userCache = JSON.toJSONString(userActivity);
            RedisUtils.set(userId,userActivity);
            RedisUtils.set(activityId,userActivity);
            log.info("插入的活动报名信息为: %s",userActivity.toString());
            return userActivity;
        }else{
            log.info("走的是缓存，无需查询缓存");
            List<UserActivity> listOfUserActivity = JSONObject.parseArray(userCache,UserActivity.class);
            for (UserActivity userActivity:listOfUserActivity) {
                if(userActivity.getActivityId().equals(activityId)){
                    return userActivity;
                }
            }
            return null;
        }
    }
}
