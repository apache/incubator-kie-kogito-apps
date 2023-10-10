package org.kie.kogito.jobs.service.messaging.kafka.stream;

import java.time.Duration;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.kie.kogito.testcontainers.quarkus.KafkaQuarkusTestResource;

import io.quarkus.runtime.StartupEvent;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.common.annotation.Identifier;
import io.vertx.mutiny.core.Vertx;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@QuarkusTest
@QuarkusTestResource(value = KafkaQuarkusTestResource.class, restrictToAnnotatedClass = true)
class KafkaConfigurationTest {

    private KafkaConfiguration tested;

    private static final String TOPIC = UUID.randomUUID().toString();

    @Inject
    Vertx vertx;

    @Inject
    @Identifier("default-kafka-broker")
    Instance<Map<String, Object>> defaultKafkaConfiguration;

    @Test
    void topicConfiguration() {
        tested = new KafkaConfiguration(defaultKafkaConfiguration, vertx, Optional.of(Boolean.TRUE), TOPIC);
        assertThat(tested.getAdminClient()).isNull();
        tested.topicConfiguration(new StartupEvent());
        assertThat(tested.getAdminClient()).isNotNull();
        await().atMost(Duration.ofSeconds(4)).untilAsserted(
                () -> assertThat(tested.getAdminClient().listTopicsAndAwait()).contains(TOPIC));
        tested.getAdminClient().deleteTopicsAndAwait(Arrays.asList(TOPIC));
    }

    @Test
    void topicConfigurationDisabledEvents() {
        tested = new KafkaConfiguration(defaultKafkaConfiguration, vertx, Optional.of(Boolean.FALSE), TOPIC);
        assertThat(tested.getAdminClient()).isNull();
        tested.topicConfiguration(new StartupEvent());
        assertThat(tested.getAdminClient()).isNull();
    }
}
