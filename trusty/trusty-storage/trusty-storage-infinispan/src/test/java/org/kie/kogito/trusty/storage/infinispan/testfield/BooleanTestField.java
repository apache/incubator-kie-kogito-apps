package org.kie.kogito.trusty.storage.infinispan.testfield;

import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.infinispan.protostream.MessageMarshaller;

import static org.mockito.ArgumentMatchers.eq;

public class BooleanTestField<M> extends AbstractTestField<M, Boolean> {

    public BooleanTestField(String fieldName, Boolean fieldValue, Function<M, Boolean> getter, BiConsumer<M, Boolean> setter) {
        super(fieldName, fieldValue, getter, setter);
    }

    @Override
    protected Boolean callMockReaderMethod(MessageMarshaller.ProtoStreamReader mock) throws IOException {
        return mock.readBoolean(eq(fieldName));
    }

    @Override
    protected void callVerifyWriterMethod(MessageMarshaller.ProtoStreamWriter mock) throws IOException {
        mock.writeBoolean(eq(fieldName), eq(fieldValue));
    }
}
