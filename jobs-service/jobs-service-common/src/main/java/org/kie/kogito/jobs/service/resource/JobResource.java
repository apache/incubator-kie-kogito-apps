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
package org.kie.kogito.jobs.service.resource;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.kie.kogito.jobs.api.Job;
import org.kie.kogito.jobs.service.adapter.ScheduledJobAdapter;
import org.kie.kogito.jobs.service.model.JobDetails;
import org.kie.kogito.jobs.service.model.ScheduledJob;
import org.kie.kogito.jobs.service.model.ScheduledJob.ScheduledJobBuilder;
import org.kie.kogito.jobs.service.repository.JobRepository;
import org.kie.kogito.jobs.service.scheduler.impl.TimerDelegateJobScheduler;
import org.kie.kogito.jobs.service.validation.JobDetailsValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@ApplicationScoped
@Path(RestApiConstants.JOBS_PATH)
public class JobResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobResource.class);

    @Inject
    TimerDelegateJobScheduler scheduler;

    @Inject
    JobRepository jobRepository;

    @Inject
    JobDetailsValidator jobDetailsValidator;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(operationId = "createJob")
    public ScheduledJob create(Job job) {
        LOGGER.debug("REST create {}", job);
        JobDetails jobDetails = jobDetailsValidator.validateToCreate(ScheduledJobAdapter.to(ScheduledJob.builder().job(job).build()));
        JobDetails scheduledJobDetails = scheduler.schedule(jobDetails);
        if (scheduledJobDetails == null) {
            throw new RuntimeException("Failed to schedule job " + job);
        }
        return ScheduledJobAdapter.of(scheduledJobDetails);
    }

    @PATCH
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(operationId = "patchJob")
    public ScheduledJob patch(@PathParam("id") String id, @RequestBody Job job) {
        LOGGER.debug("REST patch update {}", job);
        //validating allowed patch attributes
        JobDetails jobToBeMerged = jobDetailsValidator.validateToMerge(ScheduledJobAdapter.to(ScheduledJobBuilder.from(job)));
        JobDetails rescheduleJobDetails = scheduler.reschedule(id, jobToBeMerged.getTrigger());
        if (rescheduleJobDetails == null) {
            throw new NotFoundException("Failed to reschedule job " + job);
        }
        return ScheduledJobAdapter.of(rescheduleJobDetails);
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @Operation(operationId = "deleteJob")
    public ScheduledJob delete(@PathParam("id") String id) {
        LOGGER.debug("REST delete {}", id);
        JobDetails cancelJobDetails = scheduler.cancel(id);
        if (cancelJobDetails == null) {
            throw new NotFoundException("Failed to cancel job scheduling for jobId " + id);
        }
        return ScheduledJobAdapter.of(cancelJobDetails);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @Operation(operationId = "getJob")
    public ScheduledJob get(@PathParam("id") String id) {
        LOGGER.debug("REST get {}", id);
        JobDetails jobDetails = jobRepository.get(id);
        if (jobDetails == null) {
            throw new NotFoundException("Job not found id " + id);
        }
        return ScheduledJobAdapter.of(jobDetails);
    }
}
