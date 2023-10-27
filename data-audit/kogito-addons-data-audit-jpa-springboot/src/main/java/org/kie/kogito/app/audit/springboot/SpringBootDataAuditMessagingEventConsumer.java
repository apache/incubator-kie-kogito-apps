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
package org.kie.kogito.app.audit.springboot;

import javax.persistence.EntityManager;

import org.kie.kogito.app.audit.api.DataAuditStoreProxyService;
import org.kie.kogito.app.audit.jpa.JPADataAuditStore;
import org.kie.kogito.event.process.ProcessInstanceDataEvent;
import org.kie.kogito.event.usertask.UserTaskInstanceDataEvent;
import org.kie.kogito.jobs.service.api.Job;
import org.kie.kogito.jobs.service.api.event.JobCloudEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static org.kie.kogito.app.audit.api.DataAuditContext.newDataAuditContext;
import static org.kie.kogito.app.audit.api.SubsystemConstants.KOGITO_JOBS_EVENTS;
import static org.kie.kogito.app.audit.api.SubsystemConstants.KOGITO_PROCESSINSTANCES_EVENTS;
import static org.kie.kogito.app.audit.api.SubsystemConstants.KOGITO_USERTASKINSTANCES_EVENTS;

@Service
@KafkaListener(topics = { KOGITO_PROCESSINSTANCES_EVENTS, KOGITO_USERTASKINSTANCES_EVENTS, KOGITO_JOBS_EVENTS}, groupId = "data-audit", clientIdPrefix = "data-audit-events")
public class SpringBootDataAuditMessagingEventConsumer {

    private final Logger LOGGER = LoggerFactory.getLogger(SpringBootDataAuditMessagingEventConsumer.class);

    private DataAuditStoreProxyService proxy;

    private JPADataAuditStore jpaAuditStore;

    @Autowired
    EntityManager entityManager;
    
    
    public SpringBootDataAuditMessagingEventConsumer() {
        jpaAuditStore = new JPADataAuditStore();
        proxy = DataAuditStoreProxyService.newAuditStoreSerice(jpaAuditStore);
    }
    
    @KafkaHandler
    public void onProcessInstanceEvent(ProcessInstanceDataEvent<?> event) {
        LOGGER.debug("Process instance consumer received ProcessInstanceDataEvent: \n{}", event);
        proxy.storeProcessInstanceDataEvent(newDataAuditContext(entityManager), event);       
    }
    
    @KafkaHandler
    public void onUserTaskInstanceEvent(UserTaskInstanceDataEvent<?> event) {
        LOGGER.debug("Process instance consumer received ProcessInstanceDataEvent: \n{}", event);
        proxy.storeUserTaskInstanceDataEvent(newDataAuditContext(entityManager), event);    

    }
    
    @KafkaHandler
    public void onJobInstanceEvent(JobCloudEvent<Job> event) {
        LOGGER.debug("Process instance consumer received ProcessInstanceDataEvent: \n{}", event);
        proxy.storeJobDataEvent(newDataAuditContext(entityManager), event);
    }
}