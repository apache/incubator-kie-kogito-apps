package org.kie.kogito.jobs.service.repository;

import java.util.function.Function;

import org.kie.kogito.jobs.service.model.JobServiceManagementInfo;

import io.smallrye.mutiny.Uni;

public interface JobServiceManagementRepository {

    Uni<JobServiceManagementInfo> getAndUpdate(String id, Function<JobServiceManagementInfo, JobServiceManagementInfo> computeUpdate);

    Uni<JobServiceManagementInfo> set(JobServiceManagementInfo info);

    Uni<JobServiceManagementInfo> heartbeat(JobServiceManagementInfo info);

}
