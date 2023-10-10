package org.kie.kogito.jobs.service.repository.impl;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.kie.kogito.jobs.service.repository.ReactiveJobRepository;

import io.vertx.core.Vertx;

class InMemoryJobRepositoryTest extends BaseJobRepositoryTest {

    private InMemoryJobRepository tested;
    private static Vertx vertx;

    @BeforeAll
    static void init() {
        vertx = Vertx.vertx();
    }

    @BeforeEach
    public void setUp() throws Exception {
        tested = new InMemoryJobRepository(vertx, mockJobStreams());
        super.setUp();
    }

    @Override
    public ReactiveJobRepository tested() {
        return tested;
    }
}
