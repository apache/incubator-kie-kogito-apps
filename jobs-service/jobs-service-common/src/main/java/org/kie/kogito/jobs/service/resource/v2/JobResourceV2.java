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
package org.kie.kogito.jobs.service.resource.v2;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.kie.kogito.jobs.service.adapter.JobDetailsAdapter;
import org.kie.kogito.jobs.service.api.Job;
import org.kie.kogito.jobs.service.model.JobDetails;
import org.kie.kogito.jobs.service.repository.JobRepository;
import org.kie.kogito.jobs.service.resource.RestApiConstants;
import org.kie.kogito.jobs.service.scheduler.impl.TimerDelegateJobScheduler;
import org.kie.kogito.jobs.service.validation.JobValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@ApplicationScoped
@Path(RestApiConstants.V2 + RestApiConstants.JOBS_PATH)
public class JobResourceV2 {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobResourceV2.class);

    @Inject
    TimerDelegateJobScheduler scheduler;

    @Inject
    JobRepository jobRepository;

    @Inject
    JobValidator jobValidator;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(operationId = "createJobV2")
    public Job create(Job job) {
        LOGGER.debug("REST create {}", job);
        jobValidator.validateToCreate(job);
        JobDetails jobDetails = JobDetailsAdapter.from(job);
        JobDetails scheduledJobDetails = scheduler.schedule(jobDetails);
        if (scheduledJobDetails == null) {
            throw new RuntimeException("Failed to schedule job " + job);
        }
        return JobDetailsAdapter.toJob(jobDetails);
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @Operation(operationId = "deleteJobV2")
    public Job delete(@PathParam("id") String id) {
        JobDetails cancelJobDetails = scheduler.cancel(id);
        if (cancelJobDetails == null) {
            throw new NotFoundException("Failed to cancel job scheduling for jobId " + id);
        }
        return JobDetailsAdapter.toJob(cancelJobDetails);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @Operation(operationId = "getJobV2")
    public Job get(@PathParam("id") String id) {
        JobDetails jobDetails = jobRepository.get(id);
        if (jobDetails == null) {
            throw new NotFoundException("Job not found id " + id);
        }
        return JobDetailsAdapter.toJob(jobDetails);
    }
}
