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
package org.kie.kogito.index.service.messaging;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class KogitoIndexEventConverterTest {

    /*
     * <<<<<<< HEAD
     * private static final String BINARY_PROCESS_INSTANCE_CLOUD_EVENT_DATA = "process_instance_event.json";
     * =======
     * private static final String PROCESS_INSTANCE_EVENT_TYPE = "ProcessInstanceEvent";
     * private static final String USER_TASK_INSTANCE_EVENT_TYPE = "UserTaskInstanceEvent";
     * private static final String JOB_EVENT_TYPE = "JobEvent";
     * private static final String EVENT_ID = "ID";
     * private static final URI EVENT_SOURCE = URI.create("http://localhost:8080/travels");
     * private static final OffsetDateTime EVENT_TIME = OffsetDateTime.parse("2022-07-27T15:01:20.001+01:00");
     * private static final URI EVENT_DATA_SCHEMA = URI.create("http://my_event_data_schema/my_schema.json");
     * private static final String EVENT_DATA_CONTENT_TYPE = "application/json; charset=utf-8";
     * private static final String EVENT_SUBJECT = "SUBJECT";
     * private static final String STRUCTURED_PROCESS_INSTANCE_CLOUD_EVENT = "process_instance_event.json";
     * private static final String BINARY_PROCESS_INSTANCE_CLOUD_EVENT_DATA = "binary_process_instance_event_data.json";
     * private static final String BINARY_USER_TASK_INSTANCE_CLOUD_EVENT_DATA = "binary_user_task_instance_event_data.json";
     * private static final String BINARY_KOGITO_JOB_CLOUD_EVENT_DATA = "binary_job_event_data.json";
     * >>>>>>> 5e265bd9a ( KOGITO-9849 DataIndex is not processing well the http cloud events)
     * 
     * @Mock
     * private IncomingHttpMetadata httpMetadata;
     * private MultiMap headers;
     * private KogitoIndexEventConverter converter;
     * private ObjectMapper objectMapper;
     * 
     * @BeforeEach
     * void setUp() {
     * headers = MultiMap.caseInsensitiveMultiMap();
     * lenient().doReturn(headers).when(httpMetadata).getHeaders();
     * converter = new KogitoIndexEventConverter();
     * objectMapper = new ObjectMapper();
     * new ObjectMapperProducer().customize(objectMapper);
     * converter.setObjectMapper(objectMapper);
     * }
     * 
     * @Test
     * void canConvertBufferPayload() {
     * Buffer buffer = Buffer.buffer("{}");
     * Message<?> message = Message.of(buffer, Metadata.of(httpMetadata));
     * assertThat(converter.canConvert(message, ProcessInstanceDataEvent.class)).isTrue();
     * assertThat(converter.canConvert(message, UserTaskInstanceDataEvent.class)).isTrue();
     * assertThat(converter.canConvert(message, KogitoJobCloudEvent.class)).isTrue();
     * }
     * 
     * @Test
     * void canConvertNotBufferPayload() {
     * assertThat(converter.canConvert(Message.of(new ProcessInstanceDataEvent<>(), Metadata.of(httpMetadata)),
     * ProcessInstanceDataEvent.class)).isFalse();
     * assertThat(converter.canConvert(Message.of(new UserTaskInstanceDataEvent<>(), Metadata.of(httpMetadata)),
     * UserTaskInstanceDataEvent.class)).isFalse();
     * assertThat(converter.canConvert(Message.of(KogitoJobCloudEvent.builder().build(), Metadata.of(httpMetadata)),
     * KogitoJobCloudEvent.class)).isFalse();
     * }
     * 
     * @Test
     * <<<<<<< HEAD
     * void convertBinaryCloudProcessInstanceEvent() throws Exception {
     * Buffer buffer = Buffer.buffer(readFileContent(BINARY_PROCESS_INSTANCE_CLOUD_EVENT_DATA));
     * =======
     * void convertBinaryProcessInstanceDataEvent() throws Exception {
     * Buffer buffer = Buffer.buffer(readFileContent(BINARY_PROCESS_INSTANCE_CLOUD_EVENT_DATA));
     * Message<?> message = Message.of(buffer, Metadata.of(httpMetadata));
     * 
     * // set ce-xxx headers for the binary format.
     * headers.add(ceHeader(SPECVERSION), SpecVersion.V1.toString());
     * headers.add(ceHeader(ID), EVENT_ID);
     * headers.add(ceHeader(SOURCE), EVENT_SOURCE.toString());
     * headers.add(ceHeader(TYPE), PROCESS_INSTANCE_EVENT_TYPE);
     * headers.add(ceHeader(TIME), EVENT_TIME.toString());
     * headers.add(ceHeader(DATASCHEMA), EVENT_DATA_SCHEMA.toString());
     * headers.add(ceHeader(DATACONTENTTYPE), EVENT_DATA_CONTENT_TYPE);
     * headers.add(ceHeader(SUBJECT), EVENT_SUBJECT);
     * 
     * Message<?> result = converter.convert(message, ProcessInstanceDataEvent.class);
     * assertThat(result.getPayload()).isInstanceOf(ProcessInstanceDataEvent.class);
     * ProcessInstanceDataEvent cloudEvent = (ProcessInstanceDataEvent) result.getPayload();
     * 
     * assertThat(cloudEvent.getId()).isEqualTo(EVENT_ID);
     * assertThat(cloudEvent.getSpecVersion().toString()).isEqualTo(SpecVersion.V1.toString());
     * assertThat(cloudEvent.getSource().toString()).isEqualTo(EVENT_SOURCE.toString());
     * assertThat(cloudEvent.getType()).isEqualTo(PROCESS_INSTANCE_EVENT_TYPE);
     * assertThat(cloudEvent.getTime()).isEqualTo(EVENT_TIME);
     * assertThat(cloudEvent.getDataSchema()).isEqualTo(EVENT_DATA_SCHEMA);
     * assertThat(cloudEvent.getDataContentType()).isEqualTo(EVENT_DATA_CONTENT_TYPE);
     * assertThat(cloudEvent.getSubject()).isEqualTo(EVENT_SUBJECT);
     * 
     * ProcessInstance pi = new ProcessInstanceEventMapper().apply(cloudEvent);
     * assertThat(pi.getId()).isEqualTo("5f8b1a48-4d37-4bd2-a1a6-9b8f6097cfdd");
     * assertThat(pi.getProcessId()).isEqualTo("subscription_flow");
     * assertThat(pi.getProcessName()).isEqualTo("workflow");
     * assertThat(pi.getVariables()).hasSize(1);
     * assertThat(pi.getNodes()).hasSize(14);
     * assertThat(pi.getState()).isEqualTo(1);
     * assertThat(pi.getStart()).isEqualTo("2023-05-24T10:41:14.911Z");
     * assertThat(pi.getEnd()).isNull();
     * assertThat(pi.getMilestones()).isEmpty();
     * }
     * 
     * @Test
     * void convertStructuredProcessInstanceDataEvent() throws Exception {
     * Buffer buffer = Buffer.buffer(readFileContent(STRUCTURED_PROCESS_INSTANCE_CLOUD_EVENT));
     * >>>>>>> 5e265bd9a ( KOGITO-9849 DataIndex is not processing well the http cloud events)
     * Message<?> message = Message.of(buffer, Metadata.of(httpMetadata));
     * 
     * // set ce header for the structured format.
     * headers.add(HttpHeaders.CONTENT_TYPE, "application/cloudevents+json");
     * 
     * Message<?> result = converter.convert(message, ProcessInstanceDataEvent.class);
     * assertThat(result.getPayload()).isInstanceOf(ProcessInstanceStateDataEvent.class);
     * ProcessInstanceStateDataEvent cloudEvent = (ProcessInstanceStateDataEvent) result.getPayload();
     * 
     * <<<<<<< HEAD
     * ProcessInstance pi = new ProcessInstance();
     * new ProcessInstanceStateDataEventMerger().merge(pi, cloudEvent);
     * =======
     * assertThat(cloudEvent.getId()).isEqualTo(EVENT_ID);
     * assertThat(cloudEvent.getSpecVersion().toString()).isEqualTo(SpecVersion.V1.toString());
     * assertThat(cloudEvent.getSource().toString()).isEqualTo(EVENT_SOURCE.toString());
     * assertThat(cloudEvent.getType()).isEqualTo(PROCESS_INSTANCE_EVENT_TYPE);
     * assertThat(cloudEvent.getTime()).isEqualTo(EVENT_TIME);
     * 
     * ProcessInstance pi = new ProcessInstanceEventMapper().apply(cloudEvent);
     * >>>>>>> 5e265bd9a ( KOGITO-9849 DataIndex is not processing well the http cloud events)
     * assertThat(pi.getId()).isEqualTo("2308e23d-9998-47e9-a772-a078cf5b891b");
     * assertThat(pi.getProcessId()).isEqualTo("travels");
     * assertThat(pi.getProcessName()).isEqualTo("travels");
     * assertThat(pi.getState()).isEqualTo(1);
     * assertThat(pi.getStart()).isEqualTo("2022-03-18T05:32:21.887Z");
     * assertThat(pi.getEnd()).isNull();
     * <<<<<<< HEAD
     * =======
     * assertThat(pi.getMilestones()).isEmpty();
     * }
     * 
     * @Test
     * void convertBinaryKogitoJobCloudEvent() throws Exception {
     * Buffer buffer = Buffer.buffer(readFileContent(BINARY_KOGITO_JOB_CLOUD_EVENT_DATA));
     * Message<?> message = Message.of(buffer, Metadata.of(httpMetadata));
     * 
     * // set ce-xxx headers for the binary format.
     * headers.add(ceHeader(SPECVERSION), SpecVersion.V1.toString());
     * headers.add(ceHeader(ID), EVENT_ID);
     * headers.add(ceHeader(SOURCE), EVENT_SOURCE.toString());
     * headers.add(ceHeader(TYPE), JOB_EVENT_TYPE);
     * headers.add(ceHeader(TIME), EVENT_TIME.toString());
     * headers.add(ceHeader(DATASCHEMA), EVENT_DATA_SCHEMA.toString());
     * headers.add(ceHeader(DATACONTENTTYPE), EVENT_DATA_CONTENT_TYPE);
     * headers.add(ceHeader(SUBJECT), EVENT_SUBJECT);
     * 
     * Message<?> result = converter.convert(message, KogitoJobCloudEvent.class);
     * assertThat(result.getPayload()).isInstanceOf(KogitoJobCloudEvent.class);
     * KogitoJobCloudEvent cloudEvent = (KogitoJobCloudEvent) result.getPayload();
     * 
     * assertThat(cloudEvent.getId()).isEqualTo(EVENT_ID);
     * assertThat(cloudEvent.getSpecVersion()).isEqualTo(SpecVersion.V1.toString());
     * assertThat(cloudEvent.getSource().toString()).isEqualTo(EVENT_SOURCE.toString());
     * assertThat(cloudEvent.getType()).isEqualTo(JOB_EVENT_TYPE);
     * assertThat(cloudEvent.getTime()).isEqualTo(EVENT_TIME.toZonedDateTime());
     * assertThat(cloudEvent.getSchemaURL()).isEqualTo(EVENT_DATA_SCHEMA);
     * assertThat(cloudEvent.getContentType()).isEqualTo(EVENT_DATA_CONTENT_TYPE);
     * assertThat(cloudEvent.getSubject()).isEqualTo(EVENT_SUBJECT);
     * 
     * Job job = cloudEvent.getData();
     * assertThat(job.getId()).isEqualTo("8350b8b6-c5d9-432d-a339-a9fc85f642d4_0");
     * assertThat(job.getProcessId()).isEqualTo("timerscycle");
     * assertThat(job.getProcessInstanceId()).isEqualTo("7c1d9b38-b462-47c5-8bf2-d9154f54957b");
     * assertThat(job.getRepeatInterval()).isEqualTo(1000l);
     * assertThat(job.getCallbackEndpoint())
     * .isEqualTo("http://localhost:8080/management/jobs/timerscycle/instances/7c1d9b38-b462-47c5-8bf2-d9154f54957b/timers/8350b8b6-c5d9-432d-a339-a9fc85f642d4_0");
     * assertThat(job.getScheduledId()).isEqualTo("0");
     * assertThat(job.getStatus()).isEqualTo("SCHEDULED");
     * 
     * }
     * 
     * @Test
     * void convertBinaryUserTaskInstanceDataEvent() throws Exception {
     * Buffer buffer = Buffer.buffer(readFileContent(BINARY_USER_TASK_INSTANCE_CLOUD_EVENT_DATA));
     * Message<?> message = Message.of(buffer, Metadata.of(httpMetadata));
     * 
     * // set ce-xxx headers for the binary format.
     * headers.add(ceHeader(SPECVERSION), SpecVersion.V1.toString());
     * headers.add(ceHeader(ID), EVENT_ID);
     * headers.add(ceHeader(SOURCE), EVENT_SOURCE.toString());
     * headers.add(ceHeader(TYPE), USER_TASK_INSTANCE_EVENT_TYPE);
     * headers.add(ceHeader(TIME), EVENT_TIME.toString());
     * headers.add(ceHeader(DATASCHEMA), EVENT_DATA_SCHEMA.toString());
     * headers.add(ceHeader(DATACONTENTTYPE), EVENT_DATA_CONTENT_TYPE);
     * headers.add(ceHeader(SUBJECT), EVENT_SUBJECT);
     * 
     * Message<?> result = converter.convert(message, UserTaskInstanceDataEvent.class);
     * assertThat(result.getPayload()).isInstanceOf(UserTaskInstanceDataEvent.class);
     * UserTaskInstanceDataEvent cloudEvent = (UserTaskInstanceDataEvent) result.getPayload();
     * 
     * assertThat(cloudEvent.getId()).isEqualTo(EVENT_ID);
     * assertThat(cloudEvent.getSpecVersion()).isEqualTo(SpecVersion.V1);
     * assertThat(cloudEvent.getSource().toString()).isEqualTo(EVENT_SOURCE.toString());
     * assertThat(cloudEvent.getType()).isEqualTo(USER_TASK_INSTANCE_EVENT_TYPE);
     * assertThat(cloudEvent.getTime()).isEqualTo(EVENT_TIME);
     * assertThat(cloudEvent.getDataSchema()).isEqualTo(EVENT_DATA_SCHEMA);
     * assertThat(cloudEvent.getDataContentType()).isEqualTo(EVENT_DATA_CONTENT_TYPE);
     * assertThat(cloudEvent.getSubject()).isEqualTo(EVENT_SUBJECT);
     * 
     * UserTaskInstance userTaskInstance = new UserTaskInstanceEventMapper().apply(cloudEvent);
     * assertThat(userTaskInstance.getId()).isEqualTo("45fae435-b098-4f27-97cf-a0c107072e8b");
     * 
     * assertThat(userTaskInstance.getInputs().size()).isEqualTo(6);
     * assertThat(userTaskInstance.getName()).isEqualTo("VisaApplication");
     * assertThat(userTaskInstance.getState()).isEqualTo("Completed");
     * >>>>>>> 5e265bd9a ( KOGITO-9849 DataIndex is not processing well the http cloud events)
     * }
     * 
     * @Test
     * void convertFailureBinaryUnexpectedBufferContent() throws Exception {
     * Buffer buffer = Buffer.buffer("unexpected Content");
     * Message<?> message = Message.of(buffer, Metadata.of(httpMetadata));
     * assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> converter.convert(message, ProcessInstanceDataEvent.class));
     * }
     * 
     * private static String ceHeader(String name) {
     * return "ce-" + name;
     * }
     */

}
