/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
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

package org.kie.kogito.index.test;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import io.smallrye.reactive.messaging.connectors.InMemoryConnector;

import static org.kie.kogito.index.messaging.ReactiveMessagingEventConsumer.KOGITO_JOBS_EVENTS;
import static org.kie.kogito.index.messaging.ReactiveMessagingEventConsumer.KOGITO_PROCESSINSTANCES_EVENTS;
import static org.kie.kogito.index.messaging.ReactiveMessagingEventConsumer.KOGITO_USERTASKINSTANCES_EVENTS;

public class InMemoryMessagingTestResource implements QuarkusTestResourceLifecycleManager {

    @Override
    public Map<String, String> start() {
        Map<String, String> env = new HashMap<>();
        Stream.of(KOGITO_PROCESSINSTANCES_EVENTS, KOGITO_USERTASKINSTANCES_EVENTS, KOGITO_JOBS_EVENTS)
                .forEach(s -> env.putAll(switchIncomingChannel(s)));
        return env;
    }

    @Override
    public void stop() {
        InMemoryConnector.clear();
    }

    private Map<String, String> switchIncomingChannel(String channel) {
        return InMemoryConnector.switchIncomingChannelsToInMemory(channel);
    }

}
