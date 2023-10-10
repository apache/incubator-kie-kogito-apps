package org.kie.kogito.index.graphql;

import java.time.ZonedDateTime;

import javax.enterprise.context.ApplicationScoped;

import io.quarkus.arc.properties.IfBuildProperty;

import graphql.language.StringValue;

import static org.kie.kogito.persistence.api.factory.Constants.PERSISTENCE_TYPE_PROPERTY;

@ApplicationScoped
@IfBuildProperty(name = PERSISTENCE_TYPE_PROPERTY, stringValue = "postgresql")
public class PostgreSqlDateTimeCoercing extends DefaultDateTimeCoercing implements DateTimeCoercing {

    @Override
    public Object parseValue(Object input) {
        return input == null ? null : getZonedDateTime((String) input);
    }

    private ZonedDateTime getZonedDateTime(String input) {
        return parseDateTime(input);
    }

    @Override
    public Object parseLiteral(Object input) {
        if (input instanceof StringValue) {
            return getZonedDateTime(((StringValue) input).getValue());
        } else {
            return null;
        }
    }
}
