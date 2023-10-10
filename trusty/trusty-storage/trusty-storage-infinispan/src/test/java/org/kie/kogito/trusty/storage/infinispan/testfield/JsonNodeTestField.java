package org.kie.kogito.trusty.storage.infinispan.testfield;

import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.kie.kogito.persistence.infinispan.protostream.AbstractMarshaller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonNodeTestField<M> extends StringTestField<M> {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public JsonNodeTestField(String fieldName, JsonNode fieldValue, Function<M, JsonNode> getter, BiConsumer<M, JsonNode> setter) {
        super(fieldName, stringFromJson(fieldValue), obj -> stringFromJson(getter.apply(obj)), (obj, value) -> setter.accept(obj, jsonFromString(value)));
    }

    private static JsonNode jsonFromString(String value) {
        try {
            return AbstractMarshaller.jsonFromString(MAPPER, value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String stringFromJson(JsonNode value) {
        try {
            return AbstractMarshaller.stringFromJson(MAPPER, value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
