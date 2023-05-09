package org.caillou.company.service.logformater;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.caillou.company.service.GlobalDebugger;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public class PrimitiveDebugger implements Debugger{

    private Set<String> whiteClassPrimitiveNameSet = new HashSet<>();
    private final ObjectMapper objectMapper;

    public PrimitiveDebugger(final ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
        whiteClassPrimitiveNameSet.add("int");
        whiteClassPrimitiveNameSet.add("double");
        whiteClassPrimitiveNameSet.add("float");
        whiteClassPrimitiveNameSet.add("short");
        whiteClassPrimitiveNameSet.add("long");
        whiteClassPrimitiveNameSet.add("boolean");
        whiteClassPrimitiveNameSet.add("char");
    }

    @Override
    public Predicate<Class<?>> support() {
        return clazz -> whiteClassPrimitiveNameSet.contains(clazz.getName());
    }

    @Override
    public JsonNode debug(Object inst, int narrowCpt, int maxNarrowCpt, GlobalDebugger globalDebugger, Collection<Object> objectAlreadyProcessed) {
        if (inst == null) {
            return NullNode.getInstance();
        }
        return new TextNode(inst.toString());
    }
}
