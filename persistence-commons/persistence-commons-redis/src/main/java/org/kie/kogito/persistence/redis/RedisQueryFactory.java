package org.kie.kogito.persistence.redis;

import java.util.ArrayList;
import java.util.List;

import org.kie.kogito.persistence.api.query.AttributeFilter;

public class RedisQueryFactory {

    static String buildQueryBody(List<AttributeFilter<?>> filters) {
        List<String> components = new ArrayList<>();
        components.add("@myKey:myKey");
        for (AttributeFilter attributeFilter : filters) {
            switch (attributeFilter.getCondition()) {
                case CONTAINS:
                case EQUAL:
                    components.add(String.format("@%s:%s", attributeFilter.getAttribute(), attributeFilter.getValue()));
                    break;
                case LIKE:
                    components.add(String.format("@%s:%s*", attributeFilter.getAttribute(), attributeFilter.getValue()));
                    break;
            }
        }
        return String.join("&", components);
    }

    static void addFilters(io.redisearch.Query query, List<AttributeFilter<?>> filters) {
        for (AttributeFilter attributeFilter : filters) {
            switch (attributeFilter.getCondition()) {
                case CONTAINS:
                case EQUAL:
                case LIKE:
                    break;
                case GT:
                    query.addFilter(
                            new io.redisearch.Query.NumericFilter(
                                    attributeFilter.getAttribute(), convertNumeric(attributeFilter.getValue()), true, Double.POSITIVE_INFINITY, false
                            )
                    );
                    break;
                case GTE:
                    query.addFilter(
                            new io.redisearch.Query.NumericFilter(
                                    attributeFilter.getAttribute(), convertNumeric(attributeFilter.getValue()), false, Double.POSITIVE_INFINITY, false
                            )
                    );
                    break;
                case LT:
                    query.addFilter(
                            new io.redisearch.Query.NumericFilter(
                                    attributeFilter.getAttribute(), Double.NEGATIVE_INFINITY, false, convertNumeric(attributeFilter.getValue()), true
                            )
                    );
                    break;
                case LTE:
                    query.addFilter(
                            new io.redisearch.Query.NumericFilter(
                                    attributeFilter.getAttribute(), Double.NEGATIVE_INFINITY, false, convertNumeric(attributeFilter.getValue()), false
                            )
                    );
                    break;
                case BETWEEN:
                    List<?> value = (List<?>) attributeFilter.getValue();
                    query.addFilter(
                            new io.redisearch.Query.NumericFilter(
                                    attributeFilter.getAttribute(), convertNumeric(value.get(0)), false, convertNumeric(value.get(1)), false
                            )
                    );
                    break;
                default:
                    throw new UnsupportedOperationException("query filter not supported: " + attributeFilter.getCondition());
            }
        }
    }

    static Double convertNumeric(Object obj) {
        if (obj instanceof Long) {
            return ((Long) obj).doubleValue();
        }
        return (Double) obj;
    }
}
