package org.kie.kogito.explainability.local.lime.optim;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.valuerange.ValueRange;
import org.optaplanner.core.api.domain.valuerange.ValueRangeFactory;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.domain.variable.PlanningVariable;


@PlanningEntity
public class NumericLimeConfigEntity extends LimeConfigEntity<Double> {

    private double rangeMinimum;
    private double rangeMaximum;

    public NumericLimeConfigEntity() {
        super();
    }

    public NumericLimeConfigEntity(String name, double proposedValue, double rangeMinimum, double rangeMaximum) {
        super(name, proposedValue);
        this.rangeMinimum = rangeMinimum;
        this.rangeMaximum = rangeMaximum;
    }

    @ValueRangeProvider(id = "doubleRange")
    public ValueRange<Double> getValueRange() {
        return ValueRangeFactory.createDoubleValueRange(rangeMinimum, rangeMaximum);
    }

    @PlanningVariable(valueRangeProviderRefs = {"doubleRange"})
    public Double getProposedValue() {
        return proposedValue;
    }

    public void setProposedValue(Double proposedValue) {
        this.proposedValue = proposedValue;
    }

    @Override
    public String toString() {
        return "NumericLimeConfigEntity{" +
                "name="+ name +
                ",proposedValue=" + proposedValue +
                '}';
    }
}
