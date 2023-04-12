/*
 * Copyright 2023 Red Hat, Inc. and/or its affiliates.
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

package org.kie.kogito.addons.quarkus.jobs.service.embedded.stream;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.messaging.Acknowledgment;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.kie.kogito.event.AbstractDataEvent;
import org.kie.kogito.event.EventPublisher;
import org.kie.kogito.jobs.JobsServiceException;
import org.kie.kogito.jobs.service.adapter.ScheduledJobAdapter;
import org.kie.kogito.jobs.service.model.JobDetails;
import org.kie.kogito.jobs.service.model.ScheduledJob;
import org.kie.kogito.jobs.service.resource.RestApiConstants;
import org.kie.kogito.jobs.service.stream.AvailableStreams;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.kie.kogito.jobs.service.events.JobDataEvent.JOB_EVENT_TYPE;

/**
 * This class is intended to propagate the job status change events to the embedded data index by using the
 * EventPublisher API. Events propagation is enabled only when the embedded data index is present in current application.
 */
@ApplicationScoped
public class EventPublisherJobStreams {

    public static final String DATA_INDEX_EVENT_PUBLISHER = "org.kie.kogito.index.addon.DataIndexEventPublisher";

    private final String url;

    private final EventPublisher eventPublisher;

    private final ObjectMapper objectMapper;

    @Inject
    public EventPublisherJobStreams(@ConfigProperty(name = "kogito.service.url", defaultValue = "http://localhost:8080") String url,
            Instance<EventPublisher> eventPublishers,
            ObjectMapper objectMapper) {
        this.url = url;
        eventPublisher = eventPublishers.stream()
                .filter(publisher -> publisher.getClass().getName().startsWith(DATA_INDEX_EVENT_PUBLISHER))
                .findFirst()
                .orElse(null);
        this.objectMapper = objectMapper;
    }

    @Incoming(AvailableStreams.JOB_STATUS_CHANGE_EVENTS)
    @Acknowledgment(Acknowledgment.Strategy.PRE_PROCESSING)
    public void onJobStatusChange(JobDetails jobDetails) {
        if (eventPublisher != null) {
            ScheduledJob scheduledJob = ScheduledJobAdapter.of(jobDetails);
            byte[] jsonContent;
            try {
                jsonContent = objectMapper.writeValueAsBytes(scheduledJob);
            } catch (Exception e) {
                throw new JobsServiceException("It was not possible to serialize scheduledJob to json: " + scheduledJob, e);
            }
            EventPublisherJobDataEvent event = new EventPublisherJobDataEvent(JOB_EVENT_TYPE,
                    url + RestApiConstants.JOBS_PATH,
                    jsonContent,
                    scheduledJob.getProcessInstanceId(),
                    scheduledJob.getRootProcessInstanceId(),
                    scheduledJob.getProcessId(),
                    scheduledJob.getRootProcessId());
            eventPublisher.publish(event);
        }
    }

    public static class EventPublisherJobDataEvent extends AbstractDataEvent<byte[]> {
        public EventPublisherJobDataEvent(String type,
                String source,
                byte[] data,
                String kogitoProcessInstanceId,
                String kogitoRootProcessInstanceId,
                String kogitoProcessId,
                String kogitoRootProcessId) {
            super(type, source, data, kogitoProcessInstanceId, kogitoRootProcessInstanceId, kogitoProcessId,
                    kogitoRootProcessId, null);
        }
    }
}
