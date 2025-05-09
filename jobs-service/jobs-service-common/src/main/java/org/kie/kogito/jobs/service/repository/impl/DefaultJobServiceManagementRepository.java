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
package org.kie.kogito.jobs.service.repository.impl;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import org.kie.kogito.jobs.service.model.JobServiceManagementInfo;
import org.kie.kogito.jobs.service.repository.JobServiceManagementRepository;
import org.kie.kogito.jobs.service.utils.DateUtil;

import io.quarkus.arc.DefaultBean;

import jakarta.enterprise.context.ApplicationScoped;

@DefaultBean
@ApplicationScoped
public class DefaultJobServiceManagementRepository implements JobServiceManagementRepository {

    private AtomicReference<JobServiceManagementInfo> instance = new AtomicReference<>(new JobServiceManagementInfo(null, null, null));

    @Override
    public JobServiceManagementInfo getAndUpdate(String id, Function<JobServiceManagementInfo, JobServiceManagementInfo> computeUpdate) {
        return set(computeUpdate.apply(instance.get()));
    }

    @Override
    public JobServiceManagementInfo set(JobServiceManagementInfo info) {
        instance.set(info);
        return instance.get();
    }

    @Override
    public JobServiceManagementInfo heartbeat(JobServiceManagementInfo info) {
        info.setLastHeartbeat(DateUtil.now().toOffsetDateTime());
        return set(info);
    }

    @Override
    public Boolean release(JobServiceManagementInfo info) {
        instance.set(new JobServiceManagementInfo(info.getId(), null, null));
        return true;
    }
}
