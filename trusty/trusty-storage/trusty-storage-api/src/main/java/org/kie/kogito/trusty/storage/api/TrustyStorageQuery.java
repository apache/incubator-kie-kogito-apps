package org.kie.kogito.trusty.storage.api;

import java.util.ArrayList;
import java.util.List;

import org.kie.kogito.trusty.storage.api.models.WhereCondition;
import org.kie.kogito.trusty.storage.api.operators.DateOperator;
import org.kie.kogito.trusty.storage.api.operators.IntegerOperator;
import org.kie.kogito.trusty.storage.api.operators.StringOperator;

public class TrustyStorageQuery {

    private List<WhereCondition<StringOperator, String>> stringConditions = new ArrayList<>();

    private List<WhereCondition<IntegerOperator, Integer>> integerConditions = new ArrayList<>();

    private List<WhereCondition<DateOperator, String>> dateConditions = new ArrayList<>();

    public TrustyStorageQuery() {
    }

    public TrustyStorageQuery where(String property, StringOperator operator, String value) {
        stringConditions.add(new WhereCondition(property, operator, value));
        return this;
    }

    public TrustyStorageQuery where(String property, IntegerOperator operator, Integer value) {
        integerConditions.add(new WhereCondition(property, operator, value));
        return this;
    }

    public TrustyStorageQuery where(String property, DateOperator operator, String value) {
        System.out.println(value);
        dateConditions.add(new WhereCondition(property, operator, value));
        return this;
    }

    public List<WhereCondition<StringOperator, String>> getStringConditions() {
        return stringConditions;
    }

    public List<WhereCondition<IntegerOperator, Integer>> getIntegerConditions() {
        return integerConditions;
    }

    public List<WhereCondition<DateOperator, String>> getDateConditions() {
        return dateConditions;
    }
}