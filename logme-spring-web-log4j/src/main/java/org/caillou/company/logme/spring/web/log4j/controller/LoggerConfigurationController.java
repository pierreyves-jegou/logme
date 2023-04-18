package org.caillou.company.logme.spring.web.log4j.controller;


import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.caillou.company.logme.spring.web.log4j.requestScope.RequestUUID;
import org.caillou.company.logme.spring.web.log4j.dto.LoggerConfigurationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/loggers")
public class LoggerConfigurationController {

    public static final String ROOT_LOGGER = "root";

    @Autowired
    private RequestUUID requestUUID;

    @GetMapping
    public List<LoggerConfigurationDto> getConfiguration() {
        UUID uuid = requestUUID.getMonThreadLocal().get();
        final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        Configuration configuration = ctx.getConfiguration();
        return getLoggersConfiguration(configuration);
    }

    @GetMapping("/root/level")
    public String getRootLevel(){
        final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        Configuration configuration = ctx.getConfiguration();
        return getLoggersConfiguration(configuration)
                .stream()
                .filter(logger -> logger.getLoggerKey().equals("root"))
                .findFirst()
                .get()
                .getLoggerLevel();
    }


    @PostMapping("/root/level")
    public void updateRootLevel(@RequestBody String level){
        final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        Configuration configuration = ctx.getConfiguration();
        configuration.getLoggers()
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey().equals(""))
                .forEach(entry -> entry.getValue().setLevel(Level.toLevel(level)));
        ctx.updateLoggers();
    }

    @PostMapping
    public void updateConfiguration(@RequestBody List<LoggerConfigurationDto> loggerConfigurationDtos) {
        final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        Configuration configuration = ctx.getConfiguration();

        Map<String, LoggerConfig> loggersMap = configuration.getLoggers()
                .entrySet()
                .stream()
                .map(entry -> {
                    String loggerName = entry.getKey().equals("") ? ROOT_LOGGER : entry.getKey();
                    return new AbstractMap.SimpleEntry<>(loggerName, entry.getValue());
                })
                .collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue()));

        Collection<Appender> appenders = configuration.getAppenders()
                .values();


        Set<AppenderRef> appenderRefs = configuration.getLoggers()
                .entrySet()
                .stream()
                .flatMap(entry -> entry.getValue().getAppenderRefs().stream())
                .collect(Collectors.toSet());


        loggerConfigurationDtos
                .stream()
                .forEach(loggerPayload -> {
                    if (loggersMap.containsKey(loggerPayload.getLoggerKey())) {
                        loggersMap.get(loggerPayload.getLoggerKey()).setLevel(Level.toLevel(loggerPayload.getLoggerLevel()));
                    } else {
                        LoggerConfig loggerConfig = LoggerConfig.newBuilder()
                                .withConfig(configuration)
                                .withLoggerName(loggerPayload.getLoggerKey())
                                .withAdditivity(false)
                                .withRefs(appenderRefs.toArray(new AppenderRef[0]))
                                .withLevel(Level.toLevel(loggerPayload.getLoggerLevel()))
                                .build();
                        for (Appender appender : appenders) {
                            loggerConfig.addAppender(appender, loggerConfig.getLevel(), null);
                        }
                        configuration.addLogger(loggerConfig.getName(), loggerConfig);
                    }
                });

        ctx.updateLoggers();
    }

    private static List<LoggerConfigurationDto> getLoggersConfiguration(Configuration configuration) {
        return configuration.getLoggers()
                .entrySet()
                .stream()
                .map(entry -> {
                    String loggerName = entry.getKey().equals("") ? "root" : entry.getKey();
                    return new LoggerConfigurationDto(loggerName, entry.getValue().getLevel().toString());
                })
                .collect(Collectors.toList());
    }
}
