package org.kie.kogito.trusty.service.messaging;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.kie.kogito.explainability.api.typedvalue.CollectionValue;
import org.kie.kogito.explainability.api.typedvalue.StructureValue;
import org.kie.kogito.explainability.api.typedvalue.TypedValue;
import org.kie.kogito.explainability.api.typedvalue.UnitValue;

public class MessagingUtils {

    public static TypedValue modelVariableToTypedValue(org.kie.kogito.trusty.storage.api.model.TypedVariable value) {
        if (value == null) {
            return null;
        }
        switch (value.getKind()) {
            case UNIT:
                return new UnitValue(value.getTypeRef(), null, value.getValue());
            case COLLECTION:
                return new CollectionValue(value.getTypeRef(), modelVariableToTypedValueCollection(value.getComponents()));
            case STRUCTURE:
                return new StructureValue(value.getTypeRef(), modelVariableToTypedValueMap(value.getComponents()));
        }
        throw new IllegalStateException("Can't convert org.kie.kogito.trusty.storage.api.model.TypedVariable of kind " + value.getKind() + " to TypedValue");
    }

    public static Collection<TypedValue> modelVariableToTypedValueCollection(Collection<org.kie.kogito.trusty.storage.api.model.TypedVariable> input) {
        if (input == null) {
            return null;
        }
        return input.stream().map(MessagingUtils::modelVariableToTypedValue).collect(Collectors.toList());
    }

    public static Map<String, TypedValue> modelVariableToTypedValueMap(Collection<org.kie.kogito.trusty.storage.api.model.TypedVariable> input) {
        if (input == null) {
            return null;
        }
        return input.stream()
                .filter(m -> m.getName() != null)
                .collect(HashMap::new, (m, v) -> m.put(v.getName(), modelVariableToTypedValue(v)), HashMap::putAll);
    }

    public static TypedValue tracingVariableToTypedValue(org.kie.kogito.tracing.decision.event.variable.TypedVariable value) {
        if (value == null) {
            return null;
        }
        switch (value.getKind()) {
            case UNIT:
                org.kie.kogito.tracing.decision.event.variable.UnitVariable unitValue = value.toUnit();
                return new UnitValue(unitValue.getType(), unitValue.getBaseType(), unitValue.getValue());
            case COLLECTION:
                org.kie.kogito.tracing.decision.event.variable.CollectionVariable collectionValue = value.toCollection();
                return new CollectionValue(collectionValue.getType(), tracingVariableToTypedValueCollection(collectionValue.getValue()));
            case STRUCTURE:
                org.kie.kogito.tracing.decision.event.variable.StructureVariable structureValue = value.toStructure();
                return new StructureValue(structureValue.getType(), tracingVariableToTypedValueMap(structureValue.getValue()));
        }
        throw new IllegalStateException("Can't convert org.kie.kogito.tracing.decision.event.variable.TypedVariable of kind " + value.getKind() + " to TypedValue");
    }

    public static Collection<TypedValue> tracingVariableToTypedValueCollection(Collection<org.kie.kogito.tracing.decision.event.variable.TypedVariable> input) {
        if (input == null) {
            return null;
        }
        return input.stream().map(MessagingUtils::tracingVariableToTypedValue).collect(Collectors.toList());
    }

    public static Map<String, TypedValue> tracingVariableToTypedValueMap(Map<String, org.kie.kogito.tracing.decision.event.variable.TypedVariable> input) {
        if (input == null) {
            return null;
        }
        return input.entrySet().stream()
                .collect(HashMap::new, (m, v) -> m.put(v.getKey(), tracingVariableToTypedValue(v.getValue())), HashMap::putAll);
    }

    private MessagingUtils() {
    }

}
