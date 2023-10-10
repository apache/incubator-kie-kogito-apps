package org.kie.kogito.jobs.service.json;

import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

import org.kie.kogito.jobs.service.api.serlialization.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.cloudevents.jackson.JsonFormat;
import io.quarkus.jackson.ObjectMapperCustomizer;

public class JacksonConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(JacksonConfiguration.class);

    @Singleton
    @Produces
    public ObjectMapperCustomizer customizer() {
        return objectMapper -> {
            LOGGER.debug("Jackson customization initialized.");
            objectMapper
                    .registerModule(new JavaTimeModule())
                    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                    .disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
                    .registerModule(JsonFormat.getCloudEventJacksonModule());
            SerializationUtils.registerDescriptors(objectMapper);
        };
    }
}
