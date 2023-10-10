package org.kie.kogito.trusty.storage.infinispan.testfield;

import java.io.IOException;
import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.infinispan.protostream.MessageMarshaller;

import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.eq;

public class CollectionTestField<M, T> extends AbstractTestField<M, Collection<T>> {

    private final Class<T> elementClass;

    public CollectionTestField(String fieldName, Collection<T> fieldValue, Function<M, Collection<T>> getter, BiConsumer<M, Collection<T>> setter, Class<T> elementClass) {
        super(fieldName, fieldValue, getter, setter);
        this.elementClass = elementClass;
    }

    @Override
    protected Collection<T> callMockReaderMethod(MessageMarshaller.ProtoStreamReader mock) throws IOException {
        return mock.readCollection(eq(fieldName), anyCollection(), eq(elementClass));
    }

    @Override
    protected void callVerifyWriterMethod(MessageMarshaller.ProtoStreamWriter mock) throws IOException {
        mock.writeCollection(eq(fieldName), eq(fieldValue), eq(elementClass));
    }
}
