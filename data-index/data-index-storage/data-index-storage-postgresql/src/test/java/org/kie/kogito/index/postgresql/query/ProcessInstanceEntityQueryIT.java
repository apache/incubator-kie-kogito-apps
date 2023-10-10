package org.kie.kogito.index.postgresql.query;

import javax.inject.Inject;

import org.kie.kogito.index.model.ProcessInstance;
import org.kie.kogito.index.postgresql.storage.ProcessInstanceEntityStorage;
import org.kie.kogito.index.test.query.AbstractProcessInstanceQueryIT;
import org.kie.kogito.persistence.api.Storage;
import org.kie.kogito.testcontainers.quarkus.PostgreSqlQuarkusTestResource;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@QuarkusTestResource(PostgreSqlQuarkusTestResource.class)
class ProcessInstanceEntityQueryIT extends AbstractProcessInstanceQueryIT {

    @Inject
    ProcessInstanceEntityStorage storage;

    @Override
    public Storage<String, ProcessInstance> getStorage() {
        return storage;
    }

    @Override
    protected Boolean isDateTimeAsLong() {
        return false;
    }
}
