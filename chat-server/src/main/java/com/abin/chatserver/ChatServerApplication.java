package com.abin.chatserver;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@MapperScan("com.abin.**")
public class ChatServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatServerApplication.class, args);
    }

}
