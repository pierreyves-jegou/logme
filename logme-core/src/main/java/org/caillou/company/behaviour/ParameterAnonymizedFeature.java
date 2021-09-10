package org.caillou.company.behaviour;

import org.caillou.company.annotation.Confidential;
import org.caillou.company.bean.Context;
import org.caillou.company.bean.InvocationContextAdapter;
import org.caillou.company.bean.LogFeature;

import java.lang.reflect.Parameter;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ParameterAnonymizedFeature extends AbstractFeature implements LogFeature {

    private String delimiter = ",";
    private String startSymbol = "(";
    private String endSymbol = ")";

    @Override
    public WHEN when() {
        return WHEN.BEFORE_PROCEED;
    }

    @Override
    String doGenerateLog(WHEN when, InvocationContextAdapter invocationContextAdapter, Context context) {
        Parameter[] parameters = invocationContextAdapter.getTargetMethod().getParameters();
        StringBuilder toLog = new StringBuilder(startSymbol);

        String parametersResults = IntStream.range(0, parameters.length)
                .mapToObj(index -> {
                    Parameter parameter = parameters[index];
                    Object parameterValue = invocationContextAdapter.getArguments()[index];
                    if (parameter.getAnnotation(Confidential.class) != null) {
                        return "@confidentifal";
                    } else {
                        return parameterValue.toString();
                    }
                }).collect(Collectors.joining(delimiter));

        toLog.append(parametersResults);
        toLog.append(endSymbol);
        return toLog.toString();
    }
}
