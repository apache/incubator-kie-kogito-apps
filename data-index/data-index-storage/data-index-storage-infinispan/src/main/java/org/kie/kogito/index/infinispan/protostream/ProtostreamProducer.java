package org.kie.kogito.index.infinispan.protostream;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.infinispan.protostream.MessageMarshaller;

import com.fasterxml.jackson.databind.ObjectMapper;

@ApplicationScoped
public class ProtostreamProducer {

    @Inject
    ObjectMapper mapper;

    @Produces
    MessageMarshaller userTaskInstanceMarshaller() {
        return new UserTaskInstanceMarshaller(mapper);
    }

    @Produces
    MessageMarshaller processInstanceMarshaller() {
        return new ProcessInstanceMarshaller(mapper);
    }

    @Produces
    MessageMarshaller processDefinitionMarshaller() {
        return new ProcessDefinitionMarshaller(mapper);
    }

    @Produces
    MessageMarshaller nodeMarshaller() {
        return new NodeMarshaller(mapper);
    }

    @Produces
    MessageMarshaller nodeMetadataMarshaller() {
        return new NodeMetadataMarshaller(mapper);
    }

    @Produces
    MessageMarshaller nodeInstanceMarshaller() {
        return new NodeInstanceMarshaller(mapper);
    }

    @Produces
    MessageMarshaller jobMarshaller() {
        return new JobMarshaller(mapper);
    }

    @Produces
    MessageMarshaller processInstanceErrorMarshaller() {
        return new ProcessInstanceErrorMarshaller(mapper);
    }

    @Produces
    MessageMarshaller milestoneMarshaller() {
        return new MilestoneMarshaller(mapper);
    }

    @Produces
    MessageMarshaller commentMarshaller() {
        return new CommentMarshaller(mapper);
    }

    @Produces
    MessageMarshaller attachmentMarshaller() {
        return new AttachmentMarshaller(mapper);
    }
}
