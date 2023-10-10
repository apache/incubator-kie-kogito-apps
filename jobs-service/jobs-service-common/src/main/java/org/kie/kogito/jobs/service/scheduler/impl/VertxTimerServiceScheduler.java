package org.kie.kogito.jobs.service.scheduler.impl;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.util.Collection;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;

import org.kie.kogito.jobs.service.model.ManageableJobHandle;
import org.kie.kogito.jobs.service.utils.DateUtil;
import org.kie.kogito.timer.InternalSchedulerService;
import org.kie.kogito.timer.Job;
import org.kie.kogito.timer.JobContext;
import org.kie.kogito.timer.TimerService;
import org.kie.kogito.timer.Trigger;
import org.kie.kogito.timer.impl.DefaultTimerJobFactoryManager;
import org.kie.kogito.timer.impl.TimerJobFactoryManager;
import org.kie.kogito.timer.impl.TimerJobInstance;

import io.vertx.mutiny.core.Vertx;

@ApplicationScoped
public class VertxTimerServiceScheduler implements TimerService<ManageableJobHandle>,
        InternalSchedulerService {

    protected TimerJobFactoryManager jobFactoryManager = DefaultTimerJobFactoryManager.instance;

    protected final Vertx vertx;

    public VertxTimerServiceScheduler(Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    public long getCurrentTime() {
        return System.currentTimeMillis();
    }

    @Override
    public void reset() {
        //no action is required
    }

    @Override
    public void shutdown() {
        vertx.close();
    }

    @Override
    public long getTimeToNextJob() {
        return 0;
    }

    @Override
    public Collection<TimerJobInstance> getTimerJobInstances(long id) {
        return jobFactoryManager.getTimerJobInstances();
    }

    @Override
    public void setTimerJobFactoryManager(TimerJobFactoryManager timerJobFactoryManager) {
        this.jobFactoryManager = timerJobFactoryManager;
    }

    @Override
    public TimerJobFactoryManager getTimerJobFactoryManager() {
        return jobFactoryManager;
    }

    @Override
    public ManageableJobHandle scheduleJob(Job job, JobContext ctx, Trigger trigger) {
        return Optional.ofNullable(trigger.hasNextFireTime())
                .map(id -> new ManageableJobHandle(false))
                .map(jobHandle -> jobFactoryManager.createTimerJobInstance(job, ctx, trigger, jobHandle, this))
                .map(jobInstance -> {
                    internalSchedule(jobInstance);
                    return (ManageableJobHandle) jobInstance.getJobHandle();
                })
                .orElse(null);
    }

    @Override
    public boolean removeJob(ManageableJobHandle jobHandle) {
        return vertx.cancelTimer(jobHandle.getId());
    }

    @Override
    public void internalSchedule(TimerJobInstance timerJobInstance) {
        final Trigger trigger = timerJobInstance.getTrigger();
        if (trigger.hasNextFireTime() == null) {
            return;
        }
        final long then = trigger.hasNextFireTime().getTime();
        final ZonedDateTime now = DateUtil.now();
        final long delay = calculateDelay(then, now);
        final ManageableJobHandle handle = (ManageableJobHandle) timerJobInstance.getJobHandle();
        long scheduledId = vertx.setTimer(delay, i -> timerJobInstance.getJob().execute(timerJobInstance.getJobContext()));
        handle.setId(scheduledId);
        handle.setScheduledTime(now);
    }

    private Long calculateDelay(long then, ZonedDateTime now) {
        return Optional.of(now)
                .map(ChronoZonedDateTime::toInstant)
                .map(Instant::toEpochMilli)
                .filter(n -> then > n)
                .map(n -> then - n)
                .orElse(1l);
    }

    public Vertx getVertx() {
        return vertx;
    }
}
