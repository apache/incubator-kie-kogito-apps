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

import org.apache.commons.lang3.StringUtils;
import org.kie.kogito.jobs.service.model.JobDetails;
import org.kie.kogito.jobs.service.repository.JobRepository;

public abstract class AbstractJobRepository implements JobRepository {

    @Override
    public JobDetails save(JobDetails job) {
        JobDetails jobDetails = doSave(job);
        return jobDetails;
    }

    public abstract JobDetails doSave(JobDetails job);

    @Override
    public JobDetails delete(JobDetails job) {
        JobDetails jobDetails = delete(job.getId());
        return jobDetails;
    }

    @Override
    public JobDetails merge(String jobId, JobDetails jobToMerge) {
        if (jobId == null || StringUtils.isBlank(jobId)) {
            throw new IllegalArgumentException("Id is empty or not equals to Job.id : " + jobId);
        }
        JobDetails currentJob = this.get(jobId);
        if (currentJob == null) {
            return null;
        }
        if (jobToMerge.getId() == null || StringUtils.isBlank(jobToMerge.getId())) {
            throw new IllegalArgumentException("Id is empty or not equals to Job to be merged.id : " + jobId);
        }
        if (!jobId.equals(jobToMerge.getId())) {
            throw new IllegalArgumentException("Id between current job " + jobId + " and job to be merged are not equal " + jobToMerge.getId());
        }

        return doMerge(jobToMerge, currentJob);
    }

    private JobDetails doMerge(JobDetails toMerge, JobDetails current) {
        return JobDetails.builder()
                .of(current)
                .merge(toMerge)
                .build();
    }
}
