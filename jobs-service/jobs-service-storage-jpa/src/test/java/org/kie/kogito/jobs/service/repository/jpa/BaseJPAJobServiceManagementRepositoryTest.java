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
package org.kie.kogito.jobs.service.repository.jpa;

import java.time.OffsetDateTime;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.Test;
import org.kie.kogito.jobs.service.model.JobServiceManagementInfo;
import org.kie.kogito.jobs.service.repository.JobServiceManagementRepository;
import org.kie.kogito.jobs.service.utils.DateUtil;

import jakarta.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class BaseJPAJobServiceManagementRepositoryTest {
    @Inject
    JobServiceManagementRepository tested;

    @Test
    void testGetAndUpdate() {
        String id = "instance-id-1";
        String token = "token1";
        create(id, token);

        AtomicReference<OffsetDateTime> date = new AtomicReference<>();
        JobServiceManagementInfo updated = tested.getAndUpdate(id, info -> {
            date.set(DateUtil.now().toOffsetDateTime());
            info.setLastHeartbeat(date.get());
            return info;
        });
        assertThat(updated.getId()).isEqualTo(id);
        assertThat(date.get()).isNotNull();
        assertThat(updated.getLastHeartbeat()).isEqualTo(date.get());
        assertThat(updated.getToken()).isEqualTo(token);
    }

    @Test
    void testGetAndUpdateNotExisting() {
        String id = "instance-id-2";
        AtomicReference<JobServiceManagementInfo> found = new AtomicReference<>(new JobServiceManagementInfo());
        JobServiceManagementInfo updated = tested.getAndUpdate(id, info -> {
            found.set(info);
            return info;
        });
        assertThat(updated).isNull();
        assertThat(found.get()).isNull();
    }

    private JobServiceManagementInfo create(String id, String token) {
        JobServiceManagementInfo created = tested.set(new JobServiceManagementInfo(id, token, null));
        assertThat(created.getId()).isEqualTo(id);
        assertThat(created.getToken()).isEqualTo(token);
        assertThat(created.getLastHeartbeat()).isNull();
        return created;
    }

    @Test
    void testHeartbeat() {
        String id = "instance-id-3";
        String token = "token3";
        JobServiceManagementInfo created = create(id, token);

        JobServiceManagementInfo updated = tested.heartbeat(created);
        assertThat(updated.getLastHeartbeat()).isNotNull();
        assertThat(updated.getLastHeartbeat()).isBefore(DateUtil.now().plusSeconds(1).toOffsetDateTime());
    }

    @Test
    void testConflictHeartbeat() {
        String id = "instance-id-4";
        String token = "token4";
        create(id, token);

        JobServiceManagementInfo updated = tested.heartbeat(new JobServiceManagementInfo(id, "differentToken", null));
        assertThat(updated).isNull();
    }

    @Test
    void testRelease() {
        String id = "instance-id-5";
        String token = "token5";
        JobServiceManagementInfo created = create(id, token);

        Boolean released = tested.release(created);
        assertThat(released).isTrue();
    }

    @Test
    void testReleaseNotExisting() {
        String id = "instance-id-6";
        String token = "token6";
        JobServiceManagementInfo notExisting = new JobServiceManagementInfo(id, token, OffsetDateTime.now());

        Boolean released = tested.release(notExisting);
        assertThat(released).isFalse();
    }
}
