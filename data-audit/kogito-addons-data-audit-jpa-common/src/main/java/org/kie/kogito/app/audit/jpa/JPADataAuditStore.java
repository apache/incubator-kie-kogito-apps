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
package org.kie.kogito.app.audit.jpa;

import java.net.MalformedURLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;

import org.kie.kogito.app.audit.api.DataAuditContext;
import org.kie.kogito.app.audit.jpa.model.AbstractProcessInstanceLog;
import org.kie.kogito.app.audit.jpa.model.AbstractUserTaskInstanceLog;
import org.kie.kogito.app.audit.jpa.model.JobExecutionLog;
import org.kie.kogito.app.audit.jpa.model.ProcessInstanceErrorLog;
import org.kie.kogito.app.audit.jpa.model.ProcessInstanceNodeLog;
import org.kie.kogito.app.audit.jpa.model.ProcessInstanceNodeLog.NodeLogType;
import org.kie.kogito.app.audit.jpa.model.ProcessInstanceStateLog;
import org.kie.kogito.app.audit.jpa.model.ProcessInstanceStateLog.ProcessStateLogType;
import org.kie.kogito.app.audit.jpa.model.ProcessInstanceVariableLog;
import org.kie.kogito.app.audit.jpa.model.UserTaskInstanceAssignmentLog;
import org.kie.kogito.app.audit.jpa.model.UserTaskInstanceAttachmentLog;
import org.kie.kogito.app.audit.jpa.model.UserTaskInstanceCommentLog;
import org.kie.kogito.app.audit.jpa.model.UserTaskInstanceDeadlineLog;
import org.kie.kogito.app.audit.jpa.model.UserTaskInstanceStateLog;
import org.kie.kogito.app.audit.jpa.model.UserTaskInstanceVariableLog;
import org.kie.kogito.app.audit.jpa.model.UserTaskInstanceVariableLog.VariableType;
import org.kie.kogito.app.audit.spi.DataAuditStore;
import org.kie.kogito.event.process.ProcessInstanceDataEvent;
import org.kie.kogito.event.process.ProcessInstanceErrorDataEvent;
import org.kie.kogito.event.process.ProcessInstanceNodeDataEvent;
import org.kie.kogito.event.process.ProcessInstanceNodeEventBody;
import org.kie.kogito.event.process.ProcessInstanceSLADataEvent;
import org.kie.kogito.event.process.ProcessInstanceStateDataEvent;
import org.kie.kogito.event.process.ProcessInstanceStateEventBody;
import org.kie.kogito.event.process.ProcessInstanceVariableDataEvent;
import org.kie.kogito.event.usertask.UserTaskInstanceAssignmentDataEvent;
import org.kie.kogito.event.usertask.UserTaskInstanceAttachmentDataEvent;
import org.kie.kogito.event.usertask.UserTaskInstanceCommentDataEvent;
import org.kie.kogito.event.usertask.UserTaskInstanceDataEvent;
import org.kie.kogito.event.usertask.UserTaskInstanceDeadlineDataEvent;
import org.kie.kogito.event.usertask.UserTaskInstanceStateDataEvent;
import org.kie.kogito.event.usertask.UserTaskInstanceVariableDataEvent;
import org.kie.kogito.jobs.service.api.Job;
import org.kie.kogito.jobs.service.api.event.JobCloudEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JPADataAuditStore implements DataAuditStore {

    private static final Logger LOGGER = LoggerFactory.getLogger(JPADataAuditStore.class);

    private ObjectMapper mapper;

    public JPADataAuditStore() {
        mapper = new ObjectMapper();
    }

    @Override
    public void storeProcessInstanceDataEvent(DataAuditContext context, ProcessInstanceStateDataEvent event) {
        ProcessInstanceStateLog log = new ProcessInstanceStateLog();

        setProcessCommonAttributes(log, event);
        log.setState(event.getKogitoProcessInstanceState());
        log.setRoles(event.getData().getRoles());

        EntityManager entityManager = context.getContext();
        switch (event.getData().getEventType()) {
            case ProcessInstanceStateEventBody.EVENT_TYPE_STARTED:
                log.setEventType(ProcessStateLogType.STARTED);
                entityManager.persist(log);
                break;
            case ProcessInstanceStateEventBody.EVENT_TYPE_ENDED:
                log.setEventType(ProcessStateLogType.COMPLETED);
                entityManager.persist(log);
                break;
        }
    }

    @Override
    public void storeProcessInstanceDataEvent(DataAuditContext context, ProcessInstanceErrorDataEvent event) {
        ProcessInstanceErrorLog log = new ProcessInstanceErrorLog();

        setProcessCommonAttributes(log, event);

        log.setErrorMessage(event.getData().getErrorMessage());
        log.setNodeDefinitionId(event.getData().getNodeDefinitionId());
        log.setNodeInstanceId(event.getData().getNodeInstanceId());
        EntityManager entityManager = context.getContext();
        entityManager.persist(log);
    }

    @Override
    public void storeProcessInstanceDataEvent(DataAuditContext context, ProcessInstanceNodeDataEvent event) {
        ProcessInstanceNodeLog log = new ProcessInstanceNodeLog();

        setProcessCommonAttributes(log, event);

        log.setConnection(event.getData().getConnectionNodeDefinitionId());
        log.setNodeDefinitionId(event.getData().getNodeDefinitionId());
        log.setNodeType(event.getData().getNodeType());

        log.setNodeInstanceId(event.getData().getNodeInstanceId());
        log.setNodeName(event.getData().getNodeName());

        switch (event.getData().getEventType()) {
            case ProcessInstanceNodeEventBody.EVENT_TYPE_ENTER:
                log.setEventType(NodeLogType.ENTER);
                break;
            case ProcessInstanceNodeEventBody.EVENT_TYPE_EXIT:
                log.setEventType(NodeLogType.EXIT);
                break;
            case ProcessInstanceNodeEventBody.EVENT_TYPE_ABORTED:
                log.setEventType(NodeLogType.ABORTED);
                break;
            case ProcessInstanceNodeEventBody.EVENT_TYPE_SKIPPED:
                log.setEventType(NodeLogType.SKIPPED);
                break;
            case ProcessInstanceNodeEventBody.EVENT_TYPE_OBSOLETE:
                log.setEventType(NodeLogType.OBSOLETE);
                break;
            case ProcessInstanceNodeEventBody.EVENT_TYPE_ERROR:
                log.setEventType(NodeLogType.ERROR);
                break;

        }

        log.setWorkItemId(event.getData().getWorkItemId());
        EntityManager entityManager = context.getContext();
        entityManager.persist(log);
    }

    @Override
    public void storeProcessInstanceDataEvent(DataAuditContext context, ProcessInstanceSLADataEvent event) {
        EntityManager entityManager = context.getContext();

        if(event.getData().getNodeDefinitionId() != null) {
        
            ProcessInstanceStateLog log = new ProcessInstanceStateLog();
            setProcessCommonAttributes(log, event);
            
            log.setEventType(ProcessStateLogType.SLA_VIOLATION);
            
            entityManager.persist(log);
        } else {
            ProcessInstanceNodeLog log = new ProcessInstanceNodeLog();
            setProcessCommonAttributes(log, event);
            log.setNodeDefinitionId(event.getData().getNodeDefinitionId());
            log.setNodeInstanceId(event.getData().getNodeInstanceId());
            log.setNodeName(event.getData().getNodeName());
            log.setNodeType(event.getData().getNodeType());
            log.setEventType(NodeLogType.SLA_VIOLATION);
            entityManager.persist(log);
        }
    }

    @Override
    public void storeProcessInstanceDataEvent(DataAuditContext context, ProcessInstanceVariableDataEvent event) {

        ProcessInstanceVariableLog log = new ProcessInstanceVariableLog();

        setProcessCommonAttributes(log, event);

        log.setVariableId(event.getData().getVariableId());
        log.setVariableName(event.getData().getVariableName());
        log.setVariableValue(toJsonString(event.getData().getVariableValue()));
        EntityManager entityManager = context.getContext();
        entityManager.persist(log);

    }

    private void setProcessCommonAttributes(AbstractProcessInstanceLog log, ProcessInstanceDataEvent<?> event) {
        log.setEventId(event.getId());
        log.setEventDate(new Date(event.getTime().toInstant().toEpochMilli()));
        log.setProcessType(event.getKogitoProcessType());
        log.setProcessId(event.getKogitoProcessId());
        log.setProcessVersion(event.getKogitoProcessInstanceVersion());
        log.setParentProcessInstanceId(event.getKogitoParentProcessInstanceId());
        log.setProcessInstanceId(event.getKogitoProcessInstanceId());
        log.setRootProcessId(event.getKogitoRootProcessId());
        log.setRootProcessInstanceId(event.getKogitoRootProcessInstanceId());
        log.setBusinessKey(event.getKogitoBusinessKey());
    }

    @Override
    public void storeUserTaskInstanceDataEvent(DataAuditContext context, UserTaskInstanceAssignmentDataEvent event) {
        UserTaskInstanceAssignmentLog log = new UserTaskInstanceAssignmentLog();

        setUserTaskCommonAttributes(log, event);
        log.setUserTaskDefinitionId(event.getData().getUserTaskDefinitionId());
        log.setEventUser(event.getData().getEventUser());
        log.setUsers(event.getData().getUsers());
        log.setAssignmentType(event.getData().getAssignmentType());
        EntityManager entityManager = context.getContext();
        entityManager.persist(log);

    }

    @Override
    public void storeUserTaskInstanceDataEvent(DataAuditContext context, UserTaskInstanceAttachmentDataEvent event) {
        UserTaskInstanceAttachmentLog log = new UserTaskInstanceAttachmentLog();

        setUserTaskCommonAttributes(log, event);
        log.setUserTaskDefinitionId(event.getData().getUserTaskDefinitionId());
        log.setEventUser(event.getData().getEventUser());
        log.setAttachmentId(event.getData().getAttachmentId());
        log.setAttachmentName(event.getData().getAttachmentName());
        try {
            log.setAttachmentURI(event.getData().getAttachmentURI().toURL());
        } catch (MalformedURLException e) {
            LOGGER.error("Could not serialize url {}", e);
        }
        log.setEventType(event.getData().getEventType());
        EntityManager entityManager = context.getContext();
        entityManager.persist(log);

    }

    @Override
    public void storeUserTaskInstanceDataEvent(DataAuditContext context, UserTaskInstanceCommentDataEvent event) {
        UserTaskInstanceCommentLog log = new UserTaskInstanceCommentLog();

        setUserTaskCommonAttributes(log, event);
        log.setUserTaskDefinitionId(event.getData().getUserTaskDefinitionId());
        log.setEventUser(event.getData().getEventUser());
        log.setCommentId(event.getData().getCommentId());
        log.setCommentContent(event.getData().getCommentContent());
        log.setEventType(event.getData().getEventType());
        EntityManager entityManager = context.getContext();
        entityManager.persist(log);

    }

    @Override
    public void storeUserTaskInstanceDataEvent(DataAuditContext context, UserTaskInstanceDeadlineDataEvent event) {
        UserTaskInstanceDeadlineLog log = new UserTaskInstanceDeadlineLog();

        setUserTaskCommonAttributes(log, event);
        log.setUserTaskDefinitionId(event.getData().getUserTaskDefinitionId());
        log.setEventUser(event.getData().getEventUser());
        if (event.getData().getNotification() != null) {
            Map<String, String> data = new HashMap<>();
            for (Map.Entry<String, Object> entry : event.getData().getNotification().entrySet()) {
                data.put(entry.getKey(), entry.getValue().toString());
            }
            log.setNotification(data);
        }
        EntityManager entityManager = context.getContext();
        entityManager.persist(log);
    }

    @Override
    public void storeUserTaskInstanceDataEvent(DataAuditContext context, UserTaskInstanceStateDataEvent event) {
        UserTaskInstanceStateLog log = new UserTaskInstanceStateLog();

        setUserTaskCommonAttributes(log, event);
        log.setUserTaskDefinitionId(event.getData().getUserTaskDefinitionId());
        log.setActualUser(event.getData().getActualOwner());
        log.setName(event.getData().getUserTaskName());
        log.setDescription(event.getData().getUserTaskDescription());
        log.setState(event.getData().getState());
        log.setEventType(event.getData().getEventType());
        EntityManager entityManager = context.getContext();
        entityManager.persist(log);

    }

    @Override
    public void storeUserTaskInstanceDataEvent(DataAuditContext context, UserTaskInstanceVariableDataEvent event) {
        UserTaskInstanceVariableLog log = new UserTaskInstanceVariableLog();

        setUserTaskCommonAttributes(log, event);

        log.setEventUser(event.getData().getEventUser());
        log.setUserTaskDefinitionId(event.getData().getUserTaskDefinitionId());
        log.setVariableId(event.getData().getVariableId());
        log.setVariableName(event.getData().getVariableName());
        log.setVariableValue(toJsonString(event.getData().getVariableValue()));

        switch (event.getData().getVariableType()) {
            case "INPUT":
                log.setVariableType(VariableType.INPUT);
                break;
            case "OUTPUT":
                log.setVariableType(VariableType.OUTPUT);
                break;
        }
        EntityManager entityManager = context.getContext();
        entityManager.persist(log);
    }

    private void setUserTaskCommonAttributes(AbstractUserTaskInstanceLog log, UserTaskInstanceDataEvent<?> event) {
        log.setEventId(event.getId());
        log.setEventDate(new Date(event.getTime().toInstant().toEpochMilli()));
        log.setProcessInstanceId(event.getKogitoProcessInstanceId());
        log.setBusinessKey(event.getKogitoBusinessKey());
        log.setUserTaskInstanceId(event.getKogitoUserTaskInstanceId());
    }

    @Override
    public void storeJobDataEvent(DataAuditContext context, JobCloudEvent<Job> jobDataEvent) {
        Job job = jobDataEvent.getData();

        JobExecutionLog log = new JobExecutionLog();
        log.setCorrelationId(job.getCorrelationId());
        log.setJobId(job.getId());
        log.setState(job.getState().name());
        log.setExecutionTimeout(job.getExecutionTimeout());
        log.setSchedule(toJsonString(job.getSchedule()));
        log.setRetry(toJsonString(job.getRetry()));
        log.setExecutionTimeoutUnit(job.getExecutionTimeoutUnit().name());
        log.setTimestamp(Timestamp.from(jobDataEvent.getTime().toInstant()));
        EntityManager entityManager = context.getContext();
        entityManager.persist(log);
    }

    private String toJsonString(Object data) {
        try {
            if (data == null) {
                return null;
            }

            return mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            LOGGER.error("could not convert to json string {}", data);
            return null;
        }
    }

}
