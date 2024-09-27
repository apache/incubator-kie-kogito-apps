/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.kie.kogito.index.test.containers;

/**
 * This container wraps Data Index Service container
 */
public class DataIndexPostgreSqlContainer extends AbstractDataIndexContainer {
    public static final String NAME = "data-index-service-postgresql";

    public DataIndexPostgreSqlContainer() {
        super(NAME);
    }

    public void setPostgreSqlURL(String postgreSqlURL, String username, String password) {
        addEnv("QUARKUS_DATASOURCE_JDBC_URL", postgreSqlURL);
        addEnv("QUARKUS_DATASOURCE_USERNAME", username);
        addEnv("QUARKUS_DATASOURCE_PASSWORD", password);
    }

    public void migrateDB() {
        addEnv("QUARKUS_FLYWAY_MIGRATE_AT_START", "true");
        addEnv("QUARKUS_FLYWAY_BASELINE_ON_MIGRATE", "true");
        addEnv("QUARKUS_FLYWAY_TABLE", "data-index-flyway");
    }

    @Override
    public String getResourceName() {
        return NAME;
    }

}
