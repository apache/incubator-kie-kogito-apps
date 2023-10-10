package org.kie.kogito.persistence.postgresql.reporting.model;

import java.util.Collection;

import org.kie.kogito.persistence.reporting.model.BaseMappingDefinitions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PostgresMappingDefinitions extends BaseMappingDefinitions<JsonType, PostgresField, PostgresPartitionField, PostgresJsonField, PostgresMapping, PostgresMappingDefinition> {

    PostgresMappingDefinitions() {
    }

    public PostgresMappingDefinitions(final Collection<PostgresMappingDefinition> mappingDefinitions) {
        super(mappingDefinitions);
    }

}
