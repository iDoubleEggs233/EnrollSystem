package com.edu.ccnu.app.demo.mapper;

import com.edu.ccnu.app.demo.pojo.Activity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * created by IntelliJ Idea
 *
 * @Author: Anakin
 * @Date: 2020/09/22/20:51
 * @Description: 对于活动类的数据接口
 */
@Repository
public interface ActivityMapper {

    List<Activity> getAllActivity();

    /**
    * @Description: 分页获取所有的活动信息
    * @Param: []
    * @return: java.util.List<com.edu.ccnu.app.demo.pojo.Activity>
    * @Author: Anakin
    * @Date: 2020/9/22
    */
    List<Activity> getActivityByPage();

    /**
    * @Description: 根据活动的id获得活动的具体信息
    * @Param: [activityId]
    * @return: com.edu.ccnu.app.demo.pojo.Activity
    * @Author: Anakin
    * @Date: 2020/9/22
    */
    Activity getActivityById(String activityId);
    
    /**
    * @Description: 插入一条活动信息
    * @Param: [activity]
    * @return: java.lang.Integer
    * @Author: Anakin
    * @Date: 2020/9/22
    */
    String insertActivity(Activity activity);
    
    /**
    * @Description: 更新一条活动的信息
    * @Param: [activity]
    * @return: java.lang.Integer
    * @Author: Anakin
    * @Date: 2020/9/22
    */
    Integer updateActivity(Activity activity);
    
    /**
    * @Description: 根据活动id对Activity进行删除
    * @Param: [activityId]
    * @return: java.lang.Integer
    * @Author: Anakin
    * @Date: 2020/9/22
    */
    Integer deleteActivityById(String activityId);
    
    /**
    * @Description: 根据活动的id进行批量删除
    * @Param: [activityIds]
    * @return: java.lang.Integer
    * @Author: Anakin
    * @Date: 2020/9/22
    */
    Integer deleteActivityByIds(String[] activityIds);

    

}
