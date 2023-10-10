package org.kie.kogito.index.postgresql.query;

import javax.inject.Inject;

import org.kie.kogito.index.model.Job;
import org.kie.kogito.index.postgresql.storage.JobEntityStorage;
import org.kie.kogito.index.test.query.AbstractJobQueryIT;
import org.kie.kogito.persistence.api.Storage;
import org.kie.kogito.testcontainers.quarkus.PostgreSqlQuarkusTestResource;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@QuarkusTestResource(PostgreSqlQuarkusTestResource.class)
class JobEntityQueryIT extends AbstractJobQueryIT {

    @Inject
    JobEntityStorage storage;

    @Override
    public Storage<String, Job> getStorage() {
        return storage;
    }

}
