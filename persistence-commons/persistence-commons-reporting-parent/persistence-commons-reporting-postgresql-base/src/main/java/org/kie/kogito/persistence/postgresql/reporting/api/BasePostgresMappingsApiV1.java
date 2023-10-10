package org.kie.kogito.persistence.postgresql.reporting.api;

import java.util.List;

import org.kie.kogito.persistence.postgresql.reporting.database.BasePostgresDatabaseManagerImpl;
import org.kie.kogito.persistence.postgresql.reporting.database.sqlbuilders.PostgresContext;
import org.kie.kogito.persistence.postgresql.reporting.model.JsonType;
import org.kie.kogito.persistence.postgresql.reporting.model.PostgresField;
import org.kie.kogito.persistence.postgresql.reporting.model.PostgresJsonField;
import org.kie.kogito.persistence.postgresql.reporting.model.PostgresMapping;
import org.kie.kogito.persistence.postgresql.reporting.model.PostgresMappingDefinition;
import org.kie.kogito.persistence.postgresql.reporting.model.PostgresMappingDefinitions;
import org.kie.kogito.persistence.postgresql.reporting.model.PostgresPartitionField;
import org.kie.kogito.persistence.postgresql.reporting.service.PostgresMappingServiceImpl;
import org.kie.kogito.persistence.reporting.api.BaseMappingsApiV1;

public abstract class BasePostgresMappingsApiV1
        extends BaseMappingsApiV1<JsonType, PostgresField, PostgresPartitionField, PostgresJsonField, PostgresMapping, PostgresMappingDefinition, PostgresMappingDefinitions, PostgresContext> {

    protected BasePostgresMappingsApiV1() {
        //CDI proxies
    }

    protected BasePostgresMappingsApiV1(final PostgresMappingServiceImpl mappingService,
            final BasePostgresDatabaseManagerImpl databaseManager) {
        super(mappingService, databaseManager);
    }

    @Override
    protected PostgresMappingDefinitions buildMappingDefinitions(final List<PostgresMappingDefinition> definitions) {
        return new PostgresMappingDefinitions(definitions);
    }
}
