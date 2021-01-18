package org.kie.kogito.explainability.local.counterfactual;

import org.kie.kogito.explainability.local.counterfactual.entities.CounterfactualEntity;
import org.kie.kogito.explainability.model.PredictionOutput;

import java.util.List;

/**
 * Represents the result of a counterfactual search.
 * Entities represent the counterfactual features and the {@link org.kie.kogito.explainability.model.PredictionOutput}
 * contains the prediction result for the counterfactual, including the prediction score, if available.
 */
public class Counterfactual {

    public List<CounterfactualEntity> getEntities() {
        return entities;
    }

    private List<CounterfactualEntity> entities;

    public List<PredictionOutput> getOutput() {
        return output;
    }

    private List<PredictionOutput> output;

    public Counterfactual(List<CounterfactualEntity> entities, List<PredictionOutput> output) {
        this.entities = entities;
        this.output = output;
    }

}
