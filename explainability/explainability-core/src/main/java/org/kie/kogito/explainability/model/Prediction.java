package org.kie.kogito.explainability.model;

public class Prediction {

    private final PredictionInput input;

    private final PredictionOutput output;

    public Prediction(PredictionInput input,
                      PredictionOutput output) {
        this.input = input;
        this.output = output;
    }

    public PredictionInput getInput() {
        return input;
    }

    public PredictionOutput getOutput() {
        return output;
    }

}
