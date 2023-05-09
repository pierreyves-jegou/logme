package org.caillou.company.service.logformater;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.caillou.company.annotation.Confidential;
import org.caillou.company.service.GlobalDebugger;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.caillou.company.constant.ConfidentialConstant.CONFIDENTIAL_CONTENT;

public class ProjectPackageDebugger implements Debugger {

    private final Set<String> whitePackages;

    private final ObjectMapper objectMapper;

    public ProjectPackageDebugger(final ObjectMapper objectMapper, String ... whitePackages){
        this.objectMapper = objectMapper;
        this.whitePackages = Arrays.stream(whitePackages).collect(Collectors.toSet());
    }

    private boolean doSupport(String className){
        if(className.contains(".")){
            int lastIndexDot = className.lastIndexOf(".");
            final String currentPackage = className.substring(0, lastIndexDot);
            boolean inWhiteList = whitePackages.contains(currentPackage);
            if(inWhiteList){
                return true;
            }else{
                return doSupport(currentPackage);
            }
        }
        return false;
    }

    @Override
    public Predicate<Class<?>> support(){
        return clazz -> doSupport(clazz.getName());
    }

    @Override
    public JsonNode debug(Object inst, int narrowCpt, int maxNarrowCpt, GlobalDebugger globalDebugger, Collection<Object> objectAlreadyProcessed) {
        Class<?> clazz = inst.getClass();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("pointer", inst.hashCode());
        for (int i = 0; i < clazz.getDeclaredFields().length; i++) {
            Field field = clazz.getDeclaredFields()[i];

            Debugger suitableDebugger = globalDebugger.detectSuitableDebugger(field.getType());
            if(suitableDebugger == null){
                return new TextNode("not loggable");
            }

            field.trySetAccessible();
            String fieldName = field.getName();
            if(field.getAnnotation(Confidential.class) != null){
                objectNode.put(fieldName, CONFIDENTIAL_CONTENT);
                continue;
            }
            try {
                Object fieldValue = field.get(inst);
                JsonNode jsonNode = globalDebugger.debug(fieldValue, narrowCpt, maxNarrowCpt, globalDebugger, objectAlreadyProcessed);
                objectNode.put(fieldName, jsonNode);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return objectNode;
    }
}
