package org.kie.kogito.trusty.storage.infinispan;

import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.kie.kogito.trusty.storage.api.model.decision.DMNModelWithMetadata;

import com.fasterxml.jackson.databind.ObjectMapper;

public class DMNModelWithMetadataMarshallerTest extends MarshallerTestTemplate {

    @Test
    public void testWriteAndRead() throws IOException {

        DMNModelWithMetadata dmnModelWithMetadata = new DMNModelWithMetadata("groupId", "artifactId", "version",
                "dmnVersion", "name", "namespace",
                "XML_MODEL");
        DMNModelWithMetadataMarshaller marshaller = new DMNModelWithMetadataMarshaller(new ObjectMapper());

        marshaller.writeTo(writer, dmnModelWithMetadata);
        DMNModelWithMetadata retrieved = marshaller.readFrom(reader);

        Assertions.assertEquals(dmnModelWithMetadata.getGroupId(), retrieved.getGroupId());
        Assertions.assertEquals(dmnModelWithMetadata.getArtifactId(), retrieved.getArtifactId());
        Assertions.assertEquals(dmnModelWithMetadata.getModelVersion(), retrieved.getModelVersion());
        Assertions.assertEquals(dmnModelWithMetadata.getDmnVersion(), retrieved.getDmnVersion());
        Assertions.assertEquals(dmnModelWithMetadata.getModelMetaData().getName(), retrieved.getName());
        Assertions.assertEquals(dmnModelWithMetadata.getModelMetaData().getNamespace(), retrieved.getNamespace());
        Assertions.assertEquals(dmnModelWithMetadata.getModel(), retrieved.getModel());
    }
}
