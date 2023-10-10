package org.kie.kogito.trusty.storage.infinispan;

import java.io.IOException;

import org.kie.kogito.trusty.storage.api.model.decision.DMNModelWithMetadata;

import com.fasterxml.jackson.databind.ObjectMapper;

public class DMNModelWithMetadataMarshaller extends AbstractModelMarshaller<DMNModelWithMetadata> {

    public DMNModelWithMetadataMarshaller(ObjectMapper mapper) {
        super(mapper, DMNModelWithMetadata.class);
    }

    @Override
    public DMNModelWithMetadata readFrom(ProtoStreamReader reader) throws IOException {
        return mapper.readValue(reader.readString(Constants.RAW_OBJECT_FIELD), DMNModelWithMetadata.class);
    }

    @Override
    public void writeTo(ProtoStreamWriter writer, DMNModelWithMetadata dmnModelWithMetadata) throws IOException {
        writer.writeString(Constants.RAW_OBJECT_FIELD, mapper.writeValueAsString(dmnModelWithMetadata));
    }
}
