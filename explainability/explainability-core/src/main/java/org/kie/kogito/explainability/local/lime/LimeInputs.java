package org.kie.kogito.explainability.local.lime;

import java.util.List;

import org.kie.kogito.explainability.model.Feature;
import org.kie.kogito.explainability.model.Output;
import org.kie.kogito.explainability.model.PredictionInput;

public class LimeInputs {

    private final boolean classification;
    private final List<Feature> features;
    private final Output targetOutput;
    private final List<PredictionInput> perturbedInputs;
    private final List<Output> perturbedOutputs;

    public LimeInputs(boolean classification, List<Feature> features, Output targetOutput,
                      List<PredictionInput> perturbedInputs, List<Output> perturbedOutputs) {
        this.classification = classification;
        this.features = features;
        this.targetOutput = targetOutput;
        this.perturbedInputs = perturbedInputs;
        this.perturbedOutputs = perturbedOutputs;
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public List<PredictionInput> getPerturbedInputs() {
        return perturbedInputs;
    }

    public List<Output> getPerturbedOutputs() {
        return perturbedOutputs;
    }

    public Output getTargetOutput() {
        return targetOutput;
    }

    public boolean isClassification() {
        return classification;
    }
}
