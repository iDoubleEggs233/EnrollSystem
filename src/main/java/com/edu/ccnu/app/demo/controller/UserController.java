package com.edu.ccnu.app.demo.controller;

import com.edu.ccnu.app.demo.pojo.Activity;
import com.edu.ccnu.app.demo.pojo.UserActivity;
import com.edu.ccnu.app.demo.service.ActivityService;
import com.edu.ccnu.app.demo.service.UserActivityService;
import com.edu.ccnu.app.demo.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.DecimalFormat;
import java.util.*;

/**
 * created by IntelliJ Idea
 *
 * @Author: Anakin
 * @Date: 2020/10/01/23:24
 * @Description: C端用户功能实现
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private UserActivityService userActivityService;

    /**
     * 展示所有的活动信息
     * @param pageNum:当前页数
     * @param pageSize:一页能容纳的记录数量
     * @return:页面信息
     */
    @RequestMapping(value = "/showAllActivities",method = RequestMethod.POST)
    public Map<String,Object> showAllActivities(@RequestParam(defaultValue = "1",value = "pageNum")Integer pageNum,
                                                @RequestParam(defaultValue = "5",value = "pageSize")Integer pageSize){

        Map<String,Object> map = new HashMap<>();
        // 分页获得活动信息
        PageInfo<Activity> pageInfo = activityService.getActivityByPage(pageNum,pageSize);
        map.put("pageInfo",pageInfo);
        return map;
    }

    /**
     * 显示用户当前所有可报名的活动信息
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/showUserActivitiesForEnroll",method = RequestMethod.POST)
    public Map<String,Object> showUserActivityForEnroll(@RequestParam(defaultValue = "1",value = "pageNum")Integer pageNum,
                                                       @RequestParam(defaultValue = "5",value = "pageSize")Integer pageSize){

        Map<String,Object> map = new HashMap<>();
        // 先获得所有的活动信息
        List<Activity> listOfAllActivity = activityService.getAllActivity();
        List<Activity> listOfActivityForEnroll = new ArrayList<>();
        for (Activity activity:listOfAllActivity) {
            if(activity.getActivityState() == 1){
                listOfActivityForEnroll.add(activity);
            }
        }
        PageHelper.startPage(pageNum,pageSize);
        Collections.sort(listOfActivityForEnroll);
        PageInfo<Activity> pageInfo = new PageInfo<>(listOfActivityForEnroll);
        map.put("pageInfo",pageInfo);
        return map;
    }

    /**
     * 展示当前用户选择的活动信息列表,默认排序顺序为时间由远及近顺序
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/showUserSelectActivities",method = RequestMethod.POST)
    public Map<String,Object> showUserSelectActivities(@RequestParam(defaultValue = "1",value = "pageNum")Integer pageNum,
                                                       @RequestParam(defaultValue = "5",value = "pageSize")Integer pageSize,
                                                       @RequestParam("userId")String userId){

        Map<String,Object> map = new HashMap<>();
        // 根据返回的userId,获得该用户报名的活动的id
        List<UserActivity> listOfUserActivity = userActivityService.getActivityByUserId(userId);
        // 根据列表中的活动id，获得该用户报名下的所有活动的具体信息
        List<Activity> listOfActivity = new ArrayList<>();
        for (UserActivity userActivity:listOfUserActivity) {
            String activityId = userActivity.getActivityId();
            Activity activity = activityService.getActivityById(activityId);
            listOfActivity.add(activity);
        }
        PageHelper.startPage(pageNum,pageSize);
        Collections.sort(listOfActivity);
        PageInfo<Activity> pageInfo = new PageInfo<>(listOfActivity);
        map.put("pageInfo",pageInfo);
        return map;

    }

    /**
     * 根据活动类型，返回当前用户已经报名的活动
     * @param pageNum:活动类型
     * @param pageSize
     * @param userId
     * @param activityType
     * @return
     */
    @RequestMapping(value = "/showUserTypeActivities",method = RequestMethod.POST)
    public Map<String,Object> showUserTypeActivities(@RequestParam(defaultValue = "1",value = "pageNum")Integer pageNum,
                                                     @RequestParam(defaultValue = "5",value = "pageSize")Integer pageSize,
                                                     @RequestParam("userId")String userId,@RequestParam("activityType")Integer activityType){

        Map<String,Object> map = new HashMap<>();
        // 根据返回的userId,获得该用户报名的活动的id
        List<UserActivity> listOfUserActivity = userActivityService.getActivityByUserId(userId);
        List<Activity> listOfActivity = new ArrayList<>();
        for(UserActivity userActivity:listOfUserActivity){
            String activityId = userActivity.getActivityId();
            Activity activity = activityService.getActivityById(activityId);
            if (activity.getActivityType() == activityType){
                listOfActivity.add(activity);
            }
        }
        PageHelper.startPage(pageNum,pageSize);
        Collections.sort(listOfActivity);
        PageInfo<Activity> pageInfo = new PageInfo<>(listOfActivity);
        map.put("pageInfo",pageInfo);
        return map;
    }

    /**
     * 用户报名的实现
     * @param userId
     * @param activityId
     * @return
     */
    @RequestMapping(value = "/userEnrollActivity",method = RequestMethod.POST)
    public Map<String,String> userEnrollActivity(@RequestParam("userId")String userId,@RequestParam("activityId")String activityId){

        Map<String,String> map = new HashMap<>();
        UserActivity userActivity = new UserActivity();
        // 先获取当前该activityId报名的用户数量
        List<UserActivity> listOfUserActivity = userActivityService.getUserActivityByActivityId(activityId);
        int userNum = listOfUserActivity.size();
        userNum = userNum + 1;
        // 获取该activityId对应的Activity具体信息
        Activity activity = activityService.getActivityById(activityId);
        int activityMaxSize = activity.getActivityMaxSize();
        if (userNum > activityMaxSize){
            map.put("sign","full");
        }else{
            float propertion = userNum / activityMaxSize;
            DecimalFormat decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
            String strPropertion = decimalFormat.format(propertion);//返回字符串
            userActivity.setActivityId(activityId);
            userActivity.setUserId(userId);
            userActivity.setUserActivityTime(new Date());
            // 刚刚报名为未签到状态
            userActivity.setUserActivityIsRegistered(0);
            userActivityService.insertUserActivity(userActivity);
            map.put("sign","success");
            map.put("propertion",strPropertion);
        }
        return map;
    }

    /**
     * 用户进行签到，签到必须在活动签到时间后和开始时间前
     * @param userId
     * @param activityId
     * @return
     */
    @RequestMapping(value = "/userRegister",method = RequestMethod.POST)
    public Map<String,String> userRegister(@RequestParam("userId")String userId,@RequestParam("activityId")String activityId){

        Map<String,String> map = new HashMap<>();
        UserActivity userActivity = userActivityService.getUserActivityByUserIdAndActivityId(userId,activityId);
        // 根据给出的activityId得到对应的Activity
        Activity activity = activityService.getActivityById(activityId);
        Date activityRegisterTime = activity.getActivityRegisterTime();
        Date now = new Date();
        // 签到持续活动开始
        Date activityTime = activity.getActivityStartTime();
        int isRegister = userActivity.getUserActivityIsRegistered();
        if (isRegister == 1){
            map.put("sign","isRegistered");
        }else {
            if (now.before(activityRegisterTime)){
                map.put("sign","beforeRegister");
            }else if (now.after(activityRegisterTime)){
                map.put("sign","afterRegister");
            }else{
                userActivityService.register(userId,activityId);
                map.put("sign","registerSuccess");
            }
        }
        return map;
    }


}
