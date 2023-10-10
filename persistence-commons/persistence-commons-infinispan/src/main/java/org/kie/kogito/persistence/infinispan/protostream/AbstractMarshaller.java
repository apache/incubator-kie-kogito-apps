package org.kie.kogito.persistence.infinispan.protostream;

import java.io.IOException;
import java.io.StringWriter;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class AbstractMarshaller {

    protected ObjectMapper mapper;

    protected AbstractMarshaller(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public ZonedDateTime dateToZonedDateTime(Date date) {
        return date == null ? null : ZonedDateTime.ofInstant(date.toInstant(), ZoneOffset.UTC);
    }

    public Date zonedDateTimeToDate(ZonedDateTime date) {
        return date == null ? null : Date.from(date.toInstant());
    }

    public JsonNode jsonFromString(String value) throws IOException {
        return jsonFromString(mapper, value);
    }

    public String stringFromJson(JsonNode value) throws IOException {
        return stringFromJson(mapper, value);
    }

    public static JsonNode jsonFromString(ObjectMapper mapper, String value) throws IOException {
        return value == null ? null : mapper.readTree(value);
    }

    public static String stringFromJson(ObjectMapper mapper, JsonNode value) throws IOException {
        if (value == null) {
            return null;
        }
        StringWriter writer = new StringWriter();
        mapper.writeTree(mapper.getFactory().createGenerator(writer), value);
        return writer.toString();
    }

    public static <T extends Enum<T>> T enumFromString(String value, Class<T> enumClass) {
        return value == null ? null : Enum.valueOf(enumClass, value);
    }

    public static <T extends Enum<T>> String stringFromEnum(T value) {
        if (value == null) {
            return null;
        }
        return value.name();
    }
}
