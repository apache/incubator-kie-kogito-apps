package org.kie.kogito.persistence.postgresql.reporting.model;

import org.kie.kogito.persistence.reporting.model.BaseJsonField;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PostgresJsonField extends BaseJsonField<JsonType> {

    PostgresJsonField() {
    }

    public PostgresJsonField(final String fieldName,
            final JsonType fieldType) {
        super(fieldName, fieldType);
    }

}
