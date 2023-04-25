package org.caillou.company.service.logformater;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.Collection;
import java.util.Set;

public class CollectionFormatter implements Formatter {


    private final ObjectMapper objectMapper;

    public CollectionFormatter(final ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean support(Class<?> clazz) {
        return Collection.class.isAssignableFrom(clazz);
    }

    @Override
    public JsonNode extractLog(Object inst,
                               int narrowCpt,
                               int maxNarrowCpt,
                               Formatter globalFormatter,
                               Collection<Object> objectAlreadyProcessed) {
        Collection<?> collection = (Collection<?>) inst;
        ArrayNode arrayNode = objectMapper.createArrayNode();
        collection
                .stream()
                .map(nestInst -> globalFormatter.extractLog(nestInst, narrowCpt, maxNarrowCpt, globalFormatter, objectAlreadyProcessed))
                .forEach(node -> {
                    arrayNode.add(node);
                });
        return arrayNode;
    }
}
