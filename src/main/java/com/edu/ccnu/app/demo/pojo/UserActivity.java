package com.edu.ccnu.app.demo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * created by IntelliJ Idea
 *
 * @Author: Anakin
 * @Date: 2020/09/22/18:50
 * @Description: 用户活动对应的实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserActivity implements Serializable {

    // 自增长id，默认从1开始步长为1
    private int userActivityId;
    // 活动id
    private String activityId;
    // 用户id
    private String userId;
    // 报名时间
    private Date userActivityTime;
    // 是否进行了签到  0:未签到 1:已签到
    private int userActivityIsRegistered;
}
