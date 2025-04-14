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
package org.kie.kogito.jobs.service.management;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.kie.kogito.jobs.service.messaging.MessagingHandler;
import org.kie.kogito.jobs.service.model.JobServiceManagementInfo;
import org.kie.kogito.jobs.service.repository.JobServiceManagementRepository;
import org.kie.kogito.jobs.service.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.runtime.StartupEvent;
import io.vertx.core.Vertx;
import io.vertx.core.WorkerExecutor;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.event.Event;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

@Singleton
@Transactional
public class JobServiceInstanceManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobServiceInstanceManager.class);

    @ConfigProperty(name = "kogito.jobs-service.management.heartbeat.interval-in-seconds", defaultValue = "1")
    long heardBeatIntervalInSeconds;

    @ConfigProperty(name = "kogito.jobs-service.management.leader-check.interval-in-seconds", defaultValue = "1")
    long leaderCheckIntervalInSeconds;

    @ConfigProperty(name = "kogito.jobs-service.management.heartbeat.expiration-in-seconds", defaultValue = "10")
    long heartbeatExpirationInSeconds;

    @ConfigProperty(name = "kogito.jobs-service.management.heartbeat.management-id", defaultValue = "kogito-jobs-service-leader")
    String leaderManagementId;

    @Inject
    Instance<MessagingHandler> messagingHandlerInstance;

    @Inject
    Event<MessagingChangeEvent> messagingChangeEventEvent;

    @Inject
    Vertx vertx;

    @Inject
    JobServiceManagementRepository repository;

    private Long checkLeader;

    private Long heartbeat;

    private final AtomicReference<JobServiceManagementInfo> currentInfo = new AtomicReference<>();

    private final AtomicBoolean leader = new AtomicBoolean(false);

    protected WorkerExecutor workerExecutor;

    @PostConstruct
    public void init() {
        LOGGER.info("Initialized with worker executor");
        this.workerExecutor = vertx.createSharedWorkerExecutor("JobServiceInstanceManager");
    }

    @PreDestroy
    public void destroy() {
        this.shutdown();
    }

    public void disable() {
        this.disableCommunication();
        this.shutdown();
    }

    public void shutdown() {
        try {
            LOGGER.info("Shutting down leader instance check");
            cancelCheckLeader();
            cancelHeartbeat();
            LOGGER.info("releasing leader");
            release(currentInfo.get());
        } catch (Exception ex) {
            LOGGER.error("Shutdown error", ex);
        }
    }

    protected void release(JobServiceManagementInfo info) {
        leader.set(false);
        try {
            repository.release(info);
            LOGGER.info("Leader instance released");
        } catch (Throwable e) {
            LOGGER.error("Error releasing leader", e);
        }
    }

    void startup(@Observes StartupEvent startupEvent) {
        currentInfo.set(buildAndSetInstanceInfo());
        LOGGER.info("Starting JobServiceInstanceManager {}", currentInfo.get());
        //initial leader check
        tryBecomeLeader(currentInfo.get());
    }

    private void registerCheckLeader() {
        if (checkLeader != null) {
            return;
        }
        LOGGER.info("Registering job service check leader every {} seconds", leaderCheckIntervalInSeconds);
        //background task for leader check, it will be started after the first tryBecomeLeader() execution
        checkLeader = vertx.setPeriodic(TimeUnit.SECONDS.toMillis(leaderCheckIntervalInSeconds), this::internalRegisterCheckLeader);
    }

    private void internalRegisterCheckLeader(Long id) {
        Callable<JobServiceManagementInfo> transacted = () -> {
            LOGGER.debug("trying to become leader now for {}", currentInfo.get());
            return tryBecomeLeader(currentInfo.get());
        };
        if (workerExecutor == null) {
            return;
        }
        workerExecutor.executeBlocking(transacted).toCompletionStage();
    }

    private void cancelCheckLeader() {
        if (checkLeader == null) {
            return;
        }
        LOGGER.info("Cancelling job service check leader");
        vertx.cancelTimer(checkLeader);
        checkLeader = null;
    }

    private void registerHeartbeat() {
        if (heartbeat != null) {
            return;
        }
        LOGGER.info("Registering job service heartbeat every {} seconds", heardBeatIntervalInSeconds);
        heartbeat = vertx.setPeriodic(TimeUnit.SECONDS.toMillis(heardBeatIntervalInSeconds), this::internalregisterHeartbeat);
    }

    private void internalregisterHeartbeat(Long id) {
        Callable<JobServiceManagementInfo> transacted = () -> {
            LOGGER.debug("executing heartbeat {}", currentInfo.get());
            return heartbeat(currentInfo.get());
        };
        if (workerExecutor == null) {
            return;
        }
        workerExecutor.executeBlocking(transacted).toCompletionStage();
    }

    private void cancelHeartbeat() {
        if (heartbeat == null) {
            return;
        }
        LOGGER.info("Cancelling job service heartbeat");
        vertx.cancelTimer(heartbeat);
        heartbeat = null;
    }

    private void disableCommunication() {
        //disable consuming events
        messagingHandlerInstance.stream().forEach(MessagingHandler::pause);
        //disable producing events
        messagingChangeEventEvent.fire(new MessagingChangeEvent(false));

        LOGGER.warn("Disabled communication not leader instance");
    }

    private void enableCommunication() {
        //enable consuming events
        messagingHandlerInstance.stream().forEach(MessagingHandler::resume);
        //enable producing events
        messagingChangeEventEvent.fire(new MessagingChangeEvent(true));

        LOGGER.info("Enabled communication for leader instance");
    }

    protected boolean isLeader() {
        return leader.get();
    }

    protected JobServiceManagementInfo tryBecomeLeader(JobServiceManagementInfo info) {
        LOGGER.debug("Try to become Leader");
        return repository.getAndUpdate(info.getId(), updatedJobServiceManagementInfo -> {
            OffsetDateTime currentTime = DateUtil.now().toOffsetDateTime();
            if (Objects.isNull(updatedJobServiceManagementInfo) ||
                    Objects.isNull(updatedJobServiceManagementInfo.getToken()) ||
                    Objects.equals(updatedJobServiceManagementInfo.getToken(), info.getToken()) ||
                    Objects.isNull(updatedJobServiceManagementInfo.getLastHeartbeat()) ||
                    updatedJobServiceManagementInfo.getLastHeartbeat().isBefore(currentTime.minusSeconds(heartbeatExpirationInSeconds))) {
                //old instance is not active
                info.setLastHeartbeat(currentTime);
                LOGGER.info("SET Leader {}", info);
                leader.set(true);
                enableCommunication();
                registerHeartbeat();
                cancelCheckLeader();
                return info;
            } else {
                if (isLeader()) {
                    LOGGER.info("Not Leader");
                    leader.set(false);
                    disableCommunication();
                }
                //stop heartbeats if running
                cancelHeartbeat();
                registerCheckLeader();
            }
            return null;
        });
    }

    protected JobServiceManagementInfo heartbeat(JobServiceManagementInfo info) {
        if (isLeader()) {
            return repository.heartbeat(info);
        }
        return null;
    }

    private JobServiceManagementInfo buildAndSetInstanceInfo() {
        return new JobServiceManagementInfo(leaderManagementId, generateToken(), DateUtil.now().toOffsetDateTime());
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    protected JobServiceManagementInfo getCurrentInfo() {
        return currentInfo.get();
    }

    protected Long getCheckLeader() {
        return checkLeader;
    }

    protected Long getHeartbeat() {
        return heartbeat;
    }
}
