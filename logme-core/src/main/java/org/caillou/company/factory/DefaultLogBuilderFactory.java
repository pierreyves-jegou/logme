package org.caillou.company.factory;

import org.caillou.company.bean.LogFeature;
import org.caillou.company.behaviour.*;
import org.caillou.company.constant.Level;
import org.caillou.company.service.LogBuilder;

public class DefaultLogBuilderFactory {

    public LogBuilder createDefault(){
        LogBuilder logBuilder = new LogBuilder();

        addForLevels(logBuilder, new LineStarterFeature(), Level.values());
        addForLevels(logBuilder, new UUIDFeature(), Level.values());
        addForLevels(logBuilder, new MethodFeature(), Level.values());
        addForLevels(logBuilder, new ParameterAnonymizedFeature(), Level.DEBUG, Level.TRACE);
        //addForLevels(logBuilder, new ParameterFeature(), Level.DEBUG, Level.TRACE);
        addForLevels(logBuilder, new TimerFeature(), Level.values());
        addForLevels(logBuilder, new ReturnFeature(), Level.DEBUG, Level.TRACE);

        return logBuilder;
    }

    private void addForLevels(LogBuilder logBuilder, LogFeature logFeature, Level ... levels){
        logBuilder.addFeature(logFeature, levels);
    }
}
