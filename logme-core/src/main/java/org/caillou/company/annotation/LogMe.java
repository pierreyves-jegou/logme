package org.caillou.company.annotation;

import org.caillou.company.constant.Level;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogMe {

    Level level() default Level.INFO;

    String message() default "";

    String parameters() default "";

    String category() default "";

}
