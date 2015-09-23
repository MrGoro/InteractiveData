package com.github.mrgoro.interactivedata.spring.sample;

import com.github.mrgoro.interactivedata.api.service.AnnotatedJSR269ServiceLocator;
import com.github.mrgoro.interactivedata.api.service.ServiceLocator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;

/**
 * Sample Application for demonstrating the use of the interactive-data framework with Spring (Boot).
 */
@SpringBootApplication
@EnableCaching
public class SampleApplication {

    private static final Log log = LogFactory.getLog(SampleApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SampleApplication.class, args);
    }

    @Bean
    @Lazy
    public CacheManager cacheManager() {
        log.debug("Initializing ConcurrentMapCacheManager");
        return new ConcurrentMapCacheManager();
    }

    @Bean
    public ServiceLocator serviceLocator() {
        log.debug("Initializing AnnotatedJSR269ServiceLocator");
        return new AnnotatedJSR269ServiceLocator();
    }
}