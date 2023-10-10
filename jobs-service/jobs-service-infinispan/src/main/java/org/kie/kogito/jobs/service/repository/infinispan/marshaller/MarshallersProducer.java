package org.kie.kogito.jobs.service.repository.infinispan.marshaller;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.infinispan.protostream.MessageMarshaller;
import org.kie.kogito.jobs.service.repository.marshaller.RecipientMarshaller;

@ApplicationScoped
public class MarshallersProducer {

    @Produces
    public MessageMarshaller jobDetailsMarshaller(RecipientMarshaller recipientMarshaller) {
        return new JobDetailsMarshaller(recipientMarshaller);
    }

    @Produces
    public MessageMarshaller triggerMarshaller() {
        return new TriggerMarshaller();
    }
}
