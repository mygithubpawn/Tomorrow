package com.pawn.blog;


import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Slf4j
@EnableAsync  //开启异步功能
@SpringBootApplication
@MapperScan("com.pawn.blog.mapper")
@EnableSwagger2
public class pawn_blog_web {
    public static void main(String[] args) {
        SpringApplication.run(pawn_blog_web.class, args);
    }
}
