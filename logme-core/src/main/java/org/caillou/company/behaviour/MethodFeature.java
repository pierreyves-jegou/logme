package org.caillou.company.behaviour;

import org.caillou.company.bean.Context;
import org.caillou.company.bean.InvocationContextAdapter;
import org.caillou.company.bean.LogFeature;

public class MethodFeature extends AbstractFeature implements LogFeature {

    @Override
    public WHEN when() {
        return WHEN.BOTH;
    }

    @Override
    String doGenerateLog(WHEN when, InvocationContextAdapter invocationContextAdapter, Context context) {
        return invocationContextAdapter.getTargetMethod().getName();
    }
}
