package org.kie.kogito.index.oracle.query;

import javax.inject.Inject;

import org.kie.kogito.index.model.Job;
import org.kie.kogito.index.oracle.storage.JobEntityStorage;
import org.kie.kogito.index.test.query.AbstractJobQueryIT;
import org.kie.kogito.persistence.api.Storage;
import org.kie.kogito.testcontainers.quarkus.OracleSqlQuarkusTestResource;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@QuarkusTestResource(OracleSqlQuarkusTestResource.class)
class JobEntityQueryIT extends AbstractJobQueryIT {

    @Inject
    JobEntityStorage storage;

    @Override
    public Storage<String, Job> getStorage() {
        return storage;
    }

}
