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
package org.kie.kogito.index.addon.config;

import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;

@ConfigRoot(prefix = "kogito", name = "dataindex", phase = ConfigPhase.RUN_TIME)
public class DataIndexUIClientRuntimeConfig {

    /**
     * Data Index HTTP URL
     */
    @ConfigItem(name = "http.url", defaultValue = "http://localhost:${quarkus.http.port}")
    public String dataIndexHttpUrl;

    /**
     * Data Index WS URL
     */
    @ConfigItem(name = "ws.url", defaultValue = "ws://localhost:${quarkus.http.port}")
    public String dataIndexWebsocketUrl;

}
