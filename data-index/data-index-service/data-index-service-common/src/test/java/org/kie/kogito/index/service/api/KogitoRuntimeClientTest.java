/*
 * Copyright 2023 Red Hat, Inc. and/or its affiliates.
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
package org.kie.kogito.index.service.api;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kie.kogito.index.TestUtils;
import org.kie.kogito.index.api.KogitoRuntimeCommonClient;
import org.kie.kogito.index.api.KogitoRuntimeCommonClientTest;
import org.kie.kogito.index.model.ProcessInstance;
import org.kie.kogito.index.model.UserTaskInstance;
import org.kie.kogito.index.service.DataIndexServiceException;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.kie.kogito.index.service.api.KogitoRuntimeClientImpl.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KogitoRuntimeClientTest extends KogitoRuntimeCommonClientTest {

    private static int ERROR = 5;
    private static String TASK_ID = "taskId";

    private KogitoRuntimeClientImpl client;

    @BeforeEach
    public void setup() {
        client = spy(new KogitoRuntimeClientImpl());
        client.setGatewayTargetUrl(Optional.empty());
        client.addServiceWebClient(SERVICE_URL, webClientMock);
        client.setVertx(vertx);
        client.setIdentity(identityMock);
    }

    @Override
    protected KogitoRuntimeCommonClient getClient() {
        return client;
    }

    @Test
    void testAbortProcessInstance() {
        setupIdentityMock();
        when(webClientMock.delete(anyString())).thenReturn(httpRequestMock);

        ProcessInstance pI = createProcessInstance(PROCESS_INSTANCE_ID, ACTIVE);

        client.abortProcessInstance(SERVICE_URL, pI);
        verify(client).sendDeleteClientRequest(webClientMock,
                format(ABORT_PROCESS_INSTANCE_PATH, pI.getProcessId(), pI.getId()),
                "ABORT ProcessInstance with id: " + pI.getId());
        ArgumentCaptor<Handler> handlerCaptor = ArgumentCaptor.forClass(Handler.class);
        verify(httpRequestMock).send(handlerCaptor.capture());
        verify(httpRequestMock).putHeader(eq("Authorization"), eq("Bearer " + AUTHORIZED_TOKEN));
        checkResponseHandling(handlerCaptor.getValue());
    }

    @Test
    void testRetryProcessInstance() {
        setupIdentityMock();
        when(webClientMock.post(anyString())).thenReturn(httpRequestMock);
        ProcessInstance pI = createProcessInstance(PROCESS_INSTANCE_ID, ERROR);

        client.retryProcessInstance(SERVICE_URL, pI);
        verify(client).sendPostClientRequest(webClientMock,
                format(RETRY_PROCESS_INSTANCE_PATH, pI.getProcessId(), pI.getId()),
                "RETRY ProcessInstance with id: " + pI.getId());
        ArgumentCaptor<Handler> handlerCaptor = ArgumentCaptor.forClass(Handler.class);
        verify(httpRequestMock).send(handlerCaptor.capture());
        verify(httpRequestMock).putHeader(eq("Authorization"), eq("Bearer " + AUTHORIZED_TOKEN));
        checkResponseHandling(handlerCaptor.getValue());
    }

    @Test
    void testSkipProcessInstance() {
        setupIdentityMock();
        when(webClientMock.post(anyString())).thenReturn(httpRequestMock);

        ProcessInstance pI = createProcessInstance(PROCESS_INSTANCE_ID, ERROR);

        client.skipProcessInstance(SERVICE_URL, pI);
        verify(client).sendPostClientRequest(webClientMock,
                format(SKIP_PROCESS_INSTANCE_PATH, pI.getProcessId(), pI.getId()),
                "SKIP ProcessInstance with id: " + pI.getId());
        ArgumentCaptor<Handler> handlerCaptor = ArgumentCaptor.forClass(Handler.class);
        verify(httpRequestMock).send(handlerCaptor.capture());
        verify(httpRequestMock).putHeader(eq("Authorization"), eq("Bearer " + AUTHORIZED_TOKEN));
        checkResponseHandling(handlerCaptor.getValue());
    }

    @Test
    void testUpdateProcessInstanceVariables() {
        when(webClientMock.put(anyString())).thenReturn(httpRequestMock);
        when(httpRequestMock.putHeader(eq("Content-Type"), anyString())).thenReturn(httpRequestMock);
        when(httpRequestMock.putHeader(eq("Authorization"), anyString())).thenReturn(httpRequestMock);

        ProcessInstance pI = createProcessInstance(PROCESS_INSTANCE_ID, ERROR);

        client.updateProcessInstanceVariables(SERVICE_URL, pI, pI.getVariables().toString());
        verify(client).sendJSONPutClientRequest(webClientMock,
                format(UPDATE_VARIABLES_PROCESS_INSTANCE_PATH, pI.getProcessId(), pI.getId()),
                "UPDATE VARIABLES of ProcessInstance with id: " + pI.getId(), pI.getVariables().toString());
        ArgumentCaptor<Handler> handlerCaptor = ArgumentCaptor.forClass(Handler.class);
        JsonObject jsonOject = new JsonObject(pI.getVariables().toString());
        verify(httpRequestMock).sendJson(eq(jsonOject), handlerCaptor.capture());
        checkResponseHandling(handlerCaptor.getValue());
    }

    @Test
    void testTriggerNodeInstance() {
        String nodeDefId = "nodeDefId";
        setupIdentityMock();
        when(webClientMock.post(anyString())).thenReturn(httpRequestMock);

        ProcessInstance pI = createProcessInstance(PROCESS_INSTANCE_ID, ERROR);

        client.triggerNodeInstance(SERVICE_URL, pI, nodeDefId);
        verify(client).sendPostClientRequest(webClientMock,
                format(TRIGGER_NODE_INSTANCE_PATH, pI.getProcessId(), pI.getId(), nodeDefId),
                "Trigger Node " + nodeDefId + FROM_PROCESS_INSTANCE_WITH_ID + pI.getId());
        ArgumentCaptor<Handler> handlerCaptor = ArgumentCaptor.forClass(Handler.class);
        verify(httpRequestMock).send(handlerCaptor.capture());
        checkResponseHandling(handlerCaptor.getValue());
    }

    @Test
    void testRetriggerNodeInstance() {
        String nodeInstanceId = "nodeInstanceId";
        setupIdentityMock();
        when(webClientMock.post(anyString())).thenReturn(httpRequestMock);

        ProcessInstance pI = createProcessInstance(PROCESS_INSTANCE_ID, ERROR);

        client.retriggerNodeInstance(SERVICE_URL, pI, nodeInstanceId);
        verify(client).sendPostClientRequest(webClientMock,
                format(RETRIGGER_NODE_INSTANCE_PATH, pI.getProcessId(), pI.getId(), nodeInstanceId),
                "Retrigger NodeInstance " + nodeInstanceId + FROM_PROCESS_INSTANCE_WITH_ID + pI.getId());
        ArgumentCaptor<Handler> handlerCaptor = ArgumentCaptor.forClass(Handler.class);
        verify(httpRequestMock).send(handlerCaptor.capture());
        checkResponseHandling(handlerCaptor.getValue());
    }

    @Test
    void testCancelNodeInstance() {
        String nodeInstanceId = "nodeInstanceId";
        setupIdentityMock();
        when(webClientMock.delete(anyString())).thenReturn(httpRequestMock);

        ProcessInstance pI = createProcessInstance(PROCESS_INSTANCE_ID, ERROR);

        client.cancelNodeInstance(SERVICE_URL, pI, nodeInstanceId);
        verify(client).sendDeleteClientRequest(webClientMock,
                format(CANCEL_NODE_INSTANCE_PATH, pI.getProcessId(), pI.getId(), nodeInstanceId),
                "Cancel NodeInstance " + nodeInstanceId + FROM_PROCESS_INSTANCE_WITH_ID + pI.getId());
        ArgumentCaptor<Handler> handlerCaptor = ArgumentCaptor.forClass(Handler.class);
        verify(httpRequestMock).send(handlerCaptor.capture());
        checkResponseHandling(handlerCaptor.getValue());
    }

    @Test
    void testGetProcessInstanceDiagram() {
        setupIdentityMock();
        when(webClientMock.get(anyString())).thenReturn(httpRequestMock);

        ProcessInstance pI = createProcessInstance(PROCESS_INSTANCE_ID, ERROR);

        client.getProcessInstanceDiagram(SERVICE_URL, pI);
        verify(client).sendGetClientRequest(webClientMock,
                format(GET_PROCESS_INSTANCE_DIAGRAM_PATH, pI.getProcessId(), pI.getId()),
                "Get Process Instance diagram with id: " + pI.getId(),
                null);
        ArgumentCaptor<Handler> handlerCaptor = ArgumentCaptor.forClass(Handler.class);
        verify(httpRequestMock).send(handlerCaptor.capture());
        verify(httpRequestMock).putHeader(eq("Authorization"), eq("Bearer " + AUTHORIZED_TOKEN));
        checkResponseHandling(handlerCaptor.getValue());
    }

    @Test
    void testGetProcessInstanceNodeDefinitions() {
        setupIdentityMock();
        when(webClientMock.get(anyString())).thenReturn(httpRequestMock);

        ProcessInstance pI = createProcessInstance(PROCESS_INSTANCE_ID, ERROR);

        client.getProcessInstanceNodeDefinitions(SERVICE_URL, pI);
        verify(client).sendGetClientRequest(webClientMock,
                format(GET_PROCESS_INSTANCE_NODE_DEFINITIONS_PATH, pI.getProcessId(), pI.getId()),
                "Get Process Instance available nodes with id: " + pI.getId(),
                List.class);
        ArgumentCaptor<Handler> handlerCaptor = ArgumentCaptor.forClass(Handler.class);
        verify(httpRequestMock).send(handlerCaptor.capture());
        verify(httpRequestMock).putHeader(eq("Authorization"), eq("Bearer " + AUTHORIZED_TOKEN));
        HttpResponse response = mock(HttpResponse.class);

        handlerCaptor.getValue().handle(createResponseMocks(response, false, 404));
        verify(response).statusMessage();
        verify(response).body();
        verify(response, never()).bodyAsJson(List.class);

        HttpResponse responseWithoutError = mock(HttpResponse.class);
        handlerCaptor.getValue().handle(createResponseMocks(responseWithoutError, true, 200));
        verify(responseWithoutError, never()).statusMessage();
        verify(responseWithoutError, never()).body();
        verify(responseWithoutError).bodyAsJson(List.class);
    }

    @Test
    void testGetProcessInstanceSource() {
        setupIdentityMock();
        when(webClientMock.get(anyString())).thenReturn(httpRequestMock);

        ProcessInstance pI = createProcessInstance(PROCESS_INSTANCE_ID, ERROR);

        client.getProcessInstanceSourceFileContent(SERVICE_URL, pI);
        verify(client).sendGetClientRequest(webClientMock,
                format(GET_PROCESS_INSTANCE_SOURCE_PATH, pI.getProcessId()),
                "Get Process Instance source file with processId: " + pI.getProcessId(),
                null);
        ArgumentCaptor<Handler> handlerCaptor = ArgumentCaptor.forClass(Handler.class);
        verify(httpRequestMock).send(handlerCaptor.capture());
        verify(httpRequestMock).putHeader(eq("Authorization"), eq("Bearer " + AUTHORIZED_TOKEN));
        checkResponseHandling(handlerCaptor.getValue());
    }

    @Test
    void testSendOk() throws Exception {
        AsyncResult result = mock(AsyncResult.class);
        when(result.succeeded()).thenReturn(true);
        HttpResponse response = mock(HttpResponse.class);
        when(result.result()).thenReturn(response);
        when(response.statusCode()).thenReturn(200);
        when(response.bodyAsString()).thenReturn("OK");

        CompletableFuture future = new CompletableFuture();

        client.send(null, null, future, result);

        assertEquals("OK", future.get());
    }

    @Test
    void testSendNotFound() throws Exception {
        AsyncResult result = mock(AsyncResult.class);
        when(result.succeeded()).thenReturn(true);
        HttpResponse response = mock(HttpResponse.class);
        when(result.result()).thenReturn(response);
        when(response.statusCode()).thenReturn(404);

        CompletableFuture future = new CompletableFuture();

        client.send(null, null, future, result);

        assertNull(future.get());
    }

    @Test
    void testSendException() throws Exception {
        AsyncResult result = mock(AsyncResult.class);
        when(result.succeeded()).thenReturn(false);
        when(result.cause()).thenReturn(new RuntimeException());

        CompletableFuture future = new CompletableFuture();

        client.send("error", null, future, result);

        try {
            future.get();
            fail("Future should have failed");
        } catch (Exception ex) {
            assertEquals("org.kie.kogito.index.service.DataIndexServiceException: FAILED: error", ex.getMessage());
            assertThat(ex.getCause()).isInstanceOf(DataIndexServiceException.class);
        }
    }

    @Test
    void testGetUserTaskSchema() {
        setupIdentityMock();
        when(webClientMock.get(anyString())).thenReturn(httpRequestMock);

        UserTaskInstance taskInstance = createUserTaskInstance(PROCESS_INSTANCE_ID, TASK_ID, "InProgress");

        client.getUserTaskSchema(SERVICE_URL, taskInstance, "jdoe", Collections.singletonList("managers"));
        verify(client).sendGetClientRequest(webClientMock, "/travels/" + PROCESS_INSTANCE_ID + "/TaskName/" + TASK_ID + "/schema?user=jdoe&group=managers",
                "Get User Task schema for task:TaskName with id: " + taskInstance.getId(), null);
        ArgumentCaptor<Handler> handlerCaptor = ArgumentCaptor.forClass(Handler.class);
        verify(httpRequestMock).send(handlerCaptor.capture());
        verify(httpRequestMock).putHeader(eq("Authorization"), eq("Bearer " + AUTHORIZED_TOKEN));
        checkResponseHandling(handlerCaptor.getValue());
    }

    @Test
    void testUpdateUserTaskInstance() {
        setupIdentityMock();
        when(webClientMock.patch(anyString())).thenReturn(httpRequestMock);

        UserTaskInstance taskInstance = createUserTaskInstance(PROCESS_INSTANCE_ID, TASK_ID, "InProgress");
        Map taskInfo = new HashMap();
        taskInfo.put("description", "NewDescription");

        client.updateUserTaskInstance(SERVICE_URL, taskInstance, "jdoe", Collections.singletonList("managers"), taskInfo);
        ArgumentCaptor<JsonObject> jsonCaptor = ArgumentCaptor.forClass(JsonObject.class);
        verify(client).sendPatchClientRequest(eq(webClientMock),
                eq("/management/processes/travels/instances/" + PROCESS_INSTANCE_ID + "/tasks/" + TASK_ID + "?user=jdoe&group=managers"),
                eq("Update user task instance:" + taskInstance.getName() + " with id: " + taskInstance.getId()),
                jsonCaptor.capture());
        assertThat(jsonCaptor.getValue().getString("description")).isEqualTo("NewDescription");
        ArgumentCaptor<Handler> handlerCaptor = ArgumentCaptor.forClass(Handler.class);
        JsonObject jsonOject = new JsonObject(taskInfo);
        verify(httpRequestMock).sendJson(eq(jsonOject), handlerCaptor.capture());
        verify(httpRequestMock).putHeader(eq("Authorization"), eq("Bearer " + AUTHORIZED_TOKEN));
        checkResponseHandling(handlerCaptor.getValue());
    }

    @Test
    void testCreateUserTaskInstanceComment() {
        String commentInfo = "newComment";
        when(webClientMock.post(anyString())).thenReturn(httpRequestMock);
        when(httpRequestMock.putHeader(eq("Authorization"), anyString())).thenReturn(httpRequestMock);
        when(httpRequestMock.putHeader(eq("Content-Type"), anyString())).thenReturn(httpRequestMock);

        UserTaskInstance taskInstance = createUserTaskInstance(PROCESS_INSTANCE_ID, TASK_ID, "InProgress");

        client.createUserTaskInstanceComment(SERVICE_URL, taskInstance, "jdoe", Collections.singletonList("managers"), commentInfo);
        verify(client).sendPostWithBodyClientRequest(eq(webClientMock),
                eq("/travels/" + PROCESS_INSTANCE_ID + "/" + taskInstance.getName() + "/" + TASK_ID + "/comments?user=jdoe&group=managers"),
                eq("Adding comment to  UserTask:" + taskInstance.getName() + " with id: " + taskInstance.getId()), eq(commentInfo), eq("text/plain"));
        ArgumentCaptor<Handler> handlerCaptor = ArgumentCaptor.forClass(Handler.class);
        verify(httpRequestMock).sendBuffer(any(), handlerCaptor.capture());
        checkResponseHandling(handlerCaptor.getValue());
    }

    @Test
    void testCreateUserTaskInstanceAttachment() {
        String attachmentUri = "nhttps://drive.google.com/file/d/AttachmentUri";
        String attachmentName = "newAttachmentName";
        when(webClientMock.post(anyString())).thenReturn(httpRequestMock);
        when(httpRequestMock.putHeader(eq("Authorization"), anyString())).thenReturn(httpRequestMock);
        when(httpRequestMock.putHeader(eq("Content-Type"), anyString())).thenReturn(httpRequestMock);

        UserTaskInstance taskInstance = createUserTaskInstance(PROCESS_INSTANCE_ID, TASK_ID, "InProgress");

        client.createUserTaskInstanceAttachment(SERVICE_URL, taskInstance, "jdoe", Collections.singletonList("managers"), attachmentName, attachmentUri);
        verify(client).sendPostWithBodyClientRequest(eq(webClientMock),
                eq("/travels/" + PROCESS_INSTANCE_ID + "/" + taskInstance.getName() + "/" + TASK_ID + "/attachments?user=jdoe&group=managers"),
                eq("Adding attachment to  UserTask:" + taskInstance.getName() + " with id: " + taskInstance.getId()),
                eq("{ \"name\": \"" + attachmentName + "\", \"uri\": \"" + attachmentUri + "\" }"), eq("application/json"));
        ArgumentCaptor<Handler> handlerCaptor = ArgumentCaptor.forClass(Handler.class);
        JsonObject jsonObject = new JsonObject("{ \"name\": \"" + attachmentName + "\", \"uri\": \"" + attachmentUri + "\" }");

        verify(httpRequestMock).sendJson(eq(jsonObject), handlerCaptor.capture());
        checkResponseHandling(handlerCaptor.getValue());
    }

    @Test
    void testUpdateUserTaskInstanceComment() {
        String commentInfo = "NewCommentContent";
        String commentId = "commentId";
        when(webClientMock.put(anyString())).thenReturn(httpRequestMock);
        when(httpRequestMock.putHeader(eq("Authorization"), anyString())).thenReturn(httpRequestMock);
        when(httpRequestMock.putHeader(eq("Content-Type"), anyString())).thenReturn(httpRequestMock);

        UserTaskInstance taskInstance = createUserTaskInstance(PROCESS_INSTANCE_ID, TASK_ID, "InProgress");

        client.updateUserTaskInstanceComment(SERVICE_URL, taskInstance, "jdoe", Collections.singletonList("managers"), commentId, commentInfo);
        verify(client).sendPutClientRequest(eq(webClientMock),
                eq("/travels/" + PROCESS_INSTANCE_ID + "/" + taskInstance.getName() + "/" + TASK_ID + "/comments/" + commentId + "?user=jdoe&group=managers"),
                eq("Update UserTask: " + taskInstance.getName() + " comment:" + commentId + "  with taskid: " + taskInstance.getId()),
                eq(commentInfo), eq("text/plain"));

        ArgumentCaptor<Handler> handlerCaptor = ArgumentCaptor.forClass(Handler.class);
        verify(httpRequestMock).sendBuffer(any(), handlerCaptor.capture());
        checkResponseHandling(handlerCaptor.getValue());
    }

    @Test
    void testDeleteTaskInstanceComment() {
        String commentId = "commentId";
        setupIdentityMock();
        when(webClientMock.delete(anyString())).thenReturn(httpRequestMock);

        UserTaskInstance taskInstance = createUserTaskInstance(PROCESS_INSTANCE_ID, TASK_ID, "InProgress");

        client.deleteUserTaskInstanceComment(SERVICE_URL, taskInstance, "jdoe", Collections.singletonList("managers"), commentId);
        verify(client).sendDeleteClientRequest(eq(webClientMock),
                eq("/travels/" + PROCESS_INSTANCE_ID + "/" + taskInstance.getName() + "/" + TASK_ID + "/comments/" + commentId + "?user=jdoe&group=managers"),
                eq("Delete comment : " + commentId + "of Task: " + taskInstance.getName() + "  with taskid: " + taskInstance.getId()));
        ArgumentCaptor<Handler> handlerCaptor = ArgumentCaptor.forClass(Handler.class);
        verify(httpRequestMock).send(handlerCaptor.capture());
        checkResponseHandling(handlerCaptor.getValue());
    }

    @Test
    void testUpdateUserTaskInstanceAttachment() {
        String attachmentName = "NewAttachmentName";
        String attachmentContent = "NewAttachmentContent";
        String attachmentId = "attachmentId";

        when(webClientMock.put(anyString())).thenReturn(httpRequestMock);
        when(httpRequestMock.putHeader(eq("Authorization"), anyString())).thenReturn(httpRequestMock);
        when(httpRequestMock.putHeader(eq("Content-Type"), anyString())).thenReturn(httpRequestMock);

        UserTaskInstance taskInstance = createUserTaskInstance(PROCESS_INSTANCE_ID, TASK_ID, "InProgress");

        client.updateUserTaskInstanceAttachment(SERVICE_URL, taskInstance, "jdoe", Collections.singletonList("managers"),
                attachmentId, attachmentName, attachmentContent);
        verify(client).sendJSONPutClientRequest(eq(webClientMock),
                eq("/travels/" + PROCESS_INSTANCE_ID + "/" + taskInstance.getName() + "/" + TASK_ID + "/attachments/" + attachmentId + "?user=jdoe&group=managers"),
                eq("Update UserTask: " + taskInstance.getName() + " attachment:" + attachmentId +
                        " with taskid: " + taskInstance.getId() + "with: " + attachmentName +
                        " and info:" + attachmentContent),
                eq("{ \"name\": \"" + attachmentName + "\", \"uri\": \"" + attachmentContent + "\" }"));

        ArgumentCaptor<Handler> handlerCaptor = ArgumentCaptor.forClass(Handler.class);
        JsonObject jsonObject = new JsonObject("{ \"name\": \"" + attachmentName + "\", \"uri\": \"" + attachmentContent + "\" }");
        verify(httpRequestMock).sendJson(eq(jsonObject), handlerCaptor.capture());
        checkResponseHandling(handlerCaptor.getValue());
    }

    @Test
    void testDeleteTaskInstanceAttachment() {
        String attachmentId = "attachmentId";
        setupIdentityMock();
        when(webClientMock.delete(anyString())).thenReturn(httpRequestMock);

        UserTaskInstance taskInstance = createUserTaskInstance(PROCESS_INSTANCE_ID, TASK_ID, "InProgress");

        client.deleteUserTaskInstanceAttachment(SERVICE_URL, taskInstance, "jdoe", Collections.singletonList("managers"), attachmentId);
        verify(client).sendDeleteClientRequest(eq(webClientMock),
                eq("/travels/" + PROCESS_INSTANCE_ID + "/" + taskInstance.getName() + "/" + TASK_ID + "/attachments/" + attachmentId + "?user=jdoe&group=managers"),
                eq("Delete attachment : " + attachmentId + "of Task: " + taskInstance.getName() + "  with taskid: " + taskInstance.getId()));
        ArgumentCaptor<Handler> handlerCaptor = ArgumentCaptor.forClass(Handler.class);
        verify(httpRequestMock).send(handlerCaptor.capture());
        checkResponseHandling(handlerCaptor.getValue());
    }

    @Test
    protected void testCancelJob() {
        testCancelJobRest();
    }

    private UserTaskInstance createUserTaskInstance(String processInstanceId, String userTaskId, String state) {
        return TestUtils.getUserTaskInstance(userTaskId, "travels", processInstanceId, null, null, state, "jdoe");
    }

}
