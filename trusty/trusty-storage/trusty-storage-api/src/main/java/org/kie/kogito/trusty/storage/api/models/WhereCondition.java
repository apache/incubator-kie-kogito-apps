package org.kie.kogito.trusty.storage.api.models;

public class WhereCondition<T, K> {

    public String property;
    public T operator;
    public K value;

    public WhereCondition(String property, T operator, K value) {
        this.property = property;
        this.operator = operator;
        this.value = value;
    }
}