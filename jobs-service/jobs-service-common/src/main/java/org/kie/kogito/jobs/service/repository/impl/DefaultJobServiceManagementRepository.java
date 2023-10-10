package org.kie.kogito.jobs.service.repository.impl;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import javax.enterprise.context.ApplicationScoped;

import org.kie.kogito.jobs.service.model.JobServiceManagementInfo;
import org.kie.kogito.jobs.service.repository.JobServiceManagementRepository;
import org.kie.kogito.jobs.service.utils.DateUtil;

import io.quarkus.arc.DefaultBean;
import io.smallrye.mutiny.Uni;

@DefaultBean
@ApplicationScoped
public class DefaultJobServiceManagementRepository implements JobServiceManagementRepository {

    private AtomicReference<JobServiceManagementInfo> instance = new AtomicReference<>(new JobServiceManagementInfo(null, null, null));

    @Override
    public Uni<JobServiceManagementInfo> getAndUpdate(String id, Function<JobServiceManagementInfo, JobServiceManagementInfo> computeUpdate) {
        return set(computeUpdate.apply(instance.get()));
    }

    @Override
    public Uni<JobServiceManagementInfo> set(JobServiceManagementInfo info) {
        instance.set(info);
        return Uni.createFrom().item(instance.get());
    }

    @Override
    public Uni<JobServiceManagementInfo> heartbeat(JobServiceManagementInfo info) {
        info.setLastHeartbeat(DateUtil.now().toOffsetDateTime());
        return set(info);
    }
}
