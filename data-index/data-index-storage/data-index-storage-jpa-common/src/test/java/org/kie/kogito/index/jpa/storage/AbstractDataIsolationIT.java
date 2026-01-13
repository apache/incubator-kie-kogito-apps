/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.kie.kogito.index.jpa.storage;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

import org.junit.jupiter.api.Test;
import org.kie.kogito.index.jpa.model.JobEntity;
import org.kie.kogito.index.jpa.model.NodeInstanceEntity;
import org.kie.kogito.index.jpa.model.ProcessInstanceEntity;
import org.kie.kogito.index.jpa.model.UserTaskInstanceEntity;
import org.kie.kogito.index.model.ProcessInstanceState;
import org.kie.kogito.process.Process;
import org.kie.kogito.process.Processes;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Abstract integration test for data isolation filtering.
 *
 * Concrete implementations must handle database cleanup to ensure test isolation:
 * - Quarkus: Use @TestTransaction at class level + @Transactional @BeforeEach cleanup
 * - Spring Boot: Use @Transactional + @DirtiesContext at class level
 *
 * The cleanup must run in a separate transaction before each test to remove data
 * from other test classes that may have run in the same test suite.
 */
public abstract class AbstractDataIsolationIT {

    EntityManager entityManager;

    JPADataIndexStorageService storageService;

    public AbstractDataIsolationIT(EntityManager entityManager, JPADataIndexStorageService storageService) {
        this.entityManager = entityManager;
        this.storageService = storageService;
    }

    /**
     * Hook for subclasses to wrap operations in transactions.
     * Quarkus: No-op (uses @TestTransaction)
     * Spring Boot: Wraps in TransactionTemplate
     */
    protected void executeInTransaction(Runnable operation) {
        operation.run();
    }

    /**
     * Test that ProcessInstanceEntity filtering works with direct processId field
     */
    @Test
    @Transactional
    public void testProcessInstanceDataIsolation() {
        // Insert process instances directly into database
        ProcessInstanceEntity travel = createProcessInstance("travel", UUID.randomUUID().toString());
        ProcessInstanceEntity orders = createProcessInstance("orders", UUID.randomUUID().toString());
        ProcessInstanceEntity hiring = createProcessInstance("hiring", UUID.randomUUID().toString());

        executeInTransaction(() -> {
            entityManager.persist(travel);
            entityManager.persist(orders);
            entityManager.persist(hiring);
            entityManager.flush();
        });

        // Query with data isolation for travel and orders only
        Processes processes = createProcesses(Set.of("travel", "orders"));
        JPAQuery<ProcessInstanceEntity, ProcessInstanceEntity> query = new JPAQuery<>(
                entityManager,
                Function.identity(),
                ProcessInstanceEntity.class,
                Optional.empty(),
                Optional.of(processes));

        List<ProcessInstanceEntity> results = query.execute();

        // Should only return travel and orders, not hiring
        assertThat(results).hasSize(2);
        assertThat(results).extracting("processId")
                .containsExactlyInAnyOrder("travel", "orders");
    }

    /**
     * Test that querying without Processes returns all entities (no filtering)
     */
    @Test
    @Transactional
    public void testNoDataIsolationWhenProcessesNotProvided() {
        // Insert process instances directly into database
        ProcessInstanceEntity travel = createProcessInstance("travel", UUID.randomUUID().toString());
        ProcessInstanceEntity orders = createProcessInstance("orders", UUID.randomUUID().toString());
        ProcessInstanceEntity hiring = createProcessInstance("hiring", UUID.randomUUID().toString());

        executeInTransaction(() -> {
            entityManager.persist(travel);
            entityManager.persist(orders);
            entityManager.persist(hiring);
            entityManager.flush();
        });

        // Query without Processes (no data isolation)
        JPAQuery<ProcessInstanceEntity, ProcessInstanceEntity> query = new JPAQuery<>(
                entityManager,
                Function.identity(),
                ProcessInstanceEntity.class,
                Optional.empty(),
                Optional.empty());

        List<ProcessInstanceEntity> results = query.execute();

        // Should return all three test instances
        assertThat(results).hasSize(3);
        assertThat(results).extracting("processId")
                .containsExactlyInAnyOrder("travel", "orders", "hiring");
    }

    /**
     * Test that empty processIds set returns no results
     */
    @Test
    @Transactional
    public void testEmptyProcessIdsReturnsNothing() {
        // Query with empty process IDs - should return nothing regardless of existing data
        Processes processes = createProcesses(Set.of());
        JPAQuery<ProcessInstanceEntity, ProcessInstanceEntity> query = new JPAQuery<>(
                entityManager,
                Function.identity(),
                ProcessInstanceEntity.class,
                Optional.empty(),
                Optional.of(processes));

        List<ProcessInstanceEntity> results = query.execute();

        // Should return nothing
        assertThat(results).isEmpty();
    }

    /**
     * Test UserTaskInstanceEntity filtering (direct processId field)
     */
    @Test
    @Transactional
    public void testUserTaskInstanceDataIsolation() {
        // Insert user tasks directly into database
        UserTaskInstanceEntity travelTask = createUserTask("travel", UUID.randomUUID().toString());
        UserTaskInstanceEntity orderTask = createUserTask("orders", UUID.randomUUID().toString());
        UserTaskInstanceEntity hiringTask = createUserTask("hiring", UUID.randomUUID().toString());

        executeInTransaction(() -> {
            entityManager.persist(travelTask);
            entityManager.persist(orderTask);
            entityManager.persist(hiringTask);
            entityManager.flush();
        });

        // Query with data isolation
        Processes processes = createProcesses(Set.of("travel", "orders"));
        JPAQuery<UserTaskInstanceEntity, UserTaskInstanceEntity> query = new JPAQuery<>(
                entityManager,
                Function.identity(),
                UserTaskInstanceEntity.class,
                Optional.empty(),
                Optional.of(processes));

        List<UserTaskInstanceEntity> results = query.execute();

        assertThat(results).hasSize(2);
        assertThat(results).extracting("processId")
                .containsExactlyInAnyOrder("travel", "orders");
    }

    /**
     * Test JobEntity filtering (direct processId field)
     */
    @Test
    @Transactional
    public void testJobDataIsolation() {
        // Insert jobs directly into database
        JobEntity travelJob = createJob("travel", UUID.randomUUID().toString());
        JobEntity orderJob = createJob("orders", UUID.randomUUID().toString());
        JobEntity hiringJob = createJob("hiring", UUID.randomUUID().toString());

        executeInTransaction(() -> {
            entityManager.persist(travelJob);
            entityManager.persist(orderJob);
            entityManager.persist(hiringJob);
            entityManager.flush();
        });

        // Query with data isolation
        Processes processes = createProcesses(Set.of("travel", "orders"));
        JPAQuery<JobEntity, JobEntity> query = new JPAQuery<>(
                entityManager,
                Function.identity(),
                JobEntity.class,
                Optional.empty(),
                Optional.of(processes));

        List<JobEntity> results = query.execute();

        assertThat(results).hasSize(2);
        assertThat(results).extracting("processId")
                .containsExactlyInAnyOrder("travel", "orders");
    }

    /**
     * Test NodeInstanceEntity filtering (relationship-based: processInstance.processId)
     */
    @Test
    @Transactional
    public void testNodeInstanceDataIsolation() {
        // Insert process instances first
        ProcessInstanceEntity travelPI = createProcessInstance("travel", UUID.randomUUID().toString());
        ProcessInstanceEntity orderPI = createProcessInstance("orders", UUID.randomUUID().toString());
        ProcessInstanceEntity hiringPI = createProcessInstance("hiring", UUID.randomUUID().toString());

        executeInTransaction(() -> {
            entityManager.persist(travelPI);
            entityManager.persist(orderPI);
            entityManager.persist(hiringPI);
            entityManager.flush();

            // Insert node instances with relationships to process instances
            NodeInstanceEntity travelNode = createNodeInstance(travelPI);
            NodeInstanceEntity orderNode = createNodeInstance(orderPI);
            NodeInstanceEntity hiringNode = createNodeInstance(hiringPI);

            entityManager.persist(travelNode);
            entityManager.persist(orderNode);
            entityManager.persist(hiringNode);
            entityManager.flush();
        });

        // Query with data isolation - NodeInstance navigates via processInstance.processId
        Processes processes = createProcesses(Set.of("travel", "orders"));
        JPAQuery<NodeInstanceEntity, NodeInstanceEntity> query = new JPAQuery<>(
                entityManager,
                Function.identity(),
                NodeInstanceEntity.class,
                Optional.empty(),
                Optional.of(processes));

        List<NodeInstanceEntity> results = query.execute();
        assertThat(results).hasSize(2);
        // NodeInstance doesn't have direct processId, verify via processInstance relationship
        assertThat(results).allSatisfy(node -> assertThat(node.getProcessInstance().getProcessId()).isIn("travel", "orders"));
    }

    // Helper methods to create test entities

    private ProcessInstanceEntity createProcessInstance(String processId, String id) {
        ProcessInstanceEntity entity = new ProcessInstanceEntity();
        entity.setId(id);
        entity.setProcessId(processId);
        entity.setState(ProcessInstanceState.ACTIVE.ordinal());
        entity.setStart(ZonedDateTime.now());
        entity.setVersion("1.0");
        return entity;
    }

    private UserTaskInstanceEntity createUserTask(String processId, String processInstanceId) {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setProcessId(processId);
        entity.setProcessInstanceId(processInstanceId);
        entity.setState("Ready");
        entity.setStarted(ZonedDateTime.now());
        return entity;
    }

    private JobEntity createJob(String processId, String processInstanceId) {
        JobEntity entity = new JobEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setProcessId(processId);
        entity.setProcessInstanceId(processInstanceId);
        entity.setStatus("SCHEDULED");
        entity.setExpirationTime(ZonedDateTime.now().plusHours(1));
        return entity;
    }

    private NodeInstanceEntity createNodeInstance(ProcessInstanceEntity processInstance) {
        NodeInstanceEntity entity = new NodeInstanceEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setProcessInstance(processInstance);
        entity.setNodeId(UUID.randomUUID().toString());
        entity.setDefinitionId(UUID.randomUUID().toString());
        entity.setName("TestNode");
        entity.setType("StartNode");
        entity.setEnter(ZonedDateTime.now());
        return entity;
    }

    private Processes createProcesses(Set<String> processIds) {
        return new Processes() {
            @Override
            public Process<? extends org.kie.kogito.Model> processById(String processId) {
                return null;
            }

            @Override
            public Collection<String> processIds() {
                return processIds;
            }
        };
    }
}
