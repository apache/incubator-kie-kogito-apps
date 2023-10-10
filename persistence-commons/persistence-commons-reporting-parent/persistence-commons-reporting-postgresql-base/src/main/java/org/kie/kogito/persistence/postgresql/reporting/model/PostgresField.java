package org.kie.kogito.persistence.postgresql.reporting.model;

import org.kie.kogito.persistence.reporting.model.BaseField;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PostgresField extends BaseField {

    PostgresField() {
    }

    public PostgresField(final String fieldName) {
        super(fieldName);
    }

}
