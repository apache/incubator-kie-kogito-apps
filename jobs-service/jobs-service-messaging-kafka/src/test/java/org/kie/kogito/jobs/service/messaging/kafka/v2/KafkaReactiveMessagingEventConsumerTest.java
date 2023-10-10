package org.kie.kogito.jobs.service.messaging.kafka.v2;

import org.kie.kogito.jobs.service.messaging.v2.MessagingEventConsumerTest;
import org.kie.kogito.jobs.service.repository.ReactiveJobRepository;
import org.kie.kogito.jobs.service.scheduler.impl.TimerDelegateJobScheduler;

import com.fasterxml.jackson.databind.ObjectMapper;

class KafkaReactiveMessagingEventConsumerTest extends MessagingEventConsumerTest<KafkaReactiveMessagingEventConsumer> {

    @Override
    protected KafkaReactiveMessagingEventConsumer createEventConsumer(TimerDelegateJobScheduler scheduler,
            ReactiveJobRepository jobRepository,
            ObjectMapper objectMapper) {
        return new KafkaReactiveMessagingEventConsumer(scheduler, jobRepository, objectMapper);
    }
}
