package org.kie.kogito.jobs.service.repository.marshaller;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.kogito.jobs.service.api.recipient.http.HttpRecipient;
import org.kie.kogito.jobs.service.api.recipient.http.HttpRecipientStringPayloadData;
import org.kie.kogito.jobs.service.model.JobDetails;
import org.kie.kogito.jobs.service.model.JobStatus;
import org.kie.kogito.jobs.service.model.Recipient;
import org.kie.kogito.jobs.service.model.RecipientInstance;
import org.kie.kogito.timer.Trigger;
import org.kie.kogito.timer.impl.PointInTimeTrigger;

import io.vertx.core.json.JsonObject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.kie.kogito.jobs.service.utils.DateUtil.DEFAULT_ZONE;

class JobDetailsMarshallerTest {

    JobDetailsMarshaller jobDetailsMarshaller;

    JobDetails jobDetails;

    JsonObject jsonObject;

    @BeforeEach
    void setUp() {
        jobDetailsMarshaller = new JobDetailsMarshaller(new TriggerMarshaller(), new RecipientMarshaller());

        String id = "testId";
        String correlationId = "testCorrelationId";
        JobStatus status = JobStatus.SCHEDULED;
        Date date = new Date();
        ZonedDateTime lastUpdate = ZonedDateTime.ofInstant(date.toInstant(), DEFAULT_ZONE);
        Integer retries = 2;
        Integer priority = 3;
        Integer executionCounter = 4;
        String scheduledId = "testScheduledId";
        String payload = "test";
        Recipient recipient = new RecipientInstance(HttpRecipient.builder().forStringPayload().url("url").payload(HttpRecipientStringPayloadData.from(payload)).build());
        Trigger trigger = new PointInTimeTrigger(new Date().toInstant().toEpochMilli(), null, null);
        Long executionTimeout = 10L;
        ChronoUnit executionTimeoutUnit = ChronoUnit.SECONDS;

        jobDetails = JobDetails.builder()
                .id(id)
                .correlationId(correlationId)
                .status(status)
                .lastUpdate(lastUpdate)
                .retries(retries)
                .executionCounter(executionCounter)
                .scheduledId(scheduledId)
                .priority(priority)
                .recipient(recipient)
                .trigger(trigger)
                .executionTimeout(executionTimeout)
                .executionTimeoutUnit(executionTimeoutUnit)
                .build();

        jsonObject = new JsonObject()
                .put("id", id)
                .put("correlationId", correlationId)
                .put("status", status.name())
                .put("lastUpdate", date.getTime())
                .put("retries", retries)
                .put("executionCounter", executionCounter)
                .put("scheduledId", scheduledId)
                .put("priority", priority)
                .put("recipient", JsonObject
                        .mapFrom(HttpRecipient.builder().forStringPayload().url("url").payload(HttpRecipientStringPayloadData.from(payload)).build())
                        .put("classType", HttpRecipient.class.getName()))
                .put("trigger", new JsonObject()
                        .put("nextFireTime", trigger.hasNextFireTime().getTime())
                        .put("classType", PointInTimeTrigger.class.getName()))
                .put("executionTimeout", executionTimeout)
                .put("executionTimeoutUnit", executionTimeoutUnit.name());
    }

    @Test
    void marshall() {
        assertEquals(jsonObject, jobDetailsMarshaller.marshall(jobDetails));
    }

    @Test
    void marshallNull() {
        assertNull(jobDetailsMarshaller.marshall(null));
    }

    @Test
    void unmarshall() {
        assertEquals(jobDetails, jobDetailsMarshaller.unmarshall(jsonObject));
    }

    @Test
    void unmarshallNull() {
        assertNull(jobDetailsMarshaller.unmarshall(null));
    }
}
