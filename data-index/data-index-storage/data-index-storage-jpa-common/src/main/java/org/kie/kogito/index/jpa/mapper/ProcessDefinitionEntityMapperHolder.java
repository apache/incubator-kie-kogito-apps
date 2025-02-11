package org.kie.kogito.index.jpa.mapper;

public class ProcessDefinitionEntityMapperHolder {

    private static final ProcessDefinitionEntityMapperHolder instance = new ProcessDefinitionEntityMapperHolder();

    public static ProcessDefinitionEntityMapperHolder get() {
        return instance;
    }

    private ProcessDefinitionEntityMapper<?> mapper;

    private ProcessDefinitionEntityMapperHolder() {
    }

    public ProcessDefinitionEntityMapper mapper() {
        return mapper;
    }

    public void mapper(ProcessDefinitionEntityMapper instance) {
        this.mapper = instance;
    }
}
