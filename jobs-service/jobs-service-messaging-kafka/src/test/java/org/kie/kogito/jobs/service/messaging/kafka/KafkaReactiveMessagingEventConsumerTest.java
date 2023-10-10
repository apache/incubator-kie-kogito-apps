package org.kie.kogito.jobs.service.messaging.kafka;

import org.kie.kogito.jobs.service.messaging.ReactiveMessagingEventConsumerTest;
import org.kie.kogito.jobs.service.repository.ReactiveJobRepository;
import org.kie.kogito.jobs.service.scheduler.impl.TimerDelegateJobScheduler;

import com.fasterxml.jackson.databind.ObjectMapper;

class KafkaReactiveMessagingEventConsumerTest extends ReactiveMessagingEventConsumerTest<KafkaReactiveMessagingEventConsumer> {

    @Override
    protected KafkaReactiveMessagingEventConsumer createEventConsumer(TimerDelegateJobScheduler scheduler,
            ReactiveJobRepository jobRepository,
            ObjectMapper objectMapper) {
        return new KafkaReactiveMessagingEventConsumer(scheduler, jobRepository, objectMapper);
    }
}
