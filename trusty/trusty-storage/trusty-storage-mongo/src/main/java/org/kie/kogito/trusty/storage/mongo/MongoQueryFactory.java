package org.kie.kogito.trusty.storage.mongo;

import java.util.ArrayList;
import java.util.List;

import org.bson.conversions.Bson;
import org.kie.kogito.trusty.storage.api.TrustyStorageQuery;
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

    public static Bson build(TrustyStorageQuery query, String entity) {
        List<Bson> conditions = new ArrayList<>();

        for (TrustyStorageQuery.InternalWhereDecision<IntegerOperator, Integer> condition : query.integerOperations) {
            conditions.add(eq(condition.property, condition.value));
        }

        for (TrustyStorageQuery.InternalWhereDecision<StringOperator, String> condition : query.stringOperations) {
            switch (condition.operator){
                case EQUALS:
                    conditions.add(eq(condition.property, condition.value));
                    break;
                case PREFIX:
                    conditions.add(regex(condition.property, "^" + condition.value + "*"));
                    break;
            }
        }

        for (TrustyStorageQuery.InternalWhereDecision<DateOperator, String> condition : query.dateOperations) {
            switch (condition.operator) {
                case GTE:
                    conditions.add(gte(condition.property, condition.value));
                    break;
                case LTE:
                    conditions.add(lte(condition.property, condition.value));
                    break;
            }

        }

        if (conditions.size() > 1){
            return and(conditions);
        }
        return conditions.get(0);
    }
}
