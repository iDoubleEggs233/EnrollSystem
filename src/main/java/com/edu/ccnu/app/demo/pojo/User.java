package com.edu.ccnu.app.demo.pojo;

import com.edu.ccnu.app.demo.util.Page;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * created by IntelliJ Idea
 *
 * @Author: Anakin
 * @Date: 2020/09/22/18:50
 * @Description: 用户的实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User extends Page implements Serializable {

    private String userId;
    // 用户的昵称
    private String userName;
    // 用户的真实姓名
    private String userRealName;
    // 用户的手机号码
    private String userPhone;
    // 用户的登录密码
    private String userPassword;
    // 用户是否为管理员？0不是，1是
    private int userIsAdmin;


}
