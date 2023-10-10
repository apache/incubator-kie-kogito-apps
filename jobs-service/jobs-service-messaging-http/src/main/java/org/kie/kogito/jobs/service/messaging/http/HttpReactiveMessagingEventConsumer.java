package org.kie.kogito.jobs.service.messaging.http;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.reactive.messaging.Acknowledgment;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.kie.kogito.jobs.service.messaging.MessagingConsumer;
import org.kie.kogito.jobs.service.repository.ReactiveJobRepository;
import org.kie.kogito.jobs.service.scheduler.impl.TimerDelegateJobScheduler;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.cloudevents.CloudEvent;
import io.smallrye.mutiny.Uni;

@ApplicationScoped
public class HttpReactiveMessagingEventConsumer extends MessagingConsumer {

    private static final String KOGITO_JOB_SERVICE_JOB_REQUEST_EVENTS_HTTP = "kogito-job-service-job-request-events-http";

    @Inject
    public HttpReactiveMessagingEventConsumer(TimerDelegateJobScheduler scheduler,
            ReactiveJobRepository jobRepository,
            ObjectMapper objectMapper) {
        super(scheduler, jobRepository, objectMapper);
    }

    @Incoming(KOGITO_JOB_SERVICE_JOB_REQUEST_EVENTS_HTTP)
    @Acknowledgment(Acknowledgment.Strategy.MANUAL)
    @Retry(delay = 500, maxRetries = 4)
    @Override
    public Uni<Void> onKogitoServiceRequest(Message<CloudEvent> message) {
        return super.onKogitoServiceRequest(message);
    }

}
