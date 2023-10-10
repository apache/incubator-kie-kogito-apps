package org.kie.kogito.index.mongodb.query;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.kie.kogito.index.model.Job;
import org.kie.kogito.index.mongodb.model.JobEntity;
import org.kie.kogito.index.mongodb.model.JobEntityMapper;
import org.kie.kogito.index.test.query.AbstractJobQueryIT;
import org.kie.kogito.persistence.api.Storage;
import org.kie.kogito.persistence.mongodb.client.MongoClientManager;
import org.kie.kogito.persistence.mongodb.storage.MongoStorage;
import org.kie.kogito.testcontainers.quarkus.MongoDBQuarkusTestResource;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

import static org.kie.kogito.index.storage.Constants.JOBS_STORAGE;

@QuarkusTest
@QuarkusTestResource(MongoDBQuarkusTestResource.class)
class JobQueryIT extends AbstractJobQueryIT {

    @Inject
    MongoClientManager mongoClientManager;

    Storage<String, Job> storage;

    @BeforeEach
    void setUp() {
        this.storage = new MongoStorage<>(mongoClientManager.getCollection(JOBS_STORAGE, JobEntity.class),
                Job.class.getName(), new JobEntityMapper());
    }

    @Override
    public Storage<String, Job> getStorage() {
        return storage;
    }

}
