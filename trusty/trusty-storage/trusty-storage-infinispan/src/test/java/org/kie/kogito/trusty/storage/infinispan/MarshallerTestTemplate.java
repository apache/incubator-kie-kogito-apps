package org.kie.kogito.trusty.storage.infinispan;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.infinispan.protostream.MessageMarshaller;
import org.junit.jupiter.api.BeforeEach;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

public abstract class MarshallerTestTemplate {

    protected MessageMarshaller.ProtoStreamWriter writer;
    protected MessageMarshaller.ProtoStreamReader reader;
    protected Map<String, String> stringStorage;
    protected Map<String, Long> longStorage;

    @BeforeEach
    protected void setup() throws IOException {
        this.stringStorage = new HashMap<>();
        this.longStorage = new HashMap<>();
        this.writer = mock(MessageMarshaller.ProtoStreamWriter.class);
        this.reader = mock(MessageMarshaller.ProtoStreamReader.class);
        doAnswer(invocationOnMock -> stringStorage.put(invocationOnMock.getArgument(0), invocationOnMock.getArgument(1)))
                .when(writer)
                .writeString(any(String.class), any(String.class));
        doAnswer(invocationOnMock -> stringStorage.get(invocationOnMock.getArgument(0)))
                .when(reader)
                .readString(any(String.class));

        doAnswer(invocationOnMock -> longStorage.put(invocationOnMock.getArgument(0), invocationOnMock.getArgument(1)))
                .when(writer)
                .writeLong(any(String.class), any(Long.class));
        doAnswer(invocationOnMock -> longStorage.get(invocationOnMock.getArgument(0)))
                .when(reader)
                .readLong(any(String.class));
    }
}
