package org.caillou.company.behaviour;

import org.caillou.company.bean.Context;
import org.caillou.company.bean.InvocationContextAdapter;
import org.caillou.company.bean.LogFeature;

public class ReturnFeature extends AbstractFeature implements LogFeature {

    @Override
    public WHEN when() {
        return WHEN.AFTER_PROCEED;
    }

    @Override
    String doGenerateLog(WHEN when, InvocationContextAdapter invocationContextAdapter, Context context) {
        if(context.getResult() != null){
            return context.getResult().toString();
        }
        return "";
    }
}
