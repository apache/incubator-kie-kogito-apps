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

package org.kie.kogito.index.mongodb.utils;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.mongodb.DBObject;
import com.mongodb.client.MongoCollection;
import io.quarkus.mongodb.panache.runtime.MongoOperations;
import org.kie.kogito.index.query.AttributeFilter;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

public class MongoDBUtils {

    public static BiFunction<String, Object, String> filterValueAsStringFunction = (attribute, value) -> value instanceof String ? "'" + value + "'" : value.toString();

    public static <T> MongoCollection<T> getCollection(String processId, Class<T> type) {
        return MongoOperations.mongoDatabase(DBObject.class).getCollection(processId + "_domain", type);
    }

    public static Optional<String> generateQueryString(List<AttributeFilter> filters, Function<String, String> filterAttributeFunction, BiFunction<String, Object, String> filterValueFunction) {
        return Optional.ofNullable(filters).map(fs -> format("{ %s }", fs.stream().map(f -> generateSingleQueryString(f, filterAttributeFunction, filterValueFunction)).collect(joining(", "))));
    }

    private static String generateSingleQueryString(AttributeFilter filter, Function<String, String> filterAttributeFunction, BiFunction<String, Object, String> filterValueFunction) {
        switch (filter.getCondition()) {
            case CONTAINS:
            case EQUAL:
                return format("%s: %s", filterAttributeFunction.apply(filter.getAttribute()), filterValueFunction.apply(filter.getAttribute(), filter.getValue()));
            case LIKE:
                return format("%s: { $regex: /^%s$/ }", filterAttributeFunction.apply(filter.getAttribute()), ((String) filter.getValue()).replaceAll("\\*", ".*"));
            case IS_NULL:
                return format("%s: { $exists: false }", filterAttributeFunction.apply(filter.getAttribute()));
            case NOT_NULL:
                return format("%s: { $exists: true }", filterAttributeFunction.apply(filter.getAttribute()));
            case GT:
                return format("%s: { $gt: %s }", filterAttributeFunction.apply(filter.getAttribute()), filterValueFunction.apply(filter.getAttribute(), filter.getValue()));
            case GTE:
                return format("%s: { $gte: %s }", filterAttributeFunction.apply(filter.getAttribute()), filterValueFunction.apply(filter.getAttribute(), filter.getValue()));
            case LT:
                return format("%s: { $lt: %s }", filterAttributeFunction.apply(filter.getAttribute()), filterValueFunction.apply(filter.getAttribute(), filter.getValue()));
            case LTE:
                return format("%s: { $lte: %s }", filterAttributeFunction.apply(filter.getAttribute()), filterValueFunction.apply(filter.getAttribute(), filter.getValue()));
            case BETWEEN:
                List<Object> value = (List<Object>) filter.getValue();
                return format("$and: [ { %s: { $gte: %s } }, { %s: { $lte: %s } } ]", filterAttributeFunction.apply(filter.getAttribute()), filterValueFunction.apply(filter.getAttribute(), value.get(0)),
                              filterAttributeFunction.apply(filter.getAttribute()), filterValueFunction.apply(filter.getAttribute(), value.get(1)));
            case IN:
                return format("%s: { $in: [ %s ] }", filterAttributeFunction.apply(filter.getAttribute()), ((List) filter.getValue()).stream().map(v -> filterValueFunction.apply(filter.getAttribute(), v)).collect(joining(", ")));
            case CONTAINS_ALL:
                return format("%s: { $all: [ %s ] }", filterAttributeFunction.apply(filter.getAttribute()), ((List) filter.getValue()).stream().map(v -> filterValueFunction.apply(filter.getAttribute(), v)).collect(joining(", ")));
            case CONTAINS_ANY:
                return format("$or: [ %s ]", ((List) filter.getValue()).stream().map(v -> filterValueFunction.apply(filter.getAttribute(), v)).map(v -> format("{ %s: { $in: [ %s ] } }", filterAttributeFunction.apply(filter.getAttribute()), v)).collect(joining(", ")));
            case OR:
                return format("$or: [ %s ]", ((List<AttributeFilter>) filter.getValue()).stream().map(f -> format("{ %s }", generateSingleQueryString(f, filterAttributeFunction, filterValueFunction))).collect(joining(", ")));
            case AND:
                return format("$and: [ %s ]", ((List<AttributeFilter>) filter.getValue()).stream().map(f -> format("{ %s }", generateSingleQueryString(f, filterAttributeFunction, filterValueFunction))).collect(joining(", ")));
            default:
                return null;
        }
    }
}
