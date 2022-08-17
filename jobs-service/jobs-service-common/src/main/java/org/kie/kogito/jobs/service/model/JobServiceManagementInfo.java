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
import java.util.Optional;
import java.util.StringJoiner;
import java.util.UUID;

public class JobServiceManagementInfo {

    private UUID id;
    private ZonedDateTime lastHeartbeat;
    private UUID token;

    private ZonedDateTime timestamp;
    private String instanceName;
    private String instanceIp;

    public JobServiceManagementInfo(String id, String token, ZonedDateTime heartbeat) {
        this.id = Optional.ofNullable(id).map(UUID::fromString).orElse(null);
        this.token = Optional.ofNullable(token).map(UUID::fromString).orElse(null);
        this.lastHeartbeat = heartbeat;
    }

    public UUID getId() {
        return id;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public ZonedDateTime getLastHeartbeat() {
        return lastHeartbeat;
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

    public void setLastHeartbeat(ZonedDateTime lastHeartbeat) {
        this.lastHeartbeat = lastHeartbeat;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", JobServiceManagementInfo.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("lastHeartbeat=" + lastHeartbeat)
                .add("token=" + token)
                .add("timestamp=" + timestamp)
                .add("instanceName='" + instanceName + "'")
                .add("instanceIp='" + instanceIp + "'")
                .toString();
    }
}
