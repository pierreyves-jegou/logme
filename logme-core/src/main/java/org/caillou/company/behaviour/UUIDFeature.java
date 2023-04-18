package org.caillou.company.behaviour;

import org.caillou.company.bean.Context;
import org.caillou.company.bean.InvocationContextAdapter;
import org.caillou.company.bean.LogFeature;

public class UUIDFeature extends AbstractFeature  implements LogFeature {

    @Override
    public WHEN when() {
        return WHEN.BOTH;
    }

    @Override
    protected String doGenerateLog(WHEN when, InvocationContextAdapter invocationContextAdapter, Context context) {
        return context.getUuid().toString();
    }
}
