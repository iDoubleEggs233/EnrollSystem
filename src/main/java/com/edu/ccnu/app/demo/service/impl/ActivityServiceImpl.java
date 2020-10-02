package com.edu.ccnu.app.demo.service.impl;

import com.edu.ccnu.app.demo.mapper.ActivityMapper;
import com.edu.ccnu.app.demo.pojo.Activity;
import com.edu.ccnu.app.demo.pojo.User;
import com.edu.ccnu.app.demo.service.ActivityService;
import com.edu.ccnu.app.demo.util.RedisUtils;
import com.edu.ccnu.app.demo.util.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * created by IntelliJ Idea
 *
 * @Author: Anakin
 * @Date: 2020/09/23/10:55
 * @Description:
 */
@Service
@Slf4j
@CacheConfig(cacheNames = "activityCache")
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityMapper activityMapper;

    @Override
    @Cacheable(value = "activities",key = "allActivity")
    public List<Activity> getAllActivity() {
        return activityMapper.getActivityByPage();
    }

    @Override
    @Cacheable(value = "activities",key = "#pageNum+'-'+#pageSize")
    public PageInfo<Activity> getActivityByPage(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Activity> activities = activityMapper.getActivityByPage();
        PageInfo<Activity> pageInfo = new PageInfo<Activity>(activities);
        return pageInfo;
    }

    @Override
    @Cacheable(value = "activities",key = "#activityId")
    public Activity getActivityById(String activityId) {
        Activity activity = activityMapper.getActivityById(activityId);
        log.info("当前获得的活动信息为: %s",activity.toString());
        return activity;
    }

    @Override
    @CachePut(value = "activities",key = "#activity.activityId")
    public Activity insertActivity(Activity activity) {
        String activityId = activityMapper.insertActivity(activity);
        log.info("当前插入的新的活动的id为: %s",activityId);
        return activity;
    }

    @Override
    @CachePut(value = "activities",key = "#activity.activityId")
    public Integer updateActivity(Activity activity) {
        log.info("修改后的活动信息为: %s",activity.toString());
        return activityMapper.updateActivity(activity);
    }

    @Override
    @CacheEvict(value = "activities",key = "#activityId")
    public Integer deleteActivityById(String activityId) {
        log.info("当前删除的活动的id为: %s",activityId);
        return activityMapper.deleteActivityById(activityId);
    }

    @Override
    public Integer deleteActivityByIds(String ids) {
        String[] idArray = StringUtils.toStrArray(ids);
        log.info("当前批量删除的活动id有: %s",ids);
        RedisUtils.del(idArray);
        return activityMapper.deleteActivityByIds(StringUtils.toStrArray(ids));
    }

    @Override
    @Scheduled(cron = "0 0/30 * * * *")
    @CachePut(value = "activities",key = "allActivity")
    public void updateActivityState() {
        // 获取系统当前时间
        Date now = new Date();
        // 更新当前数据库中所有活动的状态
        List<Activity> listOfActivity = activityMapper.getAllActivity();
        for (Activity activity:listOfActivity) {
            Date activityEnrollStartTime = activity.getActivityEnrollTime();
            if (now.before(activityEnrollStartTime)) {
                // 当前时间早于activityEnrollTime，报名尚未开始
                activity.setActivityState(0);
            } else{
                Date activityEnrollStopTime = new Date();
                // 得到活动开始时间加上时间间隔，得到活动报名结束时间
                activityEnrollStopTime.setTime(activityEnrollStartTime.getTime() + activity.getActivityRegisterDuration());
                if (now.before(activityEnrollStopTime)){
                    activity.setActivityState(1);
                }else{
                    activity.setActivityState(2);
                }
            }
            activityMapper.updateActivity(activity);
        }
    }
}
