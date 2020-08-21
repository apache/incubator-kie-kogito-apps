package org.kie.kogito.trusty.service.messaging;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class MessagingUtils {

    public static org.kie.kogito.tracing.typedvalue.TypedValue modelToTracingTypedValue(org.kie.kogito.trusty.storage.api.model.TypedVariable value) {
        if (value == null) {
            return null;
        }
        switch (value.getKind()) {
            case UNIT:
                return new org.kie.kogito.tracing.typedvalue.UnitValue(value.getTypeRef(), null, value.getValue());
            case COLLECTION:
                return new org.kie.kogito.tracing.typedvalue.CollectionValue(value.getTypeRef(), modelToTracingTypedValueCollection(value.getComponents()));
            case STRUCTURE:
                return new org.kie.kogito.tracing.typedvalue.StructureValue(value.getTypeRef(), modelToTracingTypedValueMap(value.getComponents()));
        }
        throw new IllegalStateException("Can't convert org.kie.kogito.trusty.storage.api.model.TypedVariable of kind " + value.getKind() + " to TypedValue");
    }

    public static Collection<org.kie.kogito.tracing.typedvalue.TypedValue> modelToTracingTypedValueCollection(Collection<org.kie.kogito.trusty.storage.api.model.TypedVariable> input) {
        if (input == null) {
            return null;
        }
        return input.stream().map(MessagingUtils::modelToTracingTypedValue).collect(Collectors.toList());
    }

    public static Map<String, org.kie.kogito.tracing.typedvalue.TypedValue> modelToTracingTypedValueMap(Collection<org.kie.kogito.trusty.storage.api.model.TypedVariable> input) {
        if (input == null) {
            return null;
        }
        return input.stream()
                .filter(m -> m.getName() != null)
                .collect(HashMap::new, (m, v) -> m.put(v.getName(), modelToTracingTypedValue(v)), HashMap::putAll);
    }

    private MessagingUtils() {
    }

}
