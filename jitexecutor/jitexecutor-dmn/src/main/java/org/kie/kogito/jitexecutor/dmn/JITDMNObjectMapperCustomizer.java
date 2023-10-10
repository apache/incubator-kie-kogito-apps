package org.kie.kogito.jitexecutor.dmn;

import javax.inject.Singleton;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkus.jackson.ObjectMapperCustomizer;

@Singleton
public class JITDMNObjectMapperCustomizer implements ObjectMapperCustomizer {

    public void customize(ObjectMapper mapper) {
        mapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule())
                .registerModule(new com.fasterxml.jackson.databind.module.SimpleModule().addSerializer(
                        org.kie.dmn.feel.lang.types.impl.ComparablePeriod.class,
                        new org.kie.kogito.dmn.rest.DMNFEELComparablePeriodSerializer()))
                .disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS);
    }
}
