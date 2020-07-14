package org.kie.kogito.explainability.global;

import org.kie.kogito.explainability.model.Model;

public interface GlobalExplainer<T> {

    T explain(Model model);
}
