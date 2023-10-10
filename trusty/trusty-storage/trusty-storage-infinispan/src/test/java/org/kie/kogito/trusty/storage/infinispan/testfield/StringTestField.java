package org.kie.kogito.trusty.storage.infinispan.testfield;

import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.infinispan.protostream.MessageMarshaller;

import static org.mockito.ArgumentMatchers.eq;

public class StringTestField<M> extends AbstractTestField<M, String> {

    public StringTestField(String fieldName, String fieldValue, Function<M, String> getter, BiConsumer<M, String> setter) {
        super(fieldName, fieldValue, getter, setter);
    }

    @Override
    protected String callMockReaderMethod(MessageMarshaller.ProtoStreamReader mock) throws IOException {
        return mock.readString(eq(fieldName));
    }

    @Override
    protected void callVerifyWriterMethod(MessageMarshaller.ProtoStreamWriter mock) throws IOException {
        mock.writeString(eq(fieldName), eq(fieldValue));
    }
}
