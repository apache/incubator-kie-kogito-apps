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
package org.kie.kogito.jobs.service.management;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.messaging.spi.Connector;
import org.kie.kogito.jobs.service.model.JobServiceManagementInfo;
import org.kie.kogito.jobs.service.repository.JobServiceManagementRepository;
import org.kie.kogito.jobs.service.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.kafka.KafkaConnector;
import io.vertx.mutiny.core.TimeoutStream;
import io.vertx.mutiny.core.Vertx;

@ApplicationScoped
public class JobServiceInstanceManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobServiceInstanceManager.class);

    @ConfigProperty(name = "kogito.jobs-service.management.heartbeat.interval-in-seconds", defaultValue = "1")
    long heardBeatIntervalInSeconds;

    @ConfigProperty(name = "kogito.jobs-service.management.master-check.interval-in-seconds", defaultValue = "1")
    long masterCheckIntervalInSeconds;

    @ConfigProperty(name = "kogito.jobs-service.management.heartbeat.management-id", defaultValue = "kogito-jobs-service-master")
    String masterManagementId;

    @Inject
    @Connector(value = "smallrye-kafka")
    KafkaConnector kafkaConnector;

    @Inject
    Event<MessagingChangeEvent> messagingChangeEventEvent;

    @Inject
    Vertx vertx;

    @Inject
    JobServiceManagementRepository repository;

    private Optional<TimeoutStream> checkMaster = Optional.empty();

    private Optional<TimeoutStream> heartbeat = Optional.empty();

    private final AtomicReference<JobServiceManagementInfo> currentInfo = new AtomicReference<>();

    private final AtomicBoolean master = new AtomicBoolean(false);

    Uni<JobServiceManagementInfo> startup(@Observes StartupEvent startupEvent) {
        buildInfo();
        //started
        checkMaster = Optional.of(vertx.periodicStream(TimeUnit.SECONDS.toMillis(masterCheckIntervalInSeconds))
                .handler(id -> tryBecomeMaster(currentInfo.get())
                        .subscribe().with(i -> LOGGER.info("Checking Master"),
                                ex -> LOGGER.error("Error checking Master", ex))));
        //paused
        heartbeat = Optional.of(vertx.periodicStream(TimeUnit.SECONDS.toMillis(heardBeatIntervalInSeconds))
                .handler(t -> heartbeat(currentInfo.get())
                        .subscribe().with(i -> LOGGER.info("Heartbeat"),
                                ex -> LOGGER.error("Error on heartbeat", ex)))
                .pause());
        //initial check
        return tryBecomeMaster(currentInfo.get());
    }

    private void disableCommunication() {
        //disable consuming events
        kafkaConnector.getConsumerChannels().stream().forEach(c -> kafkaConnector.getConsumer(c).pause());

        //disable producing events
        messagingChangeEventEvent.fire(new MessagingChangeEvent(false));

        LOGGER.warn("Disabled communication not master instance");
    }

    private void enableCommunication() {
        //disable consuming events
        kafkaConnector.getConsumerChannels().stream().forEach(c -> kafkaConnector.getConsumer(c).resume());

        //disable producing events
        messagingChangeEventEvent.fire(new MessagingChangeEvent(true));

        LOGGER.warn("Enabled communication for master instance");
    }

    Uni<JobServiceManagementInfo> onShutdown(@Observes ShutdownEvent shutdownEvent) {
        LOGGER.info("Shutting down master instance check");
        checkMaster.ifPresent(TimeoutStream::cancel);
        heartbeat.ifPresent(TimeoutStream::cancel);
        return release(currentInfo.get());
    }

    protected boolean isMaster() {
        return master.get();
    }

    Uni<JobServiceManagementInfo> tryBecomeMaster(JobServiceManagementInfo info) {
        LOGGER.debug("Try to become Master");
        return repository.getAndUpdate(currentInfo.get().getId(), c -> {
            if (Objects.isNull(c) || Objects.isNull(c.getToken()) || Objects.equals(c.getToken(), info.getToken()) || Objects.isNull(c.getLastHeartbeat())
                    || c.getLastHeartbeat().isBefore(DateUtil.now().minusSeconds(10))) {
                //old instance is not active
                info.setLastHeartbeat(DateUtil.now());
                LOGGER.info("SET Master {}", info);
                master.set(true);
                enableCommunication();
                this.heartbeat.ifPresent(s -> s.resume());
                this.checkMaster.ifPresent(s -> s.pause());
                return info;
            } else if (isMaster()) {
                LOGGER.info("Not Master");
                master.set(false);
                disableCommunication();
                this.checkMaster.ifPresent(s -> s.resume());
                this.heartbeat.ifPresent(s -> s.pause());
            }
            return null;
        });
    }

    Uni<JobServiceManagementInfo> release(JobServiceManagementInfo info) {
        LOGGER.info("Release Master");
        return repository.set(new JobServiceManagementInfo(null, null, null))
                .onItem().invoke(i -> master.set(false));
    }

    Uni<JobServiceManagementInfo> heartbeat(JobServiceManagementInfo info) {
        LOGGER.debug("Heartbeat Master");
        if (isMaster()) {
            return repository.heartbeat(info);
        }
        return Uni.createFrom().nullItem();
    }

    private JobServiceManagementInfo buildInfo() {
        currentInfo.set(new JobServiceManagementInfo(masterManagementId, generateToken(), DateUtil.now()));
        LOGGER.info("Current Job Service Instance {}", currentInfo.get());
        return currentInfo.get();
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }
}
