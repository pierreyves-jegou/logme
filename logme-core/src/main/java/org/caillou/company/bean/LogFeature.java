package org.caillou.company.bean;

public interface LogFeature {

    enum WHEN { BEFORE_PROCEED,AFTER_PROCEED, BOTH }

    WHEN when();

    String generate(WHEN when, InvocationContextAdapter invocationContextAdapter, Context context);

}
