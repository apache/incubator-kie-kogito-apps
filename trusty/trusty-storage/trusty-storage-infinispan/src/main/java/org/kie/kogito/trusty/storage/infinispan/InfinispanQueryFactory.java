package org.kie.kogito.trusty.storage.infinispan;

import java.util.ArrayList;
import java.util.List;

import org.kie.kogito.trusty.storage.api.TrustyStorageQuery;
import org.kie.kogito.trusty.storage.api.models.WhereCondition;
import org.kie.kogito.trusty.storage.api.operators.DateOperator;
import org.kie.kogito.trusty.storage.api.operators.IntegerOperator;
import org.kie.kogito.trusty.storage.api.operators.StringOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InfinispanQueryFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(InfinispanQueryFactory.class);

    public static String build(TrustyStorageQuery query, String entity) {

        String qq = "from " + entity + " b";

        List<String> conditions = new ArrayList<>();
        conditions.addAll(buildIntegerConditions(query.getIntegerConditions()));
        conditions.addAll(buildStringConditions(query.getStringConditions()));
        conditions.addAll(buildDateConditions(query.getDateConditions()));

        if (conditions.isEmpty()) {
            return qq;
        }

        return qq + " where " + String.join(" and ", conditions);
    }

    private static List<String> buildIntegerConditions(List<WhereCondition<IntegerOperator, Integer>> conditions) {
        List<String> result = new ArrayList<>();
        for (WhereCondition<IntegerOperator, Integer> condition : conditions) {
            result.add("b." + condition.property + InfinispanOperatorFactory.convert(condition.operator) + condition.value);
        }
        return result;
    }

    private static List<String> buildStringConditions(List<WhereCondition<StringOperator, String>> conditions) {
        List<String> result = new ArrayList<>();
        for (WhereCondition<StringOperator, String> condition : conditions) {
            switch (condition.operator) {
                case EQUALS:
                    result.add("b." + condition.property + "=\"" + condition.value + "\"");
                    break;
                case PREFIX:
                    result.add("b." + condition.property + ":\"^" + condition.value + "*\"");
                    break;
            }
        }
        return result;
    }

    private static List<String> buildDateConditions(List<WhereCondition<DateOperator, String>> conditions) {
        List<String> result = new ArrayList<>();
        for (WhereCondition<DateOperator, String> condition : conditions) {
            result.add("b." + condition.property + InfinispanOperatorFactory.convert(condition.operator) + condition.value);
        }
        return result;
    }
}