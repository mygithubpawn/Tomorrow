package com.pawn.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/***
 * description: Security配置
 *
 * RBAC：权限控制
 * 需要三张表，
 * 用户表-->角色-->权限
 * 角色表
 * 权限表
 * @author:美茹冠玉
 * @Return
 * @param
 * @date 2020/12/19 9:46
 */
@Configuration
@EnableWebSecurity
//开启RBAC
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebStringSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 加密方式
     *
     * @Author Sans
     * @CreateTime 2019/10/1 14:00
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 配置security的控制逻辑
     *
     * @param http
     * @return
     * @author zwq
     * @date 2020/4/4
     **/
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //所有都放行
        http.authorizeRequests()
                .antMatchers("/**").permitAll()
                .and().csrf().disable();

    }
}
