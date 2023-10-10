package org.kie.kogito.trusty.storage.infinispan.testfield;

import java.io.IOException;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.infinispan.protostream.MessageMarshaller;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;

public class ListTestField<M, T> extends AbstractTestField<M, List<T>> {

    private final Class<T> elementClass;

    public ListTestField(String fieldName, List<T> fieldValue, Function<M, List<T>> getter, BiConsumer<M, List<T>> setter, Class<T> elementClass) {
        super(fieldName, fieldValue, getter, setter);
        this.elementClass = elementClass;
    }

    @Override
    protected List<T> callMockReaderMethod(MessageMarshaller.ProtoStreamReader mock) throws IOException {
        return mock.readCollection(eq(fieldName), anyList(), eq(elementClass));
    }

    @Override
    protected void callVerifyWriterMethod(MessageMarshaller.ProtoStreamWriter mock) throws IOException {
        mock.writeCollection(eq(fieldName), eq(fieldValue), eq(elementClass));
    }
}
