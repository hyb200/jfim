package com.abin.chatserver;

import com.abin.chatserver.common.config.EnableRedisSerialize;
import jakarta.annotation.PostConstruct;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import java.util.TimeZone;


@SpringBootApplication(scanBasePackages = "com.abin")
@MapperScan("com.abin.**.mapper")
@EnableRedisSerialize
@EnableConfigurationProperties
public class ChatServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatServerApplication.class, args);
    }

    @PostConstruct
    public void setDefaultTimezone() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
    }
}
