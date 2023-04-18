package org.caillou.company.logme.spring.web.log4j.util;

import org.apache.logging.log4j.ThreadContext;
import org.caillou.company.constant.Level;
import org.caillou.company.logme.spring.web.log4j.requestScope.RequestTrace;
import org.caillou.company.util.DefaultLevelExtractorServiceImpl;
import org.caillou.company.util.LevelExtractorService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class WebLevelExtractorService implements LevelExtractorService {

    private RequestTrace requestTrace;


    private LevelExtractorService defaulLevelExtractorService;

    public WebLevelExtractorService(RequestTrace requestTrace){
        this.defaulLevelExtractorService = new DefaultLevelExtractorServiceImpl();
        this.requestTrace = requestTrace;
    }

    @Override
    public Level extractCurrentLevel(Logger logger) {
        if(ThreadContext.containsKey("TRACE_IT")){
            return Level.TRACE;
        }
        return defaulLevelExtractorService.extractCurrentLevel(logger);
    }
}
