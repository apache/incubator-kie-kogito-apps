package org.kie.kogito.testcontainers;

import org.testcontainers.containers.wait.strategy.Wait;

public class QuarkusKogitoServiceContainer extends KogitoServiceContainer {

    public QuarkusKogitoServiceContainer(String kafkaBootstrapServer, String kogitoServiceUrl) {
        super(kogitoServiceUrl);
        addEnv("KAFKA_BOOTSTRAP_SERVERS", kafkaBootstrapServer);
        waitingFor(Wait.forListeningPort());
    }
}
