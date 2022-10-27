/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
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
package org.kie.kogito.testcontainers;

import org.kie.kogito.test.resources.TestResource;
import org.testcontainers.containers.wait.strategy.Wait;

public class JobServiceContainer extends KogitoGenericContainer<JobServiceContainer> implements TestResource {

    public static final String NAME = "jobs-service";
    public static final int PORT = 8080;

    public JobServiceContainer() {
        super(NAME);
        addExposedPort(PORT);
        //waitingFor(Wait.forLogMessage(".*Listening on:.*", 1));
        waitingFor(Wait.forHttp("/q/health/live").forStatusCode(200));
        addEnv("QUARKUS_HTTP_PORT", Integer.toString(PORT));
        addEnv("QUARKUS_LOG_CATEGORY__ORG_KIE_KOGITO_JOBS_SERVICE__LEVEL", "DEBUG");
    }

    @Override
    public int getMappedPort() {
        return getMappedPort(PORT);
    }

    @Override
    public String getResourceName() {
        return NAME;
    }

    public void setKafkaURL(String kafkaURL) {
        addEnv("KAFKA_BOOTSTRAP_SERVERS", kafkaURL);
    }

    public void setPostgreSQLURL(String jdbcUrl, String reactiveUrl, String username, String password) {
        addEnv("QUARKUS_DATASOURCE_JDBC_URL", jdbcUrl);
        addEnv("QUARKUS_DATASOURCE_REACTIVE_URL", reactiveUrl);
        addEnv("QUARKUS_DATASOURCE_USERNAME", username);
        addEnv("QUARKUS_DATASOURCE_PASSWORD", password);
        addEnv("QUARKUS_DATASOURCE_DB-KIND", "postgresql");
    }

    public void setQuarkusProfile(String profile) {
        addEnv("QUARKUS_PROFILE", profile);
    }
}
