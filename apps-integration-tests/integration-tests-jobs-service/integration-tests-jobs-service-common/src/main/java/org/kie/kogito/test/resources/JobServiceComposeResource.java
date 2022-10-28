/*
 * Copyright 2022 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.kogito.test.resources;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.kie.kogito.testcontainers.KogitoGenericContainer;
import org.kie.kogito.testcontainers.KogitoKafkaContainer;
import org.kie.kogito.testcontainers.KogitoPostgreSqlContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.Wait;

public class JobServiceComposeResource extends AbstractJobServiceResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobServiceComposeResource.class);
    private final List<GenericContainer<?>> dependencyContainers;
    private final Map<String, KogitoGenericContainer> serviceContainers;

    public JobServiceComposeResource() {
        this.dependencyContainers = new ArrayList<>();
        this.serviceContainers = new HashMap<>();
        withServiceContainer("job-service-main", jobService);
    }

    public JobServiceComposeResource withServiceContainer(String id, KogitoGenericContainer<?> container) {
        serviceContainers.put(id, container);
        return this;
    }

    public JobServiceComposeResource withDependencyContainer(GenericContainer<?> container) {
        dependencyContainers.add(container);
        return this;
    }

    public <T> List<T> getServiceContainers(Class<T> type) {
        return serviceContainers.values().stream().filter(type::isInstance).map(type::cast).collect(Collectors.toList());
    }

    private String hostName(GenericContainer<?> container) {
        return container.getContainerInfo().getConfig().getHostName();
    }

    @Override
    public void start() {
        LOGGER.info("Start JobService with {} test resources", dependencyContainers.stream().map(GenericContainer::getImage).collect(Collectors.toList()));
        properties.clear();

        final Network network = Network.newNetwork();
        dependencyContainers.stream()
                .map(c -> c.withNetwork(network))
                .map(c -> c.waitingFor(Wait.forListeningPort()))
                .forEach(GenericContainer::start);

        configurePostgreSQL();
        configureKafka();
        startServices(network);
    }

    protected void startServices(Network network) {
        serviceContainers.values()
                .forEach(service -> {
                    service.withNetwork(network);
                    service.start();
                    LOGGER.info("JobService test resource started");
                });
    }

    protected void configureKafka() {
        dependencyContainers.stream()
                .filter(KogitoKafkaContainer.class::isInstance)
                .map(KogitoKafkaContainer.class::cast)
                .findFirst()
                .ifPresent(kafka -> {
                    LOGGER.info("Configure Kafka");
                    // external access url
                    String kafkaURL = kafka.getBootstrapServers();
                    LOGGER.info("kafkaURL: {}", kafkaURL);
                    properties.put("kafka.bootstrap.servers", kafkaURL);
                    properties.put("spring.kafka.bootstrap-servers", kafkaURL);

                    //internal access
                    final String kafkaInternalUrl = hostName(kafka) + ":29092";
                    LOGGER.info("kafkaInternalURL: {}", kafkaInternalUrl);
                    serviceContainers.values().forEach(service -> service.addEnv("KAFKA_BOOTSTRAP_SERVERS", kafkaInternalUrl));
                });
    }

    protected void configurePostgreSQL() {
        dependencyContainers.stream()
                .filter(KogitoPostgreSqlContainer.class::isInstance)
                .map(KogitoPostgreSqlContainer.class::cast)
                .findFirst()
                .ifPresent(postgreSql -> {
                    LOGGER.info("Configure PostgreSQL");
                    final String connectionTemplate = "postgresql://{0}:{1}/{2}";
                    final String jdbcConnectionTemplate = "jdbc:postgresql://{0}:{1}/{2}";
                    final String server = hostName(postgreSql);
                    final String port = "5432";
                    final String username = postgreSql.getUsername();
                    final String password = postgreSql.getPassword();
                    final String database = postgreSql.getDatabaseName();
                    final String reactiveUrl = MessageFormat.format(connectionTemplate, server, port, database);
                    final String jdbcUrl = MessageFormat.format(jdbcConnectionTemplate, server, port, database);
                    serviceContainers.values()
                            .forEach(service -> {
                                service.addEnv("QUARKUS_DATASOURCE_JDBC_URL", jdbcUrl);
                                service.addEnv("QUARKUS_DATASOURCE_REACTIVE_URL", reactiveUrl);
                                service.addEnv("QUARKUS_DATASOURCE_USERNAME", username);
                                service.addEnv("QUARKUS_DATASOURCE_PASSWORD", password);
                                service.addEnv("QUARKUS_DATASOURCE_DB-KIND", "postgresql");
                            });
                });
    }

    @Override
    public void stop() {
        LOGGER.info("Stopping test resource");
        serviceContainers.values().forEach(GenericContainer::stop);
        dependencyContainers.forEach(GenericContainer::stop);
        LOGGER.info("Test resource stopped");
    }
}
