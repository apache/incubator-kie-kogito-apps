package org.kie.kogito.testcontainers;

import org.testcontainers.containers.wait.strategy.Wait;

public class ExplainabilityServiceMessagingContainer extends KogitoGenericContainer<ExplainabilityServiceMessagingContainer> {

    public ExplainabilityServiceMessagingContainer(String kafkaBootstrapServers, int numberOfSamples) {
        super("explainability-service-messaging");
        addEnv("KAFKA_BOOTSTRAP_SERVERS", kafkaBootstrapServers);
        addEnv("TRUSTY_EXPLAINABILITY_NUMBEROFSAMPLES", String.valueOf(numberOfSamples));
        addExposedPort(8080);
        waitingFor(Wait.forListeningPort());
    }
}
