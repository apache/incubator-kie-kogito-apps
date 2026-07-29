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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import javax.sql.DataSource;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.kie.kogito.index.model.ProcessDefinition;
import org.kie.kogito.index.model.ProcessDefinitionKey;
import org.kie.kogito.index.test.TestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

/**
 * A competing replica is simulated with a plain JDBC connection that inserts the same definition and commits
 * only once the insert of the storage is queued behind it on the primary key index, which is what PostgreSQL
 * does and what makes that insert fail with a duplicate key.
 */
public abstract class AbstractConcurrentProcessDefinitionStorageIT {

    private static final String INSERT_DEFINITION = "insert into definitions (id, version, name, type) values (?, ?, ?, ?)";
    private static final String COUNT_PENDING_LOCKS = "select count(*) from pg_locks where not granted and pid <> pg_backend_pid()";
    private static final long LOCK_POLL_TIMEOUT_MILLIS = 15_000;
    private static final long LOCK_POLL_INTERVAL_MILLIS = 100;

    private final ProcessDefinitionEntityStorage storage;
    private final DataSource dataSource;

    protected AbstractConcurrentProcessDefinitionStorageIT(ProcessDefinitionEntityStorage storage, DataSource dataSource) {
        this.storage = storage;
        this.dataSource = dataSource;
    }

    protected abstract void executeInTransaction(Runnable operation);

    @Test
    void testConcurrentRegistrationOfTheSameProcessDefinition() throws Exception {
        String processId = RandomStringUtils.randomAlphabetic(10);
        String version = "2.0";
        ProcessDefinitionKey key = new ProcessDefinitionKey(processId, version);
        ProcessDefinition definition = TestUtils.createProcessDefinition(processId, version, Set.of("admin", "kogito"));

        AtomicReference<ProcessDefinition> indexed = new AtomicReference<>();
        AtomicBoolean indexingInsertBlocked = new AtomicBoolean();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        CountDownLatch indexingStarted = new CountDownLatch(1);
        try (Connection otherReplica = dataSource.getConnection()) {
            otherReplica.setAutoCommit(false);
            insertDefinition(otherReplica, processId, version);

            Future<?> commitOfTheOtherReplica = executor.submit(() -> {
                try {
                    indexingStarted.await();
                    indexingInsertBlocked.set(waitForTheIndexingInsertToBlock(otherReplica));
                } finally {
                    otherReplica.commit();
                }
                return null;
            });

            indexingStarted.countDown();
            assertThatCode(() -> executeInTransaction(() -> indexed.set(storage.put(key, definition)))).doesNotThrowAnyException();

            commitOfTheOtherReplica.get(1, TimeUnit.MINUTES);
        } finally {
            executor.shutdownNow();
        }

        assertThat(indexingInsertBlocked).as("the storage never queued its insert behind the other replica, the race was not reproduced").isTrue();
        assertThat(indexed.get()).isNotNull().extracting(ProcessDefinition::getId, ProcessDefinition::getVersion, ProcessDefinition::getName)
                .containsExactly(processId, version, processId);
        assertThat(storage.get(key)).isEqualTo(indexed.get());
    }

    private void insertDefinition(Connection connection, String processId, String version) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_DEFINITION)) {
            statement.setString(1, processId);
            statement.setString(2, version);
            statement.setString(3, processId);
            statement.setString(4, "PROCESS");
            statement.executeUpdate();
        }
    }

    private boolean waitForTheIndexingInsertToBlock(Connection connection) throws Exception {
        long deadline = System.currentTimeMillis() + LOCK_POLL_TIMEOUT_MILLIS;
        while (System.currentTimeMillis() < deadline) {
            Thread.sleep(LOCK_POLL_INTERVAL_MILLIS);
            try (PreparedStatement statement = connection.prepareStatement(COUNT_PENDING_LOCKS);
                    ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next() && resultSet.getInt(1) > 0) {
                    return true;
                }
            }
        }
        return false;
    }
}
