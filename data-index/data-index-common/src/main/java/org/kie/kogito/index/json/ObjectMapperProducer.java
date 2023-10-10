package org.kie.kogito.index.json;

import javax.enterprise.context.ApplicationScoped;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkus.jackson.ObjectMapperCustomizer;

@ApplicationScoped
public class ObjectMapperProducer implements ObjectMapperCustomizer {

    @Override
    public void customize(ObjectMapper objectMapper) {
        JsonUtils.configure(objectMapper);
    }
}
