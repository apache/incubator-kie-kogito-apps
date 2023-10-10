package org.kie.kogito.index.oracle.query;

import javax.inject.Inject;

import org.kie.kogito.index.model.UserTaskInstance;
import org.kie.kogito.index.oracle.storage.UserTaskInstanceEntityStorage;
import org.kie.kogito.index.test.query.AbstractUserTaskInstanceQueryIT;
import org.kie.kogito.persistence.api.Storage;
import org.kie.kogito.testcontainers.quarkus.OracleSqlQuarkusTestResource;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@QuarkusTestResource(OracleSqlQuarkusTestResource.class)
class UserTaskInstanceEntityQueryIT extends AbstractUserTaskInstanceQueryIT {

    @Inject
    UserTaskInstanceEntityStorage storage;

    @Override
    public Storage<String, UserTaskInstance> getStorage() {
        return storage;
    }

    @Override
    protected Boolean isDateTimeAsLong() {
        return false;
    }
}
