package org.kie.kogito.trusty.storage.infinispan;

import org.infinispan.protostream.MessageMarshaller;
import org.kie.kogito.persistence.infinispan.protostream.AbstractMarshaller;

import com.fasterxml.jackson.databind.ObjectMapper;

abstract class AbstractModelMarshaller<T> extends AbstractMarshaller implements MessageMarshaller<T> {

    private final Class<? extends T> javaClass;

    protected AbstractModelMarshaller(ObjectMapper mapper, Class<? extends T> javaClass) {
        super(mapper);
        this.javaClass = javaClass;
    }

    @Override
    public Class<? extends T> getJavaClass() {
        return javaClass;
    }

    @Override
    public String getTypeName() {
        return getJavaClass().getTypeName();
    }
}
