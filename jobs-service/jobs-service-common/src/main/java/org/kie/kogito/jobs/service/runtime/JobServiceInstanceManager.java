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

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.spi.Connector;
import org.kie.kogito.jobs.service.stream.KafkaJobStreams;

import io.quarkus.runtime.StartupEvent;
import io.smallrye.reactive.messaging.kafka.KafkaConnector;

@ApplicationScoped
public class JobServiceInstanceManager {

    @Inject
    @Connector(value = "smallrye-kafka")
    KafkaConnector kafkaConnector;

    @Inject
    KafkaJobStreams streams;

    @Inject
    Event<RuntimeMessagingChangeEvent> disableMessagingEventEvent;

    void startup(@Observes StartupEvent startupEvent) {
        //kafkaConnector.terminate(new Object());

        //disable consuming events
        kafkaConnector.getConsumerChannels().stream().forEach(c -> kafkaConnector.getConsumer(c).pause());
        //kafkaConnector.getConsumerChannels().stream().forEach(c -> kafkaConnector.getConsumer(c).unwrap().close());

        //disable producing events
        disableMessagingEventEvent.fire(new RuntimeMessagingChangeEvent(false));
        //kafkaConnector.getProducerChannels().stream().forEach(c -> kafkaConnector.getProducer(c).unwrap().close());
    }

    public boolean isMaster() {
        return false;
    }
}
