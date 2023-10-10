package org.kie.kogito.persistence.reporting.model;

public interface Mapping<T, J extends JsonField<T>> {

    String getSourceJsonPath();

    J getTargetField();

}
