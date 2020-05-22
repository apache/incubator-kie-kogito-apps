package org.kie.kogito.trusty.storage.api;

import java.util.ArrayList;
import java.util.List;

import org.kie.kogito.trusty.storage.api.operators.DateOperator;
import org.kie.kogito.trusty.storage.api.operators.IntegerOperator;
import org.kie.kogito.trusty.storage.api.operators.StringOperator;

public class TrustyStorageQuery {

    public List<InternalWhereDecision<StringOperator, String>> stringOperations = new ArrayList<InternalWhereDecision<StringOperator, String>>();

    public List<InternalWhereDecision<IntegerOperator, Integer>> integerOperations = new ArrayList<InternalWhereDecision<IntegerOperator, Integer>>();

    public List<InternalWhereDecision<DateOperator, String>> dateOperations = new ArrayList<InternalWhereDecision<DateOperator, String>>();

    public TrustyStorageQuery() {
    }

    public TrustyStorageQuery where(String property, StringOperator operator, String value) {
        stringOperations.add(new InternalWhereDecision(property, operator, value));
        return this;
    }

    public TrustyStorageQuery where(String property, IntegerOperator operator, Integer value) {
        integerOperations.add(new InternalWhereDecision(property, operator, value));
        return this;
    }

    public TrustyStorageQuery where(String property, DateOperator operator, String value) {
        System.out.println(value);
        dateOperations.add(new InternalWhereDecision(property, operator, value));
        return this;
    }

    public class InternalWhereDecision<T, K> {

        public String property;
        public T operator;
        public K value;

        public InternalWhereDecision(String property, T operator, K value) {
            this.property = property;
            this.operator = operator;
            this.value = value;
        }
    }
}