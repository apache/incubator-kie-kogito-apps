package org.kie.kogito.trusty.storage.mongo;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.NotSupportedException;

import org.bson.conversions.Bson;
import org.kie.kogito.trusty.storage.api.TrustyStorageQuery;
import org.kie.kogito.trusty.storage.api.models.WhereCondition;
import org.kie.kogito.trusty.storage.api.operators.DateOperator;
import org.kie.kogito.trusty.storage.api.operators.IntegerOperator;
import org.kie.kogito.trusty.storage.api.operators.StringOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Filters.lte;
import static com.mongodb.client.model.Filters.regex;

public class MongoQueryFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(MongoQueryFactory.class);

    public static Bson build(TrustyStorageQuery query) {
        List<Bson> conditions = new ArrayList<>();

        conditions.addAll(buildIntegerConditions(query.integerOperations));

        conditions.addAll(buildStringConditions(query.stringOperations));

        conditions.addAll(buildDateConditions(query.dateOperations));

        if (conditions.size() > 1) {
            return and(conditions);
        }
        return conditions.get(0);
    }

    private static List<Bson> buildIntegerConditions(List<WhereCondition<IntegerOperator, Integer>> conditions) {
        List<Bson> result = new ArrayList<>();
        conditions.forEach(x -> result.add(eq(x.property, x.value)));
        return result;
    }

    private static List<Bson> buildStringConditions(List<WhereCondition<StringOperator, String>> conditions) {
        List<Bson> result = new ArrayList<>();
        for (WhereCondition<StringOperator, String> condition : conditions) {
            switch (condition.operator) {
                case EQUALS:
                    result.add(eq(condition.property, condition.value));
                    break;
                case PREFIX:
                    result.add(regex(condition.property, "^" + condition.value + "*"));
                    break;
                default:
                    throw new RuntimeException("String operator not supported.");
            }
        }
        return result;
    }

    private static List<Bson> buildDateConditions(List<WhereCondition<DateOperator, String>> conditions) {
        List<Bson> result = new ArrayList<>();
        for (WhereCondition<DateOperator, String> condition : conditions) {
            switch (condition.operator) {
                case GTE:
                    result.add(gte(condition.property, condition.value));
                    break;
                case LTE:
                    result.add(lte(condition.property, condition.value));
                    break;
                default:
                    throw new RuntimeException("Date operator not supported.");
            }
        }
        return result;
    }
}
