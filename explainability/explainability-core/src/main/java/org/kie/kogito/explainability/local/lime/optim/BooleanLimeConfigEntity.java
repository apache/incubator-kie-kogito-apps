package org.kie.kogito.explainability.local.lime.optim;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.valuerange.ValueRange;
import org.optaplanner.core.api.domain.valuerange.ValueRangeFactory;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.domain.variable.PlanningVariable;


@PlanningEntity
public class BooleanLimeConfigEntity extends LimeConfigEntity<Boolean> {

    public BooleanLimeConfigEntity(String name, Boolean proposedValue) {
        super(name, proposedValue);
    }

    @ValueRangeProvider(id = "booleanRange")
    public ValueRange<Boolean> getValueRange() {
        return ValueRangeFactory.createBooleanValueRange();
    }

    @PlanningVariable(valueRangeProviderRefs = {"booleanRange"})
    public Boolean getProposedValue() {
        return proposedValue;
    }

    public void setProposedValue(Boolean proposedValue) {
        this.proposedValue = proposedValue;
    }
}
