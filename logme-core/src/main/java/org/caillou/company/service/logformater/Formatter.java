package org.caillou.company.service.logformater;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Collection;

public interface Formatter {

    boolean support(Class<?> clazz);

    JsonNode extractLog(
            Object inst,
            int narrowCpt,
            int maxNarrowCpt,
            Formatter globalFormatter,
            Collection<Object> objectAlreadyProcessed);

}
