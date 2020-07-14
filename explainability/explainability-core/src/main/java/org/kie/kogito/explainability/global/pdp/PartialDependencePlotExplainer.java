package org.kie.kogito.explainability.global.pdp;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.kie.kogito.explainability.model.DataDistribution;
import org.kie.kogito.explainability.model.FeatureDistribution;
import org.kie.kogito.explainability.model.Model;
import org.kie.kogito.explainability.model.Output;
import org.kie.kogito.explainability.model.PredictionInput;
import org.kie.kogito.explainability.model.PredictionOutput;
import org.kie.kogito.explainability.model.DataSeries;
import org.kie.kogito.explainability.utils.DataUtils;
import org.kie.kogito.explainability.global.GlobalExplainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generates the partial dependence plot for a given feature.
 * While a strict PD implementation would need the whole training set used to train the model, this implementation seeks
 * to reproduce an approximate version of the training data by means of data distribution information (min, max, mean,
 * stdDev).
 */
public class PartialDependencePlotExplainer implements GlobalExplainer<Collection<DataSeries>> {

    private static final int TABLE_SIZE = 100;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Collection<DataSeries> explain(Model model) {
        long start = System.currentTimeMillis();

        Collection<DataSeries> pdps = new LinkedList<>();
        try {
            DataDistribution dataDistribution = model.getDataDistribution();
            int noOfFeatures = model.getInputShape().getFeatures().size();

            List<FeatureDistribution> featureDistributions = dataDistribution.getFeatureDistributions();
            for (int featureIndex = 0; featureIndex < noOfFeatures; featureIndex++) {
                for (int outputIndex = 0; outputIndex < model.getOutputShape().getOutputs().size(); outputIndex++) {
                    double[] featureXSvalues = DataUtils.generateSamples(featureDistributions.get(featureIndex).getMin(),
                                                                         featureDistributions.get(featureIndex).getMax(), TABLE_SIZE);

                    double[][] trainingData = new double[noOfFeatures][TABLE_SIZE];
                    for (int i = 0; i < noOfFeatures; i++) {
                        double[] featureData = DataUtils.generateData(featureDistributions.get(i).getMean(),
                                                                      featureDistributions.get(i).getStdDev(), TABLE_SIZE);
                        trainingData[i] = featureData;
                    }

                    double[] marginalImpacts = new double[featureXSvalues.length];
                    for (int i = 0; i < featureXSvalues.length; i++) {
                        List<PredictionInput> predictionInputs = new LinkedList<>();
                        double xs = featureXSvalues[i];
                        double[] inputs = new double[noOfFeatures];
                        inputs[featureIndex] = xs;
                        for (int j = 0; j < TABLE_SIZE; j++) {
                            for (int f = 0; f < noOfFeatures; f++) {
                                if (f != featureIndex) {
                                    inputs[f] = trainingData[f][j];
                                }
                            }
                            PredictionInput input = new PredictionInput(DataUtils.doublesToFeatures(inputs));
                            predictionInputs.add(input);
                        }

                        // prediction requests are batched per value of feature 'Xs' under analysis
                        for (PredictionOutput predictionOutput : model.predict(predictionInputs)) {
                            Output output = predictionOutput.getOutputs().get(outputIndex);
                            marginalImpacts[i] += output.getScore() / (double) TABLE_SIZE;
                        }
                    }
                    DataSeries dataSeries = new DataSeries(model.getInputShape().getFeatures().get(featureIndex),
                                                           featureXSvalues, marginalImpacts);
                    pdps.add(dataSeries);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        long end = System.currentTimeMillis();
        logger.info("explanation time: {}ms", (end - start));
        return pdps;
    }
}
