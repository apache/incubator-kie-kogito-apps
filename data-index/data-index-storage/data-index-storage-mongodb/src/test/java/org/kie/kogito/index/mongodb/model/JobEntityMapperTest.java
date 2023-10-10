package org.kie.kogito.index.mongodb.model;

import java.time.ZonedDateTime;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.kie.kogito.index.model.Job;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.kie.kogito.persistence.mongodb.model.ModelUtils.zonedDateTimeToInstant;

class JobEntityMapperTest {

    JobEntityMapper jobEntityMapper = new JobEntityMapper();

    static Job job;

    static JobEntity jobEntity;

    @BeforeAll
    static void setup() {
        String testId = "testId";
        ZonedDateTime time = ZonedDateTime.now();
        String status = "ACTIVE";
        String processId = "testProcessId";
        String processInstanceId = "testProcessInstanceId";
        String rootProcessId = "testRootProcessId";
        String rootProcessInstanceId = "testRootProcessInstanceId";
        Integer priority = 79;
        String callbackEndpoint = "testCallbackEndpoint";
        Long repeatInterval = 70L;
        Integer repeatLimit = 89;
        String scheduledId = "testScheduleId";
        Integer retries = 25;
        Integer executionCounter = 17;

        job = new Job();
        job.setId(testId);
        job.setStatus(status);
        job.setLastUpdate(time);
        job.setProcessId(processId);
        job.setProcessInstanceId(processInstanceId);
        job.setRootProcessId(rootProcessId);
        job.setRootProcessInstanceId(rootProcessInstanceId);
        job.setExpirationTime(time);
        job.setPriority(priority);
        job.setCallbackEndpoint(callbackEndpoint);
        job.setRepeatInterval(repeatInterval);
        job.setRepeatLimit(repeatLimit);
        job.setScheduledId(scheduledId);
        job.setRetries(retries);
        job.setExecutionCounter(executionCounter);

        jobEntity = new JobEntity();
        jobEntity.setId(testId);
        jobEntity.setStatus(status);
        jobEntity.setLastUpdate(zonedDateTimeToInstant(time));
        jobEntity.setProcessId(processId);
        jobEntity.setProcessInstanceId(processInstanceId);
        jobEntity.setRootProcessId(rootProcessId);
        jobEntity.setRootProcessInstanceId(rootProcessInstanceId);
        jobEntity.setExpirationTime(zonedDateTimeToInstant(time));
        jobEntity.setPriority(priority);
        jobEntity.setCallbackEndpoint(callbackEndpoint);
        jobEntity.setRepeatInterval(repeatInterval);
        jobEntity.setRepeatLimit(repeatLimit);
        jobEntity.setScheduledId(scheduledId);
        jobEntity.setRetries(retries);
        jobEntity.setExecutionCounter(executionCounter);
    }

    @Test
    void testGetEntityClass() {
        assertEquals(JobEntity.class, jobEntityMapper.getEntityClass());
    }

    @Test
    void testMapToEntity() {
        JobEntity result = jobEntityMapper.mapToEntity(job.getId(), job);
        assertEquals(jobEntity, result);
    }

    @Test
    void testMapToModel() {
        Job result = jobEntityMapper.mapToModel(jobEntity);
        assertEquals(job, result);
    }
}