package org.kie.kogito.index.oracle.query;

import javax.inject.Inject;

import org.kie.kogito.index.model.ProcessInstance;
import org.kie.kogito.index.oracle.storage.ProcessInstanceEntityStorage;
import org.kie.kogito.index.test.query.AbstractProcessInstanceQueryIT;
import org.kie.kogito.persistence.api.Storage;
import org.kie.kogito.testcontainers.quarkus.OracleSqlQuarkusTestResource;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@QuarkusTestResource(OracleSqlQuarkusTestResource.class)
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
