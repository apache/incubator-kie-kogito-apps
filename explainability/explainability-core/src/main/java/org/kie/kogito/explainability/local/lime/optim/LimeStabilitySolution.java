package org.kie.kogito.explainability.local.lime.optim;

import org.kie.kogito.explainability.model.Prediction;
import org.kie.kogito.explainability.model.PredictionProvider;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.score.buildin.simple.SimpleScore;
import org.optaplanner.core.api.score.buildin.simplebigdecimal.SimpleBigDecimalScore;

import java.util.List;

@PlanningSolution
public class LimeStabilitySolution {

    private List<Prediction> predictions;

    @PlanningEntityCollectionProperty
    private List<NumericLimeConfigEntity> entities;

    private PredictionProvider model;

    @PlanningScore
    private SimpleBigDecimalScore score;

    public LimeStabilitySolution() {
    }

    public LimeStabilitySolution(List<Prediction> predictions, List<NumericLimeConfigEntity> entities, PredictionProvider model) {
        this.predictions = predictions;
        this.entities = entities;
        this.model = model;
    }

    public List<NumericLimeConfigEntity> getEntities() {
        return entities;
    }

    public PredictionProvider getModel() {
        return model;
    }

    public List<Prediction> getPredictions() {
        return predictions;
    }

    public SimpleBigDecimalScore getScore() {
        return score;
    }

    public void setScore(SimpleBigDecimalScore score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "LimeStabilitySolution{" +
                ", entities=" + entities +
                ", score=" + score +
                '}';
    }
}
