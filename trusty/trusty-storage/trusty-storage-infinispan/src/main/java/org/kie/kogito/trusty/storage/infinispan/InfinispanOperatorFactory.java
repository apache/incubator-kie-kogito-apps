package org.kie.kogito.trusty.storage.infinispan;

import java.util.HashMap;
import java.util.Map;

import org.kie.kogito.trusty.storage.api.operators.DateOperator;
import org.kie.kogito.trusty.storage.api.operators.IntegerOperator;
import org.kie.kogito.trusty.storage.api.operators.StringOperator;

public class InfinispanOperatorFactory {

    private static Map<DateOperator, String> dateOperators = createDateOperators();
    private static Map<IntegerOperator, String> integerOperators = createIntegerOperators();

    static Map<DateOperator, String> createDateOperators() {
        HashMap<DateOperator, String> map = new HashMap<>();
        map.put(DateOperator.LTE, "<=");
        map.put(DateOperator.GTE, ">=");
        return map;
    }

    static Map<IntegerOperator, String> createIntegerOperators() {
        HashMap<IntegerOperator, String> map = new HashMap<>();
        map.put(IntegerOperator.EQUALS, "=");
        map.put(IntegerOperator.LTE, "<=");
        map.put(IntegerOperator.GTE, ">=");
        return map;
    }

    public static String convert(DateOperator operator) {
        return dateOperators.get(operator);
    }

    public static String convert(IntegerOperator operator) {
        return integerOperators.get(operator);
    }
}
