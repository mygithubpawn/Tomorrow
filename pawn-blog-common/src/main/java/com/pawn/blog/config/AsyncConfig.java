package com.pawn.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/***
 * description: 异步任务
 * @author:美茹冠玉
 * @Return
 * @param
 * @date 2020/12/20 20:08
 */

@Configuration
@EnableAsync
public class AsyncConfig {
    @Bean
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(10);
        executor.setThreadNamePrefix("pawn_blog_task_worker-");
        executor.setQueueCapacity(30);
        executor.initialize();
        return executor;
    }
}
