package org.kie.kogito.persistence.postgresql.reporting.model;

import java.util.List;

import org.kie.kogito.persistence.reporting.model.BaseMappingDefinition;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PostgresMappingDefinition extends BaseMappingDefinition<JsonType, PostgresField, PostgresPartitionField, PostgresJsonField, PostgresMapping> {

    PostgresMappingDefinition() {
    }

    public PostgresMappingDefinition(final String mappingId,
            final String sourceTableName,
            final String sourceTableJsonFieldName,
            final List<PostgresField> sourceTableIdentityFields,
            final List<PostgresPartitionField> sourceTablePartitionFields,
            final String targetTableName,
            final List<PostgresMapping> fieldMappings) {
        super(mappingId,
                sourceTableName,
                sourceTableJsonFieldName,
                sourceTableIdentityFields,
                sourceTablePartitionFields,
                targetTableName,
                fieldMappings);
    }
}
