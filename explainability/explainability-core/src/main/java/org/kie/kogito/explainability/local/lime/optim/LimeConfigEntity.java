package org.kie.kogito.explainability.local.lime.optim;

public abstract class LimeConfigEntity<T> {

    protected T proposedValue;
    protected String name;

    protected LimeConfigEntity() {

    }

    public LimeConfigEntity(String name, T proposedValue) {
        this.name = name;
        this.proposedValue = proposedValue;
    }

    public String getName() {
        return name;
    }
}
