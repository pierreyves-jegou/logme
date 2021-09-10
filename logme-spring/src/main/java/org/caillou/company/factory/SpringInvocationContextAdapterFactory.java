package org.caillou.company.factory;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.caillou.company.bean.InvocationContextAdapter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class SpringInvocationContextAdapterFactory {

    public InvocationContextAdapter create(ProceedingJoinPoint proceedingJoinPoint){
        return new InvocationContextAdapter() {
            @Override
            public Class<?> getTargetClass() {
                return proceedingJoinPoint.getTarget().getClass();
            }

            @Override
            public Method getTargetMethod() {
                MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
                return methodSignature.getMethod();
            }

            @Override
            public Object[] getArguments() {
                return proceedingJoinPoint.getArgs();
            }

            @Override
            public Object proceed() throws Throwable {
                return proceedingJoinPoint.proceed();
            }
        };
    }

}
