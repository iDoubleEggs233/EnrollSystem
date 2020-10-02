package com.edu.ccnu.app.demo.service;

import com.edu.ccnu.app.demo.pojo.UserActivity;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * created by IntelliJ Idea
 *
 * @Author: Anakin
 * @Date: 2020/09/23/10:59
 * @Description:
 */
public interface UserActivityService {

    /**
    * @Description: 根据活动的id获取报名的用户数量
    * @Param: [activityId]
    * @return: java.lang.Integer
    * @Author: Anakin
    * @Date: 2020/9/23
    */
    public List<UserActivity> getUserActivityByActivityId(String activityId);

    /**
    * @Description: 根据用户的id获取该用户报名的所有的活动信息
    * @Param: [userId]
    * @return: java.util.List<com.edu.ccnu.app.demo.pojo.UserActivity>
    * @Author: Anakin
    * @Date: 2020/9/23
    */
    public List<UserActivity> getActivityByUserId(String userId);

    /**
    * @Description: 根据用户id获取该用户报名的所有活动信息，并分页显示
    * @Param: [pageNum, pageSize]
    * @return: com.github.pagehelper.PageInfo<com.edu.ccnu.app.demo.pojo.UserActivity>
    * @Author: Anakin
    * @Date: 2020/9/23
    */
    public PageInfo<UserActivity> getActivityByUserIdByPage(Integer pageNum, Integer pageSize,String userId);

    /**
    * @Description: 插入一条用户报名的信息
    * @Param: [userActivity]
    * @return: java.lang.Integer
    * @Author: Anakin
    * @Date: 2020/9/23
    */
    public String insertUserActivity(UserActivity userActivity);

    /**
    * @Description: 根据用户id，删除该用户相关的所有报名信息
    * @Param: [userId]
    * @return: java.lang.Integer
    * @Author: Anakin
    * @Date: 2020/9/23
    */
    public Integer deleteUserActivityByUserId(String userId);

    /**
    * @Description: 根据活动id删除所有该活动相关的报名信息
    * @Param: [activityId]
    * @return: java.lang.Integer
    * @Author: Anakin
    * @Date: 2020/9/23
    */
    public Integer deleteUserActivityByActivityId(String activityId);

    /**
     * 检查当前用户在该活动下是否进行了签到
     * @param userId
     * @param activityId
     * @return
     */
    public boolean isRegistered(String userId,String activityId);

    /**
     * @Description: 用户进行签到，即修改用户在该活动下的签到状态值
     * @Param: [userId, activityId]
     * @return: java.lang.Integer
     * @Author: Anakin
     * @Date: 2020/9/29
     */
    public Integer register(String userId,String activityId);

    /**
    * @Description: 根据用户的id和活动的id，得到该报名的具体信息
    * @Param: [userId, activityId]
    * @return: com.edu.ccnu.app.demo.pojo.UserActivity
    * @Author: Anakin
    * @Date: 2020/10/1
    */
    public UserActivity getUserActivityByUserIdAndActivityId(String userId,String activityId);

}
