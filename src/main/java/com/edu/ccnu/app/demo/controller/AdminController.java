package com.edu.ccnu.app.demo.controller;

import com.edu.ccnu.app.demo.pojo.Activity;
import com.edu.ccnu.app.demo.service.ActivityService;
import com.edu.ccnu.app.demo.service.UserActivityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * created by IntelliJ Idea
 *
 * @Author: Anakin
 * @Date: 2020/10/01/11:39
 * @Description: 管理员界面的功能实现
 */
@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private UserActivityService userActivityService;

    /**
     * 管理员添加活动的功能
     * @param activityName：活动名称
     * @param activityEnrollStartTime：活动报名开始时间
     * @param activityEnrollStopTime：活动报名结束时间
     * @param activityTime：活动开始时间
     * @param activityRegisterTime：活动签到开始时间
     * @param activityRegisterType：活动签到方式
     * @param activityContent：活动内容摘要
     * @param activitySponsorId：活动赞助人的id(其实就是当前管理员的id，附加在当前页面的url中)
     * @return
     */
    @RequestMapping(value = "/insertActivity",method = {RequestMethod.POST})
    public Map<String,String> addActivity(@RequestParam("activityName")String activityName, @RequestParam("activityEnrollStartTime")String activityEnrollStartTime,
                                          @RequestParam("activityEnrollStopTime")String activityEnrollStopTime,@RequestParam("activityTime")String activityTime,
                                          @RequestParam("activityRegisterTime")String activityRegisterTime, @RequestParam("activityRegisterType")String activityRegisterType,
                                          @RequestParam("activitySchool")String activitySchool,@RequestParam("activityBuilding")String activityBuilding,
                                          @RequestParam("activityRoom")String activityRoom,@RequestParam("activityContent")String activityContent,
                                          @RequestParam("activitySponsorId")String activitySponsorId,@RequestParam("activityMaxSize")String activityMaxSize,
                                          @RequestParam("activityStopTime")String activityStopTime,@RequestParam("activityType")String activityType) throws ParseException {

        Map<String,String> map = new HashMap<>();
        Activity activity = new Activity();
        // 获取当前活动的数量
        int count = activityService.getAllActivity().size() + 1;
        // 定义活动id格式为:Activity+当前活动数量加一-发起人id
        String activityId = "Activity" + count + "-" + activitySponsorId;
        activity.setActivityId(activityId);
        activity.setActivityName(activityName);
        // 设置活动地点,确保学校-楼栋-房间的表示方法
        String activityLocation = activitySchool+"-"+activityBuilding+"-"+activityRoom;
        activity.setActivityLocation(activityLocation);

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        // 解析活动报名开始时间，将字符串转换为Date类型数据
        Date myActivityEnrollStartTime = format.parse(activityEnrollStartTime);
        // 将活动活动报名开始时间存入对象
        activity.setActivityEnrollTime(myActivityEnrollStartTime);
        // 同时解析活动报名结束时间
        Date myActivityEnrollStopTime = format.parse(activityEnrollStopTime);
        // 计算两者时间差，得到报名持续时间
        long activityRegisterDuration = myActivityEnrollStopTime.getTime() - myActivityEnrollStartTime.getTime();
        // 将活动持续时间存入数据库
        activity.setActivityRegisterDuration(activityRegisterDuration);
        // 创建活动的时候，活动的其实状态为未开始报名的0
        activity.setActivityState(0);
        // 设置活动最多容纳的人数,将传递来的String字符串转化为int类型数据
        activity.setActivityMaxSize(Integer.parseInt(activityMaxSize));
        // 解析活动签到时间
        Date myActivityRegisterTime = format.parse(activityRegisterTime);
        // 将解析得到的时间放入活动实体
        activity.setActivityRegisterTime(myActivityRegisterTime);
        // 将活动签到方式放入活动实体
        activity.setActivityRegisterMode(Integer.parseInt(activityRegisterType));
        // 将活动内容摘要放入活动实体中
        activity.setActivityContent(activityContent);

        // 解析前端传回的活动开始时间字符串
        Date myActivityTime = format.parse(activityTime);
        // 解析前端传回的活动结束时间字符串
        Date myActivityStopTime = format.parse(activityStopTime);
        // 计算得到时间差，得到活动持续时间
        long activityDuration = myActivityStopTime.getTime() - myActivityTime.getTime();
        // 将活动持续时间存入活动实体
        activity.setActivityDuration(activityDuration);
        // 将活动类型存入活动实体
        activity.setActivityType(Integer.parseInt(activityType));
        // 将活动发起人(即当前进入页面的管理员的id)
        activity.setActivitySponsorId(activitySponsorId);

        try {
            activityService.insertActivity(activity);
            map.put("sign","success");
        }catch (Exception e){
            map.put("sign","fail");
        }
        // 返回状态值
        return map;
    }

    @RequestMapping(value = "deleteActivity",method = RequestMethod.POST)
    public Map<String,String> deleteActivity(@RequestParam("activityId")String activityId){

        Map<String,String> map = new HashMap<>();
        try {
            userActivityService.deleteUserActivityByActivityId(activityId);
            activityService.deleteActivityById(activityId);
            map.put("sign","success");
        }catch (Exception e){
            map.put("sign","fail");
        }
        return map;
    }
}
