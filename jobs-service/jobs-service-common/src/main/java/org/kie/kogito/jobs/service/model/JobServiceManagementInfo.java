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
package org.kie.kogito.jobs.service.model;

import java.time.ZonedDateTime;
import java.util.UUID;

public class JobServiceManagementInfo {

    private UUID id;
    private ZonedDateTime timestamp;
    private ZonedDateTime lastKeepAlive;
    private UUID token;
    private String instanceName;
    private String instanceIp;

    public JobServiceManagementInfo(ZonedDateTime lastKeepAlive, UUID token) {
        this.lastKeepAlive = lastKeepAlive;
        this.token = token;
    }

    public UUID getId() {
        return id;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public ZonedDateTime getLastKeepAlive() {
        return lastKeepAlive;
    }

    public UUID getToken() {
        return token;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public String getInstanceIp() {
        return instanceIp;
    }

    public void setLastKeepAlive(ZonedDateTime lastKeepAlive) {
        this.lastKeepAlive = lastKeepAlive;
    }
}
