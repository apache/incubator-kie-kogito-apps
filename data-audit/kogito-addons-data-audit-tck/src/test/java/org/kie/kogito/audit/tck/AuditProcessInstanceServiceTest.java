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

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.kie.kogito.app.audit.service.api.AuditQueryService;
import org.kie.kogito.app.audit.service.api.AuditStoreService;
import org.kie.kogito.app.audit.service.spi.AuditStoreContainerBeanLocator;
import org.kie.kogito.event.process.ProcessInstanceErrorDataEvent;
import org.kie.kogito.event.process.ProcessInstanceNodeDataEvent;
import org.kie.kogito.event.process.ProcessInstanceNodeEventBody;
import org.kie.kogito.event.process.ProcessInstanceStateDataEvent;
import org.kie.kogito.event.process.ProcessInstanceStateEventBody;
import org.kie.kogito.event.process.ProcessInstanceVariableDataEvent;
import org.kie.kogito.process.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.kie.kogito.audit.tck.DataAuditTestUtils.deriveProcessInstanceStateEvent;
import static org.kie.kogito.audit.tck.DataAuditTestUtils.newProcessInstanceErrorEvent;
import static org.kie.kogito.audit.tck.DataAuditTestUtils.newProcessInstanceNodeEvent;
import static org.kie.kogito.audit.tck.DataAuditTestUtils.newProcessInstanceStateEvent;
import static org.kie.kogito.audit.tck.DataAuditTestUtils.newProcessInstanceVariableEvent;

import graphql.ExecutionResult;

public class AuditProcessInstanceServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditProcessInstanceServiceTest.class);

    @BeforeAll
    public static void init() {
        // we populate the database with simple dataset
        AuditStoreService proxy = AuditStoreService.newAuditStoreSerice();

        ProcessInstanceStateDataEvent processInstanceEvent;

        // the first process started
        processInstanceEvent = newProcessInstanceStateEvent("processId1", "1", ProcessInstance.STATE_ACTIVE, "rootI1", "rootP1", "parent1", "identity",
                ProcessInstanceStateEventBody.EVENT_TYPE_STARTED);
        proxy.storeProcessInstanceDataEvent(processInstanceEvent);

        ProcessInstanceVariableDataEvent processInstanceVariableEvent = newProcessInstanceVariableEvent(processInstanceEvent, "var_id1", "varName", "errorMessage", "identity");
        proxy.storeProcessInstanceDataEvent(processInstanceVariableEvent);
        
        processInstanceVariableEvent = newProcessInstanceVariableEvent(processInstanceEvent, "var_id1", "varName", "variableValue", "identity");
        proxy.storeProcessInstanceDataEvent(processInstanceVariableEvent);
        
        ProcessInstanceNodeDataEvent processInstanceNodeEvent;
        processInstanceNodeEvent = newProcessInstanceNodeEvent(processInstanceEvent, "StartNode", "nd1", "ni1", "name1", null, "myuser", ProcessInstanceNodeEventBody.EVENT_TYPE_ENTER);
        proxy.storeProcessInstanceDataEvent(processInstanceNodeEvent);

        processInstanceNodeEvent = newProcessInstanceNodeEvent(processInstanceEvent, "StartNode", "nd1", "ni1", "name1", null, "myuser", ProcessInstanceNodeEventBody.EVENT_TYPE_EXIT);
        proxy.storeProcessInstanceDataEvent(processInstanceNodeEvent);
        
        processInstanceNodeEvent = newProcessInstanceNodeEvent(processInstanceEvent, "EndNode", "nd2", "ni2", "name2", null, "myuser", ProcessInstanceNodeEventBody.EVENT_TYPE_ENTER);
        proxy.storeProcessInstanceDataEvent(processInstanceNodeEvent);

        // the second completed
        processInstanceEvent = newProcessInstanceStateEvent("processId1", "2", ProcessInstance.STATE_ACTIVE, "rootI1", "rootP1", "parent1", "identity",
                ProcessInstanceStateEventBody.EVENT_TYPE_STARTED);
        proxy.storeProcessInstanceDataEvent(processInstanceEvent);

        processInstanceEvent = deriveProcessInstanceStateEvent(processInstanceEvent, "identity2", ProcessInstance.STATE_COMPLETED, ProcessInstanceStateEventBody.EVENT_TYPE_ENDED);
        proxy.storeProcessInstanceDataEvent(processInstanceEvent);

        // the third in error
        processInstanceEvent = newProcessInstanceStateEvent("processId2", "3", ProcessInstance.STATE_ACTIVE, "rootI1", "rootP1", "parent1", "identity",
                ProcessInstanceStateEventBody.EVENT_TYPE_STARTED);
        proxy.storeProcessInstanceDataEvent(processInstanceEvent);
        
        ProcessInstanceErrorDataEvent processInstanceErrorEvent = newProcessInstanceErrorEvent(processInstanceEvent, "nd1", "ni1", "errorMessage1", "identity");
        proxy.storeProcessInstanceDataEvent(processInstanceErrorEvent);

        processInstanceErrorEvent = newProcessInstanceErrorEvent(processInstanceEvent, "nd2", "ni2", "errorMessage2", "identity");
        proxy.storeProcessInstanceDataEvent(processInstanceErrorEvent);
        
        processInstanceEvent = deriveProcessInstanceStateEvent(processInstanceEvent, "identity3", ProcessInstance.STATE_ERROR, ProcessInstanceStateEventBody.EVENT_TYPE_ENDED);
        proxy.storeProcessInstanceDataEvent(processInstanceEvent);

    }

    @Test
    public void testGetAllProcessInstancesState() {
        AuditQueryService service = AuditQueryService.newAuditQuerySerice();

        ExecutionResult executionResult = service.executeQuery(
                "{ GetAllProcessInstancesState { eventId, eventDate, processType, processId, processVersion, parentProcessInstanceId, rootProcessId, rootProcessInstanceId, processInstanceId, businessKey, eventType, outcome, state, slaDueDate } }");
        List<Map<String, Object>> data = ((List) ((Map<String, Object>) executionResult.getData()).get("GetAllProcessInstancesState"));
        LOGGER.info("Outcome {}", executionResult);
        assertThat(executionResult.getErrors()).hasSize(0);
        assertThat(data)
                .hasSize(3)
                .extracting(e -> e.get("processInstanceId"), e -> e.get("state"))
                .containsExactlyInAnyOrder(tuple("1", String.valueOf(ProcessInstance.STATE_ACTIVE)), tuple("2", String.valueOf(ProcessInstance.STATE_COMPLETED)),
                        tuple("3", String.valueOf(ProcessInstance.STATE_ERROR)));

    }

    @Test
    public void testGetAllProcessInstancesNodeByProcessInstanceId() {
        AuditQueryService service = AuditQueryService.newAuditQuerySerice();

        
        
        ExecutionResult executionResult = service.executeQuery(
                "{ GetAllProcessInstancesNodeByProcessInstanceId ( processInstanceId : \"1\") { eventId, eventDate, processType, processId, processVersion, parentProcessInstanceId, rootProcessId, rootProcessInstanceId, processInstanceId, businessKey, eventType, nodeContainerId, nodeDefinitionId, nodeType , nodeName, nodeInstanceId, connection, slaDueDate , eventData  } }");
        LOGGER.info("Outcome {}", executionResult);
        assertThat(executionResult.getErrors()).hasSize(0);
        List<Map<String, Object>> data = ((List) ((Map<String, Object>) executionResult.getData()).get("GetAllProcessInstancesNodeByProcessInstanceId"));
        assertThat(data)
                .hasSize(2)
                .extracting(e -> e.get("processInstanceId"), e -> e.get("nodeInstanceId"), e -> e.get("eventType"))
                .containsExactlyInAnyOrder(
                        tuple("1", "ni1", "EXIT"), 
                        tuple("1", "ni2", "ENTER"));

    }
    
    
    
    
    @Test
    public void testGetAllProcessInstancesErrorByProcessInstanceId() {
        AuditQueryService service = AuditQueryService.newAuditQuerySerice();

        ExecutionResult executionResult = service.executeQuery(
                "{ GetAllProcessInstancesErrorByProcessInstanceId ( processInstanceId : \"3\")  { eventId, eventDate, processType, processId, processVersion, parentProcessInstanceId, rootProcessId, rootProcessInstanceId, processInstanceId, businessKey, errorMessage, nodeDefinitionId, nodeInstanceId } }");
        List<Map<String, Object>> data = ((List) ((Map<String, Object>) executionResult.getData()).get("GetAllProcessInstancesErrorByProcessInstanceId"));
        LOGGER.info("Outcome {}", executionResult);
        assertThat(executionResult.getErrors()).hasSize(0);
        assertThat(data)
                .hasSize(2)
                .extracting(e -> e.get("errorMessage"), e -> e.get("nodeDefinitionId"), e -> e.get("nodeInstanceId"))
                .containsExactlyInAnyOrder(
                        tuple("errorMessage1", "nd1", "ni1"), 
                        tuple("errorMessage2", "nd2", "ni2"));

    }

    
    
    
    @Test
    public void testGetAllProcessInstancesVariablebyProcessInstanceId() {
        AuditQueryService service = AuditQueryService.newAuditQuerySerice();

        ExecutionResult executionResult = service.executeQuery(
                "{ GetAllProcessInstancesVariablebyProcessInstanceId ( processInstanceId : \"1\")  { eventId, eventDate, processType, processId, processVersion, parentProcessInstanceId, rootProcessId, rootProcessInstanceId, processInstanceId, businessKey, variableId, variableName, variableValue } }");
        List<Map<String, Object>> data = ((List) ((Map<String, Object>) executionResult.getData()).get("GetAllProcessInstancesVariablebyProcessInstanceId"));
        LOGGER.info("Outcome {}", executionResult);
        assertThat(executionResult.getErrors()).hasSize(0);
        assertThat(data)
                .hasSize(1)
                .extracting(e -> e.get("variableId"), e -> e.get("variableName"),e -> e.get("variableValue"))
                .containsExactlyInAnyOrder(
                        tuple("var_id1", "varName", "\"variableValue\""));

    }
}
