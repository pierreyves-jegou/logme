package org.caillou.company.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.caillou.company.annotation.Confidential;
import org.caillou.company.service.logformater.BasicFormatter;
import org.caillou.company.service.logformater.CollectionFormatter;
import org.caillou.company.service.logformater.EntryFormatter;
import org.caillou.company.service.logformater.Formatter;
import org.caillou.company.service.logformater.todelete.Adress;
import org.caillou.company.service.logformater.todelete.Author;
import org.caillou.company.service.logformater.todelete.Book;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.*;

import static org.caillou.company.constant.ConfidentialConstant.CONFIDENTIAL_CONTENT;

public class GlobalFormatter implements Formatter {

    private final List<Formatter> formatters = new ArrayList<>();
    private final ObjectMapper objectMapper;

    public GlobalFormatter(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void addHandler(Formatter formatter) {
        this.formatters.add(formatter);
    }

    public void addHandlers(List<Formatter> formatters) {
        this.formatters.addAll(formatters);
    }

    @Override
    public boolean support(Class<?> clazz) {
        return true;
    }

    public String extractLogs(int maxNarrowCpt, AbstractMap.SimpleEntry<Parameter, Object> ... parametersEntries){
        if(parametersEntries == null){
            return null;
        }

        ObjectNode objectNode = objectMapper.createObjectNode();

        for(AbstractMap.SimpleEntry<Parameter, Object> entry : parametersEntries){
            Parameter parameter = entry.getKey();
            Object result = entry.getValue();
            objectNode.put(parameter.getName(), extractLog(result, 0, maxNarrowCpt, this, new ArrayList<>()));
        }

        return objectNode.toString();
    }

    @Override
    public JsonNode extractLog(
            Object inst,
            int narrowCpt,
            int maxNarrowCpt,
            Formatter globalFormatter,
            Collection<Object> objectAlreadyProcessed
    ) {
        if(inst.getClass().getAnnotation(Confidential.class) != null){
            return new TextNode(CONFIDENTIAL_CONTENT);
        }

        if (narrowCpt > maxNarrowCpt) {
            return new TextNode("...");
        }

        if (inst == null) {
            return NullNode.getInstance();
        }

        if(hasAlreadyBeenProcessed(inst, objectAlreadyProcessed)){
            return new TextNode("pointer " + inst.hashCode() + " already logged");
        }
        objectAlreadyProcessed.add(inst);

        Class<?> clazz = inst.getClass();
        for (Formatter formatter : this.formatters) {
            if (formatter.support(clazz)) {
                return formatter.extractLog(inst, narrowCpt + 1, maxNarrowCpt, this, objectAlreadyProcessed);
            }
        }

        return defaultExtractLog(inst, narrowCpt, maxNarrowCpt, clazz, objectAlreadyProcessed);
    }

    private boolean hasAlreadyBeenProcessed(Object result, Collection<Object> alreadyProcessedList){
        for(Object alreadyProccessed : alreadyProcessedList){
            if(result == alreadyProccessed){
                return true;
            }
        }
        return false;
    }

    private JsonNode defaultExtractLog(
            Object inst,
            int narrowCpt,
            int maxNarrowCpt,
            Class<?> clazz,
            Collection<Object> objectAlreadyProcessed) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("pointer", inst.hashCode());
        for (int i = 0; i < clazz.getDeclaredFields().length; i++) {
            Field field = clazz.getDeclaredFields()[i];
            field.trySetAccessible();
            String fieldName = field.getName();
            if(field.getAnnotation(Confidential.class) != null){
                objectNode.put(fieldName, CONFIDENTIAL_CONTENT);
                continue;
            }
            try {
                Object fieldValue = field.get(inst);
                JsonNode jsonNode = extractLog(fieldValue, narrowCpt + 1, maxNarrowCpt, this, objectAlreadyProcessed);
                objectNode.put(fieldName, jsonNode);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return objectNode;
    }


    public static void main(String[] args) {
        ObjectMapper objectMapper1 = new ObjectMapper();
        GlobalFormatter logFormater = new GlobalFormatter(new ObjectMapper());
        CollectionFormatter collectionHandler = new CollectionFormatter(objectMapper1);
        logFormater.addHandler(collectionHandler);
        logFormater.addHandler(new BasicFormatter());
        logFormater.addHandler(new EntryFormatter(objectMapper1));

        Adress adress = new Adress("19", "rue des mouettes", 78887);
        Adress adressJohn = new Adress("4", "rue de montpellier", 245500);
        Author author = new Author("jean", 18, adress);
        Author authorBis = new Author("john", 19, adressJohn);

        Book book = new Book("autant en emporte", 1879, Arrays.asList(author, authorBis));
        Book bookBis = new Book("répare ta 206", 1879, Arrays.asList(authorBis));
        authorBis.setBooks(Arrays.asList(bookBis, book));
        author.setBooks(Arrays.asList(book));

        JsonNode jsonNode = logFormater.extractLog(book, 0, 7, null, new ArrayList<>());
        System.out.println(jsonNode);
    }

}
