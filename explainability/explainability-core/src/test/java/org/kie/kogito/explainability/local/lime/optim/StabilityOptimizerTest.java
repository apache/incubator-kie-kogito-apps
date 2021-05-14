package org.kie.kogito.explainability.local.lime.optim;

import org.junit.jupiter.api.Test;
import org.kie.kogito.explainability.TestUtils;
import org.kie.kogito.explainability.model.DataDistribution;
import org.kie.kogito.explainability.model.Prediction;
import org.kie.kogito.explainability.model.PredictionInput;
import org.kie.kogito.explainability.model.PredictionOutput;
import org.kie.kogito.explainability.model.PredictionProvider;
import org.kie.kogito.explainability.utils.DataUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class StabilityOptimizerTest {

    @Test
    void test() throws Exception {
        PredictionProvider model = TestUtils.getSumSkipModel(1);
        DataDistribution dataDistribution = DataUtils.generateRandomDataDistribution(5, 100, new Random());
        List<PredictionInput> samples = dataDistribution.sample(10);
        List<PredictionOutput> predictionOutputs = model.predictAsync(samples).get();
        List<Prediction> predictions = DataUtils.getPredictions(samples, predictionOutputs);
        LimeStabilitySolution optimize = StabilityOptimizer.optimize(predictions, model);
        System.err.println(optimize.getScore());
        System.err.println(optimize.getEntities());
    }

}