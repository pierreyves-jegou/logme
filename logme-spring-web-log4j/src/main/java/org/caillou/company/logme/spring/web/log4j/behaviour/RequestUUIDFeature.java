package org.caillou.company.logme.spring.web.log4j.behaviour;

import org.caillou.company.bean.Context;
import org.caillou.company.bean.InvocationContextAdapter;
import org.caillou.company.bean.LogFeature;
import org.caillou.company.behaviour.AbstractFeature;
import org.caillou.company.logme.spring.web.log4j.requestScope.RequestUUID;
import org.springframework.stereotype.Component;

@Component
public class RequestUUIDFeature extends AbstractFeature implements LogFeature {

    private RequestUUID requestUUID;

    public RequestUUIDFeature(RequestUUID requestUUID){
        this.requestUUID = requestUUID;
    }

    @Override
    public WHEN when() {
        return WHEN.BOTH;
    }

    @Override
    protected String doGenerateLog(WHEN when, InvocationContextAdapter invocationContextAdapter, Context context) {
        return requestUUID.getMonThreadLocal().get().toString();
    }
}
