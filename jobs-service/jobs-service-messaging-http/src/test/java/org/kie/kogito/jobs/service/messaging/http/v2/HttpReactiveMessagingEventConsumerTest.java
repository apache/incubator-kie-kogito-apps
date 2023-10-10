package org.kie.kogito.jobs.service.messaging.http.v2;

import org.kie.kogito.jobs.service.messaging.v2.MessagingEventConsumerTest;
import org.kie.kogito.jobs.service.repository.ReactiveJobRepository;
import org.kie.kogito.jobs.service.scheduler.impl.TimerDelegateJobScheduler;

import com.fasterxml.jackson.databind.ObjectMapper;

class HttpReactiveMessagingEventConsumerTest extends MessagingEventConsumerTest<HttpReactiveMessagingEventConsumer> {

    @Override
    protected HttpReactiveMessagingEventConsumer createEventConsumer(TimerDelegateJobScheduler scheduler,
            ReactiveJobRepository jobRepository,
            ObjectMapper objectMapper) {
        return new HttpReactiveMessagingEventConsumer(scheduler, jobRepository, objectMapper);
    }
}
