package org.kie.kogito.persistence.postgresql.reporting.model;

import org.kie.kogito.persistence.reporting.model.BasePartitionField;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PostgresPartitionField extends BasePartitionField {

    PostgresPartitionField() {
    }

    public PostgresPartitionField(final String fieldName,
            final String value) {
        super(fieldName, value);
    }

}
