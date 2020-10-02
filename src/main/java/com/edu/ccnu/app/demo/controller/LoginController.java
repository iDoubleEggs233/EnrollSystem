package com.edu.ccnu.app.demo.controller;

import com.edu.ccnu.app.demo.pojo.User;
import com.edu.ccnu.app.demo.service.UserService;
import com.edu.ccnu.app.demo.util.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * created by IntelliJ Idea
 *
 * @Author: Anakin
 * @Date: 2020/09/30/23:41
 * @Description: 登录、注册功能的控制层实现
 */
@RestController
@RequestMapping("/startPage")
@Slf4j
public class LoginController {

    @Autowired
    private UserService userService;

    /**
     * 通过用户id以及密码实现登录
     * @param userId:用户id
     * @param password:用户密码
     * @return:通过map反应结果和用户角色信息
     */
    @RequestMapping(value = "/userLogin",method = {RequestMethod.POST})
    public Map<String,String> userLogin(@RequestParam("userId") String userId,@RequestParam("password")String password) {
        // 返回数据使用map，用于指明用户的角色信息，即是否为管理员
        Map<String, String> userLoginMap = new HashMap<>();
        log.info("当前登录的用户账号为: %S", userId);
        log.info("当前登录的用户密码为: %s", password);
        User user = userService.getUserById(userId);
        if (user.getUserPassword().equals(password)) {
            if (user.getUserIsAdmin() == 0) {
                userLoginMap.put("role", "user");
                // 将当前输入的普通用户id传递到前端页面
                userLoginMap.put("userId",userId);
                log.info("登陆成功，为普通用户");
            } else {
                userLoginMap.put("role", "admin");
                // 将当前输入的管理员用户id传递到前端页面
                userLoginMap.put("userId",userId);
                log.info("登录成功，为管理员用户");
            }
        } else {
            userLoginMap.put("role", "wrongPassword");
            // 登陆失败，啥都没有
            userLoginMap.put("userId",null);
            log.info("登陆失败，密码错误");
        }
        return userLoginMap;
    }

    /**
     * 实现普通用户注册的功能
     * @param userId:用户id(学号)
     * @param userName:用户昵称
     * @param userRealName:用户真实姓名
     * @param userPhone:用户的手机号码
     * @param userPassword:用户的密码
     * @return 返回结果:0 手机号码输入有误
     */
    @RequestMapping(value = "/userRegist",method = {RequestMethod.POST})
    public int userRegister(@RequestParam("userId")String userId,@RequestParam("userName")String userName,@RequestParam("userRealName")String userRealName,
                            @RequestParam("userPhone")String userPhone,@RequestParam("userPassword")String userPassword){
        User user = new User();
        user.setUserId(userId);
        user.setUserName(userName);
        user.setUserRealName(userRealName);
        // 首先判断字符串是否为手机号码类型
        String telRegex = "[1][3578]\\d{9}";
        if (userPhone.matches(telRegex)){
            user.setUserPhone(userPhone);
        }else{
            log.info("用户输入的手机号码有误");
            return 0;
        }
        // 其次对用户输入的密码进行安全性校验
        // 判断是否有数字
        boolean noNumber = !userPassword.matches(".*\\p{Digit}+.*");
        // 判断书否有小写字符
        boolean noLowerCase = !userPassword.matches(".*\\p{Lower}+.*");
        // 判断是否有大写字符
        boolean noUpperCase = !userPassword.matches(".*\\p{Upper}+.*");

        if(noNumber || noLowerCase || noUpperCase) {
            log.info("输入的密码各式不符，重新输入");
            return -1;
        }
        // 符合格式的密码将放入用户实体
        user.setUserPassword(userPassword);
        // 默认注册的用户为普通用户
        user.setUserIsAdmin(0);
        userService.insertUser(user);
        // 代表插入成功
        return 1;

    }
}
