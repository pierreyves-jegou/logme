package org.caillou.company.bean;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;

public interface InvocationContextAdapter {

    Class<?> getTargetClass();

    Method getTargetMethod();

    Object[] getArguments();

    Object proceed() throws Throwable;
}
