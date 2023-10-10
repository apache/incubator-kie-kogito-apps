package org.kie.kogito.index.service.test;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import io.smallrye.reactive.messaging.providers.connectors.InMemoryConnector;

import static org.kie.kogito.index.service.messaging.ReactiveMessagingEventConsumer.KOGITO_JOBS_EVENTS;
import static org.kie.kogito.index.service.messaging.ReactiveMessagingEventConsumer.KOGITO_PROCESSINSTANCES_EVENTS;
import static org.kie.kogito.index.service.messaging.ReactiveMessagingEventConsumer.KOGITO_USERTASKINSTANCES_EVENTS;

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
