package org.caillou.company.logme.spring.web.log4j.config;

import org.caillou.company.behaviour.*;
import org.caillou.company.constant.Level;
import org.caillou.company.factory.DefaultLogBuilderFactory;
import org.caillou.company.logme.spring.web.log4j.behaviour.RequestUUIDFeature;
import org.caillou.company.logme.spring.web.log4j.requestScope.RequestTrace;
import org.caillou.company.logme.spring.web.log4j.util.WebLevelExtractorService;
import org.caillou.company.service.LogBuilder;
import org.caillou.company.service.LogService;
import org.caillou.company.util.DefaultLevelExtractorServiceImpl;
import org.caillou.company.util.LevelExtractorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class WebLogMeConfig {

    @Autowired
    private RequestUUIDFeature requestUUIDFeature;


    @Primary
    @Bean
    public LogBuilder createLogBehaviorWeb(){
        LogBuilder logBuilder = new LogBuilder();
        logBuilder.addFeature(new LineStarterFeature(), Level.values());
        logBuilder.addFeature(requestUUIDFeature, Level.values());
        logBuilder.addFeature(new UUIDFeature(), Level.values());
        logBuilder.addFeature(new MethodFeature(), Level.values());
        logBuilder.addFeature(new ParameterAnonymizedFeature(), Level.DEBUG, Level.TRACE);
        logBuilder.addFeature(new TimerFeature(), Level.values());
        logBuilder.addFeature(new ReturnFeature(),  Level.DEBUG, Level.TRACE);
        return logBuilder;
    }

    @Bean
    @Primary
    public LevelExtractorService createLevelExtractorServiceWeb(RequestTrace requestTrace){
        return new WebLevelExtractorService(requestTrace);
    }

    @Bean
    @Primary
    public LogService createLogServiceWeb(LogBuilder logBuilder, LevelExtractorService levelExtractorService){
        return new LogService(logBuilder, levelExtractorService);
    }

}
