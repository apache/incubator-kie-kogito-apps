package org.kie.kogito.jobs.service.repository.inmemory.postgresql;

import java.time.Duration;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.kie.kogito.jobs.service.repository.ReactiveJobRepository;
import org.kie.kogito.jobs.service.repository.impl.BaseJobRepositoryTest;
import org.kie.kogito.jobs.service.repository.postgresql.PostgreSqlJobRepository;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import io.vertx.mutiny.pgclient.PgPool;

@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class InmemoryPostgreSqlJobRepositoryTest extends BaseJobRepositoryTest {

    @Inject
    PostgreSqlJobRepository tested;

    @Inject
    PgPool client;

    @BeforeEach
    public void setUp() throws Exception {
        client.query("DELETE FROM job_details")
                .execute()
                .emitOn(Infrastructure.getDefaultExecutor())
                .await().atMost(Duration.ofSeconds(10L));
        super.setUp();
    }

    @Override
    public ReactiveJobRepository tested() {
        return tested;
    }
}
