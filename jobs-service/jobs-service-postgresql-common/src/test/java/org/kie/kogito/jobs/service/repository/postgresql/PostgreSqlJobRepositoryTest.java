package org.kie.kogito.jobs.service.repository.postgresql;

import java.time.Duration;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.kie.kogito.jobs.service.repository.ReactiveJobRepository;
import org.kie.kogito.jobs.service.repository.impl.BaseJobRepositoryTest;
import org.kie.kogito.testcontainers.quarkus.PostgreSqlQuarkusTestResource;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.vertx.mutiny.pgclient.PgPool;

@QuarkusTest
@QuarkusTestResource(PostgreSqlQuarkusTestResource.class)
public class PostgreSqlJobRepositoryTest extends BaseJobRepositoryTest {

    @Inject
    PostgreSqlJobRepository tested;

    @Inject
    PgPool client;

    @BeforeEach
    public void setUp() throws Exception {
        client.query("DELETE FROM job_details")
                .execute()
                .await().atMost(Duration.ofSeconds(10L));
        client.query("DELETE FROM job_service_management")
                .execute()
                .await().atMost(Duration.ofSeconds(10L));
        super.setUp();
    }

    @Override
    public ReactiveJobRepository tested() {
        return tested;
    }
}
