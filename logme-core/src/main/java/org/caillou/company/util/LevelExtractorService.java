package org.caillou.company.util;

import org.caillou.company.constant.Level;
import org.slf4j.Logger;

public class LevelExtractorService {

    public Level extractCurrentLevel(Logger logger){
        if(logger.isTraceEnabled()){
            return Level.TRACE;
        }
        if(logger.isDebugEnabled()){
            return Level.DEBUG;
        }
        if(logger.isInfoEnabled()){
            return Level.INFO;
        }
        if(logger.isWarnEnabled()){
            return Level.WARN;
        }
        if(logger.isErrorEnabled()){
            return Level.ERROR;
        }
        return Level.DEBUG;
    }
}
