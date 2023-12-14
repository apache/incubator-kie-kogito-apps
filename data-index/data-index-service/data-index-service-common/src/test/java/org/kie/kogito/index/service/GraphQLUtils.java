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
package org.kie.kogito.index.service;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.kie.kogito.index.model.Job;
import org.kie.kogito.index.model.ProcessDefinition;
import org.kie.kogito.index.model.ProcessInstance;
import org.kie.kogito.index.model.ProcessInstanceState;
import org.kie.kogito.index.model.UserTaskInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;
import static org.kie.kogito.index.json.JsonUtils.getObjectMapper;

public class GraphQLUtils {

    private static final String ID = "id";
    private static final String VERSION = "version";
    private static final String STATE = "state";
    private static final String START = "start";
    private static final String PROCESS_ID = "processId";
    private static final String PARENT_PROCESS_INSTANCE_ID = "parentProcessInstanceId";
    private static final String IS_NULL = "isNull";
    private static final String NODE = "node";
    private static final String MILESTONE = "milestone";
    private static final String ADDON = "addon";
    private static final String STATUS = "status";
    private static final String BUSSINES_KEY = "bk";
    private static final String IDENT = "ident";
    private static final String ACTUAL_OWNER = "actualOwner";
    private static final String STARTED = "started";
    private static final String COMPLETED = "completed";
    private static final String POTENTIAL_GROUPS = "potentialGroups";
    private static final String POTENTIAL_USERS = "potentialUsers";

    private static final Logger LOGGER = LoggerFactory.getLogger(GraphQLUtils.class);
    private static final Map<Class<?>, String> QUERY_FIELDS = new HashMap<>();
    private static final Map<String, String> QUERIES = new HashMap<>();

    static {
        QUERY_FIELDS.put(ProcessDefinition.class, getAllFieldsList(ProcessDefinition.class).map(getFieldName()).collect(joining(", ")));
        QUERY_FIELDS.put(UserTaskInstance.class, getAllFieldsList(UserTaskInstance.class).map(getFieldName()).collect(joining(", ")));
        QUERY_FIELDS.put(ProcessInstance.class, getAllFieldsList(ProcessInstance.class).map(getFieldName()).collect(joining(", ")));
        QUERY_FIELDS.put(Job.class, getAllFieldsList(Job.class).map(getFieldName()).collect(joining(", ")));
        QUERY_FIELDS.computeIfPresent(ProcessInstance.class, (k, v) -> v + ", serviceUrl");
        QUERY_FIELDS.computeIfPresent(ProcessInstance.class, (k, v) -> v + ", childProcessInstances { id, processName }");
        QUERY_FIELDS.computeIfPresent(ProcessInstance.class, (k, v) -> v + ", parentProcessInstance { id, processName }");

        try {
            JsonNode node = getObjectMapper().readTree(Files.readString(Path.of(Thread.currentThread().getContextClassLoader().getResource("graphql_queries.json").toURI())));
            for (Iterator<Map.Entry<String, JsonNode>> it = node.fields(); it.hasNext();) {
                Map.Entry<String, JsonNode> entry = it.next();
                QUERIES.put(entry.getKey(), entry.getValue().toString());
            }
        } catch (Exception ex) {
            LOGGER.error("Failed to parse graphql_queries.json file: {}", ex.getMessage(), ex);
            throw new RuntimeException(ex);
        }
    }

    public static String getProcessDefinitionByIdAndVersion(String id, String version) {
        return getProcessDefinitionQuery("ProcessDefinitionByIdAndVersion", Map.of(ID, id, VERSION, version));
    }

    public static String getProcessInstanceById(String id) {
        return getProcessInstanceQuery("ProcessInstanceById", Map.of(ID, id));
    }

    public static String getProcessInstanceByIdAndState(String id, ProcessInstanceState state) {
        return getProcessInstanceQuery("ProcessInstanceByIdAndState", Map.of(ID, id, STATE, state.name()));
    }

    public static String getProcessInstanceByIdAndStart(String id, String start) {
        return getProcessInstanceQuery("ProcessInstanceByIdAndStart", Map.of(ID, id, START, start));
    }

    public static String getProcessInstanceByIdAndProcessId(String id, String processId) {
        return getProcessInstanceQuery("ProcessInstanceByIdAndProcessId", Map.of(ID, id, PROCESS_ID, processId));
    }

    public static String getProcessInstanceByIdAndParentProcessInstanceId(String id, String parentProcessInstanceId) {
        return getProcessInstanceQuery("ProcessInstanceByIdAndParentProcessInstanceId", Map.of(ID, id, PARENT_PROCESS_INSTANCE_ID, parentProcessInstanceId));
    }

    public static String getProcessInstanceByParentProcessInstanceId(String parentProcessInstanceId) {
        return getProcessInstanceQuery("ProcessInstanceByParentProcessInstanceId", Map.of(PARENT_PROCESS_INSTANCE_ID, parentProcessInstanceId));
    }

    public static String getProcessInstanceByIdAndNullParentProcessInstanceId(String id, Boolean isNull) {
        return getProcessInstanceQuery("ProcessInstanceByIdAndNullParentProcessInstanceId", Map.of(ID, id, IS_NULL, isNull));
    }

    public static String getProcessInstanceByIdAndNullRootProcessInstanceId(String id, Boolean isNull) {
        return getProcessInstanceQuery("ProcessInstanceByIdAndNullRootProcessInstanceId", Map.of(ID, id, IS_NULL, isNull));
    }

    public static String getProcessInstanceByRootProcessInstanceId(String rootProcessInstanceId) {
        return getProcessInstanceQuery("ProcessInstanceByRootProcessInstanceId", Map.of(ID, rootProcessInstanceId));
    }

    public static String getProcessInstanceByIdAndErrorNode(String id, String nodeDefinitionId) {
        return getProcessInstanceQuery("ProcessInstanceByIdAndErrorNode", Map.of(ID, id, NODE, nodeDefinitionId));
    }

    public static String getProcessInstanceByIdAndAddon(String id, String addon) {
        return getProcessInstanceQuery("ProcessInstanceByIdAndAddon", Map.of(ID, id, ADDON, addon));
    }

    public static String getProcessInstanceByIdAndMilestoneName(String id, String milestone) {
        return getProcessInstanceQuery("ProcessInstanceByIdAndMilestoneName", Map.of(ID, id, MILESTONE, milestone));
    }

    public static String getProcessInstanceByIdAndMilestoneStatus(String id, String status) {
        return getProcessInstanceQuery("ProcessInstanceByIdAndMilestoneStatus", Map.of(ID, id, STATUS, status));
    }

    public static String getProcessInstanceByBusinessKey(String businessKey) {
        return getProcessInstanceQuery("ProcessInstanceByBusinessKey", Map.of(BUSSINES_KEY, businessKey));
    }

    public static String getProcessInstanceByCreatedBy(String identity) {
        return getProcessInstanceQuery("ProcessInstanceByCreatedBy", Map.of(IDENT, identity));
    }

    public static String getProcessInstanceByUpdatedBy(String identity) {
        return getProcessInstanceQuery("ProcessInstanceByUpdatedBy", Map.of(IDENT, identity));
    }

    public static String getUserTaskInstanceById(String id) {
        return getUserTaskInstanceQuery("UserTaskInstanceById", Map.of(ID, id));
    }

    public static String getUserTaskInstanceByProcessInstanceId(String id) {
        return getUserTaskInstanceQuery("UserTaskInstanceByProcessInstanceId", Map.of(ID, id));
    }

    public static String getUserTaskInstanceByIdAndActualOwner(String id, String actualOwner) {
        return getUserTaskInstanceQuery("UserTaskInstanceByIdAndActualOwner", Map.of(ID, id, ACTUAL_OWNER, actualOwner));
    }

    public static String getUserTaskInstanceByIdAndProcessId(String id, String processId) {
        return getUserTaskInstanceQuery("UserTaskInstanceByIdAndProcessId", Map.of(ID, id, PROCESS_ID, processId));
    }

    public static String getUserTaskInstanceByIdNoActualOwner(String id) {
        return getUserTaskInstanceQuery("UserTaskInstanceByIdNoActualOwner", Map.of(ID, id));
    }

    public static String getUserTaskInstanceByIdAndState(String id, String state) {
        return getUserTaskInstanceQuery("UserTaskInstanceByIdAndState", Map.of(ID, id, STATE, state));
    }

    public static String getUserTaskInstanceByIdAndStarted(String id, String started) {
        return getUserTaskInstanceQuery("UserTaskInstanceByIdAndStarted", Map.of(ID, id, STARTED, started));
    }

    public static String getUserTaskInstanceByIdAndCompleted(String id, String completed) {
        return getUserTaskInstanceQuery("UserTaskInstanceByIdAndCompleted", Map.of(ID, id, COMPLETED, completed));
    }

    public static String getUserTaskInstanceByIdAndPotentialGroups(String id, List<String> potentialGroups) throws Exception {
        return getUserTaskInstanceWithArray("UserTaskInstanceByIdAndPotentialGroups", potentialGroups, POTENTIAL_GROUPS, Map.of(ID, id));
    }

    public static String getUserTaskInstanceByIdAndPotentialUsers(String id, List<String> potentialUsers) throws Exception {
        return getUserTaskInstanceWithArray("UserTaskInstanceByIdAndPotentialUsers", potentialUsers, POTENTIAL_USERS, Map.of(ID, id));
    }

    public static String getJobById(String id) {
        return getJobQuery("JobById", Map.of(ID, id));
    }

    public static String getTravelsByUserTaskId(String id) {
        return getQuery("TravelsByUserTaskId", id);
    }

    public static String getTravelsByProcessInstanceId(String id) {
        return getQuery("TravelsByProcessInstanceId", id);
    }

    public static String getTravelsByProcessInstanceIdAndTravellerFirstName(String id, String name) {
        return getQuery("TravelsByProcessInstanceIdAndTravellerFirstName", id, name);
    }

    public static String getDealsByTaskId(String id) {
        return getQuery("DealsByTaskId", id);
    }

    public static String getDealsByTaskIdNoActualOwner(String id) {
        return getQuery("DealsByTaskIdNoActualOwner", id);
    }

    private static String getUserTaskInstanceWithArray(String query, List<String> values, String variable, Map<String, Object> args) throws Exception {
        String json = getUserTaskInstanceQuery(query, args);
        ObjectNode jsonNode = (ObjectNode) getObjectMapper().readTree(json);
        ArrayNode pg = (ArrayNode) jsonNode.get("variables").get(variable);
        values.forEach(g -> pg.add(g));
        return jsonNode.toString();
    }

    private static String getQuery(String name, String... args) {
        return format(QUERIES.get(name), args);
    }

    private static String getProcessInstanceQuery(String name, Map<String, Object> args) {
        return getQuery(name, ProcessInstance.class, args);
    }

    private static String getProcessDefinitionQuery(String name, Map<String, Object> args) {
        return getQuery(name, ProcessDefinition.class, args);
    }

    private static String getUserTaskInstanceQuery(String name, Map<String, Object> args) {
        return getQuery(name, UserTaskInstance.class, args);
    }

    private static String getJobQuery(String name, Map<String, Object> args) {
        return getQuery(name, Job.class, args);
    }

    private static String getQuery(String name, Class<?> clazz, Map<String, Object> args) {
        try {
            String query = format(QUERIES.get(name), QUERY_FIELDS.get(clazz));
            JsonNode jsonQuery = getObjectMapper().readTree(query);
            ObjectNode jsonVars = getObjectMapper().valueToTree(args);
            jsonQuery.withObject("/variables").setAll(jsonVars);
            return jsonQuery.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Stream<Field> getAllFieldsList(Class clazz) {
        return FieldUtils.getAllFieldsList(clazz).stream().filter(getSoourcePredicate().or(getSoourcePredicate()));
    }

    private static Function<Field, String> getFieldName() {
        return field -> {
            if (field.getGenericType() instanceof ParameterizedType) {
                ParameterizedType genericType = (ParameterizedType) field.getGenericType();
                StringBuilder builder = new StringBuilder();
                builder.append(Arrays.stream(genericType.getActualTypeArguments()).filter(type -> type.getTypeName().startsWith("org.kie.kogito.index.model"))
                        .flatMap(type -> {
                            try {
                                return getAllFieldsList(Class.forName(type.getTypeName()));
                            } catch (Exception ex) {
                                return Stream.empty();
                            }
                        }).map(f -> getFieldName().apply(f)).collect(joining(", ")));
                if (builder.length() > 0) {
                    return field.getName() + " { " + builder.toString() + " }";
                }
            }

            if (field.getType().getName().startsWith("org.kie.kogito.index.model")) {
                return field.getName() + " { " + getAllFieldsList(field.getType()).map(f -> getFieldName().apply(f)).collect(joining(", ")) + " }";
            }

            return field.getName();
        };
    }

    //  See https://www.eclemma.org/jacoco/trunk/doc/faq.html    
    private static Predicate<Field> getJacocoPredicate() {
        return field -> !field.getName().equals("$jacocoData");
    }

    private static Predicate<Field> getSoourcePredicate() {
        return field -> !(field.getDeclaringClass().equals(ProcessDefinition.class) && (field.getName().equals("source") || field.getName().equals("nodes")));
    }
}
