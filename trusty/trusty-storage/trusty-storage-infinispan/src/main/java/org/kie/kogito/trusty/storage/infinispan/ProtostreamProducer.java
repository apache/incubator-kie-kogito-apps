package org.kie.kogito.trusty.storage.infinispan;

import java.io.IOException;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.infinispan.protostream.FileDescriptorSource;

import com.fasterxml.jackson.databind.ObjectMapper;

@ApplicationScoped
public class ProtostreamProducer {

    @Inject
    ObjectMapper mapper;

    @Produces
    FileDescriptorSource kogitoTypesDescriptor() throws IOException {
        FileDescriptorSource source = new FileDescriptorSource();
        source.addProtoFile("decision.proto", Thread.currentThread().getContextClassLoader().getResourceAsStream("META-INF/decision.proto"));
        source.addProtoFile("explanation.proto", Thread.currentThread().getContextClassLoader().getResourceAsStream("META-INF/explanation.proto"));
        return source;
    }

    @Produces
    org.infinispan.protostream.MessageMarshaller decisionMarshaller() {
        return new DecisionMarshaller(mapper);
    }

    @Produces
    org.infinispan.protostream.MessageMarshaller explainabilityResultMarshaller() {
        return new LIMEExplainabilityResultMarshaller(mapper);
    }

    @Produces
    org.infinispan.protostream.MessageMarshaller dmnModelMarshaller() {
        return new DMNModelWithMetadataMarshaller(mapper);
    }

    @Produces
    org.infinispan.protostream.MessageMarshaller counterfactualMarshaller() {
        return new CounterfactualExplainabilityRequestMarshaller(mapper);
    }

    @Produces
    org.infinispan.protostream.MessageMarshaller counterfactualResultMarshaller() {
        return new CounterfactualExplainabilityResultMarshaller(mapper);
    }

}
