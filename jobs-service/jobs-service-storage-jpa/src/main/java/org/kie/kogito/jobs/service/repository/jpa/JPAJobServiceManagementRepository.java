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

import java.util.Objects;
import java.util.function.Function;

import org.kie.kogito.jobs.service.model.JobServiceManagementInfo;
import org.kie.kogito.jobs.service.repository.JobServiceManagementRepository;
import org.kie.kogito.jobs.service.repository.jpa.model.JobServiceManagementEntity;
import org.kie.kogito.jobs.service.repository.jpa.repository.JobServiceManagementEntityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.panache.common.Parameters;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import static java.time.OffsetDateTime.now;

@ApplicationScoped
@Transactional
public class JPAJobServiceManagementRepository implements JobServiceManagementRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(JPAJobServiceManagementRepository.class);

    private final JobServiceManagementEntityRepository repository;

    @Inject
    public JPAJobServiceManagementRepository(JobServiceManagementEntityRepository repository) {
        this.repository = repository;
    }

    @Override
    public JobServiceManagementInfo getAndUpdate(String id, Function<JobServiceManagementInfo, JobServiceManagementInfo> computeUpdate) {
        LOGGER.trace("get {}", id);
        JobServiceManagementInfo info = doGetAndUpdate(id, computeUpdate);
        LOGGER.trace("got {}", info);
        return info;
    }

    private JobServiceManagementInfo doGetAndUpdate(String id, Function<JobServiceManagementInfo, JobServiceManagementInfo> computeUpdate) {

        JobServiceManagementInfo info = this.repository.findByIdOptional(id)
                .map(this::from)
                .orElse(null);

        return this.update(computeUpdate.apply(info));
    }

    @Override
    public JobServiceManagementInfo set(JobServiceManagementInfo info) {
        LOGGER.trace("set {}", info);
        return this.doSet(info);
    }

    public JobServiceManagementInfo doSet(JobServiceManagementInfo info) {
        return this.update(info);
    }

    private JobServiceManagementInfo update(JobServiceManagementInfo info) {

        if (Objects.isNull(info)) {
            return null;
        }

        JobServiceManagementEntity jobService = this.repository.findByIdOptional(info.getId()).orElse(new JobServiceManagementEntity());

        jobService.setId(info.getId());
        jobService.setToken(info.getToken());
        jobService.setLastHeartBeat(info.getLastHeartbeat());

        repository.persist(jobService);

        return from(jobService);
    }

    @Override
    public JobServiceManagementInfo heartbeat(JobServiceManagementInfo info) {
        return this.doHeartbeat(info);
    }

    @Override
    public Boolean release(JobServiceManagementInfo info) {
        return this.doRelease(info);
    }

    private JobServiceManagementEntity findByIdAndToken(JobServiceManagementInfo info) {
        return repository.find("#JobServiceManagementEntity.GetServiceByIdAndToken", Parameters.with("id", info.getId()).and("token", info.getToken()).map())
                .firstResultOptional().orElse(null);
    }

    private JobServiceManagementInfo doHeartbeat(JobServiceManagementInfo info) {
        JobServiceManagementEntity jobService = findByIdAndToken(info);

        if (jobService == null) {
            return null;
        }

        jobService.setLastHeartBeat(now());
        repository.persist(jobService);

        return from(jobService);
    }

    private Boolean doRelease(JobServiceManagementInfo info) {
        JobServiceManagementEntity jobService = findByIdAndToken(info);
        if (jobService == null) {
            return false;
        }
        jobService.setToken(null);
        jobService.setLastHeartBeat(null);
        repository.persist(jobService);
        return true;
    }

    JobServiceManagementInfo from(JobServiceManagementEntity jobService) {
        if (Objects.isNull(jobService)) {
            return null;
        }
        return new JobServiceManagementInfo(jobService.getId(), jobService.getToken(), jobService.getLastHeartBeat());
    }
}
