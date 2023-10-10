package org.kie.kogito.persistence.postgresql.reporting.bootstrap;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
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
import org.kie.kogito.persistence.reporting.bootstrap.BaseStartupHandler;
import org.kie.kogito.persistence.reporting.database.SchemaGenerationAction;

import io.quarkus.runtime.StartupEvent;

@ApplicationScoped
public class PostgresStartupHandlerImpl
        extends BaseStartupHandler<JsonType, PostgresField, PostgresPartitionField, PostgresJsonField, PostgresMapping, PostgresMappingDefinition, PostgresMappingDefinitions, PostgresContext> {

    protected PostgresStartupHandlerImpl() {
        //CDI proxy
    }

    @Inject
    public PostgresStartupHandlerImpl(final PostgresBootstrapLoaderImpl loader,
            final BasePostgresDatabaseManagerImpl databaseManager,
            final PostgresMappingServiceImpl mappingService,
            final @ConfigProperty(name = "quarkus.hibernate-orm.database.generation") String action) {
        super(loader,
                databaseManager,
                mappingService,
                SchemaGenerationAction.fromString(action));
    }

    void onStartup(final @Observes StartupEvent event) {
        super.onStartup();
    }
}
