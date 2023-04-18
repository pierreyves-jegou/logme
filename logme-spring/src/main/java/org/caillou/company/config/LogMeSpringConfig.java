package org.caillou.company.config;

import org.caillou.company.aspect.LogSpringAspect;
import org.caillou.company.factory.DefaultLogBuilderFactory;
import org.caillou.company.factory.SpringInvocationContextAdapterFactory;
import org.caillou.company.util.DefaultLevelExtractorServiceImpl;
import org.caillou.company.service.LogBuilder;
import org.caillou.company.service.LogService;
import org.caillou.company.util.LevelExtractorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogMeSpringConfig {

    @Bean
    public SpringInvocationContextAdapterFactory createSpringInvocationContextAdapterFactory(){
        return new SpringInvocationContextAdapterFactory();
    }

    @Bean
    public LogSpringAspect createLogSpringAspect(LogService logService, SpringInvocationContextAdapterFactory springInvocationContextAdapterFactory){
        return new LogSpringAspect(logService, springInvocationContextAdapterFactory);
    }

    @Bean
    public LogBuilder createLogBehavior(){
        return new DefaultLogBuilderFactory().createDefault();
    }

    @Bean
    public LevelExtractorService createLevelExtractorService(){
        return new DefaultLevelExtractorServiceImpl();
    }

    @Bean
    public LogService createLogService(LogBuilder logBuilder, LevelExtractorService levelExtractorService){
        return new LogService(logBuilder, levelExtractorService);
    }

}
