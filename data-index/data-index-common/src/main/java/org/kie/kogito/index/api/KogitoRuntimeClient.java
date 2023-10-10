package org.kie.kogito.index.api;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.kie.kogito.index.model.Job;
import org.kie.kogito.index.model.Node;
import org.kie.kogito.index.model.ProcessInstance;
import org.kie.kogito.index.model.UserTaskInstance;

public interface KogitoRuntimeClient {

    CompletableFuture<String> abortProcessInstance(String serviceURL, ProcessInstance processInstance);

    CompletableFuture<String> retryProcessInstance(String serviceURL, ProcessInstance processInstance);

    CompletableFuture<String> skipProcessInstance(String serviceURL, ProcessInstance processInstance);

    CompletableFuture<String> updateProcessInstanceVariables(String serviceURL, ProcessInstance processInstance, String variables);

    CompletableFuture<String> getProcessInstanceDiagram(String serviceURL, ProcessInstance processInstance);

    CompletableFuture<String> getProcessDefinitionSourceFileContent(String serviceURL, String processId);

    CompletableFuture<List<Node>> getProcessDefinitionNodes(String serviceURL, String processId);

    CompletableFuture<String> triggerNodeInstance(String serviceURL, ProcessInstance processInstance, String nodeDefinitionId);

    CompletableFuture<String> retriggerNodeInstance(String serviceURL, ProcessInstance processInstance, String nodeInstanceId);

    CompletableFuture<String> cancelNodeInstance(String serviceURL, ProcessInstance processInstance, String nodeInstanceId);

    CompletableFuture<String> cancelJob(String serviceURL, Job job);

    CompletableFuture<String> rescheduleJob(String serviceURL, Job job, String newJobData);

    CompletableFuture<String> getUserTaskSchema(String serviceURL, UserTaskInstance userTaskInstance, String user,
            List<String> groups);

    CompletableFuture<String> updateUserTaskInstance(String serviceURL, UserTaskInstance userTaskInstance, String user,
            List<String> groups, Map taskInfo);

    CompletableFuture<String> createUserTaskInstanceComment(String serviceURL, UserTaskInstance userTaskInstance,
            String user, List<String> groups, String commentInfo);

    CompletableFuture<String> createUserTaskInstanceAttachment(String serviceURL, UserTaskInstance userTaskInstance,
            String user, List<String> groups, String name, String uri);

    CompletableFuture<String> updateUserTaskInstanceComment(String serviceURL, UserTaskInstance userTaskInstance, String user,
            List<String> groups, String commentId, String commentInfo);

    CompletableFuture<String> deleteUserTaskInstanceComment(String serviceURL, UserTaskInstance userTaskInstance, String user,
            List<String> groups, String commentId);

    CompletableFuture<String> updateUserTaskInstanceAttachment(String serviceURL, UserTaskInstance userTaskInstance, String user,
            List<String> groups, String attachmentId, String name, String uri);

    CompletableFuture<String> deleteUserTaskInstanceAttachment(String serviceURL, UserTaskInstance userTaskInstance, String user,
            List<String> groups, String attachmentId);
}
