package org.caillou.company.service;

import org.caillou.company.bean.Context;
import org.caillou.company.bean.InvocationContextAdapter;
import org.caillou.company.bean.LogFeature;
import org.caillou.company.constant.Level;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class LogBuilder {

    private Map<Level, List<LogFeature>> featuresPerLoggingLevel = new HashMap<>();

    public void addFeature(Level level, LogFeature logFeature){
        featuresPerLoggingLevel.computeIfAbsent(level, x -> new LinkedList<>());
        featuresPerLoggingLevel.get(level).add(logFeature);
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
