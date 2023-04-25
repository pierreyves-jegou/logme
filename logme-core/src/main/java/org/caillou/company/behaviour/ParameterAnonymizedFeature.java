package org.caillou.company.behaviour;

import org.caillou.company.annotation.Confidential;
import org.caillou.company.bean.Context;
import org.caillou.company.bean.InvocationContextAdapter;
import org.caillou.company.bean.LogFeature;
import org.caillou.company.service.GlobalFormatter;

import java.lang.reflect.Parameter;
import java.util.AbstractMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.caillou.company.constant.ConfidentialConstant.CONFIDENTIAL_CONTENT;
import static org.caillou.company.constant.ConfidentialConstant.NOT_RGPD_SAFE_MESSAGE;

public class ParameterAnonymizedFeature extends AbstractFeature implements LogFeature {

    private final GlobalFormatter globalFormatter;
    private final int maxNarrowCpt;

    public ParameterAnonymizedFeature(final GlobalFormatter globalFormatter, int maxNarrowCpt){
        this.globalFormatter = globalFormatter;
        this.maxNarrowCpt = maxNarrowCpt;
    }

    @Override
    public WHEN when() {
        return WHEN.BEFORE_PROCEED;
    }

    @Override
    protected String doGenerateLog(WHEN when, InvocationContextAdapter invocationContextAdapter, Context context) {
        if(!context.isRgpdSafe()){
            return NOT_RGPD_SAFE_MESSAGE;
        }

        Parameter[] parameters = invocationContextAdapter.getTargetMethod().getParameters();

        List<AbstractMap.SimpleEntry<Parameter, Object>> entries = IntStream
                .range(0, parameters.length)
                .mapToObj(index -> {
                    Parameter parameter = parameters[index];
                    Object finalResultObject;
                    if(parameter.isAnnotationPresent(Confidential.class)){
                        finalResultObject = CONFIDENTIAL_CONTENT;
                    }else{
                        finalResultObject = invocationContextAdapter.getArguments()[index];
                    }

                    return new AbstractMap.SimpleEntry<>(parameter, finalResultObject);
                }).collect(Collectors.toList());

        return globalFormatter.extractLogs(this.maxNarrowCpt, entries.toArray(new AbstractMap.SimpleEntry[]{}));
    }
}
