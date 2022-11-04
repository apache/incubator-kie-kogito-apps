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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.kie.kogito.testcontainers.KogitoGenericContainer;
import org.kie.kogito.testcontainers.KogitoKafkaContainer;
import org.kie.kogito.testcontainers.KogitoPostgreSqlContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.Wait;

public class JobServiceComposeResource implements TestResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobServiceComposeResource.class);
    public static final String MAIN_SERVICE_ID = "job-service-main";
    private final Map<String, GenericContainer<?>> sharedDependencyContainers;
    private final Map<String, KogitoGenericContainer<?>> serviceContainers;

    private final Map<String, List<GenericContainer<?>>> dependencyContainers;

    private final KogitoGenericContainer<?> mainContainer;
    private Map<String, String> properties;

    public JobServiceComposeResource(KogitoGenericContainer<?> mainContainer) {
        this.sharedDependencyContainers = new HashMap<>();
        this.dependencyContainers = new HashMap<>();
        this.serviceContainers = new HashMap<>();
        this.properties = new HashMap<>();
        this.mainContainer = mainContainer;
        withServiceContainer(MAIN_SERVICE_ID, mainContainer);
    }

    public JobServiceComposeResource withServiceContainer(String id, KogitoGenericContainer<?> container, GenericContainer<?>... dependency) {
        serviceContainers.put(id, container);
        List<GenericContainer<?>> containers = dependencyContainers.getOrDefault(id, new ArrayList<>());
        containers.addAll(Arrays.asList(dependency));
        dependencyContainers.put(id, containers);
        return this;
    }

    public JobServiceComposeResource withDependencyToService(String serviceId, GenericContainer<?> dependency) {
        List<GenericContainer<?>> containers = dependencyContainers.getOrDefault(serviceId, new ArrayList<>());
        containers.add(dependency);
        dependencyContainers.put(serviceId, containers);
        return this;
    }

    public JobServiceComposeResource withSharedDependencyContainer(String prefix, GenericContainer<?> container) {
        sharedDependencyContainers.put(prefix, container);
        return this;
    }

    public <T> List<T> getServiceContainers(Class<T> type) {
        return serviceContainers.values().stream().filter(type::isInstance).map(type::cast).collect(Collectors.toList());
    }

    public <T extends KogitoGenericContainer<?>> T getServiceContainer(String id) {
        return (T) serviceContainers.get(id);
    }

    private String hostName(GenericContainer<?> container) {
        return container.getContainerInfo().getConfig().getHostName();
    }

    @Override
    public void start() {
        LOGGER.info("Start JobService with {} test resources", sharedDependencyContainers.values().stream().map(GenericContainer::getImage).collect(Collectors.toList()));
        final Network network = Network.newNetwork();
        sharedDependencyContainers.values().stream()
                .map(c -> c.withNetwork(network))
                .map(c -> c.waitingFor(Wait.forListeningPort()))
                .forEach(GenericContainer::start);
        //configurePostgreSQL(sharedDependencyContainers.values(), mainContainer);
        configureKafka(sharedDependencyContainers.values(), serviceContainers.values().toArray(GenericContainer[]::new));
        startServices(network);
    }

    protected void startServices(Network network) {
        serviceContainers.entrySet()
                .stream()
                .map(entry -> {
                    List<GenericContainer<?>> dependencies = dependencyContainers.getOrDefault(entry.getKey(), Collections.emptyList())
                            .stream()
                            .map(container -> container.withNetwork(network))
                            .map(container -> {
                                if (!container.isRunning()) {
                                    container.start();
                                }
                                return container;
                            })
                            .collect(Collectors.toList());
                    configurePostgreSQL(dependencies, serviceContainers.get(entry.getKey()));
                    configureKafka(dependencies, serviceContainers.get(entry.getKey()));
                    return entry;
                })
                .map(Map.Entry::getValue)
                .forEach(service -> {
                    service.withNetwork(network);
                    service.start();
                    LOGGER.info("JobService test resource started");
                });
    }

    protected void configureKafka(Collection<GenericContainer<?>> containers, GenericContainer<?>... services) {
        containers.stream()
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
                    Stream.of(services).forEach(service -> service.addEnv("KAFKA_BOOTSTRAP_SERVERS", kafkaInternalUrl));
                });
    }

    protected void configurePostgreSQL(Collection<GenericContainer<?>> containers, GenericContainer<?>... services) {
        containers.stream()
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
                    Stream.of(services)
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
        sharedDependencyContainers.values().forEach(GenericContainer::stop);
        dependencyContainers.values().stream().flatMap(List::stream).forEach(GenericContainer::stop);
        LOGGER.info("Test resource stopped");
    }

    @Override
    public String getResourceName() {
        return mainContainer.getContainerName();
    }

    @Override
    public int getMappedPort() {
        return mainContainer.getMappedPort(8080);
    }

    public Map<String, String> getProperties() {
        return properties;
    }
}
