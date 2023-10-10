package org.kie.kogito.trusty.storage.infinispan.testfield;

import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.infinispan.protostream.MessageMarshaller;

import static org.mockito.ArgumentMatchers.eq;

public class LongTestField<M> extends AbstractTestField<M, Long> {

    public LongTestField(String fieldName, Long fieldValue, Function<M, Long> getter, BiConsumer<M, Long> setter) {
        super(fieldName, fieldValue, getter, setter);
    }

    @Override
    protected Long callMockReaderMethod(MessageMarshaller.ProtoStreamReader mock) throws IOException {
        return mock.readLong(eq(fieldName));
    }

    @Override
    protected void callVerifyWriterMethod(MessageMarshaller.ProtoStreamWriter mock) throws IOException {
        mock.writeLong(eq(fieldName), eq(fieldValue));
    }
}
