package org.kie.kogito.index.json;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.cloudevents.jackson.JsonFormat;

public final class JsonUtils {

    private static final ObjectMapper MAPPER = configure(new ObjectMapper());

    private JsonUtils() {
    }

    public static ObjectMapper getObjectMapper() {
        return MAPPER;
    }

    public static ObjectMapper configure(ObjectMapper objectMapper) {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.registerModule(JsonFormat.getCloudEventJacksonModule());
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }
}
