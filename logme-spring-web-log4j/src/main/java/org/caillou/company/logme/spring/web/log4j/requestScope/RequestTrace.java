package org.caillou.company.logme.spring.web.log4j.requestScope;

import org.apache.logging.log4j.ThreadContext;

import java.util.UUID;

public class RequestTrace {

    private ThreadLocal<Boolean> monThreadLocal;

    public RequestTrace(){
        monThreadLocal = new ThreadLocal<>();
        monThreadLocal.set(false);
    }

    public void setTraceEnabled(){
        monThreadLocal.set(true);
        ThreadContext.put("TRACE_IT", "FULL_TRACE_ENABLED");
    }

    public ThreadLocal<Boolean> getMonThreadLocal() {
        return monThreadLocal;
    }
}
