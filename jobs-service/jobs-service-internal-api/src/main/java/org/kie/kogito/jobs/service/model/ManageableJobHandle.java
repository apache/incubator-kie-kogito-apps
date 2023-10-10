package org.kie.kogito.jobs.service.model;

import java.time.ZonedDateTime;
import java.util.Optional;

import org.kie.kogito.timer.JobHandle;

public class ManageableJobHandle implements JobHandle {

    private boolean cancel;
    private Long id;
    private ZonedDateTime scheduledTime;

    public ManageableJobHandle(Long id) {
        this.id = id;
    }

    public ManageableJobHandle(String id) {
        this.id = Optional.ofNullable(id).map(Long::parseLong).orElse(null);
    }

    public ManageableJobHandle(boolean cancel) {
        this.cancel = cancel;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public long getId() {
        return Optional.ofNullable(id).orElse(0l);
    }

    @Override
    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

    @Override
    public boolean isCancel() {
        return cancel;
    }

    public ZonedDateTime getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(ZonedDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }
}
