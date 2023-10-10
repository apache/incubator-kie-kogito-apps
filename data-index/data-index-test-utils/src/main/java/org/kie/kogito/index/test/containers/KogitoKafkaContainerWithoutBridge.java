package org.kie.kogito.index.test.containers;

import org.kie.kogito.testcontainers.KogitoKafkaContainer;

import com.github.dockerjava.api.command.InspectContainerResponse;

public class KogitoKafkaContainerWithoutBridge extends KogitoKafkaContainer {

    @Override
    protected void containerIsStarting(InspectContainerResponse containerInfo, boolean reused) {
        containerInfo.getNetworkSettings().getNetworks().remove("bridge");
        super.containerIsStarting(containerInfo, reused);
    }
}
