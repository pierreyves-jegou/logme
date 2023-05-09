package org.caillou.company.service.logformater;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.caillou.company.service.GlobalDebugger;

import java.util.Collection;
import java.util.function.Predicate;

public class CollectionDebugger implements Debugger {


    private final ObjectMapper objectMapper;

    public CollectionDebugger(final ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }

    @Override
    public Predicate<Class<?>> support(){
        return clazz -> Collection.class.isAssignableFrom(clazz);
    }

    @Override
    public JsonNode debug(Object inst,
                          int narrowCpt,
                          int maxNarrowCpt,
                          GlobalDebugger globalDebugger,
                          Collection<Object> objectAlreadyProcessed) {
        Collection<?> collection = (Collection<?>) inst;
        ArrayNode arrayNode = objectMapper.createArrayNode();
        collection
                .stream()
                .map(nestInst -> globalDebugger.debug(nestInst, narrowCpt, maxNarrowCpt, globalDebugger, objectAlreadyProcessed))
                .forEach(node -> {
                    arrayNode.add(node);
                });
        return arrayNode;
    }
}
