package org.kie.kogito.jobs.service.model;

import org.kie.kogito.timer.JobContext;
import org.kie.kogito.timer.JobHandle;

public class JobDetailsContext implements JobContext {

    private JobDetails jobDetails;
    private JobHandle jobHandle;

    public JobDetailsContext(JobDetails jobDetails) {
        this.jobDetails = jobDetails;
        if (jobHandle == null) {
            jobHandle = new ManageableJobHandle(jobDetails.getScheduledId());
        }
    }

    @Override
    public void setJobHandle(JobHandle jobHandle) {
        this.jobHandle = jobHandle;
    }

    @Override
    public JobHandle getJobHandle() {
        return jobHandle;
    }

    public JobDetails getJobDetails() {
        return jobDetails;
    }
}
