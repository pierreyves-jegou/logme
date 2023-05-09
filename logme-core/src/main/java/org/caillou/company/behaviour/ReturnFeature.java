package org.caillou.company.behaviour;

import org.caillou.company.bean.Context;
import org.caillou.company.bean.InvocationContextAdapter;
import org.caillou.company.bean.LogFeature;
import org.caillou.company.service.GlobalDebugger;

import java.util.ArrayList;

import static org.caillou.company.constant.ConfidentialConstant.NOT_RGPD_SAFE_MESSAGE;

public class ReturnFeature extends AbstractFeature implements LogFeature {

    private final GlobalDebugger globalDebugger;
    private final int maxNarrowCpt;

    public ReturnFeature(final GlobalDebugger globalDebugger, int maxNarrowCpt){
        this.globalDebugger = globalDebugger;
        this.maxNarrowCpt = maxNarrowCpt;
    }

    @Override
    public WHEN when() {
        return WHEN.AFTER_PROCEED;
    }

    @Override
    protected String doGenerateLog(WHEN when, InvocationContextAdapter invocationContextAdapter, Context context) {
        if(!context.isRgpdSafe()){
            return NOT_RGPD_SAFE_MESSAGE;
        }

        Object result = context.getResult();
        if(result != null){
            return globalDebugger.debug(result, 0, this.maxNarrowCpt , globalDebugger, new ArrayList<>()).toString();
        }
        return "";
    }
}
