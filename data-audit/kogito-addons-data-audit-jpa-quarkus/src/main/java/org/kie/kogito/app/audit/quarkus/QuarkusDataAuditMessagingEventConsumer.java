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
package org.kie.kogito.app.audit.quarkus;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.kie.kogito.app.audit.api.DataAuditContext;
import org.kie.kogito.app.audit.api.DataAuditStoreProxyService;
import org.kie.kogito.app.audit.jpa.JPADataAuditStore;
import org.kie.kogito.event.process.ProcessInstanceDataEvent;
import org.kie.kogito.event.usertask.UserTaskInstanceDataEvent;
import org.kie.kogito.jobs.service.api.Job;
import org.kie.kogito.jobs.service.api.event.JobCloudEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.smallrye.common.annotation.Blocking;

import static org.kie.kogito.app.audit.api.SubsystemConstants.KOGITO_JOBS_EVENTS;
import static org.kie.kogito.app.audit.api.SubsystemConstants.KOGITO_PROCESSINSTANCES_EVENTS;
import static org.kie.kogito.app.audit.api.SubsystemConstants.KOGITO_USERTASKINSTANCES_EVENTS;

@ApplicationScoped
public class QuarkusDataAuditMessagingEventConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuarkusDataAuditMessagingEventConsumer.class);

    private DataAuditStoreProxyService proxy;

    @PersistenceContext
    EntityManager entityManager;

    public QuarkusDataAuditMessagingEventConsumer() {
        proxy = DataAuditStoreProxyService.newAuditStoreSerice(new JPADataAuditStore());
    }

    @Incoming(KOGITO_PROCESSINSTANCES_EVENTS)
    @Blocking
    @Transactional
    public void onProcessInstanceEvent(ProcessInstanceDataEvent<?> event) {
        LOGGER.debug("Process instance consumer received ProcessInstanceDataEvent: \n{}", event);
        proxy.storeProcessInstanceDataEvent(DataAuditContext.of(entityManager), event);

    }

    @Incoming(KOGITO_USERTASKINSTANCES_EVENTS)
    @Blocking
    @Transactional
    public void onUserTaskInstanceEvent(UserTaskInstanceDataEvent<?> event) {
        LOGGER.debug("Task instance received UserTaskInstanceDataEvent \n{}", event);
        proxy.storeUserTaskInstanceDataEvent(DataAuditContext.of(entityManager), event);

    }

    @Incoming(KOGITO_JOBS_EVENTS)
    @Blocking
    @Transactional
    public void onJobEvent(JobCloudEvent<Job> event) {
        LOGGER.debug("Job received KogitoJobCloudEvent \n{}", event);
        proxy.storeJobDataEvent(DataAuditContext.of(entityManager), event);
    }

}
