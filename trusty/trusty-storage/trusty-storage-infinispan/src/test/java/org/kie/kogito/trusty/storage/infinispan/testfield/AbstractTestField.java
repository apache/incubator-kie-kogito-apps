package org.kie.kogito.trusty.storage.infinispan.testfield;

import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.infinispan.protostream.MessageMarshaller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public abstract class AbstractTestField<M, T> {

    protected final String fieldName;
    protected final T fieldValue;
    private final Function<M, T> getter;
    private final BiConsumer<M, T> setter;

    public AbstractTestField(String fieldName, T fieldValue, Function<M, T> getter, BiConsumer<M, T> setter) {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.getter = getter;
        this.setter = setter;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setValue(M object) {
        setter.accept(object, fieldValue);
    }

    public void assertValue(M object) {
        assertEquals(fieldValue, getter.apply(object));
    }

    public void mockReader(MessageMarshaller.ProtoStreamReader mock) throws IOException {
        when(callMockReaderMethod(mock)).thenReturn(fieldValue);
    }

    protected abstract T callMockReaderMethod(MessageMarshaller.ProtoStreamReader mock) throws IOException;

    public void verifyWriter(MessageMarshaller.ProtoStreamWriter mock) throws IOException {
        callVerifyWriterMethod(verify(mock));
    }

    protected abstract void callVerifyWriterMethod(MessageMarshaller.ProtoStreamWriter mock) throws IOException;
}
