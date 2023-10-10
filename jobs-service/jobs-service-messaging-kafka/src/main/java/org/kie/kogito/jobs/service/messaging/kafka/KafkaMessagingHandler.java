package org.kie.kogito.jobs.service.messaging.kafka;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.spi.Connector;
import org.kie.kogito.jobs.service.messaging.MessagingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.smallrye.reactive.messaging.kafka.KafkaConnector;

@ApplicationScoped
public class KafkaMessagingHandler implements MessagingHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaMessagingHandler.class);

    @Inject
    @Connector(value = "smallrye-kafka")
    KafkaConnector kafkaConnector;

    @Override
    public void pause() {
        kafkaConnector.getConsumerChannels().forEach(c -> {
            LOGGER.debug("pausing kafka channel: {}", c);
            kafkaConnector.getConsumer(c).pause();
        });
    }

    @Override
    public void resume() {
        kafkaConnector.getConsumerChannels().forEach(c -> {
            LOGGER.debug("resuming kafka channel: {}", c);
            kafkaConnector.getConsumer(c).resume();
        });
    }
}
