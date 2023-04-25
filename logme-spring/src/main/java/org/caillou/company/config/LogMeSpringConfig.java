package org.caillou.company.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.caillou.company.aspect.LogSpringAspect;
import org.caillou.company.factory.DefaultLogBuilderFactory;
import org.caillou.company.factory.SpringInvocationContextAdapterFactory;
import org.caillou.company.service.GlobalFormatter;
import org.caillou.company.service.LogBuilder;
import org.caillou.company.service.LogService;
import org.caillou.company.service.logformater.BasicFormatter;
import org.caillou.company.service.logformater.CollectionFormatter;
import org.caillou.company.service.logformater.EntryFormatter;
import org.caillou.company.util.DefaultLevelExtractorServiceImpl;
import org.caillou.company.util.LevelExtractorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

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

    @Bean
    public GlobalFormatter globalFormatter(){
        ObjectMapper objectMapper = new ObjectMapper();
        BasicFormatter basicFormatter = new BasicFormatter();
        CollectionFormatter collectionFormatter = new CollectionFormatter(objectMapper);
        EntryFormatter entryFormatter = new EntryFormatter(objectMapper);
        GlobalFormatter globalFormatter = new GlobalFormatter(objectMapper);
        globalFormatter.addHandlers(Arrays.asList(basicFormatter, collectionFormatter, entryFormatter));
        return globalFormatter;
    }

}
