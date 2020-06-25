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

package org.kie.kogito.persistence.mongodb.utils;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import io.quarkus.mongodb.panache.runtime.MongoOperations;
import org.bson.conversions.Bson;
import org.kie.kogito.persistence.api.query.AttributeFilter;

import static com.mongodb.client.model.Filters.all;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.exists;
import static com.mongodb.client.model.Filters.gt;
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Filters.in;
import static com.mongodb.client.model.Filters.lt;
import static com.mongodb.client.model.Filters.lte;
import static com.mongodb.client.model.Filters.or;
import static com.mongodb.client.model.Filters.regex;
import static java.util.stream.Collectors.toList;

public class QueryUtils {

    public static Function<String, String> CONVERT_ATTRIBUTE_FUNCTION = attribute -> "id".equalsIgnoreCase(attribute) ? MongoOperations.ID : attribute;

    public static Optional<Bson> generateQuery(List<AttributeFilter<?>> filters, Function<String, String> filterAttributeFunction) {

        return Optional.ofNullable(filters).filter(f -> !f.isEmpty()).map(fs -> and(fs.stream().map(f -> generateSingleQuery(f, filterAttributeFunction)).collect(toList())));
    }

    private static Bson generateSingleQuery(AttributeFilter<?> filter, Function<String, String> filterAttributeFunction) {
        switch (filter.getCondition()) {
            case CONTAINS:
            case EQUAL:
                return eq(filterAttributeFunction.apply(filter.getAttribute()), filter.getValue());
            case LIKE:
                return regex(filterAttributeFunction.apply(filter.getAttribute()), ((String) filter.getValue()).replaceAll("\\*", ".*"));
            case IS_NULL:
                return exists(filterAttributeFunction.apply(filter.getAttribute()), false);
            case NOT_NULL:
                return exists(filterAttributeFunction.apply(filter.getAttribute()), true);
            case GT:
                return gt(filterAttributeFunction.apply(filter.getAttribute()), filter.getValue());
            case GTE:
                return gte(filterAttributeFunction.apply(filter.getAttribute()), filter.getValue());
            case LT:
                return lt(filterAttributeFunction.apply(filter.getAttribute()), filter.getValue());
            case LTE:
                return lte(filterAttributeFunction.apply(filter.getAttribute()), filter.getValue());
            case BETWEEN:
                List<?> value = (List<?>) filter.getValue();
                return and(gte(filterAttributeFunction.apply(filter.getAttribute()), value.get(0)),
                           lte(filterAttributeFunction.apply(filter.getAttribute()), value.get(1)));
            case IN:
                return in(filterAttributeFunction.apply(filter.getAttribute()), (List<?>) filter.getValue());
            case CONTAINS_ALL:
                return all(filterAttributeFunction.apply(filter.getAttribute()), (List<?>) filter.getValue());
            case CONTAINS_ANY:
                return or(((List<?>) filter.getValue()).stream().map(v -> in(filterAttributeFunction.apply(filter.getAttribute()), v)).collect(toList()));
            case OR:
                return or(((List<AttributeFilter<?>>) filter.getValue()).stream().map(f -> generateSingleQuery(f, filterAttributeFunction)).collect(toList()));
            case AND:
                return and(((List<AttributeFilter<?>>) filter.getValue()).stream().map(f -> generateSingleQuery(f, filterAttributeFunction)).collect(toList()));
            default:
                return null;
        }
    }
}
