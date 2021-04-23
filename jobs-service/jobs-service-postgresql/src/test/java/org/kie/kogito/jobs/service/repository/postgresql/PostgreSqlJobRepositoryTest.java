/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.kogito.jobs.service.repository.postgresql;

import java.time.ZonedDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.function.Function;
import java.util.stream.Stream;

import org.eclipse.microprofile.reactive.streams.operators.PublisherBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.kogito.jobs.service.model.JobStatus;
import org.kie.kogito.jobs.service.model.job.JobDetails;
import org.kie.kogito.jobs.service.model.job.Recipient;
import org.kie.kogito.jobs.service.repository.postgresql.marshaller.RecipientMarshaller;
import org.kie.kogito.jobs.service.repository.postgresql.marshaller.TriggerMarshaller;
import org.kie.kogito.timer.Trigger;
import org.kie.kogito.timer.impl.PointInTimeTrigger;
import org.mockito.ArgumentCaptor;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.groups.MultiOnItem;
import io.smallrye.mutiny.groups.UniOnItem;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.PreparedQuery;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.kie.kogito.jobs.service.utils.DateUtil.DEFAULT_ZONE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings({ "unchecked", "rawtypes" })
class PostgreSqlJobRepositoryTest {

    PostgreSqlJobRepository repository;

    PgPool client;

    PreparedQuery<RowSet<Row>> query;

    CompletableFuture completableFuture;

    ZonedDateTime time;

    @BeforeEach
    void setUp() {
        time = ZonedDateTime.now(DEFAULT_ZONE);

        client = mock(PgPool.class);
        query = mock(PreparedQuery.class);
        when(client.preparedQuery(anyString())).thenReturn(query);
        when(client.query(anyString())).thenReturn(query);

        Uni uni = mock(Uni.class);
        when(query.execute(any(Tuple.class))).thenReturn(uni);
        when(query.execute()).thenReturn(uni);
        UniOnItem uniOnItem = mock(UniOnItem.class);
        when(uni.onItem()).thenReturn(uniOnItem);
        when(uniOnItem.transform(any(Function.class))).thenReturn(uni);
        when(uni.emitOn(any(Executor.class))).thenReturn(uni);

        Multi multi = mock(Multi.class);
        when(uniOnItem.transformToMulti(any(Function.class))).thenReturn(multi);
        MultiOnItem multiOnItem = mock(MultiOnItem.class);
        when(multi.onItem()).thenReturn(multiOnItem);
        when(multiOnItem.transform(any(Function.class))).thenReturn(multi);
        when(multi.emitOn(any(Executor.class))).thenReturn(multi);

        completableFuture = mock(CompletableFuture.class);
        when(uni.subscribeAsCompletionStage()).thenReturn(completableFuture);

        TriggerMarshaller triggerMarshaller = mock(TriggerMarshaller.class);
        when(triggerMarshaller.marshall(any(Trigger.class))).thenReturn(new JsonObject().put("triggerMarshaller", "test"));
        when(triggerMarshaller.unmarshall(any(JsonObject.class))).thenReturn(new PointInTimeTrigger(time.toInstant().getEpochSecond(), null, null));
        RecipientMarshaller recipientMarshaller = mock(RecipientMarshaller.class);
        when(recipientMarshaller.marshall(any(Recipient.class))).thenReturn(new JsonObject().put("recipientMarshaller", "test"));
        when(recipientMarshaller.unmarshall(any(JsonObject.class))).thenReturn(new Recipient.HTTPRecipient("test"));

        repository = new PostgreSqlJobRepository(null, null, client, triggerMarshaller, recipientMarshaller);
    }

    @Test
    void doSave() {
        PointInTimeTrigger trigger = new PointInTimeTrigger(time.toInstant().getEpochSecond(), null, null);
        Recipient recipient = new Recipient.HTTPRecipient("test");

        JobDetails job = JobDetails.builder()
                .id("test")
                .correlationId("test")
                .status(JobStatus.SCHEDULED)
                .lastUpdate(time)
                .retries(1)
                .executionCounter(1)
                .scheduledId("test")
                .payload("{\"payload\": \"test\"}")
                .type(JobDetails.Type.HTTP)
                .priority(1)
                .recipient(recipient)
                .trigger(trigger)
                .build();

        CompletionStage<JobDetails> result = repository.doSave(job);
        assertEquals(completableFuture, result);

        ArgumentCaptor<String> queryCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Tuple> parameterCaptor = ArgumentCaptor.forClass(Tuple.class);
        verify(client, times(1)).preparedQuery(queryCaptor.capture());
        verify(query, times(1)).execute(parameterCaptor.capture());

        String query = "INSERT INTO job_details (id, correlation_id, status, last_update, retries, execution_counter, scheduled_id, " +
                "payload, type, priority, recipient, trigger) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12) " +
                "ON CONFLICT (id) DO UPDATE SET correlation_id = $2, status = $3, last_update = $4, retries = $5, " +
                "execution_counter = $6, scheduled_id = $7, payload = $8, type = $9, priority = $10, " +
                "recipient = $11, trigger = $12 RETURNING id, correlation_id, status, last_update, retries, " +
                "execution_counter, scheduled_id, payload, type, priority, recipient, trigger";

        Tuple parameter = Tuple.tuple(Stream.of(
                job.getId(),
                job.getCorrelationId(),
                job.getStatus().name(),
                job.getLastUpdate().toOffsetDateTime(),
                job.getRetries(),
                job.getExecutionCounter(),
                job.getScheduledId(),
                new JsonObject(job.getPayload().toString()),
                job.getType().name(),
                job.getPriority(),
                new JsonObject().put("recipientMarshaller", "test"),
                new JsonObject().put("triggerMarshaller", "test"))
                .collect(toList()));

        assertEquals(query, queryCaptor.getValue());
        assertEquals(parameter.getString(0), parameterCaptor.getValue().getString(0));
        assertEquals(parameter.getString(1), parameterCaptor.getValue().getString(1));
        assertEquals(parameter.getString(2), parameterCaptor.getValue().getString(2));
        assertEquals(parameter.getOffsetDateTime(3), parameterCaptor.getValue().getOffsetDateTime(3));
        assertEquals(parameter.getInteger(4), parameterCaptor.getValue().getInteger(4));
        assertEquals(parameter.getInteger(5), parameterCaptor.getValue().getInteger(5));
        assertEquals(parameter.getString(6), parameterCaptor.getValue().getString(6));
        assertEquals(parameter.getJson(7), parameterCaptor.getValue().getJson(7));
        assertEquals(parameter.getString(8), parameterCaptor.getValue().getString(8));
        assertEquals(parameter.getInteger(9), parameterCaptor.getValue().getInteger(9));
        assertEquals(parameter.getJson(10), parameterCaptor.getValue().getJson(10));
        assertEquals(parameter.getJson(11), parameterCaptor.getValue().getJson(11));
    }

    @Test
    void get() {
        CompletionStage<JobDetails> result = repository.get("test");
        assertEquals(completableFuture, result);

        ArgumentCaptor<String> queryCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Tuple> parameterCaptor = ArgumentCaptor.forClass(Tuple.class);
        verify(client, times(1)).preparedQuery(queryCaptor.capture());
        verify(query, times(1)).execute(parameterCaptor.capture());

        String query = "SELECT id, correlation_id, status, last_update, retries, execution_counter, scheduled_id, " +
                "payload, type, priority, recipient, trigger FROM job_details WHERE id = $1";
        String parameter = "test";

        assertEquals(query, queryCaptor.getValue());
        assertEquals(parameter, parameterCaptor.getValue().getValue(0));
    }

    @Test
    void exists() {
        CompletionStage<Boolean> result = repository.exists("test");
        assertEquals(completableFuture, result);

        ArgumentCaptor<String> queryCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Tuple> parameterCaptor = ArgumentCaptor.forClass(Tuple.class);
        verify(client, times(1)).preparedQuery(queryCaptor.capture());
        verify(query, times(1)).execute(parameterCaptor.capture());

        String query = "SELECT id FROM job_details WHERE id = $1";
        String parameter = "test";

        assertEquals(query, queryCaptor.getValue());
        assertEquals(parameter, parameterCaptor.getValue().getValue(0));
    }

    @Test
    void delete() {
        CompletionStage<JobDetails> result = repository.delete("test");
        assertEquals(completableFuture, result);

        ArgumentCaptor<String> queryCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Tuple> parameterCaptor = ArgumentCaptor.forClass(Tuple.class);
        verify(client, times(1)).preparedQuery(queryCaptor.capture());
        verify(query, times(1)).execute(parameterCaptor.capture());

        String query = "DELETE FROM job_details WHERE id = $1 " +
                "RETURNING id, correlation_id, status, last_update, retries, " +
                "execution_counter, scheduled_id, payload, type, priority, recipient, trigger";
        String parameter = "test";

        assertEquals(query, queryCaptor.getValue());
        assertEquals(parameter, parameterCaptor.getValue().getValue(0));
    }

    @Test
    void findAll() {
        PublisherBuilder<JobDetails> result = repository.findAll();
        assertNotNull(result);

        ArgumentCaptor<String> queryCaptor = ArgumentCaptor.forClass(String.class);
        verify(client, times(1)).query(queryCaptor.capture());

        String query = "SELECT id, correlation_id, status, last_update, retries, " +
                "execution_counter, scheduled_id, payload, type, priority, recipient, trigger FROM job_details";

        assertEquals(query, queryCaptor.getValue());
    }

    @Test
    void findByStatusBetweenDatesOrderByPriority() {
        ZonedDateTime from = ZonedDateTime.now();
        ZonedDateTime to = ZonedDateTime.now();

        PublisherBuilder<JobDetails> result = repository.findByStatusBetweenDatesOrderByPriority(from, to, JobStatus.SCHEDULED, JobStatus.RETRY);
        assertNotNull(result);

        ArgumentCaptor<String> queryCaptor = ArgumentCaptor.forClass(String.class);
        verify(client, times(1)).query(queryCaptor.capture());

        String query = "SELECT id, correlation_id, status, last_update, retries, execution_counter, scheduled_id, " +
                "payload, type, priority, recipient, trigger FROM job_details " +
                "WHERE status IN ('SCHEDULED', 'RETRY') AND (trigger->>'nextFireTime')::INT8 > " + from.toInstant().toEpochMilli() +
                " AND (trigger->>'nextFireTime')::INT8 < " + to.toInstant().toEpochMilli() + " ORDER BY priority DESC";

        assertEquals(query, queryCaptor.getValue());
    }

    @Test
    void findByStatusBetweenDatesOrderByPriorityNoCondition() {
        ZonedDateTime from = ZonedDateTime.now();
        ZonedDateTime to = ZonedDateTime.now();

        PublisherBuilder<JobDetails> result = repository.findByStatusBetweenDatesOrderByPriority(from, to, JobStatus.SCHEDULED);
        assertNotNull(result);

        ArgumentCaptor<String> queryCaptor = ArgumentCaptor.forClass(String.class);
        verify(client, times(1)).query(queryCaptor.capture());

        String query = "SELECT id, correlation_id, status, last_update, retries, execution_counter, scheduled_id, " +
                "payload, type, priority, recipient, trigger FROM job_details " +
                "WHERE status IN ('SCHEDULED') AND (trigger->>'nextFireTime')::INT8 > " + from.toInstant().toEpochMilli() +
                " AND (trigger->>'nextFireTime')::INT8 < " + to.toInstant().toEpochMilli() + " ORDER BY priority DESC";

        assertEquals(query, queryCaptor.getValue());
    }

    @Test
    void createStatusQuery() {
        String statusQuery = PostgreSqlJobRepository.createStatusQuery(JobStatus.SCHEDULED, JobStatus.RETRY);
        assertEquals("status IN ('SCHEDULED', 'RETRY')", statusQuery);
    }

    @Test
    void createTimeQuery() {
        ZonedDateTime from = ZonedDateTime.now();
        ZonedDateTime to = ZonedDateTime.now();

        String timeQuery = PostgreSqlJobRepository.createTimeQuery(from, to);
        assertEquals("(trigger->>'nextFireTime')::INT8 > " + from.toInstant().toEpochMilli()
                + " AND (trigger->>'nextFireTime')::INT8 < " + to.toInstant().toEpochMilli(),
                timeQuery);
    }

    @Test
    void from() {
        PointInTimeTrigger trigger = new PointInTimeTrigger(time.toInstant().getEpochSecond(), null, null);
        Recipient recipient = new Recipient.HTTPRecipient("test");

        Row row = mock(Row.class);
        when(row.getString("id")).thenReturn("test");
        when(row.getString("correlation_id")).thenReturn("test");
        when(row.getString("status")).thenReturn("SCHEDULED");
        when(row.getOffsetDateTime("last_update")).thenReturn(time.toOffsetDateTime());
        when(row.getInteger("retries")).thenReturn(1);
        when(row.getInteger("execution_counter")).thenReturn(1);
        when(row.getString("scheduled_id")).thenReturn("test");
        when(row.get(JsonObject.class, 7)).thenReturn(new JsonObject("{\"payload\": \"test\"}"));
        when(row.getString("type")).thenReturn("HTTP");
        when(row.getInteger("priority")).thenReturn(1);
        when(row.get(JsonObject.class, 10)).thenReturn(new JsonObject().put("recipientMarshaller", "test"));
        when(row.get(JsonObject.class, 11)).thenReturn(new JsonObject().put("triggerMarshaller", "test"));

        JobDetails jobDetails = repository.from(row);

        JobDetails expected = JobDetails.builder()
                .id("test")
                .correlationId("test")
                .status(JobStatus.SCHEDULED)
                .lastUpdate(time)
                .retries(1)
                .executionCounter(1)
                .scheduledId("test")
                .payload("{\"payload\": \"test\"}")
                .type(JobDetails.Type.HTTP)
                .priority(1)
                .recipient(recipient)
                .trigger(trigger)
                .build();

        assertEquals(expected, jobDetails);
    }
}
