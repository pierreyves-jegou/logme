package org.caillou.company.service.logformater;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.caillou.company.service.GlobalDebugger;

import java.util.Collection;
import java.util.function.Predicate;

public class BasicDebugger implements Debugger {


    @Override
    public Predicate<Class<?>> support(){
        return clazz -> Number.class.isAssignableFrom(clazz) || String.class.isAssignableFrom(clazz)
                || Boolean.class.isAssignableFrom(clazz);
    }


    @Override
    public JsonNode debug(Object inst,
                          int narrowCpt,
                          int maxNarrowCpt,
                          GlobalDebugger globalDebugger,
                          Collection<Object> objectAlreadyProcessed) {
        if (inst == null) {
            return NullNode.getInstance();
        }
        return new TextNode(inst.toString());
    }
}
