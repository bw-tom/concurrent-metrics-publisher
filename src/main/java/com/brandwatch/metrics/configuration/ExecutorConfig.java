package com.brandwatch.metrics.configuration;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

@Configuration
public class ExecutorConfig {

    private static final Logger logger = LoggerFactory.getLogger(ExecutorConfig.class);

    @Bean
    public ThreadPoolExecutor getTaskExecutor(@Value("${concurrent-metrics-publisher.pool.size}") int poolSize) {
        logger.info("Building task executor with {} threads", poolSize);
        return new ThreadPoolExecutor(poolSize,
                                      poolSize,
                                      1000L,
                                      TimeUnit.MILLISECONDS,
                                      new LinkedBlockingQueue<>(),
                                      new ThreadFactoryBuilder()
                                        .setNameFormat("concurrent-metrics-publisher-worker-pool-%d")
                                        .build());
    }

}
