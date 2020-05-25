package org.kie.kogito.trusty.storage.api;

import java.util.ArrayList;
import java.util.List;

import org.kie.kogito.trusty.storage.api.models.WhereCondition;
import org.kie.kogito.trusty.storage.api.operators.DateOperator;
import org.kie.kogito.trusty.storage.api.operators.IntegerOperator;
import org.kie.kogito.trusty.storage.api.operators.StringOperator;

public class TrustyStorageQuery {

    public List<WhereCondition<StringOperator, String>> stringOperations = new ArrayList<>();

    public List<WhereCondition<IntegerOperator, Integer>> integerOperations = new ArrayList<>();

    public List<WhereCondition<DateOperator, String>> dateOperations = new ArrayList<>();

    public TrustyStorageQuery() {
    }

    public TrustyStorageQuery where(String property, StringOperator operator, String value) {
        stringOperations.add(new WhereCondition(property, operator, value));
        return this;
    }

    public TrustyStorageQuery where(String property, IntegerOperator operator, Integer value) {
        integerOperations.add(new WhereCondition(property, operator, value));
        return this;
    }

    public TrustyStorageQuery where(String property, DateOperator operator, String value) {
        System.out.println(value);
        dateOperations.add(new WhereCondition(property, operator, value));
        return this;
    }
}