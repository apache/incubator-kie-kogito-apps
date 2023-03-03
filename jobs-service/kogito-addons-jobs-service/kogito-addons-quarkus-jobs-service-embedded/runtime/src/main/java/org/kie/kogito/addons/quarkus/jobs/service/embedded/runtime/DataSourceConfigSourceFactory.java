/*
 * Copyright 2023 Red Hat, Inc. and/or its affiliates.
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
package org.kie.kogito.addons.quarkus.jobs.service.embedded.runtime;

import java.util.List;
import java.util.Map;

import io.quarkus.runtime.configuration.DefaultsConfigSource;
import io.smallrye.config.ConfigSourceContext;
import io.smallrye.config.ConfigSourceFactory;
import org.eclipse.microprofile.config.spi.ConfigSource;

public class DataSourceConfigSourceFactory implements ConfigSourceFactory {

    public static final String DATA_SOURCE_NAME = "jobs-service";
    @Override
    public Iterable<ConfigSource> getConfigSources(ConfigSourceContext configSourceContext) {
        Map<String, String> configs = Map.of(
                "quarkus.datasource.\"" + DATA_SOURCE_NAME + "\".db-kind", "postgresql",
                "quarkus.flyway.\"" + DATA_SOURCE_NAME + "\".locations", "db/migration",
                "quarkus.flyway.\"" + DATA_SOURCE_NAME + "\".migrate-at-start", "true");
        return List.of(new DefaultsConfigSource(configs, "embedded-addon", 0));
    }
}
