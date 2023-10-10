package org.kie.kogito.persistence.postgresql.reporting.model;

import org.kie.kogito.persistence.reporting.model.BaseMapping;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PostgresMapping extends BaseMapping<JsonType, PostgresJsonField> {

    PostgresMapping() {
    }

    public PostgresMapping(final String sourceJsonPath,
            final PostgresJsonField targetField) {
        super(sourceJsonPath, targetField);
    }
}
