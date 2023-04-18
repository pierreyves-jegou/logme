package org.caillou.company.service;

import org.caillou.company.bean.Context;
import org.caillou.company.bean.InvocationContextAdapter;
import org.caillou.company.bean.LogFeature;
import org.caillou.company.constant.Level;

import java.util.*;

public class LogBuilder {

    private Map<Level, List<LogFeature>> featuresPerLoggingLevel = new HashMap<>();

    public void addFeature(LogFeature logFeature, Level level){
        featuresPerLoggingLevel.computeIfAbsent(level, x -> new LinkedList<>());
        featuresPerLoggingLevel.get(level).add(logFeature);
    }

    public void addFeature(LogFeature logFeature, Level ... levels){
        for(Level level : levels){
            addFeature(logFeature, level);
        }
    }


    public String generateLog(LogFeature.WHEN when, Level level, InvocationContextAdapter invocationContextAdapter, Context context){
        List<LogFeature> logFeatures = featuresPerLoggingLevel.get(level);
        StringBuilder sb = new StringBuilder();
        for(LogFeature logFeature : logFeatures){
            if(!LogFeature.WHEN.BOTH.equals(logFeature.when()) && !when.equals(logFeature.when())){
                continue;
            }
            sb.append(logFeature.generate(when, invocationContextAdapter, context));
        }
        return sb.toString();
    }

}
