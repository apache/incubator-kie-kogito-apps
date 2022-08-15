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
package org.kie.kogito.jobs.service.runtime;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import io.quarkus.runtime.ShutdownEvent;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.TimeoutStream;
import io.vertx.mutiny.core.Vertx;
import org.eclipse.microprofile.reactive.messaging.spi.Connector;
import org.kie.kogito.jobs.service.model.JobServiceManagementInfo;
import org.kie.kogito.jobs.service.repository.JobServiceManagementRepository;
import org.kie.kogito.jobs.service.stream.KafkaJobStreams;

import io.quarkus.runtime.StartupEvent;
import io.smallrye.reactive.messaging.kafka.KafkaConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class JobServiceInstanceManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobServiceInstanceManager.class);

    @Inject
    @Connector(value = "smallrye-kafka")
    KafkaConnector kafkaConnector;

    @Inject
    Event<MessagingChangeEvent> disableMessagingEventEvent;

    @Inject
    Vertx vertx;

    @Inject
    JobServiceManagementRepository repository;

    private JobServiceManagementInfo currenInfo;

    private Optional<TimeoutStream> checkMaster = Optional.empty();

    private AtomicBoolean mater = new AtomicBoolean(false);

    void startup(@Observes StartupEvent startupEvent) {
        checkMaster = Optional.of(vertx.periodicStream(TimeUnit.SECONDS.toMillis(5)))
                .map(s -> s.handler(id -> checkMasterInstance()));
        currenInfo = buildInfo();

        if (isMaster()) {
            return;
        }
        //kafkaConnector.terminate(new Object());

        //disable consuming events
        kafkaConnector.getConsumerChannels().stream().forEach(c -> kafkaConnector.getConsumer(c).pause());
        //kafkaConnector.getConsumerChannels().stream().forEach(c -> kafkaConnector.getConsumer(c).unwrap().close());

        //disable producing events
        disableMessagingEventEvent.fire(new MessagingChangeEvent(false));
        //kafkaConnector.getProducerChannels().stream().forEach(c -> kafkaConnector.getProducer(c).unwrap().close());
    }

    void onShutdown(@Observes ShutdownEvent shutdownEvent){
        LOGGER.info("Shutting down master instance check");
        checkMaster.ifPresent(TimeoutStream::cancel);
    }

    public boolean isMaster() {
        return mater.get();
    }

    public void checkMasterInstance(){
        LOGGER.info("Checking Master");

        tryBecomeMaster(currenInfo);
    }

    Uni<JobServiceManagementInfo> tryBecomeMaster(JobServiceManagementInfo info){
        //transaction

        //select lastKeepAlive > 5s || null || token == null
        //if match = last instance died
        //update with info + keepalive
        //set master true

        //if not set master false
        return Uni.createFrom().nullItem();
    }

    Uni<JobServiceManagementInfo> release(JobServiceManagementInfo info){
        //set token to null
        //set keepalive to null

        //set master false
        return Uni.createFrom().nullItem();
    }

    Uni<JobServiceManagementInfo> keepAlive(JobServiceManagementInfo info){
        //transaction

        //if master true
        //compare token with db if matches, update keepalive time
        //if not match, set master to false
        //stop keepalive, start become master

        //if fails (locked) try again
        return Uni.createFrom().nullItem();
    }

    private JobServiceManagementInfo buildInfo(){



    }
}
