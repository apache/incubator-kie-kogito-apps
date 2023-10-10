package org.kie.kogito.trusty.storage.infinispan;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.kie.kogito.explainability.api.ExplainabilityStatus;
import org.kie.kogito.explainability.api.FeatureImportanceModel;
import org.kie.kogito.explainability.api.LIMEExplainabilityResult;
import org.kie.kogito.explainability.api.SaliencyModel;

import com.fasterxml.jackson.databind.ObjectMapper;

public class LIMEExplainabilityResultMarshallerTest extends MarshallerTestTemplate {

    @Test
    public void testWriteAndRead() throws IOException {

        List<FeatureImportanceModel> featureImportanceModels = Collections.singletonList(new FeatureImportanceModel("aFeature", 0d));
        List<SaliencyModel> saliencyModels = Collections.singletonList(new SaliencyModel("outcomeName", featureImportanceModels));
        LIMEExplainabilityResult limeExplainabilityResult = new LIMEExplainabilityResult("executionId", ExplainabilityStatus.SUCCEEDED, "statusDetail", saliencyModels);
        LIMEExplainabilityResultMarshaller marshaller = new LIMEExplainabilityResultMarshaller(new ObjectMapper());

        marshaller.writeTo(writer, limeExplainabilityResult);
        LIMEExplainabilityResult retrieved = marshaller.readFrom(reader);

        Assertions.assertEquals(limeExplainabilityResult.getExecutionId(), retrieved.getExecutionId());
        Assertions.assertEquals(limeExplainabilityResult.getStatus(), retrieved.getStatus());
        Assertions.assertEquals(limeExplainabilityResult.getStatusDetails(), retrieved.getStatusDetails());
        Assertions.assertEquals(saliencyModels.get(0).getOutcomeName(), retrieved.getSaliencies().get(0).getOutcomeName());
        Assertions.assertEquals(featureImportanceModels.get(0).getFeatureName(), retrieved.getSaliencies().get(0).getFeatureImportance().get(0).getFeatureName());
        Assertions.assertEquals(featureImportanceModels.get(0).getFeatureScore(), retrieved.getSaliencies().get(0).getFeatureImportance().get(0).getFeatureScore());
    }
}
