package org.kie.kogito.jobs.service.repository.mongodb;

import java.time.Duration;

import javax.inject.Inject;

import org.bson.Document;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.BeforeEach;
import org.kie.kogito.jobs.service.repository.ReactiveJobRepository;
import org.kie.kogito.jobs.service.repository.impl.BaseJobRepositoryTest;
import org.kie.kogito.testcontainers.quarkus.MongoDBQuarkusTestResource;

import io.quarkus.mongodb.reactive.ReactiveMongoClient;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

import static org.kie.kogito.jobs.service.repository.mongodb.MongoDBJobRepository.DATABASE_PROPERTY;
import static org.kie.kogito.jobs.service.repository.mongodb.MongoDBJobRepository.JOB_DETAILS_COLLECTION;

@QuarkusTest
@QuarkusTestResource(MongoDBQuarkusTestResource.class)
public class MongoDBJobRepositoryTest extends BaseJobRepositoryTest {

    @Inject
    MongoDBJobRepository tested;

    @Inject
    ReactiveMongoClient client;

    @ConfigProperty(name = DATABASE_PROPERTY)
    String database;

    @BeforeEach
    public void setUp() throws Exception {
        client.getDatabase(database)
                .getCollection(JOB_DETAILS_COLLECTION)
                .deleteMany(new Document())
                .await().atMost(Duration.ofSeconds(10L));
        super.setUp();
    }

    @Override
    public ReactiveJobRepository tested() {
        return tested;
    }
}
