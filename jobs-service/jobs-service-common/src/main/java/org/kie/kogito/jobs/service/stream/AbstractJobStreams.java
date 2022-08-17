/*
 * Copyright 2022 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.kogito.jobs.service.stream;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.enterprise.event.Observes;

import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.kie.kogito.jobs.service.events.JobDataEvent;
import org.kie.kogito.jobs.service.management.MessagingChangeEvent;
import org.kie.kogito.jobs.service.model.job.JobDetails;
import org.kie.kogito.jobs.service.model.job.ScheduledJobAdapter;
import org.kie.kogito.jobs.service.resource.JobResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.smallrye.reactive.messaging.providers.locals.ContextAwareMessage;

public abstract class AbstractJobStreams {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractJobStreams.class);

    private ObjectMapper objectMapper;

    private final AtomicBoolean enabledAtStartup = new AtomicBoolean();;

    private final AtomicBoolean enabledAtRuntime = new AtomicBoolean();

    private Emitter<String> emitter;

    private String url;

    protected AbstractJobStreams() {
    }

    protected AbstractJobStreams(ObjectMapper objectMapper, boolean enabled, Emitter<String> emitter, String url) {
        this.objectMapper = objectMapper;
        enabledAtStartup.set(enabled);
        this.emitter = emitter;
        this.url = url;
    }

    protected void jobStatusChange(JobDetails job) {
        if (enabledAtRuntime.get() && enabledAtStartup.get()) {
            try {
                JobDataEvent event = JobDataEvent
                        .builder()
                        .source(url + JobResource.JOBS_PATH)
                        .data(ScheduledJobAdapter.of(job))
                        .build();
                String json = objectMapper.writeValueAsString(event);
                emitter.send(decorate(ContextAwareMessage.of(json)
                        .withAck(() -> onAck(job))
                        .withNack(reason -> onNack(reason, job))));
                LOGGER.info("Sent event {} to kakfa", json);
            } catch (Exception e) {
                String msg = String.format("An unexpected error was produced while processing a Job status change for the job: %s", job);
                LOGGER.error(msg, e);
            }
        }
    }

    CompletionStage<Void> onAck(JobDetails job) {
        LOGGER.debug("Job Status change published: {}", job);
        return CompletableFuture.completedFuture(null);
    }

    CompletionStage<Void> onNack(Throwable reason, JobDetails job) {
        String msg = String.format("An error was produced while publishing a Job status change for the job: %s", job);
        LOGGER.error(msg, reason);
        return CompletableFuture.completedFuture(null);
    }

    protected Message<String> decorate(Message<String> message) {
        return message;
    }

    protected void onMessagingStatusChange(@Observes MessagingChangeEvent event) {
        this.enabledAtRuntime.set(event.isEnabled());
    }
}
