package org.kie.kogito.trusty.service.messaging;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import io.vertx.core.Vertx;
import io.vertx.kafka.client.producer.KafkaProducer;
import io.vertx.kafka.client.producer.KafkaProducerRecord;
import org.kie.kogito.trusty.service.TrustyKafkaTestResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaUtils {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaUtils.class);

    public static CompletableFuture<Void> sendToKafka(String payload, KafkaProducer<String, String> producer) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        producer.write(KafkaProducerRecord.create("trusty-service-test", payload), event -> {
            if (event.succeeded()) {
                future.complete(null);
            } else {
                future.completeExceptionally(event.cause());
            }
        });
        return future;
    }

    public static void sendToKafkaAndWaitForCompletion(String payload, KafkaProducer<String, String> producer) throws Exception {
        sendToKafka(payload, producer)
                .thenRunAsync(() -> LOG.info("Sent payload to Kafka (length: {})", payload.length()), CompletableFuture.delayedExecutor(2L, TimeUnit.SECONDS))
                .get(15L, TimeUnit.SECONDS);
    }

    public static KafkaProducer<String, String>  generateProducer(){
        return KafkaProducer.create(Vertx.vertx(), Map.of(
                "bootstrap.servers", System.getProperty(TrustyKafkaTestResource.KAFKA_BOOTSTRAP_SERVERS, "localhost:9092"),
                "key.serializer", "org.apache.kafka.common.serialization.StringSerializer",
                "value.serializer", "org.apache.kafka.common.serialization.StringSerializer",
                "acks", "all"
        ));
    }
}
