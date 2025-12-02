package com.EIPplatform.configuration;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class AsyncConfig {

    @Bean(name = "emailExecutor")
    public TaskExecutor emailExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(50);
        executor.setThreadNamePrefix("Email-");
        executor.initialize();
        return executor;
    }

    // @Bean(name = "uhooExecutor")
    // public Executor uhooExecutor() {
    //     ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    //     executor.setCorePoolSize(10);
    //     executor.setMaxPoolSize(20);
    //     executor.setQueueCapacity(100);
    //     executor.setThreadNamePrefix("Uhoo-");
    //     executor.initialize();
    //     return executor;
    // }

    // @Bean(name = "iqairExecutor")
    // public Executor iqairExecutor() {
    //     ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    //     executor.setCorePoolSize(3);
    //     executor.setMaxPoolSize(5);
    //     executor.setQueueCapacity(50);
    //     executor.setThreadNamePrefix("IQAir-");
    //     executor.initialize();
    //     return executor;
    // }

    // @Bean(name = "tinyTuyaExecutor")
    // public Executor tinyTuyaExecutor() {
    //     ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    //     executor.setCorePoolSize(2);
    //     executor.setMaxPoolSize(4);
    //     executor.setQueueCapacity(10);
    //     executor.setKeepAliveSeconds(30);
    //     executor.setAllowCoreThreadTimeOut(true);
    //     executor.setThreadNamePrefix("TinyTuya-");
    //     executor.setRejectedExecutionHandler((r, e) -> {
    //         throw new RejectedExecutionException("TinyTuya scan is running, please try again later.");
    //     });
    //     executor.initialize();
    //     return executor;
    // }

    // @Bean(name = "tuyaExecutor")
    // public Executor tuyaExecutor() {
    //     ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    //     executor.setCorePoolSize(2);
    //     executor.setMaxPoolSize(4);
    //     executor.setQueueCapacity(20);
    //     executor.setKeepAliveSeconds(90);
    //     executor.setAllowCoreThreadTimeOut(true);
    //     executor.setThreadNamePrefix("Tuya-");
    //     executor.initialize();
    //     return executor;
    // }

    @Bean(name = "heavyExecutor")
    public Executor heavyExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(4);
        executor.setQueueCapacity(20);
        executor.setKeepAliveSeconds(90);
        executor.setAllowCoreThreadTimeOut(true);
        executor.setThreadNamePrefix("HeavyTask-");
        executor.initialize();
        return executor;
    }
}
