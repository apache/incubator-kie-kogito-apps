package org.kie.kogito.jobs.service.reflection;

import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * Placeholder for registering classes for reflection instead of using reflection-config.json approach or tagging
 * them individually.
 */
@RegisterForReflection(
        classNames = {
                "org.kie.kogito.event.cloudevents.SpecVersionSerializer",
                "org.kie.kogito.event.AbstractDataEvent",
                "org.kie.kogito.jobs.service.events.JobDataEvent",
                "org.kie.kogito.jobs.service.repository.marshaller.RecipientMarshaller$HTTPRecipientAccessor",
                "org.kie.kogito.jobs.service.job.model.ScheduledJobAdapter$ProcessPayload",
                "org.kie.kogito.jobs.service.repository.marshaller.TriggerMarshaller$PointInTimeTriggerAccessor",
                "org.kie.kogito.jobs.service.repository.marshaller.TriggerMarshaller$IntervalTriggerAccessor",
                "org.kie.kogito.jobs.service.repository.marshaller.TriggerMarshaller$SimpleTimerTriggerAccessor",
                "org.kie.kogito.jobs.api.event.CancelJobRequestEvent$JobId",
                "org.kie.kogito.jobs.service.api.event.serialization.SpecVersionSerializer",
                "org.kie.kogito.jobs.service.api.event.serialization.SpecVersionDeserializer",
                "org.kie.kogito.jobs.service.api.Job",
                "org.kie.kogito.jobs.service.api.JobLookupId",
                "org.kie.kogito.jobs.service.api.Recipient",
                "org.kie.kogito.jobs.service.api.recipient.http.HttpRecipient",
                "org.kie.kogito.jobs.service.api.recipient.http.HttpRecipientStringPayloadData",
                "org.kie.kogito.jobs.service.api.recipient.http.HttpRecipientBinaryPayloadData",
                "org.kie.kogito.jobs.service.api.recipient.http.HttpRecipientJsonPayloadData",
                "org.kie.kogito.jobs.service.api.recipient.sink.SinkRecipient",
                "org.kie.kogito.jobs.service.api.recipient.sink.SinkRecipientBinaryPayloadData",
                "org.kie.kogito.jobs.service.api.recipient.sink.SinkRecipientJsonPayloadData",
                "org.kie.kogito.jobs.service.api.Schedule",
                "org.kie.kogito.jobs.service.api.schedule.timer.TimerSchedule",
                "org.kie.kogito.jobs.service.api.schedule.cron.CronSchedule",
                "org.kie.kogito.jobs.service.api.event.JobCloudEvent",
                "org.kie.kogito.jobs.service.api.event.CreateJobEvent",
                "org.kie.kogito.jobs.service.api.event.DeleteJobEvent",
                "org.kie.kogito.jobs.service.resource.error.ErrorResponse"
        })
public class ReflectionConfiguration {
}
