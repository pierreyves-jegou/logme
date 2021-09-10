package org.caillou.company.behaviour;

import org.caillou.company.bean.Context;
import org.caillou.company.bean.InvocationContextAdapter;
import org.caillou.company.bean.LogFeature;

public class TimerFeature extends AbstractFeature implements LogFeature {

    @Override
    public WHEN when() {
        return WHEN.AFTER_PROCEED;
    }

    @Override
    String doGenerateLog(WHEN when, InvocationContextAdapter invocationContextAdapter, Context context) {
        double timeSpent = (context.getEndTime() - context.getStartTime()) / 1e6d;
        return String.format("time: %.2f ms", timeSpent);
    }
}
