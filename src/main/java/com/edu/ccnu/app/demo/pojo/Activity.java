package com.edu.ccnu.app.demo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

/**
 * created by IntelliJ Idea
 *
 * @Author: Anakin
 * @Date: 2020/09/22/18:50
 * @Description: 活动的实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Activity implements Serializable,Comparable<Activity> {

    private String activityId;
    // 活动的名称
    private String activityName;
    // 活动的地点
    private String activityLocation;
    // 活动的开始时间
    private Date activityStartTime;
    // 活动当前的状态   0：报名未开始  1：报名中   2：报名已结束
    private int activityState;
    // 活动最多容纳的人数
    private int activityMaxSize;
    // 活动报名持续的时间,用毫秒值来计算
    private long activityRegisterDuration;
    // 活动报名开始时间
    private Date activityEnrollTime;
    // 活动签到时间
    private Date activityRegisterTime;
    // 活动签到方式  0:现场签到 1:二维码签到
    private int activityRegisterMode;
    // 活动内容摘要
    private String activityContent;
    // 活动持续时间,用毫秒值来计算
    private long activityDuration;
    // 活动类型  0:文艺活动  1:学习交流   2:体育活动   3:志愿者活动
    private int activityType;
    // 活动发起人的id
    private String activitySponsorId;

    /**
     * 实现按照时间由远及近排序的方法
     * @param o
     * @return
     */
    @Override
    public int compareTo(Activity o) {
        // 获得比较的两个对象的开始时间
        Date activityTime = this.getActivityStartTime();
        Date oTime = o.getActivityStartTime();
        if (activityTime.before(oTime)){
            return 1;
        }else {
            return -1;
        }
    }
}
