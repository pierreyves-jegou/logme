package org.caillou.company.behaviour;

import lombok.Getter;
import lombok.Setter;
import org.caillou.company.bean.Context;
import org.caillou.company.bean.InvocationContextAdapter;
import org.caillou.company.bean.LogFeature;

@Getter
@Setter
public abstract class AbstractFeature implements LogFeature {

    private String prefix = " [";
    private String suffix = "] ";

    public String generate(WHEN when, InvocationContextAdapter invocationContextAdapter, Context context){
        return prefix + doGenerateLog(when, invocationContextAdapter, context) + suffix;
    }

    protected abstract String doGenerateLog(WHEN when,
                                            InvocationContextAdapter invocationContextAdapter,
                                            Context context);

}
