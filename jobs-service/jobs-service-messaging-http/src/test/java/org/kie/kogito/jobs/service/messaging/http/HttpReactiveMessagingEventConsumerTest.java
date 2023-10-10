package org.kie.kogito.jobs.service.messaging.http;

import org.kie.kogito.jobs.service.messaging.ReactiveMessagingEventConsumerTest;
import org.kie.kogito.jobs.service.repository.ReactiveJobRepository;
import org.kie.kogito.jobs.service.scheduler.impl.TimerDelegateJobScheduler;

import com.fasterxml.jackson.databind.ObjectMapper;

class HttpReactiveMessagingEventConsumerTest extends ReactiveMessagingEventConsumerTest<HttpReactiveMessagingEventConsumer> {

    @Override
    protected HttpReactiveMessagingEventConsumer createEventConsumer(TimerDelegateJobScheduler scheduler,
            ReactiveJobRepository jobRepository,
            ObjectMapper objectMapper) {
        return new HttpReactiveMessagingEventConsumer(scheduler, jobRepository, objectMapper);
    }
}
