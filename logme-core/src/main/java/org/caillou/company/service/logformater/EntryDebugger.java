package org.caillou.company.service.logformater;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.caillou.company.service.GlobalDebugger;

import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;

public class EntryDebugger implements Debugger {

    private final ObjectMapper objectMapper;

    public EntryDebugger(final ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }

    @Override
    public Predicate<Class<?>> support(){
        return clazz -> Map.Entry.class.isAssignableFrom(clazz);
    }


    @Override
    public JsonNode debug(Object inst,
                          int narrowCpt,
                          int maxNarrowCpt,
                          GlobalDebugger globalDebugger,
                          Collection<Object> objectAlreadyProcessed) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("pointer", inst.hashCode());
        Map.Entry entry = (Map.Entry) inst;

        objectNode.putIfAbsent("key", globalDebugger.debug(entry.getKey(), narrowCpt, maxNarrowCpt, globalDebugger, objectAlreadyProcessed));
        objectNode.putIfAbsent("value", globalDebugger.debug(entry.getValue(), narrowCpt, maxNarrowCpt, globalDebugger, objectAlreadyProcessed));
        return objectNode;
    }
}
