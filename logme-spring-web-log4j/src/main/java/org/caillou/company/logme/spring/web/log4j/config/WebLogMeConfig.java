package org.caillou.company.logme.spring.web.log4j.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.filter.DynamicThresholdFilter;
import org.apache.logging.log4j.core.util.KeyValuePair;
import org.caillou.company.behaviour.*;
import org.caillou.company.constant.Level;
import org.caillou.company.logme.spring.web.log4j.behaviour.RequestUUIDFeature;
import org.caillou.company.logme.spring.web.log4j.requestScope.RequestTrace;
import org.caillou.company.logme.spring.web.log4j.util.WebLevelExtractorService;
import org.caillou.company.service.GlobalDebugger;
import org.caillou.company.service.LogBuilder;
import org.caillou.company.service.LogService;
import org.caillou.company.service.logformater.*;
import org.caillou.company.util.LevelExtractorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@Configuration
public class WebLogMeConfig {

    @Autowired
    private RequestUUIDFeature requestUUIDFeature;


    @Primary
    @Bean
    public LogBuilder createLogBehaviorWeb(GlobalDebugger globalFormatter){
        int maxNarrowCpt = 4;
        LogBuilder logBuilder = new LogBuilder();
        logBuilder.addFeature(new LineStarterFeature(), Level.values());
        logBuilder.addFeature(requestUUIDFeature, Level.values());
        logBuilder.addFeature(new UUIDFeature(), Level.values());
        logBuilder.addFeature(new MethodFeature(), Level.values());
        logBuilder.addFeature(new ParameterAnonymizedFeature(globalFormatter, maxNarrowCpt), Level.DEBUG, Level.TRACE);
        logBuilder.addFeature(new TimerFeature(), Level.values());
        logBuilder.addFeature(new ReturnFeature(globalFormatter, maxNarrowCpt),  Level.DEBUG, Level.TRACE);
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

    @PostConstruct
    private void initLog4jConfiguration(){
        final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        KeyValuePair[] pair = {new KeyValuePair("FULL_TRACE_ENABLED", "TRACE")};
        DynamicThresholdFilter dynamicThresholdFilter = DynamicThresholdFilter.createFilter("TRACE_IT",
                pair,
                org.apache.logging.log4j.Level.ERROR,
                Filter.Result.ACCEPT,
                Filter.Result.NEUTRAL);
        ctx.addFilter(dynamicThresholdFilter);
    }


    @Value(value = "${logme.filters.packages:}")
    private String whitePackages;

    @Bean
    public GlobalDebugger globalFormatter(){
        ObjectMapper objectMapper = new ObjectMapper();
        BasicDebugger basicFormatter = new BasicDebugger();
        CollectionDebugger collectionFormatter = new CollectionDebugger(objectMapper);
        EntryDebugger entryFormatter = new EntryDebugger(objectMapper);
        GlobalDebugger globalFormatter = new GlobalDebugger(objectMapper);
        ProjectPackageDebugger projectPackageDebugger = new ProjectPackageDebugger(objectMapper, whitePackages);
        PrimitiveDebugger primitiveDebugger = new PrimitiveDebugger(objectMapper);
        globalFormatter.addHandlers(Arrays.asList(primitiveDebugger, basicFormatter, projectPackageDebugger, collectionFormatter, entryFormatter));
        return globalFormatter;
    }

}
