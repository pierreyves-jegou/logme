package org.caillou.company.service.logformater;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.TextNode;

import java.util.Collection;
import java.util.Set;

public class BasicFormatter implements Formatter {
    @Override
    public boolean support(Class<?> clazz) {
        return Number.class.isAssignableFrom(clazz) || String.class.isAssignableFrom(clazz);
    }

    @Override
    public JsonNode extractLog(Object inst,
                               int narrowCpt,
                               int maxNarrowCpt,
                               Formatter globalFormatter,
                               Collection<Object> objectAlreadyProcessed) {
        if (inst == null) {
            return NullNode.getInstance();
        }
        return new TextNode(inst.toString());
    }
}
