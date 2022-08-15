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
package org.kie.kogito.jobs.service.repository;

import java.time.ZonedDateTime;
import java.util.concurrent.CompletionStage;

import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.reactive.streams.operators.PublisherBuilder;
import org.kie.kogito.jobs.service.model.JobServiceManagementInfo;
import org.kie.kogito.jobs.service.model.JobStatus;
import org.kie.kogito.jobs.service.model.job.JobDetails;

public interface JobServiceManagementRepository {

    Uni<JobServiceManagementInfo> updateKeepAlive(JobServiceManagementInfo info);
    Uni<JobServiceManagementInfo> removeToken(JobServiceManagementInfo info);
    Uni<JobServiceManagementInfo> tryUpdateMaster(String id);


    Uni<JobServiceManagementInfo> release(JobServiceManagementInfo info);

    Uni<JobServiceManagementInfo> keepAlive(JobServiceManagementInfo info);

}
