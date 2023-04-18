package org.caillou.company.service;

import lombok.extern.slf4j.Slf4j;
import org.caillou.company.bean.Context;
import org.caillou.company.bean.InvocationContextAdapter;
import org.caillou.company.bean.LogFeature;
import org.caillou.company.constant.Level;
import org.caillou.company.util.LevelExtractorService;
import org.caillou.company.util.LevelExtractorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@Slf4j
public class LogService {

    private final LogBuilder logBuilder;
    private final LevelExtractorService levelExtractorService;

    public LogService(LogBuilder logBuilder, LevelExtractorService levelExtractorService){
        this.logBuilder = logBuilder;
        this.levelExtractorService = levelExtractorService;
    }


    public Object log(InvocationContextAdapter invocationContextAdapter) throws Throwable {
        Context context = new Context();
        context.setUuid(UUID.randomUUID());
        Logger logger = LoggerFactory.getLogger(invocationContextAdapter.getTargetClass());
        long startTime = System.nanoTime();
        context.setStartTime(startTime);
        logBefore(logger, invocationContextAdapter, context);

        Object result = invocationContextAdapter.proceed();
        context.setResult(result);

        long endTime = System.nanoTime();
        context.setEndTime(endTime);

        logAfter(logger, invocationContextAdapter, context);
        return result;
    }


    private void logBefore(Logger logger, InvocationContextAdapter invocationContextAdapter, Context context) {
        Level currentLevel = levelExtractorService.extractCurrentLevel(logger);
        String toLog = this.logBuilder.generateLog(LogFeature.WHEN.BEFORE_PROCEED, currentLevel, invocationContextAdapter, context);
        logIt(currentLevel, logger, toLog);
    }

    private void logAfter(Logger logger, InvocationContextAdapter invocationContextAdapter, Context context) {
        Level currentLevel = levelExtractorService.extractCurrentLevel(logger);
        String toLog = this.logBuilder.generateLog(LogFeature.WHEN.AFTER_PROCEED, currentLevel, invocationContextAdapter, context);
        logIt(currentLevel, logger, toLog);
    }

    private void logIt(Level level, Logger logger, String input) {
        switch (level) {
            case ERROR:
                logger.error(input);
                break;
            case WARN:
                logger.warn(input);
                break;
            case INFO:
                logger.info(input);
                break;
            case DEBUG:
                logger.debug(input);
                break;
            case TRACE:
                logger.trace(input);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + level);
        }
    }

}
