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

import java.time.OffsetDateTime;
import java.util.Objects;
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

    @ConfigProperty(name = "kogito.jobs-service.management.heartbeat.expiration-in-seconds", defaultValue = "10")
    long heartbeatExpirationInSeconds;

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

    private TimeoutStream checkMaster;

    private TimeoutStream heartbeat;

    private final AtomicReference<JobServiceManagementInfo> currentInfo = new AtomicReference<>();

    private final AtomicBoolean master = new AtomicBoolean(false);

    void startup(@Observes StartupEvent startupEvent) {
        buildAndSetInstanceInfo();

        //background task for master check, it will be started after the first tryBecomeMaster() execution
        checkMaster = vertx.periodicStream(TimeUnit.SECONDS.toMillis(masterCheckIntervalInSeconds))
                .handler(id -> tryBecomeMaster(currentInfo.get(), checkMaster, heartbeat)
                        .subscribe().with(i -> LOGGER.info("Checking Master"),
                                ex -> LOGGER.error("Error checking Master", ex)))
                .pause();

        //background task for heartbeat will be started when become master
        heartbeat = vertx.periodicStream(TimeUnit.SECONDS.toMillis(heardBeatIntervalInSeconds))
                .handler(t -> heartbeat(currentInfo.get())
                        .subscribe().with(i -> LOGGER.info("Heartbeat {}", currentInfo.get()),
                                ex -> LOGGER.error("Error on heartbeat {}", currentInfo.get(), ex)))
                .pause();

        //initial master check
        tryBecomeMaster(currentInfo.get(), checkMaster, heartbeat)
                .subscribe().with(i -> LOGGER.info("Initial check master execution"),
                        ex -> LOGGER.error("Error on initial check master", ex));
    }

    private void disableCommunication() {
        //disable consuming events
        kafkaConnector.getConsumerChannels().stream().forEach(c -> kafkaConnector.getConsumer(c).pause());

        //disable producing events
        messagingChangeEventEvent.fire(new MessagingChangeEvent(false));

        LOGGER.warn("Disabled communication not master instance");
    }

    private void enableCommunication() {
        //enable consuming events
        kafkaConnector.getConsumerChannels().stream().forEach(c -> kafkaConnector.getConsumer(c).resume());

        //enable producing events
        messagingChangeEventEvent.fire(new MessagingChangeEvent(true));

        LOGGER.warn("Enabled communication for master instance");
    }

    void onShutdown(@Observes ShutdownEvent shutdownEvent) {
        release(currentInfo.get())
                .onItem().invoke(i -> checkMaster.cancel())
                .onItem().invoke(i -> heartbeat.cancel())
                .subscribe().with(i -> LOGGER.info("Shutting down master instance check"),
                        ex -> LOGGER.error("Shutdown error", ex));
    }

    protected boolean isMaster() {
        return master.get();
    }

    protected Uni<JobServiceManagementInfo> tryBecomeMaster(JobServiceManagementInfo info, TimeoutStream checkMaster, TimeoutStream heartbeat) {
        LOGGER.debug("Try to become Master");
        return repository.getAndUpdate(info.getId(), c -> {
            final OffsetDateTime currentTime = DateUtil.now().toOffsetDateTime();
            if (Objects.isNull(c) || Objects.isNull(c.getToken()) || Objects.equals(c.getToken(), info.getToken()) || Objects.isNull(c.getLastHeartbeat())
                    || c.getLastHeartbeat().isBefore(currentTime.minusSeconds(heartbeatExpirationInSeconds))) {
                //old instance is not active
                info.setLastHeartbeat(currentTime);
                LOGGER.info("SET Master {}", info);
                master.set(true);
                enableCommunication();
                heartbeat.resume();
                checkMaster.pause();
                return info;
            } else {
                if (isMaster()) {
                    LOGGER.info("Not Master");
                    master.set(false);
                    disableCommunication();
                }
                //stop heartbeats if running
                heartbeat.pause();
                //guarantee the stream is running if not master
                checkMaster.resume();
            }
            return null;
        });
    }

    protected Uni<Void> release(JobServiceManagementInfo info) {
        return repository.set(new JobServiceManagementInfo(info.getId(), null, null))
                .onItem().invoke(i -> master.set(false))
                .onItem().invoke(i -> LOGGER.info("Master instance released"))
                .onFailure().invoke(ex -> LOGGER.error("Error releasing master"))
                .replaceWithVoid();
    }

    protected Uni<JobServiceManagementInfo> heartbeat(JobServiceManagementInfo info) {
        LOGGER.debug("Heartbeat Master");
        if (isMaster()) {
            return repository.heartbeat(info);
        }
        return Uni.createFrom().nullItem();
    }

    private void buildAndSetInstanceInfo() {
        currentInfo.set(new JobServiceManagementInfo(masterManagementId, generateToken(), DateUtil.now().toOffsetDateTime()));
        LOGGER.info("Current Job Service Instance {}", currentInfo.get());
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    protected JobServiceManagementInfo getCurrentInfo() {
        return currentInfo.get();
    }

    protected TimeoutStream getCheckMaster() {
        return checkMaster;
    }

    protected TimeoutStream getHeartbeat() {
        return heartbeat;
    }
}
