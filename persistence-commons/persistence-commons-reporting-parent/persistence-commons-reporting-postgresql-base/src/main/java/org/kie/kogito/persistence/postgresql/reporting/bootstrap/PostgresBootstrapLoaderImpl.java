package org.kie.kogito.persistence.postgresql.reporting.bootstrap;

import java.io.InputStream;
import java.util.function.Supplier;

import javax.enterprise.context.ApplicationScoped;

import org.kie.kogito.persistence.postgresql.reporting.model.JsonType;
import org.kie.kogito.persistence.postgresql.reporting.model.PostgresField;
import org.kie.kogito.persistence.postgresql.reporting.model.PostgresJsonField;
import org.kie.kogito.persistence.postgresql.reporting.model.PostgresMapping;
import org.kie.kogito.persistence.postgresql.reporting.model.PostgresMappingDefinition;
import org.kie.kogito.persistence.postgresql.reporting.model.PostgresMappingDefinitions;
import org.kie.kogito.persistence.postgresql.reporting.model.PostgresPartitionField;
import org.kie.kogito.persistence.reporting.bootstrap.BaseBootstrapLoaderImpl;

@ApplicationScoped
public class PostgresBootstrapLoaderImpl
        extends BaseBootstrapLoaderImpl<JsonType, PostgresField, PostgresPartitionField, PostgresJsonField, PostgresMapping, PostgresMappingDefinition, PostgresMappingDefinitions> {

    PostgresBootstrapLoaderImpl() {
        //CDI proxy
    }

    //For unit testing
    PostgresBootstrapLoaderImpl(final Supplier<InputStream> inputStreamSupplier) {
        super(inputStreamSupplier);
    }

    @Override
    public Class<PostgresMappingDefinitions> getMappingDefinitionsType() {
        return PostgresMappingDefinitions.class;
    }
}
