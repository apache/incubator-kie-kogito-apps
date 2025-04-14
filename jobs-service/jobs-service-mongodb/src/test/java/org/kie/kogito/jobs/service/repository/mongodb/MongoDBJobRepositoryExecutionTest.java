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
package org.kie.kogito.jobs.service.repository.mongodb;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.kogito.jobs.service.api.recipient.http.HttpRecipient;
import org.kie.kogito.jobs.service.api.recipient.http.HttpRecipientStringPayloadData;
import org.kie.kogito.jobs.service.model.JobDetails;
import org.kie.kogito.jobs.service.model.JobDetailsBuilder;
import org.kie.kogito.jobs.service.model.JobStatus;
import org.kie.kogito.jobs.service.model.Recipient;
import org.kie.kogito.jobs.service.model.RecipientInstance;
import org.kie.kogito.jobs.service.repository.JobRepository;
import org.kie.kogito.jobs.service.repository.marshaller.JobDetailsMarshaller;
import org.kie.kogito.jobs.service.repository.marshaller.RecipientMarshaller;
import org.kie.kogito.jobs.service.repository.marshaller.TriggerMarshaller;
import org.kie.kogito.jobs.service.repository.mongodb.marshaller.MongoDBJobDetailsMarshaller;
import org.kie.kogito.jobs.service.utils.DateUtil;
import org.kie.kogito.timer.Trigger;
import org.kie.kogito.timer.impl.PointInTimeTrigger;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.ReturnDocument;

import io.vertx.core.json.JsonObject;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Filters.in;
import static com.mongodb.client.model.Filters.lte;
import static com.mongodb.client.model.Indexes.ascending;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.kie.kogito.jobs.service.repository.mongodb.MongoDBJobRepository.CREATED_COLUMN;
import static org.kie.kogito.jobs.service.repository.mongodb.MongoDBJobRepository.FIRE_TIME_COLUMN;
import static org.kie.kogito.jobs.service.repository.mongodb.MongoDBJobRepository.ID;
import static org.kie.kogito.jobs.service.repository.mongodb.MongoDBJobRepository.STATUS_COLUMN;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings({ "unchecked", "rawtypes" })
class MongoDBJobRepositoryExecutionTest {

    private static final String JOB_ID = "JOB_ID";

    private MongoDBJobRepository mongoDBJobRepository;

    private MongoCollection<Document> collection;

    private JobDetailsMarshaller jobDetailsMarshaller;

    @BeforeEach
    void setUp() {
        MongoClient mongoClient = mock(MongoClient.class);
        collection = mock(MongoCollection.class);
        MongoDatabase mongoDatabase = mock(MongoDatabase.class);
        when(mongoClient.getDatabase(anyString())).thenReturn(mongoDatabase);
        when(mongoDatabase.getCollection(anyString())).thenReturn(collection);
        jobDetailsMarshaller = spy(new MongoDBJobDetailsMarshaller(new TriggerMarshaller(), new RecipientMarshaller()));
        mongoDBJobRepository = new MongoDBJobRepository(null, null, mongoClient, "test", jobDetailsMarshaller);
    }

    @Test
    void saveExisting() throws Exception {
        doSave(createExistingJob(), true);
    }

    @Test
    void saveNotExisting() throws Exception {
        JobDetails notExisting = new JobDetailsBuilder()
                .id(JOB_ID)
                .trigger(createTrigger())
                .recipient(createRecipient())
                .build();
        doSave(notExisting, false);
    }

    private void doSave(JobDetails job, boolean exists) throws Exception {
        ZonedDateTime now = ZonedDateTime.now();
        FindIterable<Document> multi = mock(FindIterable.class);
        MongoCursor<Document> cursor = mock(MongoCursor.class);
        when(multi.iterator()).thenReturn(cursor);
        when(cursor.available()).thenReturn(exists ? 1 : 0);

        Document.parse(new JobDetailsMarshaller(new TriggerMarshaller(), new RecipientMarshaller()).marshall(job).toString());

        when(collection.find(any(Bson.class))).thenReturn(multi);

        Document replaced = new Document().append("id", "replaced");
        when(collection.findOneAndReplace(any(Bson.class), any(), any(FindOneAndReplaceOptions.class))).thenReturn(replaced);

        JobDetails saved = mongoDBJobRepository.doSave(job);

        ArgumentCaptor<Bson> filterCaptor = ArgumentCaptor.forClass(Bson.class);
        ArgumentCaptor<Document> documentCaptor = ArgumentCaptor.forClass(Document.class);
        ArgumentCaptor<FindOneAndReplaceOptions> optionCaptor = ArgumentCaptor.forClass(FindOneAndReplaceOptions.class);
        ArgumentCaptor<JobDetails> marshallCaptor = ArgumentCaptor.forClass(JobDetails.class);
        ArgumentCaptor<JsonObject> unmarshallCaptor = ArgumentCaptor.forClass(JsonObject.class);

        verify(collection, times(1)).findOneAndReplace(filterCaptor.capture(), documentCaptor.capture(), optionCaptor.capture());
        verify(jobDetailsMarshaller, times(1)).marshall(marshallCaptor.capture());
        verify(jobDetailsMarshaller).unmarshall(unmarshallCaptor.capture());

        assertThat(filterCaptor.getValue()).isEqualTo(eq(ID, job.getId()));
        assertThat(optionCaptor.getValue().isUpsert()).isTrue();
        assertThat(optionCaptor.getValue().getReturnDocument()).isEqualTo(ReturnDocument.AFTER);

        JobDetails timestampedJob = marshallCaptor.getAllValues().get(0);
        assertThat(timestampedJob.getId()).isEqualTo(job.getId());
        if (exists) {
            assertThat(timestampedJob.getCreated()).isEqualTo(job.getCreated());
            assertThat(timestampedJob.getLastUpdate()).isAfter(job.getLastUpdate());
        } else {
            assertThat(timestampedJob.getCreated()).isAfter(now);
            assertThat(timestampedJob.getLastUpdate()).isAfter(now);
        }
        JsonObject replacedAsJson = new JsonObject(replaced.toJson());
        assertThat(unmarshallCaptor.getValue()).isEqualTo(replacedAsJson);
        assertThat(saved.getId()).isEqualTo(replacedAsJson.getString("id"));
    }

    @Test
    void get() throws Exception {
        JobDetails job = createExistingJob();
        Document document = Document.parse(new JobDetailsMarshaller(new TriggerMarshaller(), new RecipientMarshaller()).marshall(job).toString());
        FindIterable<Document> multi = mock(FindIterable.class);
        when(multi.first()).thenReturn(document);
        when(collection.find(any(Bson.class))).thenReturn(multi);

        JobDetails result = mongoDBJobRepository.get(job.getId());
        assertThat(result).isEqualTo(job);

        ArgumentCaptor<Bson> filterCaptor = ArgumentCaptor.forClass(Bson.class);
        ArgumentCaptor<JsonObject> unmarshallCaptor = ArgumentCaptor.forClass(JsonObject.class);

        verify(collection, times(1)).find(filterCaptor.capture());
        assertEquals(eq(ID, job.getId()), filterCaptor.getValue());

        verify(jobDetailsMarshaller).unmarshall(unmarshallCaptor.capture());
        assertThat(unmarshallCaptor.getValue()).isEqualTo(new JsonObject(document.toJson()));
    }

    @Test
    void exists() throws Exception {
        JobDetails job = createExistingJob();
        Document document = Document.parse(new JobDetailsMarshaller(new TriggerMarshaller(), new RecipientMarshaller()).marshall(job).toString());
        FindIterable<Document> multi = mock(FindIterable.class);
        when(multi.first()).thenReturn(document);
        when(collection.find(any(Bson.class))).thenReturn(multi);

        Boolean result = mongoDBJobRepository.exists(job.getId());
        assertThat(result).isTrue();

        ArgumentCaptor<Bson> filterCaptor = ArgumentCaptor.forClass(Bson.class);
        verify(collection, times(1)).find(filterCaptor.capture());
        assertEquals(eq(ID, job.getId()), filterCaptor.getValue());
    }

    @Test
    void delete() throws Exception {
        JobDetails job = createExistingJob();
        Document document = Document.parse(new JobDetailsMarshaller(new TriggerMarshaller(), new RecipientMarshaller()).marshall(job).toString());
        FindIterable<Document> multi = mock(FindIterable.class);
        when(multi.first()).thenReturn(document);
        when(collection.findOneAndDelete(any(Bson.class))).thenReturn(document);

        JobDetails result = mongoDBJobRepository.delete(job.getId());
        assertThat(result).isEqualTo(job);

        ArgumentCaptor<Bson> filterCaptor = ArgumentCaptor.forClass(Bson.class);
        ArgumentCaptor<JsonObject> unmarshallCaptor = ArgumentCaptor.forClass(JsonObject.class);

        verify(collection, times(1)).findOneAndDelete(filterCaptor.capture());
        assertEquals(eq(ID, job.getId()), filterCaptor.getValue());

        verify(jobDetailsMarshaller).unmarshall(unmarshallCaptor.capture());
        assertThat(unmarshallCaptor.getValue()).isEqualTo(new JsonObject(document.toJson()));
    }

    @Test
    void findByStatusBetweenDates() {
        JobDetails job = createExistingJob();

        FindIterable<Document> multi = mock(FindIterable.class);
        when(multi.sort(any())).thenReturn(multi);

        FindIterable<JsonObject> multiJson = mock(FindIterable.class);
        when(multi.map(ArgumentMatchers.<com.mongodb.Function<Document, JsonObject>> any())).thenReturn(multiJson);

        FindIterable<JobDetails> multiJob = mock(FindIterable.class);
        when(multiJson.map(ArgumentMatchers.<com.mongodb.Function<JsonObject, JobDetails>> any())).thenReturn(multiJob);

        doAnswer(new Answer() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Consumer<JobDetails> consumer = (Consumer<JobDetails>) invocation.getArgument(0);
                consumer.accept(job);
                return null;
            }

        }).when(multiJob).forEach(any());

        doReturn(multi).when(collection).find(any(Bson.class));

        ZonedDateTime from = ZonedDateTime.now();
        ZonedDateTime to = ZonedDateTime.now();

        List<JobDetails> result = mongoDBJobRepository.findByStatusBetweenDates(from, to,
                new JobStatus[] { JobStatus.SCHEDULED, JobStatus.RETRY },
                new JobRepository.SortTerm[] { JobRepository.SortTerm.byFireTime(true) });
        assertNotNull(result);

        ArgumentCaptor<Bson> filterCaptor = ArgumentCaptor.forClass(Bson.class);

        verify(collection, times(1)).find(filterCaptor.capture());

        assertEquals(and(
                in("status", Arrays.stream(new JobStatus[] { JobStatus.SCHEDULED, JobStatus.RETRY }).map(Enum::name).collect(toList())),
                gte("trigger.nextFireTime", from.toInstant().toEpochMilli()),
                lte("trigger.nextFireTime", to.toInstant().toEpochMilli())),
                filterCaptor.getValue());
    }

    @Test
    void onStart() {
        mongoDBJobRepository.onStart(null);

        ArgumentCaptor<Bson> indexCaptor = ArgumentCaptor.forClass(Bson.class);
        verify(collection, times(2)).createIndex(indexCaptor.capture());

        assertEquals(ascending(STATUS_COLUMN, FIRE_TIME_COLUMN), indexCaptor.getAllValues().get(0));
        assertEquals(ascending(CREATED_COLUMN, FIRE_TIME_COLUMN, ID), indexCaptor.getAllValues().get(1));
    }

    @Test
    void documentToJson() {
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("testKey1", "testValue1");
        objectMap.put("testKey2", 1618276009165L);
        JsonObject object = new JsonObject(objectMap);

        Document document = new Document()
                .append("testKey1", "testValue1")
                .append("testKey2", 1618276009165L);

        assertEquals(object, MongoDBJobRepository.documentToJson(document));
    }

    @Test
    void jsonToDocument() {
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("testKey1", "testValue1");
        objectMap.put("testKey2", 1618276009165L);
        JsonObject object = new JsonObject(objectMap);

        Document document = new Document()
                .append("testKey1", "testValue1")
                .append("testKey2", 1618276009165L);

        assertEquals(document, MongoDBJobRepository.jsonToDocument(object));
    }

    private static JobDetails createExistingJob() {
        ZonedDateTime createdTime = ZonedDateTime.parse("2024-01-30T12:00:00.000Z[UTC]");
        ZonedDateTime lastUpdateTime = ZonedDateTime.parse("2024-01-30T15:00:00.000Z[UTC]");
        return new JobDetailsBuilder()
                .id(JOB_ID)
                .trigger(createTrigger())
                .recipient(createRecipient())
                .created(createdTime)
                .lastUpdate(lastUpdateTime)
                .build();
    }

    private static Trigger createTrigger() {
        return new PointInTimeTrigger(DateUtil.now().toInstant().toEpochMilli(), null, null);
    }

    private static Recipient createRecipient() {
        return new RecipientInstance(HttpRecipient.builder()
                .forStringPayload()
                .url("http://my-service")
                .payload(HttpRecipientStringPayloadData.from("payload data"))
                .build());
    }
}
