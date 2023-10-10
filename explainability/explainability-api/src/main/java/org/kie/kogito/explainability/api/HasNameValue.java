package org.kie.kogito.explainability.api;

import org.kie.kogito.tracing.typedvalue.BaseTypedValue;

public interface HasNameValue<V extends BaseTypedValue<?, ?, ?>> {

    String getName();

    V getValue();

}
