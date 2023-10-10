package org.kie.kogito.index.graphql;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

import javax.enterprise.context.ApplicationScoped;

import org.kie.kogito.index.DateTimeUtils;

import io.quarkus.arc.DefaultBean;

import graphql.language.StringValue;
import graphql.schema.CoercingSerializeException;

@ApplicationScoped
@DefaultBean
public class DefaultDateTimeCoercing implements DateTimeCoercing {

    public static ZonedDateTime parseDateTime(String s) {
        try {
            return DateTimeUtils.parseZonedDateTime(s);
        } catch (DateTimeParseException e) {
            throw new CoercingSerializeException("Invalid ISO-8601 value : '" + s + "'. because of : '" + e.getMessage() + "'");
        }
    }

    public String formatDateTime(ZonedDateTime dateTime) {
        try {
            return DateTimeUtils.formatZonedDateTime(dateTime);
        } catch (DateTimeException e) {
            throw new CoercingSerializeException(
                    "Unable to turn TemporalAccessor into OffsetDateTime because of : '" + e.getMessage() + "'.");
        }
    }

    @Override
    public String serialize(Object input) throws CoercingSerializeException {
        ZonedDateTime dateTime;
        if (input instanceof ZonedDateTime) {
            dateTime = (ZonedDateTime) input;
            dateTime = dateTime.withZoneSameInstant(ZoneOffset.UTC);
        } else if (input instanceof String) {
            try {
                dateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(input.toString())), ZoneOffset.UTC);
            } catch (NumberFormatException ex) {
                dateTime = parseDateTime(input.toString());
            }
        } else if (input instanceof Long) {
            dateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli((Long) input), ZoneOffset.UTC);
        } else {
            throw new CoercingSerializeException(
                    "Expected something we can convert to 'java.time.ZonedDateTime' but was '" + (input == null ? "null" : input.getClass().getName()) + "'.");
        }
        return formatDateTime(dateTime);
    }

    @Override
    public Object parseValue(Object input) {
        return input == null ? null : getDateTimeAsLong((String) input);
    }

    private long getDateTimeAsLong(String input) {
        return parseDateTime(input).truncatedTo(ChronoUnit.MILLIS).toInstant().toEpochMilli();
    }

    @Override
    public Object parseLiteral(Object input) {
        if (input instanceof StringValue) {
            return getDateTimeAsLong(((StringValue) input).getValue());
        } else {
            return null;
        }
    }
}
