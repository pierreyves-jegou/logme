package org.caillou.company.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.caillou.company.bean.InvocationContextAdapter;
import org.caillou.company.factory.SpringInvocationContextAdapterFactory;
import org.caillou.company.service.LogService;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class LogSpringAspect {

    private final LogService logService;

    private final SpringInvocationContextAdapterFactory springInvocationContextAdapterFactory;

    public LogSpringAspect(LogService logService, SpringInvocationContextAdapterFactory springInvocationContextAdapterFactory){
        this.logService = logService;
        this.springInvocationContextAdapterFactory = springInvocationContextAdapterFactory;
    }

    @Around("@annotation(org.caillou.company.annotation.LogMe)")
    public Object logMe(ProceedingJoinPoint joinPoint) throws Throwable {
        InvocationContextAdapter invocationContextAdapter = springInvocationContextAdapterFactory.create(joinPoint);
        return logService.log(invocationContextAdapter);
    }

}
