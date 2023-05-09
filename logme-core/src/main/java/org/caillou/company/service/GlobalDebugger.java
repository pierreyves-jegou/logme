package org.caillou.company.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.caillou.company.annotation.Confidential;
import org.caillou.company.service.logformater.*;
import org.caillou.company.service.logformater.todelete.Adress;
import org.caillou.company.service.logformater.todelete.Author;
import org.caillou.company.service.logformater.todelete.Book;

import java.lang.reflect.Parameter;
import java.util.*;
import java.util.function.Predicate;

import static org.caillou.company.constant.ConfidentialConstant.CONFIDENTIAL_CONTENT;

public class GlobalDebugger implements Debugger {

    private final List<Debugger> debuggers = new ArrayList<>();
    private final ObjectMapper objectMapper;

    public GlobalDebugger(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void addHandlers(List<Debugger> debuggers) {
        this.debuggers.addAll(debuggers);
    }


    @Override
    public Predicate<Class<?>> support(){
        //todo : handle getPriority
        throw new IllegalStateException("This method should not be called");
    }

    public String extractLogs(int maxNarrowCpt, AbstractMap.SimpleEntry<Parameter, Object> ... parametersEntries){
        if(parametersEntries == null){
            return null;
        }

        ObjectNode objectNode = objectMapper.createObjectNode();

        for(AbstractMap.SimpleEntry<Parameter, Object> entry : parametersEntries){
            Parameter parameter = entry.getKey();
            Object result = entry.getValue();
            objectNode.put(parameter.getName(), debug(result, 0, maxNarrowCpt, this, new ArrayList<>()));
        }

        return objectNode.toString();
    }

    @Override
    public JsonNode debug(
            Object inst,
            int narrowCpt,
            int maxNarrowCpt,
            GlobalDebugger globalDebugger,
            Collection<Object> objectAlreadyProcessed) {

        if (inst == null) {
            return NullNode.getInstance();
        }

        if(inst.getClass().getAnnotation(Confidential.class) != null){
            return new TextNode(CONFIDENTIAL_CONTENT);
        }

        if (narrowCpt > maxNarrowCpt) {
            return new TextNode("...");
        }

        if(hasAlreadyBeenProcessed(inst, objectAlreadyProcessed)){
            return new TextNode("pointer " + inst.hashCode() + " already logged");
        }
        objectAlreadyProcessed.add(inst);

        Class<?> clazz = inst.getClass();
        Debugger suitableDebugger = detectSuitableDebugger(clazz);
        if(suitableDebugger == null){
            return new TextNode("not loggable");
        }

        return suitableDebugger.debug(inst, narrowCpt + 1, maxNarrowCpt, this, objectAlreadyProcessed);
    }


    public Debugger detectSuitableDebugger(Class<?> clazz){
        for (Debugger debugger : this.debuggers) {
            if (debugger.support().test(clazz)) {
                return debugger;
            }
        }
        return null;
    }

    private boolean hasAlreadyBeenProcessed(Object result, Collection<Object> alreadyProcessedList){
        for(Object alreadyProcessed : alreadyProcessedList){
            if(result == alreadyProcessed){
                return true;
            }
        }
        return false;
    }


    public static void main(String[] args) {
        String whitePackages = "org.caillou.company";
        ObjectMapper objectMapper = new ObjectMapper();
        BasicDebugger basicFormatter = new BasicDebugger();
        CollectionDebugger collectionFormatter = new CollectionDebugger(objectMapper);
        EntryDebugger entryFormatter = new EntryDebugger(objectMapper);
        GlobalDebugger globalFormatter = new GlobalDebugger(objectMapper);
        ProjectPackageDebugger projectPackageDebugger = new ProjectPackageDebugger(objectMapper, whitePackages);
        PrimitiveDebugger primitiveDebugger = new PrimitiveDebugger(objectMapper);
        globalFormatter.addHandlers(Arrays.asList(primitiveDebugger, basicFormatter, projectPackageDebugger, collectionFormatter, entryFormatter));


        Adress adress = new Adress("19", "rue des mouettes", 78887);
        Adress adressJohn = new Adress("4", "rue de montpellier", 245500);
        Author author = new Author("jean", 18, adress);
        Author authorBis = new Author("john", 19, adressJohn);

        Book book = new Book("autant en emporte", 1879, Arrays.asList(author, authorBis));
        Book bookBis = new Book("répare ta 206", 1879, Arrays.asList(authorBis));
        authorBis.setBooks(Arrays.asList(bookBis, book));
        author.setBooks(Arrays.asList(book));

        JsonNode jsonNode = globalFormatter.debug(book, 0, 7, null, new ArrayList<>());
        System.out.println(jsonNode);
    }

}
