package org.kie.kogito.trusty.storage.infinispan.testfield;

import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.infinispan.protostream.MessageMarshaller;

import static org.mockito.ArgumentMatchers.eq;

public class ObjectTestField<M, T> extends AbstractTestField<M, T> {

    private final Class<T> fieldClass;

    public ObjectTestField(String fieldName, T fieldValue, Function<M, T> getter, BiConsumer<M, T> setter, Class<T> fieldClass) {
        super(fieldName, fieldValue, getter, setter);
        this.fieldClass = fieldClass;
    }

    @Override
    protected T callMockReaderMethod(MessageMarshaller.ProtoStreamReader mock) throws IOException {
        return mock.readObject(eq(fieldName), eq(fieldClass));
    }

    @Override
    protected void callVerifyWriterMethod(MessageMarshaller.ProtoStreamWriter mock) throws IOException {
        mock.writeObject(eq(fieldName), eq(fieldValue), eq(fieldClass));
    }
}
