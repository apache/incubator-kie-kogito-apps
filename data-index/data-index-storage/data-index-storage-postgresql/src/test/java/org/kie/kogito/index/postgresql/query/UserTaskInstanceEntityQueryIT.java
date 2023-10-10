package org.kie.kogito.index.postgresql.query;

import javax.inject.Inject;

import org.kie.kogito.index.model.UserTaskInstance;
import org.kie.kogito.index.postgresql.storage.UserTaskInstanceEntityStorage;
import org.kie.kogito.index.test.query.AbstractUserTaskInstanceQueryIT;
import org.kie.kogito.persistence.api.Storage;
import org.kie.kogito.testcontainers.quarkus.PostgreSqlQuarkusTestResource;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@QuarkusTestResource(PostgreSqlQuarkusTestResource.class)
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
