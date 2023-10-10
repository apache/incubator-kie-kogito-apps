package org.kie.kogito.trusty.storage.infinispan.testfield;

import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.infinispan.protostream.MessageMarshaller;

import static org.mockito.ArgumentMatchers.eq;

public class DoubleTestField<M> extends AbstractTestField<M, Double> {

    public DoubleTestField(String fieldName, Double fieldValue, Function<M, Double> getter, BiConsumer<M, Double> setter) {
        super(fieldName, fieldValue, getter, setter);
    }

    @Override
    protected Double callMockReaderMethod(MessageMarshaller.ProtoStreamReader mock) throws IOException {
        return mock.readDouble(eq(fieldName));
    }

    @Override
    protected void callVerifyWriterMethod(MessageMarshaller.ProtoStreamWriter mock) throws IOException {
        mock.writeDouble(eq(fieldName), eq(fieldValue));
    }
}
