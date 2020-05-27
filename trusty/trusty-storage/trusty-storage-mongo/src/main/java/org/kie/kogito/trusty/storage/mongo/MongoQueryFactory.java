/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.kogito.trusty.storage.mongo;

import java.util.ArrayList;
import java.util.List;

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

        conditions.addAll(buildIntegerConditions(query.getIntegerConditions()));

        conditions.addAll(buildStringConditions(query.getStringConditions()));

        conditions.addAll(buildDateConditions(query.getDateConditions()));

        if (conditions.size() > 1) {
            return and(conditions);
        }
        return conditions.get(0);
    }

    private static List<Bson> buildIntegerConditions(List<WhereCondition<IntegerOperator, Integer>> conditions) {
        List<Bson> result = new ArrayList<>();
        for (WhereCondition<IntegerOperator, Integer> condition : conditions) {
            switch (condition.operator) {
                case EQUALS:
                    result.add(eq(condition.property, condition.value));
                    break;
                case GTE:
                    result.add(gte(condition.property, condition.value));
                    break;
                case LTE:
                    result.add(lte(condition.property, condition.value));
                    break;
                default:
                    throw new RuntimeException("Integer operator not supported.");
            }
        }

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
