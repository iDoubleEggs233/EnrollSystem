package com.edu.ccnu.app.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.edu.ccnu.app.demo.mapper")
@EnableCaching  // 开启缓存
@EnableTransactionManagement  // 保证redis与mysql的数据一致性
@EnableScheduling   // 开启定时事件
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
