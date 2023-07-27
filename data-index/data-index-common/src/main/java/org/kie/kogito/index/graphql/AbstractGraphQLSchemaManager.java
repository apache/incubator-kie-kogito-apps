/*
 * Copyright 2022 Red Hat, Inc. and/or its affiliates.
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
package org.kie.kogito.index.graphql;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.kie.kogito.index.api.KogitoRuntimeClient;
import org.kie.kogito.index.graphql.query.GraphQLQueryOrderByParser;
import org.kie.kogito.index.graphql.query.GraphQLQueryParserRegistry;
import org.kie.kogito.index.model.Job;
import org.kie.kogito.index.model.Node;
import org.kie.kogito.index.model.ProcessInstance;
import org.kie.kogito.index.model.UserTaskInstance;
import org.kie.kogito.index.service.DataIndexServiceException;
import org.kie.kogito.index.storage.DataIndexStorageService;
import org.kie.kogito.persistence.api.Storage;
import org.kie.kogito.persistence.api.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLInputObjectType;
import graphql.schema.GraphQLNamedType;
import graphql.schema.GraphQLScalarType;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;

import static java.lang.String.format;
import static java.util.Collections.singletonList;
import static org.kie.kogito.persistence.api.query.QueryFilterFactory.equalTo;

public abstract class AbstractGraphQLSchemaManager implements GraphQLSchemaManager {

    private static final String ID = "id";
    private static final String USER = "user";
    private static final String GROUPS = "groups";
    private static final String TASK_ID = "taskId";
    private static final String COMMENT_ID = "commentId";
    private static final String ATTACHMENT_ID = "attachmentId";

    private static final String UNABLE_TO_FIND_ERROR_MSG = "Unable to find the instance with %s %s";

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractGraphQLSchemaManager.class);

    @Inject
    DataIndexStorageService cacheService;

    @Inject
    GraphQLScalarType dateTimeScalarType;

    @Inject
    KogitoRuntimeClient dataIndexApiExecutor;

    private GraphQLSchema schema;

    @PostConstruct
    public void setup() {
        schema = createSchema();
        GraphQLQueryParserRegistry.get().registerParsers(
                (GraphQLInputObjectType) schema.getType("ProcessInstanceArgument"),
                (GraphQLInputObjectType) schema.getType("UserTaskInstanceArgument"),
                (GraphQLInputObjectType) schema.getType("JobArgument"));
    }

    protected TypeDefinitionRegistry loadSchemaDefinitionFile(String fileName) {
        SchemaParser schemaParser = new SchemaParser();
        try (InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
                InputStreamReader reader = new InputStreamReader(stream)) {
            return schemaParser.parse(reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract GraphQLSchema createSchema();

    public DataIndexStorageService getCacheService() {
        return cacheService;
    }

    public GraphQLScalarType getDateTimeScalarType() {
        return dateTimeScalarType;
    }

    public KogitoRuntimeClient getDataIndexApiExecutor() {
        return dataIndexApiExecutor;
    }

    public void setDataIndexApiExecutor(KogitoRuntimeClient dataIndexApiExecutor) {
        this.dataIndexApiExecutor = dataIndexApiExecutor;
    }

    public String getProcessInstanceServiceUrl(DataFetchingEnvironment env) {
        ProcessInstance source = env.getSource();
        if (source == null || source.getEndpoint() == null || source.getProcessId() == null) {
            return null;
        }
        return getServiceUrl(source.getEndpoint(), source.getProcessId());
    }

    protected String getServiceUrl(String endpoint, String processId) {
        LOGGER.debug("Process endpoint {}", endpoint);
        if (endpoint.startsWith("/")) {
            LOGGER.warn("Process '{}' endpoint '{}', does not contain full URL, please review the kogito.service.url system property to point the public URL for this runtime.",
                    processId, endpoint);
        }
        String context = getContext(processId);
        LOGGER.debug("Process context {}", context);
        if (context.equals(endpoint) || endpoint.equals("/" + context)) {
            return null;
        } else {
            return endpoint.contains("/" + context) ? endpoint.substring(0, endpoint.lastIndexOf("/" + context)) : null;
        }
    }

    private String getContext(String processId) {
        return processId.contains(".") ? processId.substring(processId.lastIndexOf('.') + 1) : processId;
    }

    protected Collection<ProcessInstance> getChildProcessInstancesValues(DataFetchingEnvironment env) {
        ProcessInstance source = env.getSource();
        Query<ProcessInstance> query = cacheService.getProcessInstancesCache().query();
        query.filter(singletonList(equalTo("parentProcessInstanceId", source.getId())));
        return query.execute();
    }

    protected ProcessInstance getParentProcessInstanceValue(DataFetchingEnvironment env) {
        ProcessInstance source = env.getSource();
        if (source.getParentProcessInstanceId() == null) {
            return null;
        }
        Query<ProcessInstance> query = cacheService.getProcessInstancesCache().query();
        query.filter(singletonList(equalTo("id", source.getParentProcessInstanceId())));
        List<ProcessInstance> execute = query.execute();
        return !execute.isEmpty() ? execute.get(0) : null;
    }

    protected Collection<ProcessInstance> getProcessInstancesValues(DataFetchingEnvironment env) {
        return executeAdvancedQueryForCache(cacheService.getProcessInstancesCache(), env);
    }

    protected <T> List<T> executeAdvancedQueryForCache(Storage<String, T> cache, DataFetchingEnvironment env) {
        Objects.requireNonNull(cache, "Cache not found");

        String inputTypeName = ((GraphQLNamedType) env.getFieldDefinition().getArgument("where").getType()).getName();

        Query<T> query = cache.query();

        Map<String, Object> where = env.getArgument("where");
        query.filter(GraphQLQueryParserRegistry.get().getParser(inputTypeName).apply(where));

        query.sort(new GraphQLQueryOrderByParser().apply(env));

        Map<String, Integer> pagination = env.getArgument("pagination");
        if (pagination != null) {
            Integer limit = pagination.get("limit");
            if (limit != null) {
                query.limit(limit);
            }
            Integer offset = pagination.get("offset");
            if (offset != null) {
                query.offset(offset);
            }
        }

        return query.execute();
    }

    protected Collection<UserTaskInstance> getUserTaskInstancesValues(DataFetchingEnvironment env) {
        return executeAdvancedQueryForCache(cacheService.getUserTaskInstancesCache(), env);
    }

    protected Collection<Job> getJobsValues(DataFetchingEnvironment env) {
        return executeAdvancedQueryForCache(getCacheService().getJobsCache(), env);
    }

    public CompletableFuture<String> getProcessInstanceDiagram(DataFetchingEnvironment env) {
        ProcessInstance processInstance = env.getSource();
        String serviceUrl = getServiceUrl(processInstance.getEndpoint(), processInstance.getProcessId());
        return dataIndexApiExecutor.getProcessInstanceDiagram(serviceUrl, processInstance);
    }

    public CompletableFuture<String> getProcessInstanceSourceFileContent(DataFetchingEnvironment env) {
        ProcessInstance processInstance = env.getSource();
        return dataIndexApiExecutor.getProcessInstanceSourceFileContent(getServiceUrl(processInstance.getEndpoint(), processInstance.getProcessId()), processInstance);
    }

    public CompletableFuture<List<Node>> getProcessNodes(DataFetchingEnvironment env) {
        ProcessInstance processInstance = env.getSource();
        return dataIndexApiExecutor.getProcessInstanceNodeDefinitions(getServiceUrl(processInstance.getEndpoint(), processInstance.getProcessId()), processInstance);
    }

    @Override
    public GraphQLSchema getGraphQLSchema() {
        return schema;
    }

    public void transform(Consumer<GraphQLSchema.Builder> builder) {
        schema = schema.transform(builder);
    }

    public CompletableFuture<String> abortProcessInstance(DataFetchingEnvironment env) {
        String id = env.getArgument("id");
        ProcessInstance processInstance = getCacheService().getProcessInstancesCache().get(id);
        if (processInstance != null) {
            return getDataIndexApiExecutor().abortProcessInstance(getServiceUrl(processInstance.getEndpoint(), processInstance.getProcessId()), processInstance);
        }
        return CompletableFuture.failedFuture(new DataIndexServiceException(format(UNABLE_TO_FIND_ERROR_MSG, ID, id)));
    }

    public CompletableFuture<String> retryProcessInstance(DataFetchingEnvironment env) {
        String id = env.getArgument("id");
        ProcessInstance processInstance = getCacheService().getProcessInstancesCache().get(id);
        if (processInstance != null) {
            return getDataIndexApiExecutor().retryProcessInstance(getServiceUrl(processInstance.getEndpoint(), processInstance.getProcessId()), processInstance);
        }
        return CompletableFuture.failedFuture(new DataIndexServiceException(format(UNABLE_TO_FIND_ERROR_MSG, ID, id)));
    }

    public CompletableFuture<String> skipProcessInstance(DataFetchingEnvironment env) {
        String id = env.getArgument("id");
        ProcessInstance processInstance = getCacheService().getProcessInstancesCache().get(id);
        if (processInstance != null) {
            return getDataIndexApiExecutor().skipProcessInstance(getServiceUrl(processInstance.getEndpoint(), processInstance.getProcessId()), processInstance);
        }
        return CompletableFuture.failedFuture(new DataIndexServiceException(format(UNABLE_TO_FIND_ERROR_MSG, ID, id)));
    }

    public CompletableFuture<String> updateProcessInstanceVariables(DataFetchingEnvironment env) {
        String id = env.getArgument("id");
        ProcessInstance processInstance = getCacheService().getProcessInstancesCache().get(id);
        if (processInstance != null) {
            return getDataIndexApiExecutor().updateProcessInstanceVariables(getServiceUrl(processInstance.getEndpoint(), processInstance.getProcessId()), processInstance,
                    env.getArgument("variables"));

        }
        return CompletableFuture.failedFuture(new DataIndexServiceException(format(UNABLE_TO_FIND_ERROR_MSG, ID, id)));
    }

    public CompletableFuture<String> triggerNodeInstance(DataFetchingEnvironment env) {
        String id = env.getArgument("id");
        ProcessInstance processInstance = getCacheService().getProcessInstancesCache().get(id);
        if (processInstance != null) {
            return getDataIndexApiExecutor().triggerNodeInstance(getServiceUrl(processInstance.getEndpoint(), processInstance.getProcessId()),
                    processInstance,
                    env.getArgument("nodeId"));
        }
        return CompletableFuture.failedFuture(new DataIndexServiceException(format(UNABLE_TO_FIND_ERROR_MSG, ID, id)));
    }

    public CompletableFuture<String> retriggerNodeInstance(DataFetchingEnvironment env) {
        String id = env.getArgument("id");
        ProcessInstance processInstance = getCacheService().getProcessInstancesCache().get(id);
        if (processInstance != null) {
            return getDataIndexApiExecutor().retriggerNodeInstance(getServiceUrl(processInstance.getEndpoint(), processInstance.getProcessId()),
                    processInstance,
                    env.getArgument("nodeInstanceId"));
        }
        return CompletableFuture.failedFuture(new DataIndexServiceException(format(UNABLE_TO_FIND_ERROR_MSG, ID, id)));
    }

    public CompletableFuture<String> cancelNodeInstance(DataFetchingEnvironment env) {
        String id = env.getArgument("id");
        ProcessInstance processInstance = getCacheService().getProcessInstancesCache().get(id);
        if (processInstance != null) {
            return getDataIndexApiExecutor().cancelNodeInstance(getServiceUrl(processInstance.getEndpoint(), processInstance.getProcessId()),
                    processInstance,
                    env.getArgument("nodeInstanceId"));
        }
        return CompletableFuture.failedFuture(new DataIndexServiceException(format(UNABLE_TO_FIND_ERROR_MSG, ID, id)));
    }

    public CompletableFuture<String> cancelJob(DataFetchingEnvironment env) {
        String id = env.getArgument("id");
        Job job = getCacheService().getJobsCache().get(id);
        if (job != null) {
            return getDataIndexApiExecutor().cancelJob(job.getEndpoint(), job);
        }
        return CompletableFuture.failedFuture(new DataIndexServiceException(format(UNABLE_TO_FIND_ERROR_MSG, ID, id)));
    }

    public CompletableFuture<String> rescheduleJob(DataFetchingEnvironment env) {
        String id = env.getArgument("id");
        Job job = getCacheService().getJobsCache().get(id);
        if (job != null) {
            return getDataIndexApiExecutor().rescheduleJob(job.getEndpoint(), job, env.getArgument("data"));
        }
        return CompletableFuture.failedFuture(new DataIndexServiceException(format(UNABLE_TO_FIND_ERROR_MSG, ID, id)));
    }

    protected CompletableFuture<String> getUserTaskInstanceSchema(DataFetchingEnvironment env) {
        UserTaskInstance userTaskInstance = env.getSource();
        return getDataIndexApiExecutor().getUserTaskSchema(getServiceUrl(userTaskInstance.getEndpoint(), userTaskInstance.getProcessId()),
                userTaskInstance,
                env.getArgument(USER),
                env.getArgument(GROUPS));
    }

    protected CompletableFuture<String> updateUserTaskInstance(DataFetchingEnvironment env) {
        UserTaskInstance userTaskInstance = getCacheService().getUserTaskInstancesCache().get(env.getArgument(TASK_ID));
        return getDataIndexApiExecutor().updateUserTaskInstance(getServiceUrl(userTaskInstance.getEndpoint(), userTaskInstance.getProcessId()),
                userTaskInstance,
                env.getArgument(USER),
                env.getArgument(GROUPS),
                env.getArguments());
    }

    protected CompletableFuture<String> createTaskInstanceComment(DataFetchingEnvironment env) {
        UserTaskInstance userTaskInstance = getCacheService().getUserTaskInstancesCache().get(env.getArgument(TASK_ID));
        return getDataIndexApiExecutor().createUserTaskInstanceComment(getServiceUrl(userTaskInstance.getEndpoint(), userTaskInstance.getProcessId()),
                userTaskInstance,
                env.getArgument(USER),
                env.getArgument(GROUPS),
                env.getArgument("comment"));
    }

    protected CompletableFuture<String> createTaskInstanceAttachment(DataFetchingEnvironment env) {
        UserTaskInstance userTaskInstance = getCacheService().getUserTaskInstancesCache().get(env.getArgument(TASK_ID));
        return getDataIndexApiExecutor().createUserTaskInstanceAttachment(getServiceUrl(userTaskInstance.getEndpoint(), userTaskInstance.getProcessId()),
                userTaskInstance,
                env.getArgument(USER),
                env.getArgument(GROUPS),
                env.getArgument("name"),
                env.getArgument("uri"));
    }

    protected CompletableFuture<String> updateUserTaskComment(DataFetchingEnvironment env) {
        Query<UserTaskInstance> query = getCacheService().getUserTaskInstancesCache().query();
        query.filter(singletonList(equalTo("comments.id", env.getArgument(COMMENT_ID))));
        UserTaskInstance userTaskInstance = query.execute().get(0);
        return getDataIndexApiExecutor().updateUserTaskInstanceComment(getServiceUrl(userTaskInstance.getEndpoint(), userTaskInstance.getProcessId()),
                userTaskInstance,
                env.getArgument(USER),
                env.getArgument(GROUPS),
                env.getArgument(COMMENT_ID),
                env.getArgument("comment"));
    }

    protected CompletableFuture<String> deleteUserTaskComment(DataFetchingEnvironment env) {
        Query<UserTaskInstance> query = getCacheService().getUserTaskInstancesCache().query();
        query.filter(singletonList(equalTo("comments.id", env.getArgument(COMMENT_ID))));
        UserTaskInstance userTaskInstance = query.execute().get(0);
        return getDataIndexApiExecutor().deleteUserTaskInstanceComment(getServiceUrl(userTaskInstance.getEndpoint(), userTaskInstance.getProcessId()),
                userTaskInstance,
                env.getArgument(USER),
                env.getArgument(GROUPS),
                env.getArgument(COMMENT_ID));
    }

    protected CompletableFuture<String> updateUserTaskAttachment(DataFetchingEnvironment env) {
        Query<UserTaskInstance> query = getCacheService().getUserTaskInstancesCache().query();
        query.filter(singletonList(equalTo("attachments.id", env.getArgument(ATTACHMENT_ID))));
        UserTaskInstance userTaskInstance = query.execute().get(0);
        return getDataIndexApiExecutor().updateUserTaskInstanceAttachment(getServiceUrl(userTaskInstance.getEndpoint(), userTaskInstance.getProcessId()),
                userTaskInstance,
                env.getArgument(USER),
                env.getArgument(GROUPS),
                env.getArgument(ATTACHMENT_ID),
                env.getArgument("name"),
                env.getArgument("uri"));
    }

    protected CompletableFuture<String> deleteUserTaskAttachment(DataFetchingEnvironment env) {
        Query<UserTaskInstance> query = getCacheService().getUserTaskInstancesCache().query();
        query.filter(singletonList(equalTo("attachments.id", env.getArgument(ATTACHMENT_ID))));
        UserTaskInstance userTaskInstance = query.execute().get(0);
        return getDataIndexApiExecutor().deleteUserTaskInstanceAttachment(getServiceUrl(userTaskInstance.getEndpoint(), userTaskInstance.getProcessId()),
                userTaskInstance,
                env.getArgument(USER),
                env.getArgument(GROUPS),
                env.getArgument(ATTACHMENT_ID));
    }

}
