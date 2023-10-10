package org.kie.kogito.index.test.containers;

import java.io.File;

import org.kie.kogito.test.resources.TestResource;
import org.kie.kogito.testcontainers.KogitoGenericContainer;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.wait.strategy.Wait;

/**
 * This container wraps Data Index Service container
 */
public abstract class AbstractDataIndexContainer extends KogitoGenericContainer<AbstractDataIndexContainer>
        implements TestResource {

    public static final int PORT = 8080;

    public AbstractDataIndexContainer(String containerName) {
        super(containerName);
        addExposedPort(PORT);
        waitingFor(Wait.forListeningPort());
        addEnv("KOGITO_PROTOBUF_FOLDER", "/home/kogito/data/protobufs/");
        withAccessToHost(true);
    }

    public void setKafkaURL(String kafkaURL) {
        addEnv("KAFKA_BOOTSTRAP_SERVERS", kafkaURL);
    }

    public void addProtoFileFolder() {
        String pathStr = "target/classes/META-INF/resources/persistence/protobuf/";
        String absolutePath = new File(pathStr).getAbsolutePath();
        withFileSystemBind(absolutePath, "/home/kogito/data/protobufs/", BindMode.READ_ONLY);
    }

    @Override
    public int getMappedPort() {
        return getMappedPort(PORT);
    }

}
