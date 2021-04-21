package org.kie.kogito.explainability.model;

public abstract class BasePrediction implements Prediction {
    private final PredictionInput input;

    private final PredictionOutput output;

    public BasePrediction(PredictionInput input, PredictionOutput output) {
        this.input = input;
        this.output = output;
    }

    @Override
    public PredictionInput getInput() {
        return input;
    }

    @Override
    public PredictionOutput getOutput() {
        return output;
    }

}
