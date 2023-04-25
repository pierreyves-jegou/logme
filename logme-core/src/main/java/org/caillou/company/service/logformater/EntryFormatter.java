package org.caillou.company.service.logformater;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Collection;
import java.util.Map;

public class EntryFormatter implements Formatter {

    private final ObjectMapper objectMapper;

    public EntryFormatter(final ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean support(Class<?> clazz) {
        return Map.Entry.class.isAssignableFrom(clazz);
    }

    @Override
    public JsonNode extractLog(Object inst,
                               int narrowCpt,
                               int maxNarrowCpt,
                               Formatter globalFormatter,
                               Collection<Object> objectAlreadyProcessed) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("pointer", inst.hashCode());
        Map.Entry entry = (Map.Entry) inst;

        objectNode.putIfAbsent("key", globalFormatter.extractLog(entry.getKey(), narrowCpt, maxNarrowCpt, globalFormatter, objectAlreadyProcessed));
        objectNode.putIfAbsent("value", globalFormatter.extractLog(entry.getValue(), narrowCpt, maxNarrowCpt, globalFormatter, objectAlreadyProcessed));
        return objectNode;
    }
}
