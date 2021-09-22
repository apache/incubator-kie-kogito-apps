/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
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
package org.kie.kogito.trusty.storage.postgresql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.kie.kogito.persistence.api.query.AttributeFilter;
import org.kie.kogito.persistence.api.query.AttributeSort;
import org.kie.kogito.persistence.api.query.Query;
import org.kie.kogito.persistence.postgresql.model.CacheEntityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vladmihalcea.hibernate.type.json.JsonNodeBinaryType;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

public class PostgreSqlQuery<T> implements Query<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PostgreSqlQuery.class);
    private static final String AND = " and ";
    private static final String OR = " or ";
    private static final String ATTRIBUTE_VALUE = "%s = %s";

    private final String name;
    private final CacheEntityRepository repository;
    private final ObjectMapper objectMapper;
    private final Class<T> type;

    private Integer limit;
    private Integer offset;
    private List<AttributeFilter<?>> filters;
    private List<AttributeSort> sortBy;

    private static final class JsonField {

        String name;
        Object value;

        JsonField(String name) {
            this(name, null);
        }

        JsonField(String name, Object value) {
            this.name = name;
            this.value = value;
        }
    }

    public PostgreSqlQuery(String name, CacheEntityRepository repository, ObjectMapper objectMapper, Class<T> type) {
        this.name = name;
        this.repository = repository;
        this.objectMapper = objectMapper;
        this.type = type;
    }

    @Override
    public Query<T> limit(Integer limit) {
        this.limit = limit;
        return this;
    }

    @Override
    public Query<T> offset(Integer offset) {
        this.offset = offset;
        return this;
    }

    @Override
    public Query<T> filter(List<AttributeFilter<?>> filters) {
        this.filters = filters;
        return this;
    }

    @Override
    public Query<T> sort(List<AttributeSort> sortBy) {
        this.sortBy = sortBy;
        return this;
    }

    @Override
    public List<T> execute() {
        // Build a temporary table containing the extracted JSON values against which we want to query.
        // For example: SELECT name, json_value, json_value->'columnX'->>0 as columnX from kogito_data_cache
        // - "name" identifies the correct segment of the underlying table for the particular object type being stored.
        // - "json_value" contains the JSON serialised object
        // - "columnX" is the JSON property on which you want to filter (only "top-level" JSON keys are supported)
        StringBuilder subQueryBuilder = new StringBuilder("SELECT ");
        subQueryBuilder.append("name AS name, json_value AS json_value");
        Map<String, JsonField> fields = new HashMap<>();
        if (filters != null && !filters.isEmpty()) {
            filters.stream().filter(filter -> !fields.containsKey(filter.getAttribute()))
                    .forEach(filter -> fields.put(filter.getAttribute(),
                            new JsonField(filter.getAttribute(), filter.getValue())));
        }
        if (sortBy != null && !sortBy.isEmpty()) {
            sortBy.stream().filter(sortBy -> !fields.containsKey(sortBy.getAttribute()))
                    .forEach(sortBy -> fields.put(sortBy.getAttribute(),
                            new JsonField(sortBy.getAttribute())));
        }
        if (!fields.isEmpty()) {
            subQueryBuilder.append(", ");
            subQueryBuilder.append(fields.values().stream()
                    .map(field -> cast(field, new StringBuilder("json_value->'")
                            .append(field.name)
                            .append("'->>0"))
                                    .append(" AS ")
                                    .append(field.name))
                    .collect(joining(", ")));
        }
        subQueryBuilder.append(" FROM kogito_data_cache");
        // Build the query to retrieve the filtered data from the temporary table above.
        StringBuilder queryString = new StringBuilder("SELECT o.* FROM (")
                .append(subQueryBuilder).append(") o")
                .append(" WHERE o.name = '")
                .append(name)
                .append("'");
        if (filters != null && !filters.isEmpty()) {
            queryString.append(" AND ");
            queryString.append(filters.stream()
                    .map(filter -> new StringBuilder("o.")
                            .append(filterStringFunction(filter)))
                    .collect(joining(AND)));
        }

        // Sorting
        if (sortBy != null && !sortBy.isEmpty()) {
            queryString.append(" ORDER BY ");
            queryString.append(sortBy.stream().map(f -> "o." + f.getAttribute() + " " + f.getSort().name()).collect(joining(", ")));
        }

        LOGGER.debug("Executing PostgreSQL query: {}", queryString);
        javax.persistence.Query query = repository.getEntityManager().createNativeQuery(queryString.toString());
        query.unwrap(org.hibernate.query.NativeQuery.class).addScalar("json_value", JsonNodeBinaryType.INSTANCE);

        if (limit != null) {
            query.setMaxResults(limit);
        }
        if (offset != null) {
            query.setFirstResult(offset);
        }

        List<?> results = query.getResultList();

        return results.stream().map(r -> {
            if (r == null) {
                return null;
            }
            try {
                return objectMapper.readValue(objectMapper.writeValueAsString(r), type);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private String filterStringFunction(AttributeFilter<?> filter) {
        switch (filter.getCondition()) {
            case CONTAINS:
                return format(ATTRIBUTE_VALUE, filter.getAttribute(), getValueForQueryString(filter.getValue()));
            case CONTAINS_ALL:
                return (String) ((List) filter.getValue()).stream().map(o -> format(ATTRIBUTE_VALUE, filter.getAttribute(), getValueForQueryString(o))).collect(joining(AND));
            case CONTAINS_ANY:
                return (String) ((List) filter.getValue()).stream().map(o -> format(ATTRIBUTE_VALUE, filter.getAttribute(), getValueForQueryString(o))).collect(joining(OR));
            case LIKE:
                return format("%s like %s", filter.getAttribute(), getValueForQueryString(filter.getValue())).replaceAll("\\*", "%");
            case EQUAL:
                return format(ATTRIBUTE_VALUE, filter.getAttribute(), getValueForQueryString(filter.getValue()));
            case IN:
                return format("%s in (%s)", filter.getAttribute(), ((List) filter.getValue()).stream().map(PostgreSqlQuery::getValueForQueryString).collect(joining(", ")));
            case IS_NULL:
                return format("%s is null", filter.getAttribute());
            case NOT_NULL:
                return format("%s is not null", filter.getAttribute());
            case BETWEEN:
                List<Object> value = (List<Object>) filter.getValue();
                return format("%s between %s and %s", filter.getAttribute(), getValueForQueryString(value.get(0)), getValueForQueryString(value.get(1)));
            case GT:
                return format("%s > %s", filter.getAttribute(), getValueForQueryString(filter.getValue()));
            case GTE:
                return format("%s >= %s", filter.getAttribute(), getValueForQueryString(filter.getValue()));
            case LT:
                return format("%s < %s", filter.getAttribute(), getValueForQueryString(filter.getValue()));
            case LTE:
                return format("%s <= %s", filter.getAttribute(), getValueForQueryString(filter.getValue()));
            case OR:
                return getRecursiveString(filter, OR);
            case AND:
                return getRecursiveString(filter, AND);
            case NOT:
                return format("not %s", filterStringFunction((AttributeFilter<?>) filter.getValue()));
            default:
                return null;
        }
    }

    private static String getValueForQueryString(Object value) {
        return value instanceof String ? "'" + value + "'" : value.toString();
    }

    // The values extracted from the JSON structure need casting into primitive types
    private static StringBuilder cast(JsonField field, StringBuilder extractor) {
        StringBuilder cast = new StringBuilder();
        Object value = field.value;
        if (value instanceof Number) {
            cast.append("(").append(extractor).append(")\\:\\:numeric");
        } else {
            cast.append(extractor);
        }
        return cast;
    }

    @SuppressWarnings("unchecked")
    private String getRecursiveString(AttributeFilter<?> filter, String joining) {
        return ((List<AttributeFilter<?>>) filter.getValue())
                .stream()
                .map(this::filterStringFunction)
                .collect(joining(joining, "(", ")"));
    }
}