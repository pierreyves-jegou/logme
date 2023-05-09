package org.caillou.company.service.logformater;

import com.fasterxml.jackson.databind.JsonNode;
import org.caillou.company.service.GlobalDebugger;

import java.util.Collection;
import java.util.function.Predicate;

public interface Debugger {


    Predicate<Class<?>> support();

    default Integer getPriority(){
        return 500;
    }

    JsonNode debug(
            Object inst,
            int narrowCpt,
            int maxNarrowCpt,
            GlobalDebugger globalDebugger,
            Collection<Object> objectAlreadyProcessed);

}
