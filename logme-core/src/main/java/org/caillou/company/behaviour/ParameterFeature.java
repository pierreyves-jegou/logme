package org.caillou.company.behaviour;

import org.caillou.company.bean.Context;
import org.caillou.company.bean.InvocationContextAdapter;
import org.caillou.company.bean.LogFeature;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ParameterFeature extends AbstractFeature implements LogFeature {

    private String delimiter = ",";
    private String startSymbol = "(";
    private String endSymbol = ")";

    @Override
    public WHEN when() {
        return WHEN.BEFORE_PROCEED;
    }

    @Override
    protected String doGenerateLog(WHEN when, InvocationContextAdapter invocationContextAdapter, Context context) {
        StringBuilder toLog = new StringBuilder(startSymbol);

        String parametersResults = Arrays.stream(invocationContextAdapter.getArguments())
                .map(Object::toString)
                .collect(Collectors.joining(delimiter));

        toLog.append(parametersResults);
        toLog.append(endSymbol);
        return toLog.toString();
    }
}
