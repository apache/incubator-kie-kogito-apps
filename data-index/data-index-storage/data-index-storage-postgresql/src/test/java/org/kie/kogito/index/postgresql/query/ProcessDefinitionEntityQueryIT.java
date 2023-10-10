package org.kie.kogito.index.postgresql.query;

import javax.inject.Inject;

import org.kie.kogito.index.model.ProcessDefinition;
import org.kie.kogito.index.postgresql.storage.ProcessDefinitionEntityStorage;
import org.kie.kogito.index.test.query.AbstractProcessDefinitionQueryIT;
import org.kie.kogito.persistence.api.Storage;
import org.kie.kogito.testcontainers.quarkus.PostgreSqlQuarkusTestResource;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@QuarkusTestResource(PostgreSqlQuarkusTestResource.class)
class ProcessDefinitionEntityQueryIT extends AbstractProcessDefinitionQueryIT {

    @Inject
    ProcessDefinitionEntityStorage storage;

    @Override
    public Storage<String, ProcessDefinition> getStorage() {
        return storage;
    }

    @Override
    protected Boolean isDateTimeAsLong() {
        return false;
    }
}
