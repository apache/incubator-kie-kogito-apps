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
package org.kie.kogito.jobs.service.repository.postgresql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.sql.DataSource;

import org.kie.kogito.jobs.service.model.JobDetails;
import org.kie.kogito.jobs.service.model.JobStatus;
import org.kie.kogito.jobs.service.repository.JobRepository;
import org.kie.kogito.jobs.service.repository.impl.AbstractJobRepository;
import org.kie.kogito.jobs.service.repository.marshaller.RecipientMarshaller;
import org.kie.kogito.jobs.service.repository.marshaller.TriggerMarshaller;
import org.kie.kogito.jobs.service.utils.DateUtil;
import org.kie.kogito.timer.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.json.JsonObject;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import static java.util.Collections.emptyList;

@ApplicationScoped
public class PostgreSqlJobRepository extends AbstractJobRepository implements JobRepository {
    private static Logger LOG = LoggerFactory.getLogger(PostgreSqlJobRepository.class);

    private static final String JOB_DETAILS_TABLE = "job_details";

    private static final String JOB_DETAILS_COLUMNS = "id, correlation_id, status, last_update, retries, " +
            "execution_counter, scheduled_id, priority, recipient, trigger, fire_time, execution_timeout, execution_timeout_unit, created";

    private DataSource client;

    private final TriggerMarshaller triggerMarshaller;

    private final RecipientMarshaller recipientMarshaller;

    PostgreSqlJobRepository() {
        this(null, null, null);
    }

    @Inject
    public PostgreSqlJobRepository(DataSource client,
            TriggerMarshaller triggerMarshaller, RecipientMarshaller recipientMarshaller) {
        super();
        this.client = client;
        this.triggerMarshaller = triggerMarshaller;
        this.recipientMarshaller = recipientMarshaller;
    }

    private static final String DO_SAVE =
            "INSERT INTO " + JOB_DETAILS_TABLE + " (" + JOB_DETAILS_COLUMNS + ") VALUES (?, ?, ?, now(), ?, ?, ?, ?, ?, ?, ?, ?, ?, now()) " +
                    "ON CONFLICT (id) DO " +
                    "UPDATE SET correlation_id = ?, status = ?, last_update = now(), retries = ?, " +
                    "execution_counter = ?, scheduled_id = ?, priority = ?, " +
                    "recipient = ?, trigger = ?, fire_time = ?, execution_timeout = ?, execution_timeout_unit = ? " +
                    "RETURNING " + JOB_DETAILS_COLUMNS;

    private static final String FIND = "SELECT " + JOB_DETAILS_COLUMNS + " FROM " + JOB_DETAILS_TABLE + " WHERE id = ?";

    private static final String DELETE = "DELETE FROM " + JOB_DETAILS_TABLE + " WHERE id = ? RETURNING " + JOB_DETAILS_COLUMNS;

    @Override
    public JobDetails doSave(JobDetails job) {
        JobDetails next = null;
        String status = Optional.ofNullable(job.getStatus()).map(JobStatus::name).orElse(null);
        var timeoutUnit = Optional.ofNullable(job.getExecutionTimeoutUnit()).map(ChronoUnit::name).orElse(null);
        Timestamp fireTime = Optional.ofNullable(job.getTrigger()).map(Trigger::hasNextFireTime).map(date -> new java.sql.Timestamp(date.getTime())).orElse(null);
        try (Connection connection = client.getConnection(); PreparedStatement stmt = connection.prepareStatement(DO_SAVE)) {
            stmt.setString(1, job.getId());
            stmt.setString(2, job.getCorrelationId());
            stmt.setObject(3, status);
            stmt.setInt(4, job.getRetries());
            stmt.setInt(5, job.getExecutionCounter());
            stmt.setString(6, job.getScheduledId());
            if (job.getPriority() != null) {
                stmt.setInt(7, job.getPriority());
            } else {
                stmt.setNull(7, Types.INTEGER);
            }
            JsonObject recipient = recipientMarshaller.marshall(job.getRecipient());
            if (recipient != null) {
                stmt.setObject(8, recipient.toString(), Types.OTHER);
            } else {
                stmt.setNull(8, Types.OTHER);
            }
            JsonObject trigger = triggerMarshaller.marshall(job.getTrigger());
            if (trigger != null) {
                stmt.setObject(9, trigger.toString(), Types.OTHER);
            } else {
                stmt.setNull(9, Types.OTHER);
            }
            stmt.setTimestamp(10, fireTime);
            if (job.getExecutionTimeout() != null) {
                stmt.setLong(11, job.getExecutionTimeout());
            } else {
                stmt.setNull(11, Types.BIGINT);
            }
            stmt.setString(12, timeoutUnit);

            // on conflict
            stmt.setString(13, job.getCorrelationId());
            stmt.setObject(14, status);
            stmt.setInt(15, job.getRetries());
            stmt.setInt(16, job.getExecutionCounter());
            stmt.setString(17, job.getScheduledId());
            if (job.getPriority() != null) {
                stmt.setInt(18, job.getPriority());
            } else {
                stmt.setNull(18, Types.INTEGER);
            }

            if (recipient != null) {
                stmt.setObject(19, recipient.toString(), Types.OTHER);
            } else {
                stmt.setNull(19, Types.OTHER);
            }
            if (trigger != null) {
                stmt.setObject(20, trigger.toString(), Types.OTHER);
            } else {
                stmt.setNull(20, Types.OTHER);
            }
            stmt.setTimestamp(21, fireTime);
            if (job.getExecutionTimeout() != null) {
                stmt.setLong(22, job.getExecutionTimeout());
            } else {
                stmt.setNull(22, Types.BIGINT);
            }
            stmt.setString(23, timeoutUnit);

            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                next = from(resultSet);
            }
            resultSet.close();
            return next;
        } catch (SQLException ex) {
            LOG.error("Error during job insertion in pgsql", ex);
            return null;
        }

    }

    @Override
    public JobDetails get(String id) {
        JobDetails jobDetails = null;
        try (Connection connection = client.getConnection(); PreparedStatement stmt = connection.prepareStatement(FIND)) {
            stmt.setString(1, id);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                jobDetails = from(resultSet);
            }
            resultSet.close();
        } catch (SQLException ex) {
            LOG.error("Error during job insertion in pgsql", ex);
            return null;
        }
        return jobDetails;
    }

    @Override
    public Boolean exists(String id) {
        return get(id) != null;
    }

    @Override
    public JobDetails delete(String id) {
        JobDetails jobDetails = null;
        try (Connection connection = client.getConnection(); PreparedStatement stmt = connection.prepareStatement(DELETE)) {
            stmt.setString(1, id);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                jobDetails = from(resultSet);
            }
            resultSet.close();
        } catch (SQLException ex) {
            LOG.error("Error during job insertion in pgsql", ex);
            return null;
        }
        return jobDetails;

    }

    @Override
    public List<JobDetails> findByStatusBetweenDates(
            ZonedDateTime fromFireTime,
            ZonedDateTime toFireTime,
            JobStatus[] status,
            SortTerm[] orderBy) {

        String statusFilter = (status != null && status.length > 0) ? createStatusFilter(status) : null;
        String fireTimeFilter = createFireTimeFilter("?", "?");
        String orderByCriteria = (orderBy != null && orderBy.length > 0) ? createOrderBy(orderBy) : "";

        StringBuilder queryFilter = new StringBuilder();
        if (statusFilter != null) {
            queryFilter.append(statusFilter);
            queryFilter.append(" AND ");
        }
        queryFilter.append(fireTimeFilter);

        String findQuery = "SELECT " + JOB_DETAILS_COLUMNS +
                " FROM " + JOB_DETAILS_TABLE +
                " WHERE " + queryFilter +
                " " + orderByCriteria;

        String all = "SELECT " + JOB_DETAILS_COLUMNS + " FROM " + JOB_DETAILS_TABLE;
        try (Connection connection = client.getConnection(); Statement stmt = connection.createStatement()) {
            List<JobDetails> jobDetails = new ArrayList<>();
            ResultSet resultSet = stmt.executeQuery(all);
            while (resultSet.next()) {
                jobDetails.add(from(resultSet));
            }
            resultSet.close();
        } catch (SQLException ex) {

        }
        List<JobDetails> jobDetails = new ArrayList<>();
        try (Connection connection = client.getConnection(); PreparedStatement stmt = connection.prepareStatement(findQuery)) {
            stmt.setTimestamp(1, Timestamp.from(fromFireTime.toInstant()));
            stmt.setTimestamp(2, Timestamp.from(toFireTime.toInstant()));
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                jobDetails.add(from(resultSet));
            }
            resultSet.close();
        } catch (SQLException ex) {
            LOG.error("Error during job insertion in pgsql", ex);
            return emptyList();
        }
        return jobDetails;
    }

    static String createStatusFilter(JobStatus... status) {
        return Arrays.stream(status).map(JobStatus::name)
                .collect(Collectors.joining("', '", "status IN ('", "')"));
    }

    static String createFireTimeFilter(String indexFrom, String indexTo) {
        return String.format("fire_time BETWEEN %s AND %s", indexFrom, indexTo);
    }

    static String createOrderBy(SortTerm[] sortTerms) {
        return Stream.of(sortTerms).map(PostgreSqlJobRepository::createOrderByTerm)
                .collect(Collectors.joining(", ", "ORDER BY ", ""));
    }

    static String createOrderByTerm(SortTerm sortTerm) {
        return toColumName(sortTerm.getField()) + (sortTerm.isAsc() ? " ASC" : " DESC");
    }

    static String toColumName(SortTermField field) {
        return switch (field) {
            case FIRE_TIME -> "fire_time";
            case CREATED -> "created";
            case ID -> "id";
            default -> throw new IllegalArgumentException("No colum name is defined for field: " + field);
        };
    }

    JobDetails from(ResultSet row) throws SQLException {
        JobStatus status = Optional.ofNullable(row.getString("status")).map(e -> JobStatus.valueOf(e)).orElse((JobStatus) null);
        ChronoUnit timeoutUnit = Optional.ofNullable(row.getString("execution_timeout_unit")).map(e -> ChronoUnit.valueOf(e)).orElse(null);
        String recipient = row.getString("recipient");
        String trigger = row.getString("trigger");
        Long executionTimeout = row.getLong("execution_timeout");
        executionTimeout = row.wasNull() ? null : executionTimeout;
        Integer priority = row.getInt("priority");
        priority = row.wasNull() ? null : priority;
        Integer executionCounter = row.getInt("execution_counter");
        executionCounter = row.wasNull() ? null : executionCounter;
        Integer retries = row.getInt("retries");
        retries = row.wasNull() ? null : executionCounter;

        return JobDetails.builder()
                .id(row.getString("id"))
                .correlationId(row.getString("correlation_id"))
                .status(status)
                .lastUpdate(Optional.ofNullable(row.getTimestamp("last_update")).map(e -> new Date(e.getTime())).map(DateUtil::fromDate).orElse(null))
                .retries(retries)
                .executionCounter(executionCounter)
                .scheduledId(row.getString("scheduled_id"))
                .priority(priority)
                .recipient(recipient != null ? recipientMarshaller.unmarshall(new JsonObject(recipient)) : null)
                .trigger(trigger != null ? triggerMarshaller.unmarshall(new JsonObject(trigger)) : null)
                .executionTimeout(executionTimeout)
                .executionTimeoutUnit(timeoutUnit)
                .created(Optional.ofNullable(row.getTimestamp("created")).map(e -> new Date(e.getTime())).map(DateUtil::fromDate).orElse(null))
                .build();
    }
}
