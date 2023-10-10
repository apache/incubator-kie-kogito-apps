package org.kie.kogito.testcontainers;

import org.testcontainers.containers.wait.strategy.Wait;

public class SpringBootKogitoServiceContainer extends KogitoServiceContainer {

    public SpringBootKogitoServiceContainer(String kafkaBootstrapServer, String kogitoServiceUrl) {
        super(kogitoServiceUrl);
        addEnv("KOGITO_ADDON_TRACING_DECISION_KAFKA_BOOTSTRAPADDRESS", kafkaBootstrapServer);
        waitingFor(Wait.forLogMessage(".*Started KogitoApplication in.*", 1));
    }
}
