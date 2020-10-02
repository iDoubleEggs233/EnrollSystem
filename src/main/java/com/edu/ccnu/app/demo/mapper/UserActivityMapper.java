package com.edu.ccnu.app.demo.mapper;

import com.edu.ccnu.app.demo.pojo.UserActivity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * created by IntelliJ Idea
 *
 * @Author: Anakin
 * @Date: 2020/09/23/8:52
 * @Description: 用户活动对应实体的数据接口
 */
@Repository
public interface UserActivityMapper {
    
    /**
    * @Description: 根据用户id，获得该用户已报名的所有活动
    * @Param: [userId]
    * @return: java.util.List<com.edu.ccnu.app.demo.pojo.UserActivity>
    * @Author: Anakin
    * @Date: 2020/9/23
    */
    List<UserActivity> getUserActivityByUserId(String userId);
    
    /**
    * @Description: 根据活动id，获得该活动报名的人数
    * @Param: [activityId]
    * @return: java.util.List<com.edu.ccnu.app.demo.pojo.UserActivity>
    * @Author: Anakin
    * @Date: 2020/9/23
    */
    List<UserActivity> getUserActivityByActivityId(String activityId);

    /**
     * 根据用户id和活动id获取用户报名的具体信息
     * @param userId
     * @param activityId
     * @return
     */
    UserActivity getUserActivityByUserIdAndActivityId(String userId,String activityId);

    /**
    * @Description: 插入一条用户报名的信息
    * @Param: [userActivity]
    * @return: java.lang.Integer
    * @Author: Anakin
    * @Date: 2020/9/23
    */
    String insertUserActivity(UserActivity userActivity);

    /**
    * @Description: 删除该用户的报名信息
    * @Param: [userId]
    * @return: java.lang.Integer
    * @Author: Anakin
    * @Date: 2020/9/23
    */
    Integer deleteUserActivityByUserId(String userId);

    /**
    * @Description: 删除该活动相关的所有报名信息
    * @Param: [activityId]
    * @return: java.lang.Integer
    * @Author: Anakin
    * @Date: 2020/9/23
    */
    Integer deleteUserActivityByActivityId(String activityId);

    /**
     * 根据用户id已经活动id修改该用户在该活动下的签到状态
     * @param userId
     * @param activityId
     * @return
     */
    Integer updateUserActivityRegister(String userId,String activityId);
}
