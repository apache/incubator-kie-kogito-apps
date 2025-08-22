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
package org.kie.kogito.app.jobs.jpa;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import org.kie.kogito.app.jobs.impl.InVMRecipient;
import org.kie.kogito.app.jobs.jpa.model.JobDetailsEntity;
import org.kie.kogito.jackson.utils.ObjectMapperFactory;
import org.kie.kogito.jobs.DurationExpirationTime;
import org.kie.kogito.jobs.ExactExpirationTime;
import org.kie.kogito.jobs.JobDescription;
import org.kie.kogito.jobs.service.json.DurationExpirationTimeDeserializer;
import org.kie.kogito.jobs.service.json.DurationExpirationTimeSerializer;
import org.kie.kogito.jobs.service.json.ExactExpirationTimeDeserializer;
import org.kie.kogito.jobs.service.json.ExactExpirationTimeSerializer;
import org.kie.kogito.jobs.service.json.JobDescriptionDeserializer;
import org.kie.kogito.jobs.service.json.JobDescriptionSerializer;
import org.kie.kogito.jobs.service.model.JobDetails;
import org.kie.kogito.jobs.service.model.JobStatus;
import org.kie.kogito.jobs.service.model.RecipientInstance;
import org.kie.kogito.jobs.service.utils.DateUtil;
import org.kie.kogito.timer.Trigger;
import org.kie.kogito.timer.impl.SimpleTimerTrigger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;

import static java.time.OffsetDateTime.now;

public class JobDetailsEntityHelper {
    public static final ZoneId DEFAULT_ZONE = ZoneId.of("UTC");
    private static ObjectMapper OBJECT_MAPPER;

    private static ObjectMapper getObjectMapperInstance() {
        if (OBJECT_MAPPER == null) {
            OBJECT_MAPPER = ObjectMapperFactory.get().copy();
            OBJECT_MAPPER.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            SimpleModule kogitoCustomModule = new SimpleModule();
            kogitoCustomModule.addSerializer(JobDescription.class, new JobDescriptionSerializer());
            kogitoCustomModule.addDeserializer(JobDescription.class, new JobDescriptionDeserializer());
            kogitoCustomModule.addSerializer(DurationExpirationTime.class, new DurationExpirationTimeSerializer());
            kogitoCustomModule.addDeserializer(DurationExpirationTime.class, new DurationExpirationTimeDeserializer());
            kogitoCustomModule.addSerializer(ExactExpirationTime.class, new ExactExpirationTimeSerializer());
            kogitoCustomModule.addDeserializer(ExactExpirationTime.class, new ExactExpirationTimeDeserializer());
            OBJECT_MAPPER.registerModule(kogitoCustomModule);
        }
        return OBJECT_MAPPER;
    }

    public static JobDetailsEntity merge(JobDetails job, JobDetailsEntity instance) {
        if (Objects.isNull(instance)) {
            instance = new JobDetailsEntity();
        }

        OffsetDateTime lastUpdate = now().truncatedTo(ChronoUnit.MILLIS);

        instance.setId(job.getId());
        instance.setCorrelationId(job.getCorrelationId());
        instance.setStatus(mapOptionalValue(job.getStatus(), Enum::name));

        instance.setRetries(job.getRetries());
        instance.setExecutionCounter(job.getExecutionCounter());
        instance.setScheduledId(job.getScheduledId());
        instance.setPriority(job.getPriority());

        ObjectNode recipient = getObjectMapperInstance().convertValue(job.getRecipient().getRecipient(), ObjectNode.class);
        recipient.put("classType", "org.kie.kogito.jobs.embedded.InVMRecipient");
        instance.setRecipient(recipient);

        ObjectNode trigger = getObjectMapperInstance().convertValue(job.getTrigger(), ObjectNode.class);
        trigger.remove("indefinitely");
        trigger.put("classType", job.getTrigger().getClass().getName());
        instance.setTrigger(trigger);

        instance.setFireTime(mapOptionalValue(job.getTrigger().hasNextFireTime(), DateUtil::dateToOffsetDateTime));

        instance.setExecutionTimeout(job.getExecutionTimeout());
        instance.setExecutionTimeoutUnit(mapOptionalValue(job.getExecutionTimeoutUnit(), Enum::name));

        instance.setCreated(Optional.ofNullable(job.getCreated()).map(ZonedDateTime::toOffsetDateTime).orElse(lastUpdate));
        instance.setLastUpdate(lastUpdate);
        return instance;
    }

    public static JobDetails from(JobDetailsEntity instance) {
        if (instance == null) {
            return null;
        }

        instance.getRecipient().remove("classType");
        RecipientInstance recipient = new RecipientInstance(getObjectMapperInstance().convertValue(instance.getRecipient(), InVMRecipient.class));

        instance.getTrigger().remove("classType");
        Trigger trigger = getObjectMapperInstance().convertValue(instance.getTrigger(), SimpleTimerTrigger.class);

        return JobDetails.builder()
                .id(instance.getId())
                .correlationId(instance.getCorrelationId())
                .status(mapOptionalValue(instance.getStatus(), JobStatus::valueOf))
                .retries(instance.getRetries())
                .executionCounter(instance.getExecutionCounter())
                .scheduledId(instance.getScheduledId())
                .priority(instance.getPriority()).recipient(recipient)
                .trigger(trigger)
                .executionTimeout(instance.getExecutionTimeout())
                .executionTimeoutUnit(mapOptionalValue(instance.getExecutionTimeoutUnit(), ChronoUnit::valueOf))
                .created(instance.getCreated().atZoneSameInstant(DEFAULT_ZONE))
                .lastUpdate(instance.getLastUpdate().atZoneSameInstant(DEFAULT_ZONE))
                .build();
    }

    private static <T, R> R mapOptionalValue(T object, Function<T, R> mapper) {
        return Optional.ofNullable(object).map(mapper).orElse(null);
    }
}
