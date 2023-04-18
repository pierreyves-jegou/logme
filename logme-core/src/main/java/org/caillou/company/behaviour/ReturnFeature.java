package org.caillou.company.behaviour;

import org.caillou.company.annotation.Confidential;
import org.caillou.company.bean.Context;
import org.caillou.company.bean.InvocationContextAdapter;
import org.caillou.company.bean.LogFeature;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.caillou.company.constant.ConfidentialConstant.REPLACED_CONTENT;
import static org.caillou.company.constant.ConfidentialConstant.RGPD_METHODE;

public class ReturnFeature extends AbstractFeature implements LogFeature {

    @Override
    public WHEN when() {
        return WHEN.AFTER_PROCEED;
    }

    @Override
    protected String doGenerateLog(WHEN when, InvocationContextAdapter invocationContextAdapter, Context context) {
        Object result = context.getResult();
        if(result != null){
            Class<?> resultClass = result.getClass();
            if(resultClass.getAnnotation(Confidential.class) == null) {
                return result.toString();
            }

            String stringToLog = logThroughSpecificMethod(resultClass, result);
            if(stringToLog != null){
                return stringToLog;
            }

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(resultClass.getSimpleName());
            stringBuilder.append("(");

            for(int i=0; i< resultClass.getDeclaredFields().length; i++){
                Field field = resultClass.getDeclaredFields()[i];
                stringBuilder.append(field.getName()).append("=");
                Confidential annotation = field.getAnnotation(Confidential.class);
                if(annotation != null){
                    stringBuilder.append(REPLACED_CONTENT);
                }else{
                    try {
                        field.trySetAccessible();
                        stringBuilder.append(field.get(result));
                        if(i< resultClass.getDeclaredFields().length-1){
                            stringBuilder.append(",");
                        }
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            stringBuilder.append(")");
            return stringBuilder.toString();
        }
        return "";
    }

    private String logThroughSpecificMethod(Class<?> resultClass, Object result){
        Method rgpdMethod = hasRgpdMethod(resultClass);
        if(rgpdMethod != null){
            try {
                Object objectToLog = rgpdMethod.invoke(result);
                if(objectToLog != null){
                    return objectToLog.toString();
                }
                return null;
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    private Method hasRgpdMethod(final Class<?> resultClass){
        try {
            return resultClass.getDeclaredMethod(RGPD_METHODE);
        } catch (NoSuchMethodException e){
            return null;
        }
    }



}
