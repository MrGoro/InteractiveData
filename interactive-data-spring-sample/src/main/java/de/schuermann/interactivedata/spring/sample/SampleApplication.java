package de.schuermann.interactivedata.spring.sample;

import de.schuermann.interactivedata.api.service.AnnotatedJSR269ServiceLocator;
import de.schuermann.interactivedata.api.service.ServiceLocator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Sample Application for demonstrating the use of the interactive-data framework with Spring (Boot).
 */
@SpringBootApplication
@EnableCaching
@EnableAsync
public class SampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SampleApplication.class, args);
    }

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager();
    }

    @Bean
    public ServiceLocator serviceLocator() {
        return new AnnotatedJSR269ServiceLocator();
    }
}