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
package org.kie.kogito.audit.tck;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.kie.kogito.app.audit.service.api.AuditQueryService;
import org.kie.kogito.app.audit.service.api.AuditStoreService;
import org.kie.kogito.app.audit.service.spi.AuditStoreContainerBeanLocator;
import org.kie.kogito.event.usertask.UserTaskInstanceAssignmentDataEvent;
import org.kie.kogito.event.usertask.UserTaskInstanceAttachmentDataEvent;
import org.kie.kogito.event.usertask.UserTaskInstanceAttachmentEventBody;
import org.kie.kogito.event.usertask.UserTaskInstanceCommentDataEvent;
import org.kie.kogito.event.usertask.UserTaskInstanceCommentEventBody;
import org.kie.kogito.event.usertask.UserTaskInstanceDeadlineDataEvent;
import org.kie.kogito.event.usertask.UserTaskInstanceStateDataEvent;
import org.kie.kogito.event.usertask.UserTaskInstanceVariableDataEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

import graphql.ExecutionResult;

public class AuditUserTaskInstanceServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditUserTaskInstanceServiceTest.class);

    @BeforeAll
    public static void init() {

        // we populate the database with simple dataset
        AuditStoreService proxy = AuditStoreService.newAuditStoreSerice();

        UserTaskInstanceStateDataEvent uEvent;
        uEvent = DataAuditTestUtils.newUserTaskInstanceStateEvent("eventUser", "utd1", "1", "utn1", 1, "utd1", "utp1", "utrn1", "Ready", "owner", "1");
        proxy.storeUserTaskInstanceDataEvent(uEvent);

        uEvent = DataAuditTestUtils.newUserTaskInstanceStateEvent("eventUser", "utd1", "1", "utn1", 1, "utd1", "utp1", "utrn1", "Claimed", "owner", "1");
        proxy.storeUserTaskInstanceDataEvent(uEvent);

        UserTaskInstanceVariableDataEvent vEvent;
        vEvent = DataAuditTestUtils.newUserTaskInstanceVariableEvent(uEvent, "eventUser", "varId1", "varName1", "INPUT", 1);
        proxy.storeUserTaskInstanceDataEvent(vEvent);

        vEvent = DataAuditTestUtils.newUserTaskInstanceVariableEvent(uEvent, "eventUser", "varId1", "varName1", "INPUT", 2);
        proxy.storeUserTaskInstanceDataEvent(vEvent);

        vEvent = DataAuditTestUtils.newUserTaskInstanceVariableEvent(uEvent, "eventUser", "varId2", "varName2", "OUTPUT", 1);
        proxy.storeUserTaskInstanceDataEvent(vEvent);

        vEvent = DataAuditTestUtils.newUserTaskInstanceVariableEvent(uEvent, "eventUser", "varId3", "varName3", "OUTPUT", 1);
        proxy.storeUserTaskInstanceDataEvent(vEvent);

        UserTaskInstanceAssignmentDataEvent aEvent;
        aEvent = DataAuditTestUtils.newUserTaskInstanceAssignmentEvent(uEvent, "eventUser", "POT_OWNERS", "user1", "user2", "user3");
        proxy.storeUserTaskInstanceDataEvent(aEvent);

        aEvent = DataAuditTestUtils.newUserTaskInstanceAssignmentEvent(uEvent, "eventUser", "ADMINISTRATORS", "user1", "user2", "user3");
        proxy.storeUserTaskInstanceDataEvent(aEvent);

        UserTaskInstanceAttachmentDataEvent attEvent;
        attEvent =
                DataAuditTestUtils.newUserTaskInstanceAttachmentEvent(uEvent, "eventUser", "att1", "attName1", URI.create("http://localhost:8080/att1"),
                        UserTaskInstanceAttachmentEventBody.EVENT_TYPE_ADDED);
        proxy.storeUserTaskInstanceDataEvent(attEvent);

        attEvent =
                DataAuditTestUtils.newUserTaskInstanceAttachmentEvent(uEvent, "eventUser", "att2", "attName2", URI.create("http://localhost:8080/att2"),
                        UserTaskInstanceAttachmentEventBody.EVENT_TYPE_ADDED);
        proxy.storeUserTaskInstanceDataEvent(attEvent);

        UserTaskInstanceCommentDataEvent commentEvent;
        commentEvent = DataAuditTestUtils.newUserTaskInstanceCommentEvent(uEvent, "eventUser", "att1", "attName1", UserTaskInstanceCommentEventBody.EVENT_TYPE_ADDED);
        proxy.storeUserTaskInstanceDataEvent(commentEvent);

        UserTaskInstanceDeadlineDataEvent deadlineEvent;
        deadlineEvent =
                DataAuditTestUtils.newUserTaskInstanceDeadlineEvent(uEvent, "eventUser", Collections.singletonMap("input1", "value1"), Collections.singletonMap("notification1", "notificationValue"));
        proxy.storeUserTaskInstanceDataEvent(deadlineEvent);

        uEvent = DataAuditTestUtils.newUserTaskInstanceStateEvent("eventUser", "utd2", "2", "utn2", 1, "utd2", "utp2", "utrn2", "Claimed", "owner", "1");
        proxy.storeUserTaskInstanceDataEvent(uEvent);

    }


    @Test
    public void testGetAllUserTaskInstanceState() {
        AuditQueryService service = AuditQueryService.newAuditQuerySerice();

        ExecutionResult executionResult = service.executeQuery(
                "{ GetAllUserTaskInstanceState { eventId, eventDate, userTaskDefinitionId, userTaskInstanceId, processInstanceId, businessKey, taskId, name, description, actualUser, state, nodeDefitionId, nodeInstanceId, eventType } }");
        LOGGER.info("Outcome {}", executionResult);
        List<Map<String, Object>> data = ((List) ((Map<String, Object>) executionResult.getData()).get("GetAllUserTaskInstanceState"));

        assertThat(executionResult.getErrors()).hasSize(0);
        assertThat(data).hasSize(3);

    }

    @Test
    public void testGetAllUserTaskInstanceAssignments() {
        AuditQueryService service = AuditQueryService.newAuditQuerySerice();

        ExecutionResult executionResult = service.executeQuery(
                "{ GetAllUserTaskInstanceAssignments (userTaskInstanceId : \"1\") { eventId, eventDate, userTaskDefinitionId, userTaskInstanceId, processInstanceId, businessKey, userTaskName, assignmentType, users } }");
        LOGGER.info("Outcome {}", executionResult);
        List<Map<String, Object>> data = ((List) ((Map<String, Object>) executionResult.getData()).get("GetAllUserTaskInstanceAssignments"));

        assertThat(executionResult.getErrors()).hasSize(0);
        assertThat(data).hasSize(2);

    }

    @Test
    public void testGetAllUserTaskInstanceAttachments() {
        AuditQueryService service = AuditQueryService.newAuditQuerySerice();

        ExecutionResult executionResult = service.executeQuery(
                "{ GetAllUserTaskInstanceAttachments (userTaskInstanceId : \"1\") { eventId, eventDate, userTaskDefinitionId, userTaskInstanceId, processInstanceId, businessKey, attachmentId, attachmentName, attachmentURI, eventType } }");
        LOGGER.info("Outcome {}", executionResult);
        List<Map<String, Object>> data = ((List) ((Map<String, Object>) executionResult.getData()).get("GetAllUserTaskInstanceAttachments"));

        assertThat(executionResult.getErrors()).hasSize(0);
        assertThat(data).hasSize(2);

    }

    @Test
    public void testGetAllUserTaskInstanceComment() {
        AuditQueryService service = AuditQueryService.newAuditQuerySerice();

        ExecutionResult executionResult = service.executeQuery(
                "{ GetAllUserTaskInstanceComment (userTaskInstanceId : \"1\") { eventId, eventDate, userTaskDefinitionId, userTaskInstanceId, processInstanceId, businessKey, commentId, commentContent, eventType } }");
        LOGGER.info("Outcome {}", executionResult);
        List<Map<String, Object>> data = ((List) ((Map<String, Object>) executionResult.getData()).get("GetAllUserTaskInstanceComment"));

        assertThat(executionResult.getErrors()).hasSize(0);
        assertThat(data).hasSize(1);

    }

    @Test
    public void testGetAllUserTaskInstanceDeadline() {
        AuditQueryService service = AuditQueryService.newAuditQuerySerice();

        ExecutionResult executionResult = service.executeQuery(
                "{ GetAllUserTaskInstanceDeadline (userTaskInstanceId : \"1\") { eventId, eventDate, userTaskDefinitionId, userTaskInstanceId, processInstanceId, businessKey, eventType, notification {key, value} } }");
        LOGGER.info("Outcome {}", executionResult);
        List<Map<String, Object>> data = ((List) ((Map<String, Object>) executionResult.getData()).get("GetAllUserTaskInstanceDeadline"));
        assertThat(executionResult.getErrors()).hasSize(0);
        assertThat(data).hasSize(1);

    }

    @Test
    public void testGetAllUserTaskInstanceVariable() {
        AuditQueryService service = AuditQueryService.newAuditQuerySerice();

        ExecutionResult executionResult = service.executeQuery(
                "{ GetAllUserTaskInstanceVariable (userTaskInstanceId : \"1\") {  eventId, eventDate, userTaskDefinitionId, userTaskInstanceId, processInstanceId, businessKey, variableId, variableName, variableValue, variableType } }");
        LOGGER.info("Outcome {}", executionResult);
        List<Map<String, Object>> data = ((List) ((Map<String, Object>) executionResult.getData()).get("GetAllUserTaskInstanceVariable"));
        assertThat(executionResult.getErrors()).hasSize(0);
        assertThat(data).hasSize(3);

    }
}
