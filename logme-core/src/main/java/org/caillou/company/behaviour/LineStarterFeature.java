package org.caillou.company.behaviour;

import org.caillou.company.bean.Context;
import org.caillou.company.bean.InvocationContextAdapter;
import org.caillou.company.bean.LogFeature;

public class LineStarterFeature extends AbstractFeature implements LogFeature {

    private String startingPrefix = ">>>";
    private String endingPrefix = "<<<";

    @Override
    public WHEN when() {
        return WHEN.BOTH;
    }

    @Override
    String doGenerateLog(WHEN when, InvocationContextAdapter invocationContextAdapter, Context context) {
        return WHEN.BEFORE_PROCEED.equals(when) ? startingPrefix : endingPrefix;
    }
}
