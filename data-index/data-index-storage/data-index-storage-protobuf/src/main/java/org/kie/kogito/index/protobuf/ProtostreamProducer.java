package org.kie.kogito.index.protobuf;

import java.io.IOException;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.infinispan.protostream.FileDescriptorSource;

@ApplicationScoped
public class ProtostreamProducer {

    static final String KOGITO_INDEX_PROTO = "kogito-index.proto";
    static final String KOGITO_TYPES_PROTO = "kogito-types.proto";

    @Produces
    FileDescriptorSource kogitoTypesDescriptor() throws IOException {
        FileDescriptorSource source = new FileDescriptorSource();
        source.addProtoFile(KOGITO_INDEX_PROTO, Thread.currentThread().getContextClassLoader().getResourceAsStream("META-INF/kogito-index.proto"));
        source.addProtoFile(KOGITO_TYPES_PROTO, Thread.currentThread().getContextClassLoader().getResourceAsStream("META-INF/kogito-types.proto"));
        return source;
    }
}
