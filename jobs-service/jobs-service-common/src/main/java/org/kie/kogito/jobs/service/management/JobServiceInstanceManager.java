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

import java.time.ZonedDateTime;
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

    @Inject
    @Connector(value = "smallrye-kafka")
    KafkaConnector kafkaConnector;

    @Inject
    Event<MessagingChangeEvent> messagingChangeEventEvent;

    @Inject
    Vertx vertx;

    @Inject
    JobServiceManagementRepository repository;

    private AtomicReference<JobServiceManagementInfo> currentInfo = new AtomicReference<>();

    private Optional<TimeoutStream> checkMaster = Optional.empty();
    private Optional<TimeoutStream> heartbeat = Optional.empty();

    private AtomicBoolean master = new AtomicBoolean(false);

    void startup(@Observes StartupEvent startupEvent) {
        buildInfo();
        //started
        checkMaster = Optional.of(vertx.periodicStream(TimeUnit.SECONDS.toMillis(5))
                .handler(id -> tryBecomeMaster(currentInfo.get())
                        .subscribe().with(i -> LOGGER.info("Checking Master"),
                                ex -> LOGGER.error("Error checking Master", ex))));
        //paused
        heartbeat = Optional.of(vertx.periodicStream(TimeUnit.SECONDS.toMillis(3))
                .handler(t -> heartbeat(currentInfo.get())
                        .subscribe().with(i -> LOGGER.info("Heartbeat"),
                                ex -> LOGGER.error("Error on heartbeat", ex)))
                .pause());
        if (isMaster()) {
            return;
        }
        disableCommunication();
    }

    private void disableCommunication() {
        //kafkaConnector.terminate(new Object());

        //disable consuming events
        kafkaConnector.getConsumerChannels().stream().forEach(c -> kafkaConnector.getConsumer(c).pause());
        //kafkaConnector.getConsumerChannels().stream().forEach(c -> kafkaConnector.getConsumer(c).unwrap().close());

        //disable producing events
        messagingChangeEventEvent.fire(new MessagingChangeEvent(false));
        //kafkaConnector.getProducerChannels().stream().forEach(c -> kafkaConnector.getProducer(c).unwrap().close());

        LOGGER.info("Disabled communication not master instance");
    }

    private void enableCommunication() {
        //disable consuming events
        kafkaConnector.getConsumerChannels().stream().forEach(c -> kafkaConnector.getConsumer(c).resume());
        //kafkaConnector.getConsumerChannels().stream().forEach(c -> kafkaConnector.getConsumer(c).unwrap().close());

        //disable producing events
        messagingChangeEventEvent.fire(new MessagingChangeEvent(true));

        LOGGER.info("Enabled communication for master instance");
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
        LOGGER.info("Try to become Master");

        //transaction

        //select lastKeepAlive > 5s || null || token == null
        //if match = last instance died
        //update with info + keepalive
        //set master true

        //if not set master false

        Uni<JobServiceManagementInfo> res = repository.get((current) ->
        //expired
        current.onItem().invoke(c -> {
            if (Objects.nonNull(c) && !Objects.equals(c.getToken(), info.getToken()) || c.getLastKeepAlive().isBefore(ZonedDateTime.now().minusSeconds(1))) {
                //old instance is not active
                repository.set(info);

                master.set(true);
                LOGGER.info("Master Ok {}", info);
                enableCommunication();
                this.heartbeat.ifPresent(s -> s.resume());
                this.checkMaster.ifPresent(s -> s.pause());
            } else {
                LOGGER.info("Not Master");
                master.set(false);
                disableCommunication();
                this.checkMaster.ifPresent(s -> s.resume());
                this.heartbeat.ifPresent(s -> s.pause());
            }
        }));

        return res;
    }

    //    Uni<JobServiceManagementInfo> tryBecomeMaster(JobServiceManagementInfo info) {
    //        LOGGER.info("Try to become Master");
    //
    //        //transaction
    //
    //        //select lastKeepAlive > 5s || null || token == null
    //        //if match = last instance died
    //        //update with info + keepalive
    //        //set master true
    //
    //        //if not set master false
    //
    //        JobServiceManagementInfo current = this.currentInfo.get();
    //
    //        //expired
    //        if (!Objects.equals(current.getToken(), info.getToken()) || current.getLastKeepAlive().isBefore(ZonedDateTime.now().minusSeconds(6))) {
    //            //old instance is not active
    //            currentInfo.set(info);
    //            master.set(true);
    //            LOGGER.info("Master Ok {}", currentInfo.get());
    //            enableCommunication();
    //            this.heartbeat.ifPresent(s -> s.resume());
    //            this.checkMaster.ifPresent(s -> s.pause());
    //        } else {
    //            LOGGER.info("Not Master");
    //            master.set(false);
    //            disableCommunication();
    //            this.checkMaster.ifPresent(s -> s.resume());
    //            this.heartbeat.ifPresent(s -> s.pause());
    //        }
    //        return Uni.createFrom().item(currentInfo.get());
    //    }

    Uni<JobServiceManagementInfo> release(JobServiceManagementInfo info) {
        LOGGER.info("Release Master");

        //set token to null
        //set keepalive to null

        //set master false

        return repository.set(new JobServiceManagementInfo(null, null))
                .onItem().invoke(i -> master.set(false));
    }

    Uni<JobServiceManagementInfo> heartbeat(JobServiceManagementInfo info) {
        LOGGER.info("Heartbeat Master");

        //transaction

        //if master true
        //compare token with db if matches, update keepalive time
        //if not match, set master to false
        //stop keepalive, start become master

        //if fails (locked) try again

        if (isMaster()) {
            return repository.heartbeat(info);
        }
        return Uni.createFrom().nullItem();
    }

    private JobServiceManagementInfo buildInfo() {
        currentInfo.set(new JobServiceManagementInfo(DateUtil.now(), UUID.randomUUID()));
        return currentInfo.get();
    }
}
