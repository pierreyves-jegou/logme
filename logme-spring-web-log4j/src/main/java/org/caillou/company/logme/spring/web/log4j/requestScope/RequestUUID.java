package org.caillou.company.logme.spring.web.log4j.requestScope;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.UUID;

public class RequestUUID {

    private ThreadLocal<UUID> monThreadLocal;

    public RequestUUID(){
        monThreadLocal = new ThreadLocal<>();
        monThreadLocal.set(UUID.randomUUID());
    }

    public ThreadLocal<UUID> getMonThreadLocal() {
        return monThreadLocal;
    }
}
