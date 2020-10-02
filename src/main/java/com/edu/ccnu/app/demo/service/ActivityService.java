package com.edu.ccnu.app.demo.service;

import com.edu.ccnu.app.demo.pojo.Activity;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * created by IntelliJ Idea
 *
 * @Author: Anakin
 * @Date: 2020/09/23/10:34
 * @Description:
 */
public interface ActivityService {

    /**
    * @Description: 获得所有的活动信息
    * @Param: []
    * @return: java.util.List<com.edu.ccnu.app.demo.pojo.Activity>
    * @Author: Anakin
    * @Date: 2020/9/23
    */
    public List<Activity> getAllActivity();

    /**
    * @Description: 分页获得活动的信息
    * @Param: []
    * @return: com.github.pagehelper.PageInfo<com.edu.ccnu.app.demo.pojo.Activity>
    * @Author: Anakin
    * @Date: 2020/9/23
    */
    public PageInfo<Activity> getActivityByPage(Integer pageNum, Integer pageSize);

    /**
    * @Description: 根据id获得活动的信息
    * @Param: [activityId]
    * @return: com.edu.ccnu.app.demo.pojo.Activity
    * @Author: Anakin
    * @Date: 2020/9/23
    */
    public Activity getActivityById(String activityId);

    /**
    * @Description: 插入一条活动的信息
    * @Param: [activity]
    * @return: java.lang.Integer
    * @Author: Anakin
    * @Date: 2020/9/23
    */
    public Activity insertActivity(Activity activity);

    /**
    * @Description: 更新一条活动的信息
    * @Param: [activity]
    * @return: java.lang.Integer
    * @Author: Anakin
    * @Date: 2020/9/23
    */
    public Integer updateActivity(Activity activity);

    /**
    * @Description: 根据id删除一条活动信息
    * @Param: [activityId]
    * @return: java.lang.Integer
    * @Author: Anakin
    * @Date: 2020/9/23
    */
    public Integer deleteActivityById(String activityId);

    /**
    * @Description: 根据一组id对活动进行批量删除
    * @Param: [ids]
    * @return: java.lang.Integer
    * @Author: Anakin
    * @Date: 2020/9/23
    */
    public Integer deleteActivityByIds(String ids);

    /**
     * 执行定时任务，做到更新活动状态信息
     */
    void updateActivityState();


}
