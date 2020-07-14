package org.kie.kogito.explainability.local;

import org.kie.kogito.explainability.model.Model;
import org.kie.kogito.explainability.model.Prediction;

public interface LocalExplainer<T> {

    T explain(Prediction prediction, Model model);
}
