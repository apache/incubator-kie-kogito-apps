package org.kie.kogito.testcontainers;

import org.testcontainers.containers.wait.strategy.Wait;

public class InfinispanTrustyServiceContainer extends KogitoGenericContainer<InfinispanTrustyServiceContainer> {

    public InfinispanTrustyServiceContainer(String infinispanServerList, String kafkaBootstrapServer,
            boolean explainabilityEnabled) {
        super("trusty-service-infinispan");
        addEnv("INFINISPAN_SERVER_LIST", infinispanServerList);
        addEnv("KAFKA_BOOTSTRAP_SERVERS", kafkaBootstrapServer);
        addEnv("TRUSTY_EXPLAINABILITY_ENABLED", String.valueOf(explainabilityEnabled));
        addExposedPort(8080);
        waitingFor(Wait.forListeningPort());
    }
}
